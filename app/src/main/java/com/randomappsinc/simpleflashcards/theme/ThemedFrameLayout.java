package com.randomappsinc.simpleflashcards.theme;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.randomappsinc.simpleflashcards.R;

import androidx.core.content.ContextCompat;

public class ThemedFrameLayout extends FrameLayout implements ThemeManager.Listener {

    private ThemeManager themeManager;
    private int normalModeColor;
    private int darkModeColor;

    public ThemedFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        themeManager = ThemeManager.get();
        normalModeColor = ContextCompat.getColor(context, R.color.white);
        darkModeColor = ContextCompat.getColor(context, R.color.dark_mode_black);

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
