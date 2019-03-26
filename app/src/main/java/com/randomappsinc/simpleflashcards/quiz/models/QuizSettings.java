package com.randomappsinc.simpleflashcards.quiz.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class QuizSettings implements Parcelable {

    private int numQuestions;
    private int numSeconds;
    private List<Integer> questionTypes;
    private boolean useTermsAsQuestions;

    public QuizSettings(int numQuestions, int numMinutes, List<Integer> questionTypes, boolean useTermsAsQuestions) {
        this.numQuestions = numQuestions;
        this.numSeconds = (int) TimeUnit.MINUTES.toSeconds(numMinutes);
        this.questionTypes = questionTypes;
        this.useTermsAsQuestions = useTermsAsQuestions;
    }

    public int getNumQuestions() {
        return numQuestions;
    }

    public int getNumSeconds() {
        return numSeconds;
    }

    public List<Integer> getQuestionTypes() {
        return questionTypes;
    }

    public boolean useTermsAsQuestions() {
        return useTermsAsQuestions;
    }

    protected QuizSettings(Parcel in) {
        numQuestions = in.readInt();
        numSeconds = in.readInt();
        if (in.readByte() == 0x01) {
            questionTypes = new ArrayList<>();
            in.readList(questionTypes, Integer.class.getClassLoader());
        } else {
            questionTypes = null;
        }
        useTermsAsQuestions = in.readByte() != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(numQuestions);
        dest.writeInt(numSeconds);
        if (questionTypes == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(questionTypes);
        }
        dest.writeByte((byte) (useTermsAsQuestions ? 0x01 : 0x00));
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<QuizSettings> CREATOR = new Parcelable.Creator<QuizSettings>() {
        @Override
        public QuizSettings createFromParcel(Parcel in) {
            return new QuizSettings(in);
        }

        @Override
        public QuizSettings[] newArray(int size) {
            return new QuizSettings[size];
        }
    };
}
