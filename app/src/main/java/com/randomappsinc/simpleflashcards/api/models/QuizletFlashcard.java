package com.randomappsinc.simpleflashcards.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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

    public String getImageUrl() {
        return image.getUrl();
    }
}