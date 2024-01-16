package com.udemy.frech.teacher.app;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnBlack, btnGreen, btnRed, btnPurple, btnYellow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnBlack = findViewById(R.id.btnBlack);
        btnGreen = findViewById(R.id.btnGreen);
        btnRed = findViewById(R.id.btnRed);
        btnPurple = findViewById(R.id.btnPurple);
        btnYellow = findViewById(R.id.btnYellow);

        btnBlack.setOnClickListener(this);
        btnGreen.setOnClickListener(this);
        btnRed.setOnClickListener(this);
        btnPurple.setOnClickListener(this);
        btnYellow.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btnBlack) {
            play(R.raw.black);
        } else if (id == R.id.btnGreen) {
            play(R.raw.green);
        } else if (id == R.id.btnRed) {
            play(R.raw.red);
        } else if (id == R.id.btnPurple) {
            play(R.raw.purple);
        } else if (id == R.id.btnYellow) {
            play(R.raw.yellow);
        }
    }

    private void play(int rawId) {
        MediaPlayer mediaPlayer = MediaPlayer.create(MainActivity.this, rawId);
        mediaPlayer.start();
    }
}