package com.randomappsinc.simpleflashcards.folders.models;

import com.randomappsinc.simpleflashcards.persistence.models.FlashcardSetDO;

import java.util.List;

public class Folder {

    private int id;
    private String name;
    private List<FlashcardSetDO> flashcardSets;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<FlashcardSetDO> getFlashcardSets() {
        return flashcardSets;
    }

    public void setFlashcardSets(List<FlashcardSetDO> flashcardSets) {
        this.flashcardSets = flashcardSets;
    }
}
