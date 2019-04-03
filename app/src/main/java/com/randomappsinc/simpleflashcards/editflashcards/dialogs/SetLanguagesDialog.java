package com.randomappsinc.simpleflashcards.editflashcards.dialogs;

import android.content.Context;
import android.graphics.PorterDuff;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.randomappsinc.simpleflashcards.R;
import com.randomappsinc.simpleflashcards.theme.ThemeManager;

import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SetLanguagesDialog implements ThemeManager.Listener {

    @BindView(R.id.term_language_options) Spinner termOptions;
    @BindView(R.id.definition_language_options) Spinner definitionOptions;

    private MaterialDialog dialog;
    private Context context;
    private ThemeManager themeManager = ThemeManager.get();

    public SetLanguagesDialog(Context context) {
        this.context = context;
        createDialog(themeManager.getDarkModeEnabled(context));
        themeManager.registerListener(this);
    }

    public void createDialog(boolean darkModeEnabled) {
        int darkModeBackground = ContextCompat.getColor(context, R.color.dark_mode_black);
        int white = ContextCompat.getColor(context, R.color.white);
        dialog = new MaterialDialog.Builder(context)
                .theme(darkModeEnabled ? Theme.DARK : Theme.LIGHT)
                .backgroundColor(darkModeEnabled ? darkModeBackground : white)
                .title(R.string.more_options_title)
                .customView(R.layout.choose_set_languages, true)
                .positiveText(R.string.apply)
                .negativeText(R.string.cancel)
                .cancelable(false)
                .build();
        ButterKnife.bind(this, dialog.getCustomView());

        int textColor = darkModeEnabled ? white : ContextCompat.getColor(context, R.color.dark_gray);
        String[] languageChoices = context.getResources().getStringArray(R.array.language_options);
        termOptions.setAdapter(new ArrayAdapter<>(context, R.layout.spinner_item, languageChoices));
        termOptions.getBackground().setColorFilter(textColor, PorterDuff.Mode.SRC_ATOP);
        definitionOptions.setAdapter(new ArrayAdapter<>(context, R.layout.spinner_item, languageChoices));
        definitionOptions.getBackground().setColorFilter(textColor, PorterDuff.Mode.SRC_ATOP);
    }

    public void show() {
        dialog.show();
    }

    @Override
    public void onThemeChanged(boolean darkModeEnabled) {
        createDialog(darkModeEnabled);
    }

    public void cleanUp() {
        context = null;
        themeManager.unregisterListener(this);
    }
}
