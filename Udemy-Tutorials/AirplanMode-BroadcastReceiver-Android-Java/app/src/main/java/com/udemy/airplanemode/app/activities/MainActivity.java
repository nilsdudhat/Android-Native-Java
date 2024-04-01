package com.udemy.airplanemode.app.activities;

import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.udemy.airplanemode.app.R;
import com.udemy.airplanemode.app.receivers.AirplaneModeReceiver;

public class MainActivity extends AppCompatActivity {

    Button btnStart, btnStop;
    AirplaneModeReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnStart = findViewById(R.id.btn_start);
        btnStop = findViewById(R.id.btn_stop);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentFilter intentFilter = new IntentFilter("android.intent.action.AIRPLANE_MODE");

                receiver = new AirplaneModeReceiver();
                registerReceiver(receiver, intentFilter);
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (receiver != null) {
                    unregisterReceiver(receiver);
                    receiver = null;
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        btnStop.callOnClick();
        super.onDestroy();
    }
}