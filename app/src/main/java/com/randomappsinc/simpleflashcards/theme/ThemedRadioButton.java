package com.randomappsinc.simpleflashcards.theme;

import android.content.Context;
import android.content.res.ColorStateList;
import androidx.core.content.ContextCompat;
import androidx.core.widget.CompoundButtonCompat;
import androidx.appcompat.widget.AppCompatRadioButton;
import android.util.AttributeSet;

import com.randomappsinc.simpleflashcards.R;

public class ThemedRadioButton extends AppCompatRadioButton implements ThemeManager.Listener {

    private ThemeManager themeManager;
    private int normalModeTextColor;
    private int darkModeTextColor;
    private int unselectedColor;
    private int unselectedColorDarkMode;
    private int selectedColor;

    public ThemedRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        themeManager = ThemeManager.get();
        normalModeTextColor = ContextCompat.getColor(context, R.color.dark_gray);
        darkModeTextColor = ContextCompat.getColor(context, R.color.white);
        unselectedColor = ContextCompat.getColor(context, R.color.dark_gray);
        unselectedColorDarkMode = ContextCompat.getColor(context, R.color.white);
        selectedColor = ContextCompat.getColor(context, R.color.app_blue);

        setTextColor(themeManager.getDarkModeEnabled(context) ? darkModeTextColor : normalModeTextColor);
        setButtonTint(themeManager.getDarkModeEnabled(context));
    }

    private void setButtonTint(boolean darkModeEnabled) {
        ColorStateList colorStateList = new ColorStateList(
                new int[][] {
                        new int[]{-android.R.attr.state_checked},
                        new int[]{android.R.attr.state_checked}
                },
                new int[] {
                        darkModeEnabled ? unselectedColorDarkMode : unselectedColor,
                        selectedColor
                }
        );
        CompoundButtonCompat.setButtonTintList(this, colorStateList);
    }

    @Override
    public void onThemeChanged(boolean darkModeEnabled) {
        setTextColor(darkModeEnabled ? darkModeTextColor : normalModeTextColor);
        setButtonTint(darkModeEnabled);
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
