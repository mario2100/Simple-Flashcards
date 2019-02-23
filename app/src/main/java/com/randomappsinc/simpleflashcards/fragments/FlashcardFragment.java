package com.randomappsinc.simpleflashcards.fragments;

import android.animation.Animator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.randomappsinc.simpleflashcards.R;
import com.randomappsinc.simpleflashcards.activities.BrowseFlashcardsActivity;
import com.randomappsinc.simpleflashcards.activities.PictureFullViewActivity;
import com.randomappsinc.simpleflashcards.constants.Constants;
import com.randomappsinc.simpleflashcards.managers.BrowseFlashcardsSettingsManager;
import com.randomappsinc.simpleflashcards.persistence.DatabaseManager;
import com.randomappsinc.simpleflashcards.persistence.models.Flashcard;
import com.randomappsinc.simpleflashcards.utils.ViewUtils;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import butterknife.BindDimen;
import butterknife.BindInt;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class FlashcardFragment extends Fragment {

    public static FlashcardFragment create(int flashcardId, int flashcardPosition, int setSize) {
        FlashcardFragment flashcardFragment = new FlashcardFragment();
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
    @BindView(R.id.card_image) ImageView cardImage;
    @BindView(R.id.content) TextView content;
    @BindView(R.id.flip_icon) View flipIcon;
    @BindView(R.id.content_container) View contentContainer;

    @BindInt(R.integer.default_anim_length) int flipAnimLength;
    @BindString(R.string.view_all) String viewAll;

    protected Flashcard flashcard;
    protected boolean isShowingTerm;
    private Unbinder unbinder;
    private BrowseFlashcardsSettingsManager settingsManager = BrowseFlashcardsSettingsManager.get();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.flashcard_for_browsing,
                container,
                false);
        unbinder = ButterKnife.bind(this, rootView);

        settingsManager.addListener(defaultSideListener);
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

        String contentText = isShowingTerm ? flashcard.getTerm() : flashcard.getDefinition();
        if (TextUtils.isEmpty(contentText)) {
            content.setVisibility(View.GONE);
        } else {
            content.setText(contentText);
            content.setVisibility(View.INVISIBLE);
        }

        final String imageUrl = isShowingTerm ? flashcard.getTermImageUrl() : flashcard.getDefinitionImageUrl();
        if (!TextUtils.isEmpty(imageUrl)) {
            cardImage.setVisibility(View.INVISIBLE);
            if (ViewCompat.isLaidOut(cardImage)) {
                loadImage(imageUrl);
            } else {
                ViewUtils.runOnPreDraw(cardImage, new Runnable() {
                    @Override
                    public void run() {
                        loadImage(imageUrl);
                    }
                });
            }
        } else {
            cardImage.setVisibility(View.GONE);
        }
        maybeAdjustContent();
    }

    /**
     * It's possible that the user put in too much text for the flashcard container.
     * If so, we need to truncate it.
     */
    private void maybeAdjustContent() {
        contentContainer.post(new Runnable() {
            @Override
            public void run() {
                int containerHeight = contentContainer.getHeight()
                        - getResources().getDimensionPixelSize(R.dimen.browse_card_content_padding) * 2;
                int cardImageHeight = cardImage.getVisibility() == View.GONE ? 0 : cardImage.getHeight();
                int textHeight = content.getVisibility() == View.GONE ? 0 : content.getHeight();
                if (containerHeight < cardImageHeight + textHeight) {
                    adjustContentText(containerHeight - cardImageHeight);
                }
                content.setVisibility(View.VISIBLE);
                if (cardImage.getVisibility() == View.INVISIBLE) {
                    cardImage.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    protected void adjustContentText(int allowedHeight) {
        String fullText = content.getText().toString();
        int start = 1;
        int end = fullText.length();
        while (start < end) {
            int mid = start + (end-start) / 2;
            int height = ViewUtils.measureHeightOfText(
                    fullText.substring(0, mid),
                    (int) content.getTextSize(),
                    content.getWidth());
            if (height <= allowedHeight) {
                start = mid + 1;
            } else {
                end = mid;
            }
        }

        String truncationText = "... " + viewAll;
        String finalText = fullText.substring(0, start - 1 - truncationText.length()) + truncationText;
        SpannableString textWithViewAll = new SpannableString(finalText);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View textView) {
            }

            @Override
            public void updateDrawState(@NonNull TextPaint textPaint) {
                super.updateDrawState(textPaint);
            }
        };
        textWithViewAll.setSpan(
                clickableSpan,
                finalText.length() - viewAll.length(),
                finalText.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        content.setText(textWithViewAll);
        content.setMovementMethod(LinkMovementMethod.getInstance());
        content.setHighlightColor(Color.BLUE);
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

    private final BrowseFlashcardsSettingsManager.Listener defaultSideListener =
            new BrowseFlashcardsSettingsManager.Listener() {
                @Override
                public void onDefaultSideChanged(boolean showTermsByDefault) {
                    isShowingTerm = showTermsByDefault;
                    loadFlashcardIntoView();
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

    @OnClick(R.id.card_image)
    public void openImageInFullView() {
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        settingsManager.removeListener(defaultSideListener);
        unbinder.unbind();
    }
}
