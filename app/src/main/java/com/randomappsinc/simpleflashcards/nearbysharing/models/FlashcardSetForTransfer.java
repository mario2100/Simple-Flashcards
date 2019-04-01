package com.randomappsinc.simpleflashcards.nearbysharing.models;

import com.randomappsinc.simpleflashcards.nearbysharing.constants.FlashcardSetTransferState;
import com.randomappsinc.simpleflashcards.persistence.models.FlashcardSetDO;

public class FlashcardSetForTransfer {

    private FlashcardSetDO flashcardSet;
    @FlashcardSetTransferState private int transferState = FlashcardSetTransferState.NOT_YET_SENT;

    public FlashcardSetForTransfer(FlashcardSetDO flashcardSet) {
        this.flashcardSet = flashcardSet;
    }

    public FlashcardSetDO getFlashcardSet() {
        return flashcardSet;
    }

    public int getTransferState() {
        return transferState;
    }

    public void setTransferState(int transferState) {
        this.transferState = transferState;
    }
}
