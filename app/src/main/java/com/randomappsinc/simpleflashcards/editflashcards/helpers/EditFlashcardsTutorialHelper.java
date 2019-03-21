package com.randomappsinc.simpleflashcards.editflashcards.helpers;

import android.content.Context;
import androidx.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.randomappsinc.simpleflashcards.R;
import com.randomappsinc.simpleflashcards.persistence.PreferencesManager;
import com.randomappsinc.simpleflashcards.utils.DialogUtil;

public class EditFlashcardsTutorialHelper {

    public static void teach(final Context context) {
        PreferencesManager preferencesManager = new PreferencesManager(context);
        final boolean shouldTeachImport = preferencesManager.showTeachImportFlashcards();
        if (preferencesManager.shouldShowRenameFlashcardSetInstructions()) {
            DialogUtil.createDialogWithIconTextBody(
                    context,
                    R.string.rename_set_instructions,
                    R.string.rename_set_instructions_title,
                    android.R.string.ok,
                    new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            showImportFlashcardsDialog(context);
                        }
                    })
                    .show();
        } else if (shouldTeachImport) {
            showImportFlashcardsDialog(context);
        }
    }

    protected static void showImportFlashcardsDialog(Context context) {
        DialogUtil.createDialogWithIconTextBody(
                context,
                R.string.import_flashcards_instructions,
                R.string.import_flashcards_instructions_title,
                android.R.string.ok,
                null)
                .show();
    }
}
