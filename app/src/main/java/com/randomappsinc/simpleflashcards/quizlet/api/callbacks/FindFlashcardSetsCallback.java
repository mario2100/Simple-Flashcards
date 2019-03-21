package com.randomappsinc.simpleflashcards.quizlet.api.callbacks;

import androidx.annotation.NonNull;

import com.randomappsinc.simpleflashcards.quizlet.api.ApiConstants;
import com.randomappsinc.simpleflashcards.quizlet.api.QuizletSearchManager;
import com.randomappsinc.simpleflashcards.quizlet.api.models.QuizletSearchResults;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FindFlashcardSetsCallback implements Callback<QuizletSearchResults> {

    @Override
    public void onResponse(@NonNull Call<QuizletSearchResults> call, @NonNull Response<QuizletSearchResults> response) {
        if (response.code() == ApiConstants.HTTP_STATUS_OK) {
            QuizletSearchResults searchResults = response.body();
            if (searchResults != null) {
                QuizletSearchManager.getInstance().onFlashcardSetsFound(searchResults.getResults());
            }
        }
        // TODO: Have error handling here
    }

    @Override
    public void onFailure(@NonNull Call<QuizletSearchResults> call, @NonNull Throwable t) {
        // TODO: Have error handling here
    }
}
