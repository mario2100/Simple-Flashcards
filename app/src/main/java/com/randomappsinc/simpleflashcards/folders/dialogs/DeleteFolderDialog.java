package com.randomappsinc.simpleflashcards.folders.dialogs;

import android.content.Context;

import androidx.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.randomappsinc.simpleflashcards.R;
import com.randomappsinc.simpleflashcards.folders.models.Folder;
import com.randomappsinc.simpleflashcards.theme.ThemeManager;

public class DeleteFolderDialog implements ThemeManager.Listener {

    public interface Listener {
        void onFolderDeleted(Folder folder);
    }

    private Context context;
    protected Listener listener;
    private MaterialDialog dialog;
    protected Folder folder;
    private ThemeManager themeManager = ThemeManager.get();

    public DeleteFolderDialog(Context context, @NonNull Listener listener) {
        this.context = context;
        this.listener = listener;
        createDialog();
        themeManager.registerListener(this);
    }

    private void createDialog() {
        dialog = new MaterialDialog.Builder(context)
                .theme(themeManager.getDarkModeEnabled(context) ? Theme.DARK : Theme.LIGHT)
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

    @Override
    public void onThemeChanged(boolean darkModeEnabled) {
        createDialog();
    }

    public void show(Folder folder) {
        this.folder = folder;
        Context context = dialog.getContext();
        String nameWithQuotes = "\"" + folder.getName() + "\"";
        dialog.setContent(context.getString(R.string.folder_delete_body, nameWithQuotes));
        dialog.show();
    }

    public void cleanUp() {
        context = null;
        listener = null;
        themeManager.unregisterListener(this);
    }
}
