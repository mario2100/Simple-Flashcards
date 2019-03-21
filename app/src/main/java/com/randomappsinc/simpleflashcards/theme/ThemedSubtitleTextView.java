package com.randomappsinc.simpleflashcards.theme;

import android.content.Context;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.randomappsinc.simpleflashcards.R;

public class ThemedSubtitleTextView extends AppCompatTextView implements ThemeManager.Listener {

    private ThemeManager themeManager;
    private int normalModeColor;
    private int darkModeColor;

    public ThemedSubtitleTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        themeManager = ThemeManager.get();
        normalModeColor = ContextCompat.getColor(context, R.color.dark_gray);
        darkModeColor = ContextCompat.getColor(context, R.color.three_fourths_white);

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
