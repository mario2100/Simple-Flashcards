package com.randomappsinc.simpleflashcards.editflashcards.activities;

import android.os.Bundle;

import com.randomappsinc.simpleflashcards.R;
import com.randomappsinc.simpleflashcards.common.activities.StandardActivity;
import com.randomappsinc.simpleflashcards.common.constants.Constants;
import com.randomappsinc.simpleflashcards.editflashcards.adapters.FlashcardOrderingAdapter;
import com.randomappsinc.simpleflashcards.editflashcards.adapters.SimpleItemTouchHelperCallback;
import com.randomappsinc.simpleflashcards.persistence.DatabaseManager;
import com.randomappsinc.simpleflashcards.persistence.models.FlashcardDO;
import com.randomappsinc.simpleflashcards.utils.UIUtils;

import java.util.List;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FlashcardOrderingActivity extends StandardActivity {

    @BindView(R.id.flashcards_list) RecyclerView flashcardsList;

    private FlashcardOrderingAdapter flashcardOrderingAdapter;
    private DatabaseManager databaseManager = DatabaseManager.get();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flashcard_ordering);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int setId = getIntent().getIntExtra(Constants.FLASHCARD_SET_ID_KEY, 0);
        List<FlashcardDO> flashcardList = databaseManager.getAllFlashcards(setId);
        flashcardOrderingAdapter = new FlashcardOrderingAdapter(flashcardList);
        flashcardsList.setAdapter(flashcardOrderingAdapter);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(flashcardOrderingAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(flashcardsList);
    }

    @OnClick(R.id.save)
    public void saveOrder() {
        databaseManager.setFlashcardPositions(flashcardOrderingAdapter.getFlashcards());
        UIUtils.showShortToast(R.string.flashcards_reordered, this);
        finish();
    }
}
