package com.randomappsinc.simpleflashcards.home.dialogs;

import android.content.Context;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.randomappsinc.simpleflashcards.R;
import com.randomappsinc.simpleflashcards.persistence.models.FlashcardSet;
import com.randomappsinc.simpleflashcards.theme.ThemeManager;

public class DeleteFlashcardSetDialog implements ThemeManager.Listener {

    public interface Listener {
        void onFlashcardSetDeleted(int flashcardSetId);
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
                .positiveText(R.string.yes)
                .negativeText(R.string.cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        listener.onFlashcardSetDeleted(flashcardSetId);
                    }
                })
                .build();
    }

    @Override
    public void onThemeChanged(boolean darkModeEnabled) {
        createDialog();
    }

    public void show(FlashcardSet flashcardSet) {
        flashcardSetId = flashcardSet.getId();
        String quotedName = "\"" + flashcardSet.getName() + "\"";
        dialog.setContent(context.getString(R.string.flashcard_set_delete_message, quotedName));
        dialog.show();
    }

    public void cleanUp() {
        context = null;
        listener = null;
        themeManager.unregisterListener(this);
    }
}
