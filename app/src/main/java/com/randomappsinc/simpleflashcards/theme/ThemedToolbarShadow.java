package com.randomappsinc.simpleflashcards.theme;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.randomappsinc.simpleflashcards.R;

public class ThemedToolbarShadow extends View implements ThemeManager.Listener {

    private ThemeManager themeManager;

    public ThemedToolbarShadow(Context context, AttributeSet attrs) {
        super(context, attrs);
        themeManager = ThemeManager.get();
        setBackgroundResource(themeManager.getDarkModeEnabled(context)
                ? R.drawable.toolbar_shadow_dark_mode
                : R.drawable.toolbar_shadow);
    }

    @Override
    public void onThemeChanged(boolean darkModeEnabled) {
        setBackgroundResource(darkModeEnabled ? R.drawable.toolbar_shadow_dark_mode : R.drawable.toolbar_shadow);
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
