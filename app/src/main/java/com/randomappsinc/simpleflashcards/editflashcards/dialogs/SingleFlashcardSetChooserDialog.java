package com.randomappsinc.simpleflashcards.editflashcards.dialogs;

import android.content.Context;

import androidx.annotation.StringRes;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.randomappsinc.simpleflashcards.R;
import com.randomappsinc.simpleflashcards.common.views.SimpleDividerItemDecoration;
import com.randomappsinc.simpleflashcards.editflashcards.adapters.SingleFlashcardSetChooserAdapter;
import com.randomappsinc.simpleflashcards.persistence.DatabaseManager;
import com.randomappsinc.simpleflashcards.persistence.models.FlashcardSetDO;

public class SingleFlashcardSetChooserDialog implements SingleFlashcardSetChooserAdapter.Listener {

    public interface Listener {
        void onFlashcardSetChosen(FlashcardSetDO flashcardSet);
    }

    private Context context;
    private Listener listener;
    private int setId;
    private MaterialDialog chooserDialog;
    private SingleFlashcardSetChooserAdapter setsAdapter;
    private DatabaseManager databaseManager = DatabaseManager.get();

    public SingleFlashcardSetChooserDialog(Context context, Listener listener, int setId) {
        this.context = context;
        this.listener = listener;
        this.setId = setId;
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
    public void onFlashcardSetSelected(FlashcardSetDO flashcardSet) {
        chooserDialog.dismiss();
        listener.onFlashcardSetChosen(flashcardSet);
    }

    public void show(@StringRes int contentId) {
        setsAdapter.setFlashcardSets(databaseManager.getNonEmptyFlashcardSets(setId));
        chooserDialog.setContent(contentId);
        chooserDialog.show();
    }

    public void cleanUp() {
        context = null;
    }
}
