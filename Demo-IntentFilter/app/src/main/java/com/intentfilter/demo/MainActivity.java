package com.intentfilter.demo;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.intentfilter.demo.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnSend.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_SEND); // intent
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_EMAIL, "nilsdudhat14@gmail.com");
            intent.putExtra(Intent.EXTRA_SUBJECT, "This is a dummy message");
            intent.putExtra(Intent.EXTRA_TEXT, "Dummy test message");
            startActivity(intent);
        });

        binding.btnView.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            startActivity(intent);
        });
    }
}