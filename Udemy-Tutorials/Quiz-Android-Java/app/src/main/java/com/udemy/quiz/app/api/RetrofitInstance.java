package com.udemy.quiz.app.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {

    String baseUrl = "http://10.0.2.2/quiz/";
    // for pc localhost = 127.0.0.1
    // for emulator localhost = 10.0.2.2

    // create and return configured retrofit instance
    public Retrofit getRetrofitInstance() {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
