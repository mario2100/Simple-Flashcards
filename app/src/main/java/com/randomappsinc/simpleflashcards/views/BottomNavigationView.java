package com.randomappsinc.simpleflashcards.views;

import android.content.Context;
import android.support.annotation.IdRes;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.randomappsinc.simpleflashcards.R;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BottomNavigationView extends LinearLayout {

    public interface Listener {
        void onNavItemSelected(@IdRes int viewId);
    }

    @BindView(R.id.home) TextView homeButton;
    @BindView(R.id.search) TextView searchButton;
    @BindView(R.id.settings) TextView settingsButton;
    @BindColor(R.color.dark_gray) int darkGray;
    @BindColor(R.color.app_blue) int blue;

    private Listener listener;
    private TextView currentlySelected;

    public BottomNavigationView(Context context) {
        this(context, null, 0);
    }

    public BottomNavigationView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomNavigationView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        inflate(getContext(), R.layout.bottom_navigation, this);
        ButterKnife.bind(this);
        currentlySelected = homeButton;
        homeButton.setTextColor(blue);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @OnClick(R.id.home)
    public void onHomeClicked() {
        if (currentlySelected == homeButton) {
            return;
        }

        currentlySelected.setTextColor(darkGray);
        currentlySelected = homeButton;
        homeButton.setTextColor(blue);
        listener.onNavItemSelected(R.id.home);
    }

    @OnClick(R.id.search)
    public void onSearchClicked() {
        if (currentlySelected == searchButton) {
            return;
        }

        currentlySelected.setTextColor(darkGray);
        searchButton.setTextColor(blue);
        currentlySelected = searchButton;
        listener.onNavItemSelected(R.id.search);
    }

    @OnClick(R.id.settings)
    public void onProfileClicked() {
        if (currentlySelected == settingsButton) {
            return;
        }

        currentlySelected.setTextColor(darkGray);
        settingsButton.setTextColor(blue);
        currentlySelected = settingsButton;
        listener.onNavItemSelected(R.id.settings);
    }
}
