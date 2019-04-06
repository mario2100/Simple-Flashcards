package com.randomappsinc.simpleflashcards.editflashcards.helpers;

import android.content.Context;

import com.randomappsinc.simpleflashcards.R;
import com.randomappsinc.simpleflashcards.persistence.PreferencesManager;
import com.randomappsinc.simpleflashcards.utils.DialogUtil;

public class EditFlashcardsTutorialHelper {

    public static void teach(final Context context) {
        PreferencesManager preferencesManager = new PreferencesManager(context);
        if (preferencesManager.shouldShowRenameFlashcardSetInstructions()) {
            DialogUtil.createDialogWithIconTextBody(
                    context,
                    R.string.rename_set_instructions,
                    R.string.rename_set_instructions_title,
                    android.R.string.ok,
                    (dialog, which) -> maybeShowImportFlashcardsDialog(context))
                    .show();
        } else {
            maybeShowImportFlashcardsDialog(context);
        }
    }

    protected static void maybeShowImportFlashcardsDialog(Context context) {
        PreferencesManager preferencesManager = new PreferencesManager(context);
        if (preferencesManager.shouldShowTeachImportFlashcards()) {
            DialogUtil.createDialogWithIconTextBody(
                    context,
                    R.string.import_flashcards_instructions,
                    R.string.import_flashcards_instructions_title,
                    android.R.string.ok,
                    (dialog, which) -> maybeShowLanguageSettingDialog(context))
                    .show();
        } else {
            maybeShowLanguageSettingDialog(context);
        }
    }

    protected static void maybeShowLanguageSettingDialog(Context context) {
        PreferencesManager preferencesManager = new PreferencesManager(context);
        if (preferencesManager.shouldTeachLanguageSetting()) {
            DialogUtil.createDialogWithIconTextBody(
                    context,
                    R.string.language_setting_instructions,
                    R.string.language_setting_instructions_title,
                    android.R.string.ok,
                    null)
                    .show();
        }
    }
}
