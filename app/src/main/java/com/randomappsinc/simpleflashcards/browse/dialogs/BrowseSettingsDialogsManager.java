package com.randomappsinc.simpleflashcards.browse.dialogs;

import android.content.Context;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.randomappsinc.simpleflashcards.R;
import com.randomappsinc.simpleflashcards.theme.ThemeManager;

public class BrowseSettingsDialogsManager implements ThemeManager.Listener {

    public interface Listener {
        void onTextColorChangeRequested();

        void onShuffleRequested();

        void onRestoreRequested();
    }

    private MaterialDialog optionsDialog;
    protected SetTextSizeDialog setTextSizeDialog;
    protected BrowseMoreOptionsDialog moreOptionsDialog;
    protected Listener listener;
    private Context context;
    private ThemeManager themeManager = ThemeManager.get();

    public BrowseSettingsDialogsManager(Context context, Listener listener) {
        this.listener = listener;
        this.context = context;
        this.setTextSizeDialog = new SetTextSizeDialog(context);
        this.moreOptionsDialog = new BrowseMoreOptionsDialog(context);
        createDialogs();
        themeManager.registerListener(this);
    }

    private void createDialogs() {
        boolean darkModeEnabled = themeManager.getDarkModeEnabled(context);
        optionsDialog = new MaterialDialog.Builder(context)
                .theme(darkModeEnabled ? Theme.DARK : Theme.LIGHT)
                .title(R.string.settings)
                .items(R.array.browse_settings_options)
                .itemsCallback((MaterialDialog dialog, View itemView, int position, CharSequence text) -> {
                    switch (position) {
                        case 0:
                            setTextSizeDialog.show();
                            break;
                        case 1:
                            listener.onTextColorChangeRequested();
                            break;
                        case 2:
                            listener.onShuffleRequested();
                            break;
                        case 3:
                            listener.onRestoreRequested();
                            break;
                        case 4:
                            moreOptionsDialog.show();
                            break;
                    }
                })
                .negativeText(R.string.cancel)
                .build();
        setTextSizeDialog.createDialog(darkModeEnabled);
        moreOptionsDialog.createDialog(darkModeEnabled);
    }

    public void showOptions() {
        optionsDialog.show();
    }

    @Override
    public void onThemeChanged(boolean darkModeEnabled) {
        createDialogs();
    }

    public void shutdown() {
        listener = null;
        context = null;
        moreOptionsDialog.shutdown();
        setTextSizeDialog.shutdown();
        themeManager.unregisterListener(this);
    }
}
