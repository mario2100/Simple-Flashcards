package com.randomappsinc.simpleflashcards.fragments;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.IoniconsIcons;
import com.randomappsinc.simpleflashcards.R;
import com.randomappsinc.simpleflashcards.activities.BackupAndRestoreActivity;
import com.randomappsinc.simpleflashcards.activities.BrowseFlashcardsActivity;
import com.randomappsinc.simpleflashcards.activities.EditFlashcardSetActivity;
import com.randomappsinc.simpleflashcards.activities.MainActivity;
import com.randomappsinc.simpleflashcards.activities.NearbySharingActivity;
import com.randomappsinc.simpleflashcards.activities.QuizSettingsActivity;
import com.randomappsinc.simpleflashcards.adapters.FlashcardSetsAdapter;
import com.randomappsinc.simpleflashcards.constants.Constants;
import com.randomappsinc.simpleflashcards.dialogs.DeleteFlashcardSetDialog;
import com.randomappsinc.simpleflashcards.dialogs.CreateFlashcardSetDialog;
import com.randomappsinc.simpleflashcards.persistence.DatabaseManager;
import com.randomappsinc.simpleflashcards.persistence.models.FlashcardSet;
import com.randomappsinc.simpleflashcards.utils.StringUtils;
import com.randomappsinc.simpleflashcards.utils.UIUtils;
import com.randomappsinc.simpleflashcards.views.SimpleDividerItemDecoration;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.Unbinder;

