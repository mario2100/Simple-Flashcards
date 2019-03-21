package com.randomappsinc.simpleflashcards.quiz.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.randomappsinc.simpleflashcards.quiz.fragments.QuizResultsFragment;
import com.randomappsinc.simpleflashcards.quiz.models.Problem;

import java.util.ArrayList;
import java.util.List;

public class QuizResultsTabsAdapter extends FragmentPagerAdapter {

    private String[] resultsTabs;
    private List<Problem> problems;

    public QuizResultsTabsAdapter(FragmentManager fragmentManager, List<Problem> problems, String[] tabNames) {
        super(fragmentManager);
        this.resultsTabs = tabNames;
        this.problems = problems;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                ArrayList<Problem> wrongProblems = new ArrayList<>();
                for (Problem problem : problems) {
                    if (!problem.wasUserCorrect()) {
                        wrongProblems.add(problem);
                    }
                }
                return QuizResultsFragment.getInstance(wrongProblems, problems.size(), true);
            case 1:
                ArrayList<Problem> rightProblems = new ArrayList<>();
                for (Problem problem : problems) {
                    if (problem.wasUserCorrect()) {
                        rightProblems.add(problem);
                    }
                }
                return QuizResultsFragment.getInstance(rightProblems, problems.size(), false);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return resultsTabs.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return resultsTabs[position];
    }
}
