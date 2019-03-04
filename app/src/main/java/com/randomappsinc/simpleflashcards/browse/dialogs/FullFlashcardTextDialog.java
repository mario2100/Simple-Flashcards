package com.randomappsinc.simpleflashcards.browse.dialogs;

import android.content.Context;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.randomappsinc.simpleflashcards.R;
import com.randomappsinc.simpleflashcards.theme.ThemeManager;

/** Shows the complete term/definition when the browser flashcard pane can't show it all */
public class FullFlashcardTextDialog implements ThemeManager.Listener {

    private Context context;
    private MaterialDialog dialog;
    private ThemeManager themeManager = ThemeManager.get();

    public FullFlashcardTextDialog(Context context) {
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
        View parent = (View) dialog.getContentView().getParent();
        parent.scrollTo(0, 0);
        dialog.show();
    }

    public void cleanUp() {
        context = null;
        themeManager.unregisterListener(this);
    }
}
