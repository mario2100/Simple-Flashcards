package com.randomappsinc.simpleflashcards.common.constants;

import androidx.annotation.IntDef;

@IntDef({
        Language.ENGLISH,
        Language.SPANISH,
        Language.FRENCH,
        Language.JAPANESE,
        Language.PORTUGUESE
})
public @interface Language {
    int ENGLISH = 0;
    int SPANISH = 1;
    int FRENCH = 2;
    int JAPANESE = 3;
    int PORTUGUESE = 4;
}
