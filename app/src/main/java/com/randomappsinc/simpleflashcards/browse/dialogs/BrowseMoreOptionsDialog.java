package com.randomappsinc.simpleflashcards.browse.dialogs;

import android.content.Context;
import android.widget.CheckBox;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.randomappsinc.simpleflashcards.R;
import com.randomappsinc.simpleflashcards.persistence.PreferencesManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;

public class BrowseMoreOptionsDialog {

    @BindView(R.id.enable_shake) CheckBox enableShake;
    @BindView(R.id.show_terms_by_default) CheckBox showTerms;
    @BindView(R.id.show_definitions_by_default) CheckBox showDefinitions;

    private MaterialDialog dialog;
    private Context context;
    private PreferencesManager preferencesManager;

    public BrowseMoreOptionsDialog(Context context) {
        this.context = context;
        this.preferencesManager = new PreferencesManager(context);
    }

    public void createDialog(boolean darkModeEnabled) {
        dialog = new MaterialDialog.Builder(context)
                .theme(darkModeEnabled ? Theme.DARK : Theme.LIGHT)
                .title(R.string.more_options_title)
                .customView(R.layout.browse_more_options, true)
                .positiveText(R.string.apply)
                .negativeText(R.string.cancel)
                .cancelable(false)
                .build();
        ButterKnife.bind(this, dialog.getCustomView());
        enableShake.setChecked(preferencesManager.isShakeEnabled());
    }

    @OnCheckedChanged(R.id.show_terms_by_default)
    public void onTermsChosen(boolean isChecked) {
        if (isChecked) {
            showTerms.setClickable(false);
            showDefinitions.setChecked(false);
            showDefinitions.setClickable(true);
        }
    }

    @OnCheckedChanged(R.id.show_definitions_by_default)
    public void onDefinitionsChosen(boolean isChecked) {
        if (isChecked) {
            showDefinitions.setClickable(false);
            showTerms.setChecked(false);
            showTerms.setClickable(true);
        }
    }

    public void show() {
        dialog.show();
    }

    public void shutdown() {
        context = null;
    }
}
