package com.randomappsinc.simpleflashcards.browse.dialogs;

import android.content.Context;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.randomappsinc.simpleflashcards.R;
import com.randomappsinc.simpleflashcards.theme.ThemeManager;

public class BrowseSettingsDialogsManager implements ThemeManager.Listener {

    public interface Listener {
        void onShuffleRequested();

        void onRestoreRequested();
    }

    private MaterialDialog optionsDialog;
    protected Listener listener;
    private Context context;
    private ThemeManager themeManager = ThemeManager.get();

    public BrowseSettingsDialogsManager(Context context, Listener listener) {
        this.listener = listener;
        this.context = context;
        createDialog();
        themeManager.registerListener(this);
    }

    private void createDialog() {
        optionsDialog = new MaterialDialog.Builder(context)
                .theme(themeManager.getDarkModeEnabled(context) ? Theme.DARK : Theme.LIGHT)
                .items(R.array.browse_settings_options)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(
                            MaterialDialog dialog,
                            View itemView,
                            int position,
                            CharSequence text) {
                        switch (position) {
                            case 0:
                                listener.onShuffleRequested();
                                break;
                            case 1:
                                listener.onRestoreRequested();
                                break;
                        }
                    }
                })
                .negativeText(R.string.cancel)
                .build();
    }

    public void showOptions() {
        optionsDialog.show();
    }

    @Override
    public void onThemeChanged(boolean darkModeEnabled) {
        createDialog();
    }

    public void shutdown() {
        listener = null;
        context = null;
    }
}
