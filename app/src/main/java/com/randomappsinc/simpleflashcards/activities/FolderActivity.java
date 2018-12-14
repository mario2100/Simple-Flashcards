package com.randomappsinc.simpleflashcards.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.IoniconsIcons;
import com.randomappsinc.simpleflashcards.R;
import com.randomappsinc.simpleflashcards.constants.Constants;
import com.randomappsinc.simpleflashcards.models.Folder;
import com.randomappsinc.simpleflashcards.persistence.DatabaseManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FolderActivity extends StandardActivity {

    @BindView(R.id.add_sets) FloatingActionButton addFolder;
    @BindView(R.id.no_sets) View noSets;
    @BindView(R.id.flashcard_sets) RecyclerView setsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.folder_view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);

        addFolder.setImageDrawable(new IconDrawable(this, IoniconsIcons.ion_android_add)
                .colorRes(R.color.white)
                .actionBarSize());

        int folderId = getIntent().getIntExtra(Constants.FOLDER_ID_KEY, 0);
        Folder folder = DatabaseManager.get().getFolder(folderId);
        setTitle(folder.getName());
    }

    @OnClick(R.id.add_sets)
    public void addFlashcardSets() {
        
    }
}
