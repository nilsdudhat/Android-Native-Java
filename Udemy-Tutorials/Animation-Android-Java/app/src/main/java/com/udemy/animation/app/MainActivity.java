package com.udemy.animation.app;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.udemy.animation.app.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    Animation animate_button;
    Animation animate_text;

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        animate_button = AnimationUtils.loadAnimation(this, R.anim.animate_button);
        animate_text = AnimationUtils.loadAnimation(this, R.anim.animate_text);
        animate_button.setStartOffset(1000);

        binding.btnGetStarted.setAnimation(animate_button);
        binding.txtLogin.setAnimation(animate_button);
        binding.txtTitle.setAnimation(animate_text);
        binding.txtSubTitle.setAnimation(animate_text);

        binding.btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}