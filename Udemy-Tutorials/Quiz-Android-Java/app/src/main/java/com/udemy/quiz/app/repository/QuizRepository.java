package com.udemy.quiz.app.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.udemy.quiz.app.api.QuestionsInterface;
import com.udemy.quiz.app.api.RetrofitInstance;
import com.udemy.quiz.app.models.QuestionList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * interacts with the API service interfaces
 * handling data retrieval and operations
 */
public class QuizRepository {

    private QuestionsInterface questionsInterface;

    public QuizRepository() {
        this.questionsInterface = new RetrofitInstance()
                .getRetrofitInstance()
                .create(QuestionsInterface.class);
    }

    public LiveData<QuestionList> getQuestions() {
        MutableLiveData<QuestionList> data = new MutableLiveData<>();

        Call<QuestionList> response = questionsInterface.getQuestionList();
        response.enqueue(new Callback<QuestionList>() {
            @Override
            public void onResponse(Call<QuestionList> call, Response<QuestionList> response) {
                QuestionList questions = response.body();
                data.setValue(questions);
            }

            @Override
            public void onFailure(Call<QuestionList> call, Throwable throwable) {

            }
        });
        return data;
    }
}
