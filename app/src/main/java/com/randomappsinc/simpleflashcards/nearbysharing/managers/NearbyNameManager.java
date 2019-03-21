package com.randomappsinc.simpleflashcards.nearbysharing.managers;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.randomappsinc.simpleflashcards.R;
import com.randomappsinc.simpleflashcards.persistence.PreferencesManager;
import com.randomappsinc.simpleflashcards.theme.ThemeManager;

public class NearbyNameManager implements ThemeManager.Listener {

    public interface Listener {
        void onNameChanged();
    }

    protected PreferencesManager preferencesManager;
    @Nullable protected Listener listener;
    protected String currentName;
    private MaterialDialog nameSettingDialog;
    private Context context;
    private ThemeManager themeManager = ThemeManager.get();

    public NearbyNameManager(Context context, @Nullable Listener listener) {
        this.preferencesManager = new PreferencesManager(context);
        this.currentName = preferencesManager.getNearbyName();
        this.context = context;
        this.listener = listener;
        createDialog();
        themeManager.registerListener(this);
    }

    private void createDialog() {
        String nearbyNameHint = context.getString(R.string.nearby_name);
        nameSettingDialog = new MaterialDialog.Builder(context)
                .theme(themeManager.getDarkModeEnabled(context) ? Theme.DARK : Theme.LIGHT)
                .title(R.string.set_nearby_name_title)
                .content(R.string.nearby_name_explanation)
                .alwaysCallInputCallback()
                .input(nearbyNameHint, currentName, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        String setName = input.toString();
                        boolean notEmpty = !setName.trim().isEmpty();
                        dialog.getActionButton(DialogAction.POSITIVE).setEnabled(notEmpty);
                    }
                })
                .positiveText(R.string.save)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        String nearbyName = dialog.getInputEditText().getText().toString().trim();
                        currentName = nearbyName;
                        preferencesManager.setNearbyName(nearbyName);
                        if (listener != null) {
                            listener.onNameChanged();
                        }
                    }
                })
                .negativeText(R.string.cancel)
                .build();
    }

    public String getCurrentName() {
        return currentName;
    }

    public void showNameSetter() {
        nameSettingDialog.show();
    }

    @Override
    public void onThemeChanged(boolean darkModeEnabled) {
        createDialog();
    }

    public void cleanUp() {
        context = null;
        listener = null;
        themeManager.unregisterListener(this);
    }
}
