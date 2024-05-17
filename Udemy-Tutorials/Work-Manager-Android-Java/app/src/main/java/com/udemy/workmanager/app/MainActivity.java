package com.udemy.workmanager.app;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.udemy.workmanager.app.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

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

        // Data to send to WorkManager
        Data inputData = new Data.Builder()
                .putInt("max_limit", 500)
                .build();

        // Constraints
        Constraints constraints = new Constraints.Builder()
                .setRequiresCharging(true)
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        // making use of worker
        WorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(MyWorker.class)
                .setConstraints(constraints)
                .setInputData(inputData)
                .build();

        // enqueue the request with WorkManager
        binding.btnRunWorkManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WorkManager.getInstance(getApplicationContext()).enqueue(oneTimeWorkRequest);
            }
        });

        // monitoring the status of WorkManager
        WorkManager.getInstance(getApplicationContext())
                .getWorkInfoByIdLiveData(oneTimeWorkRequest.getId())
                .observe(MainActivity.this, workInfo -> {
                    if (workInfo != null) {
                        ToastUtils.showShortToast(MainActivity.this,
                                "Status: " + workInfo.getState().name());

                        // Data to get from WorkManager
                        if (workInfo.getState().isFinished()) {
                            Data outputData = workInfo.getOutputData();
                            if (outputData.hasKeyWithValueOfType("status", String.class)) {
                                Log.d("--status--", "output: " + outputData.getString("status"));
                            }
                        }
                    }
                });
    }
}