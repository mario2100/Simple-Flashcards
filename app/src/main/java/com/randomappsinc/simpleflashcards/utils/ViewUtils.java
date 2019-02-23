package com.randomappsinc.simpleflashcards.utils;

import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.View;
import android.view.ViewTreeObserver;

public class ViewUtils {

    public static void runOnPreDraw(final View view, final Runnable runnable) {
        view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                view.getViewTreeObserver().removeOnPreDrawListener(this);
                runnable.run();
                return false;
            }
        });
    }

    public static int measureHeightOfText(
            final CharSequence text,
            final int textSize,
            final int textViewWidth) {
        TextPaint myTextPaint = new TextPaint();
        myTextPaint.setAntiAlias(true);
        myTextPaint.setTextSize(textSize);
        Layout.Alignment alignment = Layout.Alignment.ALIGN_NORMAL;
        StaticLayout myStaticLayout = new StaticLayout(
                text,
                myTextPaint,
                textViewWidth,
                alignment,
                1f,
                0,
                false);
        return myStaticLayout.getHeight();
    }
}
