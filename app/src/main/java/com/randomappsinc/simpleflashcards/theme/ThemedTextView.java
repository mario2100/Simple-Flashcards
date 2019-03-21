package com.randomappsinc.simpleflashcards.theme;

import android.content.Context;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.randomappsinc.simpleflashcards.R;

public class ThemedTextView extends AppCompatTextView implements ThemeManager.Listener {

    private ThemeManager themeManager;
    private int normalModeColor;
    private int normalModeHintColor;
    private int darkModeColor;
    private int darkModeHintColor;

    public ThemedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        themeManager = ThemeManager.get();
        normalModeColor = ContextCompat.getColor(context, R.color.dark_gray);
        normalModeHintColor = ContextCompat.getColor(context, R.color.gray);
        darkModeColor = ContextCompat.getColor(context, R.color.white);
        darkModeHintColor = ContextCompat.getColor(context, R.color.half_white);
        setProperTextColor();
    }

    public void setProperTextColor() {
        setTextColor(themeManager.getDarkModeEnabled(getContext()) ? darkModeColor : normalModeColor);
    }

    // Call this method if you're using the TextView to show regular-style text and want to ensure proper colors
    public void setTextNormally(String text) {
        setText(text);
        setProperTextColor();
    }

    // Call this method if you're using the TextView to show hint-style text
    public void setTextAsHint(@StringRes int resId) {
        setText(resId);
        setTextColor(themeManager.getDarkModeEnabled(getContext()) ? darkModeHintColor : normalModeHintColor);
    }

    @Override
    public void onThemeChanged(boolean darkModeEnabled) {
        setProperTextColor();
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
