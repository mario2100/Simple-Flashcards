package com.randomappsinc.simpleflashcards.theme;

import android.content.Context;
import android.util.AttributeSet;

import com.randomappsinc.simpleflashcards.R;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

public class ThemedSpinnerTextView extends AppCompatTextView implements ThemeManager.Listener {

    private ThemeManager themeManager;
    private int normalModeTextColor;
    private int darkModeTextColor;
    private int normalModeBackgroundColor;
    private int darkModeBackgroundColor;

    public ThemedSpinnerTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        themeManager = ThemeManager.get();
        normalModeTextColor = ContextCompat.getColor(context, R.color.dark_gray);
        darkModeTextColor = ContextCompat.getColor(context, R.color.white);
        normalModeBackgroundColor = ContextCompat.getColor(context, R.color.white);
        darkModeBackgroundColor = ContextCompat.getColor(context, R.color.dialog_dark_background);
        setColors();
    }

    @Override
    public void onThemeChanged(boolean darkModeEnabled) {
        setColors();
    }

    private void setColors() {
        setTextColor(themeManager.getDarkModeEnabled(getContext()) ? darkModeTextColor : normalModeTextColor);
        if (themeManager.getDarkModeEnabled(getContext())) {
            setBackgroundColor(darkModeBackgroundColor);
        } else {
            setBackgroundColor(normalModeBackgroundColor);
        }
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
