package com.randomappsinc.simpleflashcards.editflashcards.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.IoniconsIcons;
import com.randomappsinc.simpleflashcards.R;
import com.randomappsinc.simpleflashcards.common.Constants;
import com.randomappsinc.simpleflashcards.common.activities.StandardActivity;
import com.randomappsinc.simpleflashcards.editflashcards.adapters.MultiFlashcardsSelectorAdapter;
import com.randomappsinc.simpleflashcards.editflashcards.constants.ImportFlashcardsMode;
import com.randomappsinc.simpleflashcards.persistence.DatabaseManager;
import com.randomappsinc.simpleflashcards.persistence.models.Flashcard;
import com.randomappsinc.simpleflashcards.utils.UIUtils;

import java.util.List;
import java.util.Set;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PickAndImportFlashcardsActivity extends StandardActivity
        implements MultiFlashcardsSelectorAdapter.Listener {

    @BindView(R.id.flashcards_list) RecyclerView flashcardsList;
    @BindView(R.id.action_button) TextView actionButton;

    private int receivingSetId;
    private int sendingSetId;
    private @ImportFlashcardsMode int importMode;
    private MultiFlashcardsSelectorAdapter flashcardsAdapter;
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

        receivingSetId = getIntent().getIntExtra(Constants.RECEIVING_SET_ID,0);
        sendingSetId = getIntent().getIntExtra(Constants.SENDING_SET_ID,0);
        importMode = getIntent().getIntExtra(Constants.IMPORT_MODE_KEY, 0);

        List<Flashcard> flashcards = databaseManager.getAllFlashcards(sendingSetId);
        flashcardsAdapter = new MultiFlashcardsSelectorAdapter(this, flashcards);
        flashcardsList.setAdapter(flashcardsAdapter);

        onNumSelectedSetsUpdated(0);
    }

    @Override
    public void onNumSelectedSetsUpdated(int numSelectedFlashcards) {
        int textId = importMode == ImportFlashcardsMode.MOVE ? R.string.move_x : R.string.copy_x;
        actionButton.setText(getString(textId, numSelectedFlashcards));
    }

    @OnClick(R.id.action_button)
    public void onActionSubmitted() {
        Set<Integer> selectedFlashcardIds = flashcardsAdapter.getSelectedFlashcardIds();
        switch (importMode) {
            case ImportFlashcardsMode.MOVE:
                databaseManager.moveFlashcards(receivingSetId, sendingSetId, selectedFlashcardIds);
                break;
            case ImportFlashcardsMode.COPY:
                databaseManager.copyFlashcards(receivingSetId, sendingSetId, selectedFlashcardIds);
                break;
        }
        UIUtils.showLongToast(R.string.flashcards_import_success, this);
        setResult(RESULT_OK);
        finish();
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
                flashcardsAdapter.selectAll();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
