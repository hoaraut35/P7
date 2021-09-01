package com.hoarauthomas.go4lunchthp7.workmanager;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.jetbrains.annotations.NotNull;

public class WorkManager extends Worker {

    public WorkManager(@NonNull @NotNull Context context, @NonNull @NotNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @NotNull
    @Override
    public Result doWork() {


        //create notification sample

        Log.i("[ALARM]", "Working....");
        //result to return
        return Result.success();

        //result failure

        //result retry
    }
}
