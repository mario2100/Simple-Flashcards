package com.randomappsinc.simpleflashcards.browse.fragments;

import android.animation.Animator;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.randomappsinc.simpleflashcards.R;
import com.randomappsinc.simpleflashcards.browse.activities.BrowseFlashcardsActivity;
import com.randomappsinc.simpleflashcards.browse.managers.BrowseFlashcardsSettingsManager;
import com.randomappsinc.simpleflashcards.common.Constants;
import com.randomappsinc.simpleflashcards.common.activities.PictureFullViewActivity;
import com.randomappsinc.simpleflashcards.persistence.DatabaseManager;
import com.randomappsinc.simpleflashcards.persistence.models.Flashcard;
import com.randomappsinc.simpleflashcards.utils.ViewUtils;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import butterknife.BindInt;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class BrowseFlashcardFragment extends Fragment {

    public static BrowseFlashcardFragment create(int flashcardId, int flashcardPosition, int setSize) {
        BrowseFlashcardFragment flashcardFragment = new BrowseFlashcardFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.FLASHCARD_ID_KEY, flashcardId);
        bundle.putInt(Constants.FLASHCARD_POSITION_KEY, flashcardPosition);
        bundle.putInt(Constants.FLASHCARD_SET_SIZE_KEY, setSize);
        flashcardFragment.setArguments(bundle);
        return flashcardFragment;
    }

    @BindView(R.id.flashcard_container) View flashcardContainer;
    @BindView(R.id.position_info) TextView positionInfo;
    @BindView(R.id.side_header) TextView sideHeader;
    @BindView(R.id.speak) View speak;
    @BindView(R.id.flip_icon) View flipIcon;
    @BindView(R.id.content_container) ViewGroup contentContainer;

    protected ImageView cardImage;
    protected TextView content;

    @BindInt(R.integer.default_anim_length) int flipAnimLength;
    @BindString(R.string.view_all) String viewAll;

    protected Flashcard flashcard;
    protected boolean isShowingTerm;
    private BrowseFlashcardsSettingsManager settingsManager = BrowseFlashcardsSettingsManager.get();
    private Unbinder unbinder;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.flashcard_for_browsing,
                container,
                false);
        unbinder = ButterKnife.bind(this, rootView);

        settingsManager.addDefaultSideListener(flashcardListener);
        isShowingTerm = settingsManager.getShowTermsByDefault();

        int flashcardId = getArguments().getInt(Constants.FLASHCARD_ID_KEY);
        flashcard = DatabaseManager.get().getFlashcard(flashcardId);

        int cardPosition = getArguments().getInt(Constants.FLASHCARD_POSITION_KEY);
        int setSize = getArguments().getInt(Constants.FLASHCARD_SET_SIZE_KEY);

        String positionTemplate = "%d/%d";
        String positionText = String.format(Locale.US, positionTemplate, cardPosition, setSize);
        positionInfo.setText(positionText);

        loadFlashcardIntoView();

        return rootView;
    }

    @OnClick(R.id.flashcard_container)
    public void flipFlashcard() {
        stopSpeaking();
        flashcardContainer.setEnabled(false);
        flashcardContainer.clearAnimation();
        flashcardContainer
                .animate()
                .rotationY(180)
                .setDuration(flipAnimLength)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        isShowingTerm = !isShowingTerm;
                        positionInfo.setVisibility(View.GONE);
                        sideHeader.setVisibility(View.GONE);
                        speak.setVisibility(View.GONE);
                        content.setVisibility(View.GONE);
                        cardImage.setVisibility(View.GONE);
                        flipIcon.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        flashcardContainer.setRotationY(0);
                        positionInfo.setVisibility(View.VISIBLE);
                        loadFlashcardIntoView();
                        sideHeader.setVisibility(View.VISIBLE);
                        speak.setVisibility(View.VISIBLE);
                        flipIcon.setVisibility(View.VISIBLE);
                        flashcardContainer.setEnabled(true);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        flashcardContainer.setEnabled(true);
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {}
                });
    }

    protected void loadFlashcardIntoView() {
        sideHeader.setText(isShowingTerm ? R.string.term_underlined : R.string.definition_underlined);

        // Load proper layout for card content based on orientation
        contentContainer.removeAllViews();
        int orientation = getActivity().getResources().getConfiguration().orientation;
        View contentView;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            contentView = LayoutInflater
                    .from(getContext())
                    .inflate(R.layout.browse_flashcard_content_horizontal, contentContainer);
        } else {
            contentView = LayoutInflater
                    .from(getContext())
                    .inflate(R.layout.browse_flashcard_content_vertical, contentContainer);
        }
        content = contentView.findViewById(R.id.content);
        content.setMovementMethod(new ScrollingMovementMethod());
        cardImage = contentView.findViewById(R.id.card_image);
        cardImage.setOnClickListener(imageClickListener);

        String contentText = isShowingTerm ? flashcard.getTerm() : flashcard.getDefinition();
        if (TextUtils.isEmpty(contentText)) {
            content.setVisibility(View.GONE);
        } else {
            content.setText(contentText);
            content.setVisibility(View.VISIBLE);
        }
        content.setTextSize(TypedValue.COMPLEX_UNIT_SP, settingsManager.getTextSize());

        setUpImageView();
    }

    private void setUpImageView() {
        final String imageUrl = isShowingTerm ? flashcard.getTermImageUrl() : flashcard.getDefinitionImageUrl();
        if (!TextUtils.isEmpty(imageUrl)) {
            cardImage.setVisibility(View.VISIBLE);
            if (ViewCompat.isLaidOut(cardImage)) {
                loadImage(imageUrl);
            } else {
                ViewUtils.runOnPreDraw(cardImage, () -> loadImage(imageUrl));
            }
        } else {
            cardImage.setVisibility(View.GONE);
        }
    }

    protected void loadImage(String imageUrl) {
        // TODO: Figure out how cardImage can be null when this method is invoked from runOnPreDraw
        if (cardImage != null) {
            Picasso.get()
                    .load(imageUrl)
                    .resize(0, cardImage.getHeight())
                    .into(cardImage);
        }
    }

    private final BrowseFlashcardsSettingsManager.FlashcardListener flashcardListener =
            new BrowseFlashcardsSettingsManager.FlashcardListener() {
                @Override
                public void onDefaultSideChanged(boolean showTermsByDefault) {
                    isShowingTerm = showTermsByDefault;
                    loadFlashcardIntoView();
                }

                @Override
                public void onTextSizeChanged(int textSize) {
                    content.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
                }
            };

    @OnClick(R.id.speak)
    public void speakFlashcard() {
        speak(isShowingTerm ? flashcard.getTerm() : flashcard.getDefinition());
    }

    private void speak(String text) {
        BrowseFlashcardsActivity activity = (BrowseFlashcardsActivity) getActivity();
        if (activity != null) {
            activity.speak(text);
        }
    }

    private void stopSpeaking() {
        BrowseFlashcardsActivity activity = (BrowseFlashcardsActivity) getActivity();
        if (activity != null) {
            activity.stopSpeaking();
        }
    }

    private final View.OnClickListener imageClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String imageUrl = isShowingTerm
                    ? flashcard.getTermImageUrl()
                    : flashcard.getDefinitionImageUrl();
            Activity activity = getActivity();
            if (!TextUtils.isEmpty(imageUrl) && activity != null) {
                Intent intent = new Intent(activity, PictureFullViewActivity.class)
                        .putExtra(Constants.IMAGE_URL_KEY, imageUrl)
                        .putExtra(Constants.CAPTION_KEY, isShowingTerm
                                ? flashcard.getTerm()
                                : flashcard.getDefinition());
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.fade_in, 0);
            }
        }
    };

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        loadFlashcardIntoView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        settingsManager.removeDefaultSideListener(flashcardListener);
        unbinder.unbind();
    }
}
