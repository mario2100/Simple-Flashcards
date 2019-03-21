package com.randomappsinc.simpleflashcards.nearbysharing.constants;

import androidx.annotation.IntDef;

@IntDef({
        FlashcardSetTransferState.NOT_YET_SENT,
        FlashcardSetTransferState.SENT,
})
public @interface FlashcardSetTransferState {
    int NOT_YET_SENT = 0;
    int SENT = 1;
}
