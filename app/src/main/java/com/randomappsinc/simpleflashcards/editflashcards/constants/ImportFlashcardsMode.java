package com.randomappsinc.simpleflashcards.editflashcards.constants;

import android.support.annotation.IntDef;

/** Constants representing the different ways you can import flashcards on the edit set page */
@IntDef({
        ImportFlashcardsMode.MOVE,
        ImportFlashcardsMode.COPY,
})
public @interface ImportFlashcardsMode {
    int MOVE = 0;
    int COPY = 1;
}
