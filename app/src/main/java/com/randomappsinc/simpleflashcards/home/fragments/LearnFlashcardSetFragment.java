package com.randomappsinc.simpleflashcards.home.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.randomappsinc.simpleflashcards.R;
import com.randomappsinc.simpleflashcards.browse.activities.BrowseFlashcardsActivity;
import com.randomappsinc.simpleflashcards.common.constants.Constants;
import com.randomappsinc.simpleflashcards.common.views.SimpleDividerItemDecoration;
import com.randomappsinc.simpleflashcards.home.adapters.FlashcardSetOptionsAdapter;
import com.randomappsinc.simpleflashcards.persistence.DatabaseManager;
import com.randomappsinc.simpleflashcards.persistence.models.FlashcardSetDO;
import com.randomappsinc.simpleflashcards.quiz.activities.QuizSettingsActivity;
import com.randomappsinc.simpleflashcards.utils.UIUtils;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class LearnFlashcardSetFragment extends Fragment implements FlashcardSetOptionsAdapter.ItemSelectionListener {

    public static LearnFlashcardSetFragment getInstance(int setId) {
        LearnFlashcardSetFragment fragment = new LearnFlashcardSetFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.FLASHCARD_SET_ID_KEY, setId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @BindView(R.id.learn_set_options) RecyclerView learnSetOptions;

    private int setId;
    private DatabaseManager databaseManager = DatabaseManager.get();
    private Unbinder unbinder;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.learn_flashcard_set_fragment,
                container,
                false);
        setId = getArguments().getInt(Constants.FLASHCARD_SET_ID_KEY);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        learnSetOptions.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        learnSetOptions.setAdapter(new FlashcardSetOptionsAdapter(
                getActivity(),
                this,
                R.array.flashcard_set_learning_options,
                R.array.flashcard_set_learning_icons));
    }

    @Override
    public void onItemClick(int position) {
        FlashcardSetDO flashcardSetDO = databaseManager.getFlashcardSet(setId);
        switch (position) {
            case 0:
                if (flashcardSetDO.getFlashcards().isEmpty()) {
                    UIUtils.showLongToast(R.string.no_flashcards_for_browsing, getContext());
                } else {
                    startActivity(new Intent(
                            getActivity(), BrowseFlashcardsActivity.class)
                            .putExtra(Constants.FLASHCARD_SET_ID_KEY, setId));
                    getActivity().overridePendingTransition(R.anim.slide_left_out, R.anim.slide_left_in);
                }
                break;
            case 1:
                if (flashcardSetDO.getFlashcards().size() < 2) {
                    UIUtils.showLongToast(R.string.not_enough_for_quiz, getContext());
                } else {
                    startActivity(new Intent(
                            getActivity(), QuizSettingsActivity.class)
                            .putExtra(Constants.FLASHCARD_SET_ID_KEY, setId));
                    getActivity().overridePendingTransition(R.anim.slide_in_bottom, R.anim.stay);
                }
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
