package com.randomappsinc.simpleflashcards.common.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;

import com.randomappsinc.simpleflashcards.R;
import com.randomappsinc.simpleflashcards.theme.ThemeManager;

public class SkeletonView extends View implements ThemeManager.Listener {

    private ValueAnimator colorAnimator;
    private ThemeManager themeManager = ThemeManager.get();
    protected float[] from = new float[3];
    protected float[] to = new float[3];
    private int normalModeStartColor;
    private int normalModeEndColor;
    private int darkModeStartColor;
    private int darkModeEndColor;

    public SkeletonView(Context context, AttributeSet attrs) {
        super(context, attrs);

        normalModeStartColor = context.getResources().getColor(R.color.gray_100);
        normalModeEndColor = context.getResources().getColor(R.color.gray_300);
        darkModeStartColor = context.getResources().getColor(R.color.gray);
        darkModeEndColor = context.getResources().getColor(R.color.dark_gray);

        Color.colorToHSV(
                themeManager.getDarkModeEnabled(context) ? darkModeStartColor : normalModeStartColor,
                from);
        Color.colorToHSV(themeManager.getDarkModeEnabled(context) ? darkModeEndColor : normalModeEndColor, to);

        colorAnimator = ValueAnimator.ofFloat(0, 1);
        colorAnimator.setDuration(context.getResources().getInteger(R.integer.skeleton_anim_length));
        colorAnimator.setRepeatCount(ValueAnimator.INFINITE);
        colorAnimator.setRepeatMode(ValueAnimator.REVERSE);

        final float[] hsv  = new float[3];
        colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // Transition along each axis of HSV (hue, saturation, value)
                hsv[0] = from[0] + (to[0] - from[0]) * animation.getAnimatedFraction();
                hsv[1] = from[1] + (to[1] - from[1]) * animation.getAnimatedFraction();
                hsv[2] = from[2] + (to[2] - from[2]) * animation.getAnimatedFraction();

                setBackgroundColor(Color.HSVToColor(hsv));
            }
        });
    }

    @Override
    public void onThemeChanged(boolean darkModeEnabled) {
        Color.colorToHSV(darkModeEnabled ? darkModeStartColor : normalModeStartColor, from);
        Color.colorToHSV(darkModeEnabled ? darkModeEndColor : normalModeEndColor, to);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        colorAnimator.start();
        themeManager.registerListener(this);
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        colorAnimator.cancel();
        themeManager.unregisterListener(this);
    }
}
