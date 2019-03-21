package com.randomappsinc.simpleflashcards.folders.dialogs;

import android.content.Context;
import androidx.annotation.NonNull;
import android.text.InputType;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.randomappsinc.simpleflashcards.R;
import com.randomappsinc.simpleflashcards.theme.ThemeManager;

public class CreateFolderDialog implements ThemeManager.Listener {

    public interface Listener {
        void onNewFolderSubmitted(String folderName);
    }

    private Context context;
    protected Listener listener;
    private MaterialDialog adderDialog;
    private ThemeManager themeManager = ThemeManager.get();

    public CreateFolderDialog(Context context, @NonNull Listener listener) {
        this.context = context;
        this.listener = listener;
        createDialog();
        themeManager.registerListener(this);
    }

    private void createDialog() {
        adderDialog = new MaterialDialog.Builder(context)
                .theme(themeManager.getDarkModeEnabled(context) ? Theme.DARK : Theme.LIGHT)
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

    @Override
    public void onThemeChanged(boolean darkModeEnabled) {
        createDialog();
    }

    public void show() {
        adderDialog.getInputEditText().setText("");
        adderDialog.show();
    }

    public void cleanUp() {
        context = null;
        listener = null;
        themeManager.unregisterListener(this);
    }
}
