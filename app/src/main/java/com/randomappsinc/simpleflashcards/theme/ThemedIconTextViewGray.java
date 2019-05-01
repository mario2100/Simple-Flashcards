package com.randomappsinc.simpleflashcards.theme;

import android.content.Context;
import android.util.AttributeSet;

import androidx.core.content.ContextCompat;

import com.joanzapata.iconify.widget.IconTextView;
import com.randomappsinc.simpleflashcards.R;

public class ThemedIconTextViewGray extends IconTextView implements ThemeManager.Listener {

    private ThemeManager themeManager;
    private int normalModeColor;
    private int darkModeColor;

    public ThemedIconTextViewGray(Context context, AttributeSet attrs) {
        super(context, attrs);
        themeManager = ThemeManager.get();
        normalModeColor = ContextCompat.getColor(context, R.color.gray);
        darkModeColor = ContextCompat.getColor(context, R.color.half_white);

        setTextColor(themeManager.getDarkModeEnabled(context) ? darkModeColor : normalModeColor);
    }

    @Override
    public void onThemeChanged(boolean darkModeEnabled) {
        setTextColor(darkModeEnabled ? darkModeColor : normalModeColor);
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
