package com.udemy.quiz.app.api;

import com.udemy.quiz.app.models.QuestionList;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * used to define the structure and behaviour of network requests to a RESTful API.
 * acts as a bridge between Android App and the Web Service
 * */
public interface QuestionsInterface {

    @GET("my_quiz_api.php") // end point
    Call<QuestionList> getQuestionList();
}
