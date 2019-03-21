package com.randomappsinc.simpleflashcards.quiz.constants;

import androidx.annotation.IntDef;

@IntDef({
        QuestionType.MULTIPLE_CHOICE,
        QuestionType.FREE_FORM_INPUT
})
public @interface QuestionType {
    int MULTIPLE_CHOICE = 0;
    int FREE_FORM_INPUT = 1;
}
