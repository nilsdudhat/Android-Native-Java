package com.demo.architecture.mvc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.demo.architecture.mvc.databinding.ActivityMainBinding;

import java.util.Observable;
import java.util.Observer;

public class MainActivity extends AppCompatActivity implements Observer {

    // creating object of Model class
    Model model;

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // creating relationship between the
        // observable Model and the
        // observer Activity
        model = new Model();
        model.addObserver(this);

        // calling setValueAtIndex() method
        // by passing appropriate arguments
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                model.setValueAtIndex(0);
            }
        });
        binding.button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                model.setValueAtIndex(1);
            }
        });
        binding.button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                model.setValueAtIndex(2);
            }
        });
    }

    // function to update the view after
    // the values are modified by the model
    @Override
    public void update(Observable observable, Object o) {

        // changing text of the buttons
        // according to updated values
        binding.button.setText(new StringBuilder().append("Count: ").append(model.getValueAtIndex(0)));
        binding.button2.setText(new StringBuilder("Count: ").append(model.getValueAtIndex(1)));
        binding.button3.setText(new StringBuilder("Count: ").append(model.getValueAtIndex(2)));
    }
}