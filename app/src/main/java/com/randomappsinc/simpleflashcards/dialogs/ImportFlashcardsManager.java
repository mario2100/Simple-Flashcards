package com.randomappsinc.simpleflashcards.dialogs;

import android.content.Context;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.randomappsinc.simpleflashcards.R;
import com.randomappsinc.simpleflashcards.theme.ThemeManager;

public class ImportFlashcardsManager implements ThemeManager.Listener{

    private Context context;
    private MaterialDialog optionsDialog;
    private ThemeManager themeManager = ThemeManager.get();

    public ImportFlashcardsManager(Context context) {
        this.context = context;
        createDialogs();
        themeManager.registerListener(this);
    }

    private void createDialogs() {
        optionsDialog = new MaterialDialog.Builder(context)
                .theme(themeManager.getDarkModeEnabled(context) ? Theme.DARK : Theme.LIGHT)
                .title(R.string.import_flashcards_options_title)
                .items(context.getResources().getStringArray(R.array.import_flashcards_options))
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(
                            MaterialDialog dialog, View itemView, int position, CharSequence text) {

                    }
                })
                .positiveText(R.string.cancel)
                .build();
    }

    @Override
    public void onThemeChanged(boolean darkModeEnabled) {
        createDialogs();
    }

    public void startImport() {
        optionsDialog.show();
    }

    public void cleanUp() {
        context = null;
        themeManager.unregisterListener(this);
    }
}
