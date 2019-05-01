package com.randomappsinc.simpleflashcards.browse.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.randomappsinc.simpleflashcards.browse.fragments.BrowseFlashcardFragment;
import com.randomappsinc.simpleflashcards.browse.managers.BrowseFlashcardsSettingsManager;
import com.randomappsinc.simpleflashcards.common.models.Flashcard;
import com.randomappsinc.simpleflashcards.persistence.DatabaseManager;
import com.randomappsinc.simpleflashcards.persistence.models.FlashcardDO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FlashcardsBrowsingAdapter extends FragmentStatePagerAdapter {

    private List<Flashcard> originalFlashcards = new ArrayList<>();
    private List<Flashcard> filteredFlashcards = new ArrayList<>();
    private boolean isShuffled = false;
    private boolean doNotShowLearned;

    public FlashcardsBrowsingAdapter(FragmentManager fragmentManager, int setId) {
        super(fragmentManager);
        doNotShowLearned = BrowseFlashcardsSettingsManager.get().getDoNotShowLearned();
        List<FlashcardDO> flashcards = DatabaseManager.get().getAllFlashcards(setId);
        for (FlashcardDO flashcardDO : flashcards) {
            Flashcard flashcard = new Flashcard(flashcardDO);
            originalFlashcards.add(flashcard);
            if ((!doNotShowLearned || !flashcard.isLearned())) {
                filteredFlashcards.add(flashcard);
            }
        }
    }

    public void shuffle() {
        isShuffled = true;
        Collections.shuffle(filteredFlashcards);
    }

    public void restoreOrder() {
        isShuffled = false;
        filteredFlashcards.clear();
        for (Flashcard flashcard : originalFlashcards) {
            if ((!doNotShowLearned || !flashcard.isLearned())) {
                filteredFlashcards.add(flashcard);
            }
        }
    }

    public boolean isShuffled() {
        return isShuffled;
    }

    public boolean getDoNotShowLearned() {
        return doNotShowLearned;
    }

    public void removeFlashcard(int index) {
        filteredFlashcards.remove(index);
        notifyDataSetChanged();
    }

    public void setDoNotShowLearned(boolean doNotShowLearned) {
        this.doNotShowLearned = doNotShowLearned;
        filteredFlashcards.clear();
        for (Flashcard flashcard : originalFlashcards) {
            if ((!doNotShowLearned || !flashcard.isLearned())) {
                filteredFlashcards.add(flashcard);
            }
            if (isShuffled) {
                Collections.shuffle(filteredFlashcards);
            }
        }
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return BrowseFlashcardFragment.create(filteredFlashcards.get(position));
    }

    @Override
    public int getCount() {
        return filteredFlashcards.size();
    }
}
