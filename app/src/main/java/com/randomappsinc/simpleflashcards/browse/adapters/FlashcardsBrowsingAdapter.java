package com.randomappsinc.simpleflashcards.browse.adapters;

import com.randomappsinc.simpleflashcards.browse.fragments.BrowseFlashcardFragment;
import com.randomappsinc.simpleflashcards.persistence.DatabaseManager;
import com.randomappsinc.simpleflashcards.persistence.models.FlashcardDO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class FlashcardsBrowsingAdapter extends FragmentStatePagerAdapter {

    private List<Integer> originalFlashcardIds = new ArrayList<>();
    private List<Integer> shuffledFlashcardsIds = new ArrayList<>();
    private boolean isShuffled = false;

    public FlashcardsBrowsingAdapter(FragmentManager fragmentManager, int setId) {
        super(fragmentManager);
        List<FlashcardDO> flashcards = DatabaseManager.get().getAllFlashcards(setId);
        for (FlashcardDO flashcard : flashcards) {
            originalFlashcardIds.add(flashcard.getId());
            shuffledFlashcardsIds.add(flashcard.getId());
        }
    }

    public void shuffle() {
        isShuffled = true;
        Collections.shuffle(shuffledFlashcardsIds);
    }

    public void restoreOrder() {
        isShuffled = false;
    }

    public boolean isShuffled() {
        return isShuffled;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        List<Integer> flashcardIds = isShuffled ? shuffledFlashcardsIds : originalFlashcardIds;
        return BrowseFlashcardFragment.create(flashcardIds.get(position));
    }

    @Override
    public int getCount() {
        return originalFlashcardIds.size();
    }
}
