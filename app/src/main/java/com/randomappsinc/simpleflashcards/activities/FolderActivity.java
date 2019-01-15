package com.randomappsinc.simpleflashcards.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.IoniconsIcons;
import com.randomappsinc.simpleflashcards.R;
import com.randomappsinc.simpleflashcards.adapters.FolderSetsAdapter;
import com.randomappsinc.simpleflashcards.constants.Constants;
import com.randomappsinc.simpleflashcards.dialogs.FlashcardSetSelectionDialog;
import com.randomappsinc.simpleflashcards.dialogs.RenameFolderDialog;
import com.randomappsinc.simpleflashcards.models.Folder;
import com.randomappsinc.simpleflashcards.persistence.DatabaseManager;
import com.randomappsinc.simpleflashcards.persistence.models.FlashcardSet;
import com.randomappsinc.simpleflashcards.utils.UIUtils;
import com.randomappsinc.simpleflashcards.views.SimpleDividerItemDecoration;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FolderActivity extends StandardActivity
        implements FlashcardSetSelectionDialog.Listener, FolderSetsAdapter.Listener, RenameFolderDialog.Listener {

    @BindView(R.id.add_sets) FloatingActionButton addSetsButton;
    @BindView(R.id.no_sets) View noSets;
    @BindView(R.id.flashcard_sets) RecyclerView setsList;

    private int folderId;
    private DatabaseManager databaseManager = DatabaseManager.get();
    private FlashcardSetSelectionDialog setAdderDialog;
    private FolderSetsAdapter setsAdapter;
    private RenameFolderDialog renameFolderDialog;

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

        setsAdapter = new FolderSetsAdapter(this);
        setsList.setAdapter(setsAdapter);
        setsList.addItemDecoration(new SimpleDividerItemDecoration(this));

        setAdderDialog = new FlashcardSetSelectionDialog(this, this);
        setAdderDialog.setFlashcardSetList(databaseManager.getFlashcardSetsNotInFolder(folderId));

        renameFolderDialog = new RenameFolderDialog(this, folder.getName(), this);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateSetsList();
    }

    // Syncs the sets adapter against what's in DB
    private void updateSetsList() {
        setsAdapter.refreshContent(databaseManager.getFlashcardSetsInFolder(folderId));
        setsList.setVisibility(setsAdapter.getItemCount() == 0 ? View.GONE : View.VISIBLE);
        noSets.setVisibility(setsAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }

    @OnClick(R.id.add_sets)
    public void addFlashcardSets() {
        if (setAdderDialog.getNumSets() > 0) {
            setAdderDialog.show();
        } else {
            UIUtils.showLongToast(R.string.no_sets_to_add, this);
        }
    }

    @Override
    public void onFlashcardSetsSelected(List<FlashcardSet> flashcardSets) {
        databaseManager.addFlashcardSetsIntoFolder(folderId, flashcardSets);
        setAdderDialog.setFlashcardSetList(databaseManager.getFlashcardSetsNotInFolder(folderId));
        updateSetsList();
        UIUtils.showShortToast(flashcardSets.size() == 1
                ? R.string.flashcard_set_added
                : R.string.flashcard_sets_added, this);
    }

    @Override
    public void browseFlashcardSet(FlashcardSet flashcardSet) {
        if (flashcardSet.getFlashcards().isEmpty()) {
            UIUtils.showLongToast(R.string.no_flashcards_for_browsing, this);
        } else {
            startActivity(new Intent(this, BrowseFlashcardsActivity.class)
                    .putExtra(Constants.FLASHCARD_SET_ID_KEY, flashcardSet.getId()));
        }
    }

    @Override
    public void takeQuiz(FlashcardSet flashcardSet) {
        if (flashcardSet.getFlashcards().size() < 2) {
            UIUtils.showLongToast(R.string.not_enough_for_quiz, this);
        } else {
            startActivity(new Intent(this, QuizSettingsActivity.class)
                    .putExtra(Constants.FLASHCARD_SET_ID_KEY, flashcardSet.getId()));
            overridePendingTransition(R.anim.slide_in_bottom, R.anim.stay);
        }
    }

    @Override
    public void editFlashcardSet(FlashcardSet flashcardSet) {
        startActivity(new Intent(
                this, EditFlashcardSetActivity.class)
                .putExtra(Constants.FLASHCARD_SET_ID_KEY, flashcardSet.getId()));
    }

    @Override
    public void removeFlashcardSet(FlashcardSet flashcardSet) {
        databaseManager.removeFlashcardSetFromFolder(folderId, flashcardSet);
        setsList.setVisibility(setsAdapter.getItemCount() == 0 ? View.GONE : View.VISIBLE);
        noSets.setVisibility(setsAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
        setAdderDialog.setFlashcardSetList(databaseManager.getFlashcardSetsNotInFolder(folderId));
    }

    @Override
    public void onFolderName(String newFolderName) {
        databaseManager.renameFolder(folderId, newFolderName);
        setTitle(newFolderName);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        setAdderDialog.cleanUp();
        renameFolderDialog.cleanUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_folder, menu);
        UIUtils.loadMenuIcon(menu, R.id.rename_folder, IoniconsIcons.ion_edit, this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.rename_folder:
                renameFolderDialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
