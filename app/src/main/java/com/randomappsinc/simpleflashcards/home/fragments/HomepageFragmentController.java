package com.randomappsinc.simpleflashcards.home.fragments;

import androidx.annotation.IdRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.randomappsinc.simpleflashcards.R;
import com.randomappsinc.simpleflashcards.folders.fragments.FoldersFragment;
import com.randomappsinc.simpleflashcards.quizlet.fragments.QuizletSearchFragment;

public class HomepageFragmentController {

    private FragmentManager fragmentManager;
    private int containerId;
    private HomepageFragment homepageFragment;
    private QuizletSearchFragment searchFragment;
    private FoldersFragment foldersFragment;
    private SettingsFragment settingsFragment;
    @IdRes private int currentViewId;

    public HomepageFragmentController(FragmentManager fragmentManager, int containerId) {
        this.fragmentManager = fragmentManager;
        this.containerId = containerId;
        this.homepageFragment = HomepageFragment.newInstance();
    }

    public void onNavItemSelected(@IdRes int viewId) {
        if (currentViewId == viewId) {
            return;
        }

        switch (currentViewId) {
            case R.id.home:
                hideFragment(homepageFragment);
                break;
            case R.id.search:
                hideFragment(searchFragment);
                break;
            case R.id.folders:
                hideFragment(foldersFragment);
                break;
            case R.id.settings:
                hideFragment(settingsFragment);
                break;
        }

        currentViewId = viewId;
        switch (viewId) {
            case R.id.home:
                showFragment(homepageFragment);
                break;
            case R.id.search:
                if (searchFragment == null) {
                    searchFragment = QuizletSearchFragment.newInstance();
                    addFragment(searchFragment);
                } else {
                    showFragment(searchFragment);
                }
                break;
            case R.id.folders:
                if (foldersFragment == null) {
                    foldersFragment = FoldersFragment.newInstance();
                    addFragment(foldersFragment);
                } else {
                    showFragment(foldersFragment);
                }
                break;
            case R.id.settings:
                if (settingsFragment == null) {
                    settingsFragment = SettingsFragment.newInstance();
                    addFragment(settingsFragment);
                } else {
                    showFragment(settingsFragment);
                }
                break;
        }
    }

    /** Called by the app upon start up to load the home fragment */
    public void loadHomeInitially() {
        currentViewId = R.id.home;
        addFragment(homepageFragment);
    }

    private void addFragment(Fragment fragment) {
        fragmentManager.beginTransaction().add(containerId, fragment).commit();
    }

    private void showFragment(Fragment fragment) {
        fragmentManager.beginTransaction().show(fragment).commit();
    }

    private void hideFragment(Fragment fragment) {
        fragmentManager.beginTransaction().hide(fragment).commit();
    }
}
