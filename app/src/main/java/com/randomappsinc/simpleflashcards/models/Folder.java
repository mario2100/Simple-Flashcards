package com.randomappsinc.simpleflashcards.models;

import com.randomappsinc.simpleflashcards.persistence.models.FlashcardSet;

import java.util.List;

public class Folder {

    private int id;
    private String name;
    private List<FlashcardSet> flashcardSets;

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

    public List<FlashcardSet> getFlashcardSets() {
        return flashcardSets;
    }

    public void setFlashcardSets(List<FlashcardSet> flashcardSets) {
        this.flashcardSets = flashcardSets;
    }
}
