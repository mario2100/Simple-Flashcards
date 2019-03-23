package com.randomappsinc.simpleflashcards.theme;

import android.content.Context;
import android.util.AttributeSet;

import com.randomappsinc.simpleflashcards.R;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;

public class ThemedEditText extends AppCompatEditText implements ThemeManager.Listener {

    private ThemeManager themeManager;
    private int normalModeTextColor;
    private int normalModeHintColor;
    private int darkModeTextColor;
    private int darkModeHintColor;

    public ThemedEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        themeManager = ThemeManager.get();
        normalModeTextColor = ContextCompat.getColor(context, R.color.dark_gray);
        normalModeHintColor = ContextCompat.getColor(context, R.color.gray);
        darkModeTextColor = ContextCompat.getColor(context, R.color.white);
        darkModeHintColor = ContextCompat.getColor(context, R.color.half_white);

        setTextColor(themeManager.getDarkModeEnabled(context) ? darkModeTextColor : normalModeTextColor);
        setHintTextColor(themeManager.getDarkModeEnabled(context) ? darkModeHintColor : normalModeHintColor);
    }

    @Override
    public void onThemeChanged(boolean darkModeEnabled) {
        setTextColor(darkModeEnabled ? darkModeTextColor : normalModeTextColor);
        setHintTextColor(darkModeEnabled ? darkModeHintColor : normalModeHintColor);
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
