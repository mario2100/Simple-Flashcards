package com.randomappsinc.simpleflashcards.theme;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.randomappsinc.simpleflashcards.R;

public class ThemedListFade extends View implements ThemeManager.Listener {

    private ThemeManager themeManager;

    public ThemedListFade(Context context, AttributeSet attrs) {
        super(context, attrs);
        themeManager = ThemeManager.get();
        setBackgroundResource(themeManager.getDarkModeEnabled(context)
                ? R.drawable.list_fade_dark_mode
                : R.drawable.list_fade);
    }

    @Override
    public void onThemeChanged(boolean darkModeEnabled) {
        setBackgroundResource(darkModeEnabled ? R.drawable.list_fade_dark_mode : R.drawable.list_fade);
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
