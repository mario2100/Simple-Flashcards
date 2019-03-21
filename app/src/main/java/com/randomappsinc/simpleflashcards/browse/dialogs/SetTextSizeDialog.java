package com.randomappsinc.simpleflashcards.browse.dialogs;

import android.content.Context;
import android.util.TypedValue;
import android.widget.SeekBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.randomappsinc.simpleflashcards.R;
import com.randomappsinc.simpleflashcards.browse.managers.BrowseFlashcardsSettingsManager;
import com.randomappsinc.simpleflashcards.persistence.PreferencesManager;

import androidx.annotation.NonNull;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SetTextSizeDialog implements SeekBar.OnSeekBarChangeListener {

    private static final int PROGRESS_VALUE_MULTIPLIER = 8;

    @BindView(R.id.text_size_slider) public SeekBar textSizeSlider;
    @BindView(R.id.sample_text) public TextView sampleText;

    private MaterialDialog dialog;
    private Context context;
    private PreferencesManager preferencesManager;
    private BrowseFlashcardsSettingsManager settingsManager = BrowseFlashcardsSettingsManager.get();

    public SetTextSizeDialog(Context context) {
        this.context = context;
        this.preferencesManager = new PreferencesManager(context);
    }

    public void createDialog(boolean darkModeEnabled) {
        dialog = new MaterialDialog.Builder(context)
                .theme(darkModeEnabled ? Theme.DARK : Theme.LIGHT)
                .title(R.string.set_text_size_title)
                .customView(R.layout.set_text_size, true)
                .positiveText(android.R.string.yes)
                .negativeText(android.R.string.no)
                .onPositive((@NonNull MaterialDialog dialog, @NonNull DialogAction which) -> {
                    int newTextSizeSp = (textSizeSlider.getProgress() + 1) * PROGRESS_VALUE_MULTIPLIER;
                    preferencesManager.setBrowseTextSize(newTextSizeSp);
                    settingsManager.changeTextSize(newTextSizeSp);
                })
                .onNegative((@NonNull MaterialDialog dialog, @NonNull DialogAction which) -> {
                    textSizeSlider.setProgress((preferencesManager.getBrowseTextSize()/PROGRESS_VALUE_MULTIPLIER) - 1);
                    textSizeSlider.jumpDrawablesToCurrentState();
                })
                .build();
        ButterKnife.bind(this, dialog.getCustomView());
        textSizeSlider.setOnSeekBarChangeListener(this);
        textSizeSlider.setProgress((preferencesManager.getBrowseTextSize()/PROGRESS_VALUE_MULTIPLIER) - 1);
        textSizeSlider.jumpDrawablesToCurrentState();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        sampleText.setTextSize(TypedValue.COMPLEX_UNIT_SP, (progress + 1) * PROGRESS_VALUE_MULTIPLIER);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {}

    public void show() {
        dialog.show();
    }

    public void shutdown() {
        context = null;
    }
}
