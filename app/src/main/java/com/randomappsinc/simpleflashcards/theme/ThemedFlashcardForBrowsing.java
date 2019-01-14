package com.randomappsinc.simpleflashcards.theme;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.randomappsinc.simpleflashcards.R;

public class ThemedFlashcardForBrowsing extends RelativeLayout implements ThemeManager.Listener {

    private ThemeManager themeManager;

    public ThemedFlashcardForBrowsing(Context context, AttributeSet attrs) {
        super(context, attrs);
        themeManager = ThemeManager.get();
        setBackgroundResource(themeManager.getDarkModeEnabled(context)
                ? R.drawable.rounded_rectangle_card_black
                : R.drawable.rounded_white_rectangle);
    }

    @Override
    public void onThemeChanged(boolean darkModeEnabled) {
        setBackgroundResource(darkModeEnabled
                ? R.drawable.rounded_rectangle_card_black
                : R.drawable.rounded_white_rectangle);
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
