package com.randomappsinc.simpleflashcards.persistence.models;

import io.realm.RealmList;
import io.realm.RealmObject;

public class FolderDO extends RealmObject {

    private int id;
    private String name;
    private RealmList<FlashcardSet> flashcardSets;

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

    public RealmList<FlashcardSet> getFlashcardSets() {
        return flashcardSets;
    }

    public void setFlashcardSets(RealmList<FlashcardSet> flashcardSets) {
        this.flashcardSets = flashcardSets;
    }
}
