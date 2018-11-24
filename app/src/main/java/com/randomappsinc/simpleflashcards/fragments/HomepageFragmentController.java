package com.randomappsinc.simpleflashcards.fragments;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.randomappsinc.simpleflashcards.R;

public class HomepageFragmentController {

    private FragmentManager fragmentManager;
    private int containerId;
    private HomepageFragment homepageFragment;
    private QuizletSearchFragment searchFragment;
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
