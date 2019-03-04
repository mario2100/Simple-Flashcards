package com.randomappsinc.simpleflashcards.dialogs;

import com.randomappsinc.simpleflashcards.persistence.models.FlashcardSet;

public class SingleFlashcardSetChooserDialog {

    public interface Listener {
        void onFlashcardSetChosen(FlashcardSet flashcardSet);
    }

    public void SingleFlashcardSetChooserDialog(Listener listener) {

    }
}
