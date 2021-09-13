package com.hoarauthomas.go4lunchthp7.workmanager;

import static androidx.core.content.ContextCompat.getSystemService;

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

public class WorkManagerTest extends Worker {

    public WorkManagerTest(@NonNull @NotNull Context context, @NonNull @NotNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @NotNull
    @Override
    public Result doWork() {

        String CHANNEL_ID = "123";



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            CharSequence name = "Go4Lunch";
            String description = "Channel for Go4Lunch";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(getApplicationContext(), null);
            notificationManager.createNotificationChannel(channel);

        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "123")
                .setSmallIcon(R.drawable.ic_logo)
                .setContentTitle("Go4Lunch")
                .setContentText("Message de test")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        Log.i("[JOB]", "Working....");


        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        //notificationManagerCompat.notify(1, builder.build());
//        notificationManagerCompat.createNotificationChannel( CHANNEL_ID);

        return Result.success();


    }
}
