package com.randomappsinc.simpleflashcards.theme;

import android.content.Context;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.randomappsinc.simpleflashcards.R;

public class ThemedRelativeLayout extends RelativeLayout implements ThemeManager.Listener {

    private ThemeManager themeManager;
    private int normalModeColor;
    private int darkModeColor;

    public ThemedRelativeLayout(Context context, AttributeSet attrs) {
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
