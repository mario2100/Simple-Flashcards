package com.randomappsinc.simpleflashcards.editflashcards.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.IoniconsIcons;
import com.randomappsinc.simpleflashcards.R;
import com.randomappsinc.simpleflashcards.common.Constants;
import com.randomappsinc.simpleflashcards.common.activities.StandardActivity;
import com.randomappsinc.simpleflashcards.editflashcards.constants.ImportFlashcardsMode;
import com.randomappsinc.simpleflashcards.persistence.DatabaseManager;

import butterknife.ButterKnife;

public class PickAndImportFlashcardsActivity extends StandardActivity {

    private @ImportFlashcardsMode int importMode;
    private DatabaseManager databaseManager = DatabaseManager.get();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pick_flashcards);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar()
                .setHomeAsUpIndicator(new IconDrawable(this, IoniconsIcons.ion_android_close)
                        .colorRes(R.color.white)
                        .actionBarSize());

        int setId = getIntent().getIntExtra(Constants.FLASHCARD_SET_ID_KEY,0);
        importMode = getIntent().getIntExtra(Constants.IMPORT_MODE_KEY, 0);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.slide_out_bottom);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pick_flashcards, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.select_all:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
