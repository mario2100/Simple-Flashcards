package com.randomappsinc.simpleflashcards.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.joanzapata.iconify.Icon;
import com.joanzapata.iconify.IconDrawable;
import com.randomappsinc.simpleflashcards.R;

import androidx.annotation.StringRes;

public class UIUtils {

    public static void closeKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager == null) {
            return;
        }
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void showSnackbar(View parent, String message, int length) {
        SpannableStringBuilder spanBuilder = new SpannableStringBuilder(message);
        spanBuilder.setSpan(
                new ForegroundColorSpan(Color.WHITE),
                0,
                message.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        Snackbar snackbar = Snackbar.make(parent, message, length);
        View rootView = snackbar.getView();
        snackbar.getView().setBackgroundColor(parent
                .getContext()
                .getResources()
                .getColor(R.color.app_blue));
        snackbar.show();
    }

    public static void showShortToast(@StringRes int stringId, Context context) {
        showToast(stringId, Toast.LENGTH_SHORT, context);
    }

    public static void showLongToast(@StringRes int stringId, Context context) {
        showToast(stringId, Toast.LENGTH_LONG, context);
    }

    private static void showToast(@StringRes int stringId, int duration, Context context) {
        Toast.makeText(context, stringId, duration).show();
    }

    public static void showShortToast(String text, Context context) {
        showToast(text, Toast.LENGTH_SHORT, context);
    }

    public static void showLongToast(String text, Context context) {
        showToast(text, Toast.LENGTH_LONG, context);
    }

    private static void showToast(String text, int duration, Context context) {
        Toast.makeText(context, text, duration).show();
    }

    public static void loadMenuIcon(Menu menu, int itemId, Icon icon, Context context) {
        menu.findItem(itemId).setIcon(
                new IconDrawable(context, icon)
                        .colorRes(R.color.white)
                        .actionBarSize());
    }

    public static void setCheckedImmediately(CompoundButton checkableView, boolean checked) {
        checkableView.setChecked(checked);
        checkableView.jumpDrawablesToCurrentState();
    }
}
