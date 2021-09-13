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

        String CHANNEL_ID = "Go4Lunch";




//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
//            Log.i("[JOB]", "Working.... with older version");
//            Notification.Builder myNotification = new Notification.Builder(getApplicationContext());
//
//            myNotification
//                    .setAutoCancel(false)
//                    //.setContentIntent(PendingIntent.readPendingIntentOrNullFromParcel())
//                    .setContentTitle("Go4Lunch")
//                    .setContentText("text detaill");
//
//            Notification notification = myNotification.build();
//
//
//        } else {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "test")
                .setSmallIcon(R.drawable.ic_logo)
                .setContentTitle("Go4Lunch")
                .setContentText(" n'oubliez pas votre restaurant ce midi")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);


        Log.i("[JOB]", "Working....");
        //result to return


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "getString(R.string.channel_name)";
            String description = "etString(R.string.channel_description)";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            // NotificationManagerCompat notificationManager = (NotificationManagerCompat) getSystemService(NotificationManager.class);
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
            notificationManagerCompat.notify(1, builder.build());

//            notificationManager.createNotificationChannel(channel);
        }

        return Result.success();


}
}
