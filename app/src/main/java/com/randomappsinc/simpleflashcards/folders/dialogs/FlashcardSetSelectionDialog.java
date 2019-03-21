package com.randomappsinc.simpleflashcards.folders.dialogs;

import android.content.Context;
import androidx.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.randomappsinc.simpleflashcards.R;
import com.randomappsinc.simpleflashcards.common.views.SimpleDividerItemDecoration;
import com.randomappsinc.simpleflashcards.folders.adapters.MultiFlashcardSetSelectionAdapter;
import com.randomappsinc.simpleflashcards.persistence.models.FlashcardSet;
import com.randomappsinc.simpleflashcards.theme.ThemeManager;

import java.util.List;

/** Dialog to let user choose flashcard sets to put into a folder */
public class FlashcardSetSelectionDialog implements MultiFlashcardSetSelectionAdapter.Listener, ThemeManager.Listener {

    public interface Listener {
        void onFlashcardSetsSelected(List<FlashcardSet> flashcardSets);
    }

    private MaterialDialog adderDialog;
    protected MultiFlashcardSetSelectionAdapter setsAdapter;
    protected Listener listener;
    private Context context;
    private ThemeManager themeManager = ThemeManager.get();

    public FlashcardSetSelectionDialog(Context context, Listener listenerImpl) {
        this.listener = listenerImpl;
        this.context = context;
        createDialog();
        themeManager.registerListener(this);
    }

    private void createDialog() {
        setsAdapter = new MultiFlashcardSetSelectionAdapter(this);
        adderDialog = new MaterialDialog.Builder(context)
                .theme(themeManager.getDarkModeEnabled(context) ? Theme.DARK : Theme.LIGHT)
                .title(R.string.add_flashcard_sets)
                .positiveText(R.string.add)
                .negativeText(R.string.cancel)
                .adapter(setsAdapter, null)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        listener.onFlashcardSetsSelected(setsAdapter.getSelectedSets());
                    }
                })
                .build();
        adderDialog.getRecyclerView().addItemDecoration(new SimpleDividerItemDecoration(context));
    }

    public int getNumSets() {
        return setsAdapter.getItemCount();
    }

    @Override
    public void onNumSelectedSetsUpdated(int numSelectedSets) {
        adderDialog.getActionButton(DialogAction.POSITIVE).setEnabled(numSelectedSets > 0);
    }

    public void setFlashcardSetList(List<FlashcardSet> flashcardSets) {
        setsAdapter.setFlashcardSets(flashcardSets);
    }

    public void show() {
        adderDialog.show();
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
