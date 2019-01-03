package com.randomappsinc.simpleflashcards.dialogs;

import android.content.Context;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.randomappsinc.simpleflashcards.R;

public class RenameFolderDialog {

    public interface Listener {
        void onFolderName(String newFolderName);
    }

    private MaterialDialog dialog;

    public RenameFolderDialog(Context context, String initialName, @NonNull final Listener listener) {
        dialog = new MaterialDialog.Builder(context)
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
}
