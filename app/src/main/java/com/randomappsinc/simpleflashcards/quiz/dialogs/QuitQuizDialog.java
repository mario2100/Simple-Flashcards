package com.randomappsinc.simpleflashcards.quiz.dialogs;

import android.content.Context;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.randomappsinc.simpleflashcards.R;
import com.randomappsinc.simpleflashcards.theme.ThemeManager;

public class QuitQuizDialog implements ThemeManager.Listener {

    public interface Listener {
        void onQuitQuizConfirmed();
    }

    private MaterialDialog dialog;
    private Context context;
    protected Listener listener;
    private ThemeManager themeManager = ThemeManager.get();

    public QuitQuizDialog(Context context, @NonNull Listener listener) {
        this.context = context;
        this.listener = listener;
        createDialog();
        themeManager.registerListener(this);
    }

    private void createDialog() {
        dialog = new MaterialDialog.Builder(context)
                .theme(themeManager.getDarkModeEnabled(context) ? Theme.DARK : Theme.LIGHT)
                .title(R.string.confirm_quiz_exit)
                .content(R.string.confirm_quiz_exit_body)
                .positiveText(R.string.yes)
                .negativeText(R.string.cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        listener.onQuitQuizConfirmed();
                    }
                })
                .build();
    }

    public void show() {
        dialog.show();
    }

    @Override
    public void onThemeChanged(boolean darkModeEnabled) {
        createDialog();
    }

    public void cleanUp() {
        context = null;
        listener = null;
        themeManager.unregisterListener(this);
    }
}
