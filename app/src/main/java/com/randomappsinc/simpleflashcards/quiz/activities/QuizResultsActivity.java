package com.randomappsinc.simpleflashcards.quiz.activities;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.IoniconsIcons;
import com.randomappsinc.simpleflashcards.R;
import com.randomappsinc.simpleflashcards.common.Constants;
import com.randomappsinc.simpleflashcards.common.activities.StandardActivity;
import com.randomappsinc.simpleflashcards.quiz.adapters.QuizResultsTabsAdapter;
import com.randomappsinc.simpleflashcards.quiz.models.Problem;

import java.util.List;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindArray;
import butterknife.BindView;
import butterknife.ButterKnife;

public class QuizResultsActivity extends StandardActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.quiz_results_tabs) TabLayout tabs;
    @BindView(R.id.quiz_results_pager) ViewPager viewPager;
    @BindArray(R.array.quiz_results_tabs) String[] resultTabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_results);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar()
                .setHomeAsUpIndicator(new IconDrawable(this, IoniconsIcons.ion_android_close)
                .colorRes(R.color.white)
                .actionBarSize());
        setActionBarColors();

        List<Problem> problems = getIntent().getParcelableArrayListExtra(Constants.QUIZ_RESULTS_KEY);
        viewPager.setAdapter(new QuizResultsTabsAdapter(getSupportFragmentManager(), problems, resultTabs));
        tabs.setupWithViewPager(viewPager);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.slide_out_bottom);
    }
}
