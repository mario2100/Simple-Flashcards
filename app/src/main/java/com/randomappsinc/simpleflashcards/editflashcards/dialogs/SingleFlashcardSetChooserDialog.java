package com.randomappsinc.simpleflashcards.editflashcards.dialogs;

import android.content.Context;
import android.support.annotation.StringRes;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.randomappsinc.simpleflashcards.R;
import com.randomappsinc.simpleflashcards.common.views.SimpleDividerItemDecoration;
import com.randomappsinc.simpleflashcards.editflashcards.adapters.SingleFlashcardSetChooserAdapter;
import com.randomappsinc.simpleflashcards.persistence.DatabaseManager;
import com.randomappsinc.simpleflashcards.persistence.models.FlashcardSet;

public class SingleFlashcardSetChooserDialog implements SingleFlashcardSetChooserAdapter.Listener {

    public interface Listener {
        void onFlashcardSetChosen(FlashcardSet flashcardSet);
    }

    private Context context;
    private Listener listener;
    private MaterialDialog chooserDialog;
    private SingleFlashcardSetChooserAdapter setsAdapter;
    private DatabaseManager databaseManager = DatabaseManager.get();

    public SingleFlashcardSetChooserDialog(Context context, Listener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void createDialog(boolean darkModeEnabled) {
        setsAdapter = new SingleFlashcardSetChooserAdapter(this);
        chooserDialog = new MaterialDialog.Builder(context)
                .theme(darkModeEnabled ? Theme.DARK : Theme.LIGHT)
                .content("")
                .positiveText(R.string.cancel)
                .adapter(setsAdapter, null)
                .build();
        chooserDialog.getRecyclerView().addItemDecoration(new SimpleDividerItemDecoration(context));
    }

    @Override
    public void onFlashcardSetSelected(FlashcardSet flashcardSet) {
        chooserDialog.dismiss();
        listener.onFlashcardSetChosen(flashcardSet);
    }

    public void show(@StringRes int contentId, int setId) {
        setsAdapter.setFlashcardSets(databaseManager.getNonEmptyFlashcardSets(setId));
        chooserDialog.setContent(contentId);
        chooserDialog.show();
    }

    public void cleanUp() {
        context = null;
    }
}
