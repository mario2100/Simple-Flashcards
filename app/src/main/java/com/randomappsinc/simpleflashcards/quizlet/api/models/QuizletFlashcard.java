package com.randomappsinc.simpleflashcards.quizlet.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.randomappsinc.simpleflashcards.persistence.models.FlashcardDO;

import androidx.annotation.Nullable;

public class QuizletFlashcard {

    @SerializedName("term")
    @Expose
    private String term;

    @SerializedName("definition")
    @Expose
    private String definition;

    @SerializedName("image")
    @Expose
    private Image image;

    private class Image {

        @SerializedName("url")
        @Expose
        private String url;

        String getUrl() {
            return url;
        }
    }

    public String getTerm() {
        return term;
    }

    public String getDefinition() {
        return definition;
    }

    @Nullable
    public String getImageUrl() {
        return image == null ? null : image.getUrl();
    }

    public FlashcardDO toFlashcard() {
        FlashcardDO flashcard = new FlashcardDO();
        flashcard.setTerm(term);
        flashcard.setDefinition(definition);
        flashcard.setTermImageUrl(getImageUrl());
        return flashcard;
    }
}
