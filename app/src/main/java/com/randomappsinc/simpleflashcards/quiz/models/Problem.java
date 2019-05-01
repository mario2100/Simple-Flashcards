package com.randomappsinc.simpleflashcards.quiz.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

import com.randomappsinc.simpleflashcards.persistence.models.FlashcardDO;
import com.randomappsinc.simpleflashcards.quiz.constants.QuestionType;
import com.randomappsinc.simpleflashcards.utils.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Problem implements Parcelable {

    public static final int NORMAL_NUM_ANSWER_OPTIONS = 4;

    private @QuestionType int questionType;
    private int questionNumber;
    private String question;
    @Nullable private String questionImageUrl;
    private String answer;
    @Nullable private List<String> options;
    private String givenAnswer;

    Problem(int questionNumber) {
        this.questionNumber = questionNumber;
    }

    public void setAsMultipleChoiceQuestion(
            FlashcardDO flashcard,
            List<FlashcardDO> flashcards,
            boolean useTermAsQuestion) {
        questionType = QuestionType.MULTIPLE_CHOICE;
        question = useTermAsQuestion ? flashcard.getTerm() : flashcard.getDefinition();
        questionImageUrl = useTermAsQuestion ? flashcard.getTermImageUrl() : flashcard.getDefinitionImageUrl();
        answer = useTermAsQuestion ? flashcard.getDefinition() : flashcard.getTerm();

        // Remove duplicates from answer pool since flashcards may have identical definitions/terms
        Set<String> optionsSet = new HashSet<>();
        for (FlashcardDO currentCard : flashcards) {
            optionsSet.add(useTermAsQuestion ? currentCard.getDefinition() : currentCard.getTerm());
        }
        // Remove the answer from the options set, since we're going to inject it by itself
        optionsSet.remove(useTermAsQuestion ? flashcard.getDefinition() : flashcard.getTerm());
        List<String> wrongOptionsList = new ArrayList<>(optionsSet);

        // Shuffle wrong options so we don't see similar orders every time
        Collections.shuffle(wrongOptionsList);

        // Account for the answer with the + 1
        int numOptions = Math.min(NORMAL_NUM_ANSWER_OPTIONS, wrongOptionsList.size() + 1);
        // Create answer options
        List<String> options = new ArrayList<>(numOptions);
        // Add answer
        options.add(useTermAsQuestion ? flashcard.getDefinition() : flashcard.getTerm());
        // Start at 1 because we already have the answer
        for (int i = 1; i < numOptions; i++) {
            options.add(wrongOptionsList.get(i - 1));
        }
        // Shuffle options so the right answer isn't always the first one
        Collections.shuffle(options);

        this.options = options;
    }

    public void setAsFreeFormInputQuestion(FlashcardDO flashcard, boolean useTermAsQuestion) {
        questionType = QuestionType.FREE_FORM_INPUT;
        question = useTermAsQuestion ? flashcard.getTerm() : flashcard.getDefinition();
        questionImageUrl = useTermAsQuestion ? flashcard.getTermImageUrl() : flashcard.getDefinitionImageUrl();
        answer = useTermAsQuestion ? flashcard.getDefinition() : flashcard.getTerm();
    }

    public @QuestionType int getQuestionType() {
        return questionType;
    }

    public int getQuestionNumber() {
        return questionNumber;
    }

    public String getQuestion() {
        return question;
    }

    void setQuestion(String question) {
        this.question = question;
    }

    @Nullable
    public String getQuestionImageUrl() {
        return questionImageUrl;
    }

    public String getAnswer() {
        return answer;
    }

    @Nullable
    public List<String> getOptions() {
        return options;
    }

    public String getGivenAnswer() {
        return givenAnswer;
    }

    void setGivenAnswer(String givenAnswer) {
        this.givenAnswer = givenAnswer;
    }

    public boolean wasUserCorrect() {
        switch (questionType) {
            case QuestionType.MULTIPLE_CHOICE:
                return answer.equals(givenAnswer);
            case QuestionType.FREE_FORM_INPUT:
                return isFreeFormInputCloseEnoughMatch();
            default:
                throw new IllegalStateException("Unsupported question type");
        }
    }

    private boolean isFreeFormInputCloseEnoughMatch() {
        String[] answerSplits = answer.split("\\s+");
        HashMap<String, Integer> answerWords = StringUtils.getWordAmounts(answerSplits);

        // TODO: Figure out how givenAnswer can be null
        if (givenAnswer == null) {
            return false;
        }

        HashMap<String, Integer> responseWords = StringUtils.getWordAmounts(givenAnswer.split("\\s+"));
        int allowedMisses = answerSplits.length / 10;
        int numMisses = 0;
        for (String responseWord : responseWords.keySet()) {
            int responseAmount = responseWords.get(responseWord);
            if (answerWords.containsKey(responseWord)) {
                int answerAmount = answerWords.get(responseWord);
                int newAmount = answerAmount - responseAmount;
                answerWords.put(responseWord, newAmount);
            } else {
                numMisses += responseAmount;
            }
        }
        for (String answerWord : answerWords.keySet()) {
            numMisses += Math.abs(answerWords.get(answerWord));
        }
        return numMisses <= allowedMisses;
    }

    protected Problem(Parcel in) {
        questionType = in.readInt();
        questionNumber = in.readInt();
        question = in.readString();
        questionImageUrl = in.readString();
        answer = in.readString();
        if (in.readByte() == 0x01) {
            options = new ArrayList<>();
            in.readList(options, String.class.getClassLoader());
        } else {
            options = null;
        }
        givenAnswer = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(questionType);
        dest.writeInt(questionNumber);
        dest.writeString(question);
        dest.writeString(questionImageUrl);
        dest.writeString(answer);
        if (options == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(options);
        }
        dest.writeString(givenAnswer);
    }

    @SuppressWarnings("unused")
    public static final Creator<Problem> CREATOR = new Creator<Problem>() {
        @Override
        public Problem createFromParcel(Parcel in) {
            return new Problem(in);
        }

        @Override
        public Problem[] newArray(int size) {
            return new Problem[size];
        }
    };
}
