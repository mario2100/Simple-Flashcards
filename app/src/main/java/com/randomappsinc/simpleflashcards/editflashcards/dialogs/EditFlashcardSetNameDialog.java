package com.randomappsinc.simpleflashcards.editflashcards.dialogs;


import android.content.Context;

import androidx.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.randomappsinc.simpleflashcards.R;
import com.randomappsinc.simpleflashcards.theme.ThemeManager;

public class EditFlashcardSetNameDialog implements ThemeManager.Listener {

    public interface Listener {
        void onFlashcardSetRename(String newSetName);
    }

    private MaterialDialog dialog;
    private Context context;
    protected Listener listener;
    private ThemeManager themeManager = ThemeManager.get();

    public EditFlashcardSetNameDialog(Context context, String initialSetName, @NonNull Listener listener) {
        this.context = context;
        this.listener = listener;
        createDialog(initialSetName);
        themeManager.registerListener(this);
    }

    private void createDialog(String currentName) {
        dialog = new MaterialDialog.Builder(context)
                .theme(themeManager.getDarkModeEnabled(context) ? Theme.DARK : Theme.LIGHT)
                .title(R.string.rename_flashcard_set_title)
                .alwaysCallInputCallback()
                .input(context.getString(R.string.flashcard_set_name),
                        currentName,
                        new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                                String title = input.toString();
                                boolean notEmpty = !title.trim().isEmpty();
                                dialog.getActionButton(DialogAction.POSITIVE).setEnabled(notEmpty);
                            }
                        })
                .positiveText(R.string.save)
                .negativeText(android.R.string.cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        String newTitle = dialog.getInputEditText().getText().toString().trim();
                        listener.onFlashcardSetRename(newTitle);
                    }
                })
                .build();
    }

    public void show() {
        dialog.show();
    }

    @Override
    public void onThemeChanged(boolean darkModeEnabled) {
        createDialog(dialog.getInputEditText().getText().toString());
    }

    public void cleanUp() {
        context = null;
        listener = null;
        themeManager.unregisterListener(this);
    }
}
