package com.udemy.quiz.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.udemy.quiz.app.R;
import com.udemy.quiz.app.databinding.ActivityMainBinding;
import com.udemy.quiz.app.models.Question;
import com.udemy.quiz.app.viewmodel.QuizViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    QuizViewModel quizViewModel;
    List<Question> questionList;

    int result = 0;
    int totalQuestions = 0;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // resetting the sources
        result = 0;
        totalQuestions = 0;

        // creating an instance of "QuizViewModel"
        quizViewModel = new ViewModelProvider(MainActivity.this).get(QuizViewModel.class);

        // displaying the first question
        displayFirstQuestion();

        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayNextQuestion();
            }
        });
    }

    public void displayFirstQuestion() {
        // observing live data from a ViewModel
        quizViewModel.getQuestionListLiveData().observe(MainActivity.this, questions -> {
            // called when the data inside LiveData changes
            questionList = questions;

            binding.txtQuestion.setText(
                    new StringBuilder()
                            .append("Question 1:\n")
                            .append(questionList.get(0).getQuestion()));

            binding.radio1.setText(questions.get(0).getOption1());
            binding.radio2.setText(questions.get(0).getOption2());
            binding.radio3.setText(questions.get(0).getOption3());
            binding.radio4.setText(questions.get(0).getOption4());
        });
    }

    public void displayNextQuestion() {

        // displaying the next question
        int selectedOption = binding.radioOptions.getCheckedRadioButtonId();
        if (selectedOption != -1) {
            RadioButton radioButton = findViewById(selectedOption);

            // getting the number of questions
            totalQuestions = questionList.size();

            // check if the chosen option is correct
            if (radioButton.getText().toString().equals(questionList.get(i).getCorrectOption())) {
                result++;
                binding.txtResult.setText(new StringBuilder().append("Correct Answers: ").append(result));
            }

            if (i < (questionList.size() - 1)) {
                i++;
            } else {
                Intent intent = new Intent(MainActivity.this, ResultsActivity.class);
                intent.putExtra("result", result);
                intent.putExtra("total", totalQuestions);
                startActivity(intent);
                finish();
                return;
            }

            // displaying next question
            binding.txtQuestion.setText(
                    new StringBuilder()
                            .append("Question ")
                            .append(i + 1)
                            .append(" : \n")
                            .append(questionList.get(i).getQuestion()));

            binding.radio1.setText(questionList.get(i).getOption1());
            binding.radio2.setText(questionList.get(i).getOption2());
            binding.radio3.setText(questionList.get(i).getOption3());
            binding.radio4.setText(questionList.get(i).getOption4());

            if (i == (questionList.size() - 1)) {
                binding.btnNext.setText(new StringBuilder().append("Finish"));
            }

            binding.radioOptions.clearCheck();
        } else {
            Toast.makeText(this, "Please select an option", Toast.LENGTH_SHORT).show();
        }
    }
}