package com.udemy.quiz.app.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.udemy.quiz.app.models.QuestionList;
import com.udemy.quiz.app.repository.QuizRepository;

public class QuizViewModel extends ViewModel {

    QuizRepository quizRepository = new QuizRepository();
    LiveData<QuestionList> questionListLiveData;

    public QuizViewModel() {
        questionListLiveData = quizRepository.getQuestions();
    }

    public LiveData<QuestionList> getQuestionListLiveData() {
        return questionListLiveData;
    }
}
