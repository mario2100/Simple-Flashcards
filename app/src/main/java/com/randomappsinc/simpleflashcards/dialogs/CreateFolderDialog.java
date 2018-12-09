package com.randomappsinc.simpleflashcards.dialogs;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.InputType;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.randomappsinc.simpleflashcards.R;

public class CreateFolderDialog {

    public interface Listener {
        void onNewFolderSubmitted(String folderName);
    }

    private MaterialDialog adderDialog;

    public CreateFolderDialog(Context context, @NonNull final Listener listener) {
        adderDialog = new MaterialDialog.Builder(context)
                .title(R.string.create_folder)
                .alwaysCallInputCallback()
                .inputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS)
                .input(context.getString(R.string.folder_name),
                        "",
                        new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                                String folderName = input.toString();
                                boolean notEmpty = !folderName.trim().isEmpty();
                                dialog.getActionButton(DialogAction.POSITIVE).setEnabled(notEmpty);
                            }
                        })
                .positiveText(R.string.create)
                .negativeText(android.R.string.cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        String folderName = dialog.getInputEditText().getText().toString().trim();
                        listener.onNewFolderSubmitted(folderName);
                    }
                })
                .build();
    }

    public void show() {
        adderDialog.getInputEditText().setText("");
        adderDialog.show();
    }
}
