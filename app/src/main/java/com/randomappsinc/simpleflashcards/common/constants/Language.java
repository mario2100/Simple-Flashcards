package com.randomappsinc.simpleflashcards.common.constants;

import androidx.annotation.IntDef;

@IntDef({
        Language.ENGLISH,
        Language.SPANISH,
        Language.FRENCH,
        Language.JAPANESE,
        Language.PORTUGUESE,
        Language.CHINESE,
        Language.GERMAN,
        Language.ITALIAN,
        Language.KOREAN,
        Language.HINDI,
        Language.ARABIC,
        Language.BENGALI,
        Language.RUSSIAN
})
public @interface Language {
    int ENGLISH = 0;
    int SPANISH = 1;
    int FRENCH = 2;
    int JAPANESE = 3;
    int PORTUGUESE = 4;
    int CHINESE = 5;
    int GERMAN = 6;
    int ITALIAN = 7;
    int KOREAN = 8;
    int HINDI = 9;
    int ARABIC = 10;
    int BENGALI = 11;
    int RUSSIAN = 12;
}
