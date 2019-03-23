package com.randomappsinc.simpleflashcards.common.activities;

import android.content.Intent;
import android.os.Bundle;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.IoniconsIcons;
import com.randomappsinc.simpleflashcards.R;
import com.randomappsinc.simpleflashcards.common.Constants;
import com.randomappsinc.simpleflashcards.common.adapters.PlainSetViewAdapter;
import com.randomappsinc.simpleflashcards.common.models.FlashcardSetPreview;
import com.randomappsinc.simpleflashcards.persistence.models.Flashcard;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PlainSetViewActivity extends StandardActivity {

    @BindView(R.id.flashcards_list) RecyclerView flashcardsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plain_set_view);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar()
                .setHomeAsUpIndicator(new IconDrawable(this, IoniconsIcons.ion_android_close)
                        .colorRes(R.color.white)
                        .actionBarSize());
        FlashcardSetPreview setPreview = getIntent().getParcelableExtra(Constants.SET_PREVIEW_KEY);
        setTitle(setPreview.getSetName());

        FlashcardSetPreview preview = getIntent().getParcelableExtra(Constants.SET_PREVIEW_KEY);
        PlainSetViewAdapter adapter = new PlainSetViewAdapter(listClickListener, preview);
        flashcardsList.setAdapter(adapter);
    }

    private final PlainSetViewAdapter.Listener listClickListener = new PlainSetViewAdapter.Listener() {
        @Override
        public void onImageClicked(Flashcard flashcard) {
            Intent intent = new Intent(
                    PlainSetViewActivity.this,
                    PictureFullViewActivity.class)
                    .putExtra(Constants.IMAGE_URL_KEY, flashcard.getTermImageUrl())
                    .putExtra(Constants.CAPTION_KEY, flashcard.getTerm());
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, 0);
        }
    };

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.slide_out_bottom);
    }
}
