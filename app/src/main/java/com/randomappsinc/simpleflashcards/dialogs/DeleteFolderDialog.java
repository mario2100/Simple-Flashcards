package com.randomappsinc.simpleflashcards.dialogs;

import android.content.Context;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.randomappsinc.simpleflashcards.R;
import com.randomappsinc.simpleflashcards.models.Folder;

public class DeleteFolderDialog {

    public interface Listener {
        void onFolderDeleted(Folder folder);
    }

    private MaterialDialog dialog;
    protected Folder folder;

    public DeleteFolderDialog(Context context, @NonNull final Listener listener) {
        dialog = new MaterialDialog.Builder(context)
                .title(R.string.folder_delete_title)
                .content(R.string.folder_delete_body)
                .positiveText(R.string.yes)
                .negativeText(R.string.cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        listener.onFolderDeleted(folder);
                    }
                })
                .build();
    }

    public void show(Folder folder) {
        this.folder = folder;
        Context context = dialog.getContext();
        String nameWithQuotes = "\"" + folder.getName() + "\"";
        dialog.setContent(context.getString(R.string.folder_delete_body, nameWithQuotes));
        dialog.show();
    }
}
