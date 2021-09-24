package com.hoarauthomas.go4lunchthp7.workmanager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.jetbrains.annotations.NotNull;

public class WorkManagerTest extends Worker {


    private static final String PROGRESS = "PROGRESS";


    public WorkManagerTest(@NonNull @NotNull Context context, @NonNull @NotNull WorkerParameters workerParams) {
        super(context, workerParams);
    setProgressAsync(new Data.Builder().putInt(PROGRESS,0).build());

    }







    @NonNull
    @NotNull
    @Override
    public Result doWork() {


        Context applicationContext = getApplicationContext();

        //get data from ui
        String restaurant_title = getInputData().getString("restaurant_title");
        String restaurant_address = getInputData().getString("restaurant_address");
        String[] myWorkmates = getInputData().getStringArray("workmates");

        try {
            Log.i("[JOB]", "doWork enable...");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // Create the NotificationChannel, but only on API 26+ because
                // the NotificationChannel class is new and not in the support library
                CharSequence name = "go4lunch";
                String description = "go4lunchdatachannel";
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel channel =
                        new NotificationChannel("go4lunch", "go4lunch", importance);
                channel.setDescription(description);

                // Add the channel
                NotificationManager notificationManager =
                        (NotificationManager) applicationContext.getSystemService(Context.NOTIFICATION_SERVICE);

                if (notificationManager != null) {
                    notificationManager.createNotificationChannel(channel);
                }
            }

            // Create the notification
            NotificationCompat.Builder builder = new NotificationCompat.Builder(applicationContext, "go4lunch")
                    .setSmallIcon(android.R.drawable.star_big_on)
                    .setContentTitle("Go4Lunch It's time to lunch")
                    .setContentText(restaurant_title + " \n" + restaurant_address + " \n" + myWorkmates[0])
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setVibrate(new long[0]);

            // Show the notification
            NotificationManagerCompat.from(applicationContext).notify(1, builder.build());


            setProgressAsync(new Data.Builder().putInt(PROGRESS,100).build());

            return Result.success();
        } catch (Throwable throwable) {
           return Result.failure();
        }


    }


}
