package com.randomappsinc.simpleflashcards.editflashcards.activities;

import android.os.Bundle;

import com.randomappsinc.simpleflashcards.R;
import com.randomappsinc.simpleflashcards.common.activities.StandardActivity;
import com.randomappsinc.simpleflashcards.common.constants.Constants;
import com.randomappsinc.simpleflashcards.editflashcards.adapters.FlashcardOrderingAdapter;
import com.randomappsinc.simpleflashcards.persistence.DatabaseManager;
import com.randomappsinc.simpleflashcards.persistence.models.FlashcardDO;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FlashcardOrderingActivity extends StandardActivity {

    @BindView(R.id.flashcards_list) RecyclerView flashcardsList;

    private DatabaseManager databaseManager = DatabaseManager.get();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flashcard_ordering);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int setId = getIntent().getIntExtra(Constants.FLASHCARD_SET_ID_KEY, 0);
        List<FlashcardDO> flashcardList = databaseManager.getAllFlashcards(setId);
        flashcardsList.setAdapter(new FlashcardOrderingAdapter(flashcardList));
    }

    @OnClick(R.id.save)
    public void saveOrder() {

    }
}
