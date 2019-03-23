package com.randomappsinc.simpleflashcards.quizlet.api;

import java.io.IOException;

import androidx.annotation.NonNull;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class QuizletAuthInterceptor implements Interceptor {

    @Override
    public Response intercept(@NonNull Interceptor.Chain chain) throws IOException {
        Request request = chain.request();
        HttpUrl url = request
                .url()
                .newBuilder()
                .addQueryParameter(ApiConstants.CLIENT_ID_KEY, QuizletAuthConstants.CLIENT_ID)
                .build();
        request = request.newBuilder().url(url).build();
        return chain.proceed(request);
    }
}