public class HomepageFragment extends Fragment
        implements FlashcardSetsAdapter.Listener, DeleteFlashcardSetDialog.Listener {

    public static HomepageFragment newInstance() {
        return new HomepageFragment();
    }

    private static final int SPEECH_REQUEST_CODE = 1;

    @BindView(R.id.parent) View parent;
    @BindView(R.id.focus_sink) View focusSink;
    @BindView(R.id.search_bar) View searchBar;
    @BindView(R.id.search_input) EditText setSearch;
    @BindView(R.id.voice_search) View voiceSearch;
    @BindView(R.id.clear_search) View clearSearch;

    // Contains both the list and the fade it has
    @BindView(R.id.sets_list_container) View setsContainer;

    @BindView(R.id.flashcard_sets) RecyclerView sets;
    @BindView(R.id.no_sets) View noSetsAtAll;
    @BindView(R.id.no_sets_match) View noSetsMatch;
    @BindView(R.id.add_flashcard_set) FloatingActionButton addFlashcardSet;

    protected FlashcardSetsAdapter adapter;
    private CreateFlashcardSetDialog createFlashcardSetDialog;
    private DeleteFlashcardSetDialog deleteFlashcardSetDialog;
    private Unbinder unbinder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.homepage,
                container,
                false);
        unbinder = ButterKnife.bind(this, rootView);

        addFlashcardSet.setImageDrawable(new IconDrawable(getContext(), IoniconsIcons.ion_android_add)
                .colorRes(R.color.white)
                .actionBarSize());

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        createFlashcardSetDialog = new CreateFlashcardSetDialog(getActivity(), setCreatedListener);
        deleteFlashcardSetDialog = new DeleteFlashcardSetDialog(getActivity(), this);

        adapter = new FlashcardSetsAdapter(this, getActivity());
        sets.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        sets.setAdapter(adapter);

        // When the user is scrolling to browse flashcards, close the soft keyboard
        sets.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    UIUtils.closeKeyboard(getActivity());
                    takeAwayFocusFromSearch();
                }
            }
        });
    }

    // Stop the EditText cursor from blinking
    protected void takeAwayFocusFromSearch() {
        focusSink.requestFocus();
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.refreshContent(setSearch.getText().toString());
    }

    @OnTextChanged(value = R.id.search_input, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void afterTextChanged(Editable input) {
        adapter.refreshContent(input.toString());
        voiceSearch.setVisibility(input.length() == 0 ? View.VISIBLE : View.GONE);
        clearSearch.setVisibility(input.length() == 0 ? View.GONE : View.VISIBLE);
    }

    @OnClick(R.id.clear_search)
    public void clearSearch() {
        setSearch.setText("");
    }

    @OnClick(R.id.add_flashcard_set)
    public void addSet() {
        createFlashcardSetDialog.show();
    }

    @OnClick(R.id.download_sets_button)
    public void downloadFlashcards() {
        MainActivity activity = (MainActivity) getActivity();
        activity.loadQuizletSetSearch();
    }

    @OnClick(R.id.create_set_button)
    public void createSet() {
        createFlashcardSetDialog.show();
    }

    @OnClick(R.id.restore_sets_button)
    public void restoreSets() {
        Intent intent = new Intent(getActivity(), BackupAndRestoreActivity.class)
                .putExtra(Constants.GO_TO_RESTORE_IMMEDIATELY_KEY, true);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_left_out, R.anim.slide_left_in);
    }

    @OnClick(R.id.share_with_nearby_button)
    public void shareWithNearby() {
        startActivity(new Intent(getActivity(), NearbySharingActivity.class));
        getActivity().overridePendingTransition(R.anim.slide_left_out, R.anim.slide_left_in);
    }

    private final CreateFlashcardSetDialog.Listener setCreatedListener =
            new CreateFlashcardSetDialog.Listener() {
                @Override
                public void onFlashcardSetCreated(int createdSetId) {
                    adapter.refreshContent(setSearch.getText().toString());
                    Intent intent = new Intent(getActivity(), EditFlashcardSetActivity.class);
                    intent.putExtra(Constants.FLASHCARD_SET_ID_KEY, createdSetId);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.slide_left_out, R.anim.slide_left_in);
                }
            };

    @Override
    public void browseFlashcardSet(FlashcardSet flashcardSet) {
        if (flashcardSet.getFlashcards().isEmpty()) {
            UIUtils.showSnackbar(
                    parent,
                    getString(R.string.no_flashcards_for_browsing),
                    Snackbar.LENGTH_LONG);
        } else {
            startActivity(new Intent(
                    getActivity(), BrowseFlashcardsActivity.class)
                    .putExtra(Constants.FLASHCARD_SET_ID_KEY, flashcardSet.getId()));
            getActivity().overridePendingTransition(R.anim.slide_left_out, R.anim.slide_left_in);
        }
    }

    @Override
    public void takeQuiz(FlashcardSet flashcardSet) {
        if (flashcardSet.getFlashcards().size() < 2) {
            UIUtils.showSnackbar(
                    parent,
                    getString(R.string.not_enough_for_quiz),
                    Snackbar.LENGTH_LONG);
        } else {
            startActivity(new Intent(
                    getActivity(), QuizSettingsActivity.class)
                    .putExtra(Constants.FLASHCARD_SET_ID_KEY, flashcardSet.getId()));
            getActivity().overridePendingTransition(R.anim.slide_in_bottom, R.anim.stay);
        }
    }

    @Override
    public void editFlashcardSet(FlashcardSet flashcardSet) {
        startActivity(new Intent(
                getActivity(), EditFlashcardSetActivity.class)
                .putExtra(Constants.FLASHCARD_SET_ID_KEY, flashcardSet.getId()));
        getActivity().overridePendingTransition(R.anim.slide_left_out, R.anim.slide_left_in);
    }

    @Override
    public void deleteFlashcardSet(FlashcardSet flashcardSet) {
        deleteFlashcardSetDialog.show(flashcardSet.getId());
    }

    @Override
    public void onFlashcardSetDeleted() {

        adapter.onFlashcardSetDeleted();
    }

    @Override
    public void onContentUpdated(int numSets) {
        if (DatabaseManager.get().getNumFlashcardSets() == 0) {
            searchBar.setVisibility(View.GONE);
            setsContainer.setVisibility(View.GONE);
            noSetsMatch.setVisibility(View.GONE);
            noSetsAtAll.setVisibility(View.VISIBLE);
        } else {
            noSetsAtAll.setVisibility(View.GONE);
            searchBar.setVisibility(View.VISIBLE);
            if (numSets == 0) {
                setsContainer.setVisibility(View.GONE);
                noSetsMatch.setVisibility(View.VISIBLE);
            } else {
                noSetsMatch.setVisibility(View.GONE);
                setsContainer.setVisibility(View.VISIBLE);
            }
        }
    }

    @OnClick(R.id.voice_search)
    public void searchWithVoice() {
        showGoogleSpeechDialog();
    }

    private void showGoogleSpeechDialog() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.speech_message));
        try {
            startActivityForResult(intent, SPEECH_REQUEST_CODE);
            getActivity().overridePendingTransition(R.anim.stay, R.anim.slide_in_bottom);
        } catch (ActivityNotFoundException exception) {
            UIUtils.showLongToast(R.string.speech_not_supported, getContext());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SPEECH_REQUEST_CODE:
                if (resultCode != Activity.RESULT_OK || data == null) {
                    return;
                }
                List<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                if (result == null || result.isEmpty()) {
                    UIUtils.showLongToast(R.string.speech_unrecognized, getContext());
                    return;
                }
                String searchInput = StringUtils.capitalizeFirstWord(result.get(0));
                setSearch.setText(searchInput);
                break;
        }
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        takeAwayFocusFromSearch();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        createFlashcardSetDialog.cleanUp();
        deleteFlashcardSetDialog.cleanUp();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.findItem(R.id.filter).setVisible(false);
    }
}
