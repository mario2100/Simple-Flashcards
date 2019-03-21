package com.randomappsinc.simpleflashcards.folders.dialogs;

import android.content.Context;
import androidx.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.randomappsinc.simpleflashcards.R;
import com.randomappsinc.simpleflashcards.theme.ThemeManager;

public class RenameFolderDialog implements ThemeManager.Listener {

    public interface Listener {
        void onFolderName(String newFolderName);
    }

    private MaterialDialog dialog;
    private Context context;
    protected Listener listener;
    private ThemeManager themeManager = ThemeManager.get();

    public RenameFolderDialog(Context context, String initialName, @NonNull Listener listener) {
        this.context = context;
        this.listener = listener;
        createDialog(initialName);
        themeManager.registerListener(this);
    }

    private void createDialog(String initialName) {
        dialog = new MaterialDialog.Builder(context)
                .theme(themeManager.getDarkModeEnabled(context) ? Theme.DARK : Theme.LIGHT)
                .title(R.string.rename_folder_title)
                .alwaysCallInputCallback()
                .input(context.getString(R.string.folder_name),
                        initialName,
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
                        listener.onFolderName(newTitle);
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
