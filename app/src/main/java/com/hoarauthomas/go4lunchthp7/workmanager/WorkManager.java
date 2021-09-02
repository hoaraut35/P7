package com.hoarauthomas.go4lunchthp7.workmanager;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.hoarauthomas.go4lunchthp7.R;

import org.jetbrains.annotations.NotNull;

import static android.provider.Settings.System.getString;

public class WorkManager extends Worker {

    public WorkManager(@NonNull @NotNull Context context, @NonNull @NotNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @NotNull
    @Override
    public Result doWork() {


        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "123")
                .setSmallIcon(R.drawable.ic_logo)
                .setContentTitle("Go4Lunch")
                .setContentText(" n'oubliez pas votre restaurant ce midi")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.notify(1, builder.build());

        Log.i("[ALARM]", "Working....");
        //result to return
        return Result.success();

        //result failure

        //result retry
    }
}
