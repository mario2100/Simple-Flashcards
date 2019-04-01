package com.randomappsinc.simpleflashcards.common.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.randomappsinc.simpleflashcards.persistence.models.FlashcardSetDO;

/**
 * Used to show added flashcard sets from data restoration or nearby sharing.
 */
public class FlashcardSetPreview implements Parcelable {

    private int setId;
    private String setName;
    private int numCards;

    public FlashcardSetPreview(FlashcardSetDO flashcardSet) {
        this.setId = flashcardSet.getId();
        this.setName = flashcardSet.getName();
        this.numCards = flashcardSet.getFlashcards().size();
    }

    public int getSetId() {
        return setId;
    }

    public String getSetName() {
        return setName;
    }

    public int getNumCards() {
        return numCards;
    }

    protected FlashcardSetPreview(Parcel in) {
        setId = in.readInt();
        setName = in.readString();
        numCards = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(setId);
        dest.writeString(setName);
        dest.writeInt(numCards);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<FlashcardSetPreview> CREATOR =
            new Parcelable.Creator<FlashcardSetPreview>() {
                @Override
                public FlashcardSetPreview createFromParcel(Parcel in) {
                    return new FlashcardSetPreview(in);
                }

                @Override
                public FlashcardSetPreview[] newArray(int size) {
                    return new FlashcardSetPreview[size];
                }
            };
}
