package com.randomappsinc.simpleflashcards.quiz.constants;

import androidx.annotation.IntDef;

@IntDef({
        QuizScore.GOOD,
        QuizScore.OKAY,
        QuizScore.BAD,
})
public @interface QuizScore {
    int GOOD = 0;
    int OKAY = 1;
    int BAD = 2;
}
