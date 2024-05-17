package com.udemy.workmanager.app;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class MyWorker extends Worker {

    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    /**
     * doWork() runs asynchronously on the background
     * thread provided by WorkManager
     */
    @NonNull
    @Override
    public Result doWork() {

        int maxLimit = 0;

        // getting Data from Activity
        Data inputData = getInputData();
        if (inputData.hasKeyWithValueOfType("max_limit", Integer.class)) {
            maxLimit = inputData.getInt("max_limit", 0);
        }

        for (int i = 0; i < maxLimit; i++) {
            Log.d("--do_work--", "Count: " + i);
        }

        // sending Data to Activity
        Data outputData = new Data.Builder()
                .putString("status", "success")
                .build();

        return Result.success(outputData);
    }
}
