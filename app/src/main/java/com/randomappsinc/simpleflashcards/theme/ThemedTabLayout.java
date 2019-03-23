package com.randomappsinc.simpleflashcards.theme;

import android.content.Context;
import android.util.AttributeSet;

import com.google.android.material.tabs.TabLayout;
import com.randomappsinc.simpleflashcards.R;

import androidx.core.content.ContextCompat;

public class ThemedTabLayout extends TabLayout implements ThemeManager.Listener {

    private ThemeManager themeManager;
    private int normalModeColor;
    private int darkModeColor;

    public ThemedTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        themeManager = ThemeManager.get();
        normalModeColor = ContextCompat.getColor(context, R.color.app_blue);
        darkModeColor = ContextCompat.getColor(context, R.color.dark_mode_toolbar_black);

        setBackgroundColor(themeManager.getDarkModeEnabled(context) ? darkModeColor : normalModeColor);
    }

    @Override
    public void onThemeChanged(boolean darkModeEnabled) {
        setBackgroundColor(darkModeEnabled ? darkModeColor : normalModeColor);
    }

    @Override
    public void onAttachedToWindow() {
        themeManager.registerListener(this);
        super.onAttachedToWindow();
    }

    @Override
    public void onDetachedFromWindow() {
        themeManager.unregisterListener(this);
        super.onDetachedFromWindow();
    }
}
