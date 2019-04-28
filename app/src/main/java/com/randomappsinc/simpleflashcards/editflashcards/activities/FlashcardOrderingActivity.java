package com.randomappsinc.simpleflashcards.editflashcards.activities;

import android.os.Bundle;

import com.randomappsinc.simpleflashcards.R;
import com.randomappsinc.simpleflashcards.common.activities.StandardActivity;

import butterknife.ButterKnife;

public class FlashcardOrderingActivity extends StandardActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flashcard_ordering);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
