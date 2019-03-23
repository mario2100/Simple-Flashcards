package com.randomappsinc.simpleflashcards.backupandrestore.adapters;

import com.randomappsinc.simpleflashcards.backupandrestore.fragments.BackupDataFragment;
import com.randomappsinc.simpleflashcards.backupandrestore.fragments.RestoreDataFragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class BackupAndRestoreTabsAdapter extends FragmentPagerAdapter {

    private String[] tabNames;

    public BackupAndRestoreTabsAdapter(FragmentManager fragmentManager, String[] tabNames) {
        super(fragmentManager);
        this.tabNames = tabNames;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new BackupDataFragment();
            case 1:
                return new RestoreDataFragment();
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
