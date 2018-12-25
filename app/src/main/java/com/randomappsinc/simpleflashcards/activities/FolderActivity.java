package com.randomappsinc.simpleflashcards.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.IoniconsIcons;
import com.randomappsinc.simpleflashcards.R;
import com.randomappsinc.simpleflashcards.constants.Constants;
import com.randomappsinc.simpleflashcards.dialogs.FlashcardSetSelectionDialog;
import com.randomappsinc.simpleflashcards.models.Folder;
import com.randomappsinc.simpleflashcards.persistence.DatabaseManager;
import com.randomappsinc.simpleflashcards.persistence.models.FlashcardSet;
import com.randomappsinc.simpleflashcards.utils.UIUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FolderActivity extends StandardActivity implements FlashcardSetSelectionDialog.Listener {

    @BindView(R.id.add_sets) FloatingActionButton addSetsButton;
    @BindView(R.id.no_sets) View noSets;
    @BindView(R.id.flashcard_sets) RecyclerView setsList;

    private int folderId;
    private DatabaseManager databaseManager = DatabaseManager.get();
    private FlashcardSetSelectionDialog setAdderDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.folder_view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);

        addSetsButton.setImageDrawable(new IconDrawable(this, IoniconsIcons.ion_android_add)
                .colorRes(R.color.white)
                .actionBarSize());

        folderId = getIntent().getIntExtra(Constants.FOLDER_ID_KEY, 0);
        Folder folder = databaseManager.getFolder(folderId);
        setTitle(folder.getName());

        setAdderDialog = new FlashcardSetSelectionDialog(this, this);
        setAdderDialog.setFlashcardSetList(databaseManager.getFlashcardSetsNotInFolder(folderId));
    }

    @OnClick(R.id.add_sets)
    public void addFlashcardSets() {
        setAdderDialog.show();
    }

    @Override
    public void onFlashcardSetsSelected(List<FlashcardSet> flashcardSets) {
        databaseManager.addFlashcardSetsIntoFolder(folderId, flashcardSets);
        setAdderDialog.setFlashcardSetList(databaseManager.getFlashcardSetsNotInFolder(folderId));
        UIUtils.showShortToast(flashcardSets.size() == 1
                ? R.string.flashcard_set_added
                : R.string.flashcard_sets_added, this);
    }
}
