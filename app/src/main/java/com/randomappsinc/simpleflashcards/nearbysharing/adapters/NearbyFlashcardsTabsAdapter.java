package com.randomappsinc.simpleflashcards.nearbysharing.adapters;

import com.randomappsinc.simpleflashcards.nearbysharing.fragments.ReceivedFlashcardsFragment;
import com.randomappsinc.simpleflashcards.nearbysharing.fragments.SendFlashcardsFragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class NearbyFlashcardsTabsAdapter extends FragmentPagerAdapter {

    private String[] tabNames;

    public NearbyFlashcardsTabsAdapter(FragmentManager fragmentManager, String[] tabNames) {
        super(fragmentManager);
        this.tabNames = tabNames;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new SendFlashcardsFragment();
            case 1:
                return new ReceivedFlashcardsFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabNames.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabNames[position];
    }
}
