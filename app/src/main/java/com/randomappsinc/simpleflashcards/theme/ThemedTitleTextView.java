package com.randomappsinc.simpleflashcards.theme;

import android.content.Context;
import android.util.AttributeSet;

import com.randomappsinc.simpleflashcards.R;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

public class ThemedTitleTextView extends AppCompatTextView implements ThemeManager.Listener {

    private ThemeManager themeManager;
    private int normalModeColor;
    private int darkModeColor;

    public ThemedTitleTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        themeManager = ThemeManager.get();
        normalModeColor = ContextCompat.getColor(context, R.color.black);
        darkModeColor = ContextCompat.getColor(context, R.color.white);

        setTextColor(themeManager.getDarkModeEnabled(context) ? darkModeColor : normalModeColor);
    }

    @Override
    public void onThemeChanged(boolean darkModeEnabled) {
        setTextColor(darkModeEnabled ? darkModeColor : normalModeColor);
    }

    public void setProperColors() {
        setTextColor(themeManager.getDarkModeEnabled(getContext()) ? darkModeColor : normalModeColor);
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
