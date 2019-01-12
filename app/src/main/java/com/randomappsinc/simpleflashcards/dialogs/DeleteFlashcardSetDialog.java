package com.randomappsinc.simpleflashcards.dialogs;

import android.content.Context;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.randomappsinc.simpleflashcards.R;
import com.randomappsinc.simpleflashcards.persistence.DatabaseManager;
import com.randomappsinc.simpleflashcards.theme.ThemeManager;

public class DeleteFlashcardSetDialog implements ThemeManager.Listener {

    public interface Listener {
        void onFlashcardSetDeleted();
    }

    private Context context;
    protected Listener listener;
    private MaterialDialog dialog;
    protected int flashcardSetId;
    private ThemeManager themeManager = ThemeManager.get();

    public DeleteFlashcardSetDialog(Context context, @NonNull final Listener listener) {
        this.context = context;
        this.listener = listener;
        createDialog();
        themeManager.registerListener(this);
    }

    private void createDialog() {
        dialog = new MaterialDialog.Builder(context)
                .theme(themeManager.getDarkModeEnabled(context) ? Theme.DARK : Theme.LIGHT)
                .title(R.string.flashcard_set_delete_title)
                .content(R.string.flashcard_set_delete_message)
                .positiveText(R.string.yes)
                .negativeText(R.string.cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        DatabaseManager.get().deleteFlashcardSet(flashcardSetId);
                        listener.onFlashcardSetDeleted();
                    }
                })
                .build();
    }

    @Override
    public void onThemeChanged(boolean darkModeEnabled) {
        createDialog();
    }

    public void show(int flashcardSetId) {
        this.flashcardSetId = flashcardSetId;
        dialog.show();
    }

    public void cleanUp() {
        context = null;
        listener = null;
        themeManager.unregisterListener(this);
    }
}
