package com.randomappsinc.simpleflashcards.dialogs;

import android.content.Context;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.randomappsinc.simpleflashcards.R;
import com.randomappsinc.simpleflashcards.adapters.FlashcardSetSelectionAdapter;
import com.randomappsinc.simpleflashcards.persistence.models.FlashcardSet;
import com.randomappsinc.simpleflashcards.views.SimpleDividerItemDecoration;

import java.util.List;

/** Dialog to let user choose flashcard sets to put into a folder */
public class FlashcardSetSelectionDialog {

    public interface Listener {
        void onFlashcardSetsSelected(List<FlashcardSet> flashcardSets);
    }

    private MaterialDialog adderDialog;
    private Listener listener;
    private FlashcardSetSelectionAdapter flashcardSetSelectionAdapter;

    public FlashcardSetSelectionDialog(Context context, Listener listener) {
        this.listener = listener;
        flashcardSetSelectionAdapter = new FlashcardSetSelectionAdapter();
        adderDialog = new MaterialDialog.Builder(context)
                .title(R.string.add_flashcard_sets)
                .positiveText(R.string.add)
                .negativeText(R.string.cancel)
                .adapter(flashcardSetSelectionAdapter, null)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    }
                })
                .build();
        adderDialog.getRecyclerView().addItemDecoration(new SimpleDividerItemDecoration(context));
    }

    public void setFlashcardSetList(List<FlashcardSet> flashcardSets) {
        flashcardSetSelectionAdapter.setFlashcardSets(flashcardSets);
    }

    public void show() {
        adderDialog.show();
    }
}
