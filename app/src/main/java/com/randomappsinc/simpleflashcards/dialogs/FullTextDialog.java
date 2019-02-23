package com.randomappsinc.simpleflashcards.dialogs;

import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.randomappsinc.simpleflashcards.R;
import com.randomappsinc.simpleflashcards.theme.ThemeManager;

public class FullTextDialog implements ThemeManager.Listener {

    private Context context;
    private MaterialDialog dialog;
    private ThemeManager themeManager = ThemeManager.get();

    public FullTextDialog(Context context) {
        this.context = context;
        createDialog();
        themeManager.registerListener(this);
    }

    private void createDialog() {
        dialog = new MaterialDialog.Builder(context)
                .theme(themeManager.getDarkModeEnabled(context) ? Theme.DARK : Theme.LIGHT)
                .title("")
                .content("")
                .alwaysCallInputCallback()
                .positiveText(R.string.done)
                .build();
    }

    @Override
    public void onThemeChanged(boolean darkModeEnabled) {
        createDialog();
    }

    public void show(String content, boolean isTerm) {
        dialog.setTitle(isTerm ? R.string.term : R.string.definition);
        dialog.setContent(content);
        dialog.show();
    }

    public void cleanUp() {
        context = null;
        themeManager.unregisterListener(this);
    }
}
