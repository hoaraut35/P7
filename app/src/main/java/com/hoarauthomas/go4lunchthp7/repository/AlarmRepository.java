package com.hoarauthomas.go4lunchthp7.repository;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkRequest;

import com.hoarauthomas.go4lunchthp7.R;
import com.hoarauthomas.go4lunchthp7.workmanager.WorkManagerTest;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class AlarmRepository {

    androidx.work.WorkManager myWorkManager;
    Context myContext;

    public AlarmRepository(Context context) {
        this.myContext = context;
        this.myWorkManager = androidx.work.WorkManager.getInstance(context);
    }

    public void setAlarm() {

        //for production
        // LocalTime alarmTime = LocalTime.of(12, 00);

        //for test
        LocalTime alarmTime = LocalTime.of(17, 45);

        Log.i("[ALARME]", "Alarm time :" + alarmTime.toString());
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        LocalTime nowTime = now.toLocalTime();
        Log.i("[ALARME]", "Time now : " + nowTime.toString());

        if (nowTime == alarmTime || nowTime.isAfter(alarmTime)) {
            now = now.plusDays(1);
            Log.i("[ALARME]", "Add one day to delay if the time is passed : " + now.toString());
        }

        now = now.withHour(alarmTime.getHour()).withMinute(alarmTime.getMinute());
        Duration duration = Duration.between(LocalDateTime.now(), now);

        Log.i("[ALARME]", "Load work in : " + duration.getSeconds() + " sec");

        //TODO: set repeat interval to 24h, initialDelay to dephase start
        //repeat interval must be greather or equal tio 15min
        PeriodicWorkRequest myPeriodicWorkRequest = new PeriodicWorkRequest.Builder(WorkManagerTest.class, 15, TimeUnit.MINUTES)
                .setInitialDelay(duration.getSeconds(), TimeUnit.SECONDS)
                .addTag("go4lunch")
                .build();

        myWorkManager.enqueueUniquePeriodicWork("go4lunch", ExistingPeriodicWorkPolicy.REPLACE, myPeriodicWorkRequest);

    }

    public void removeAlarm() {
        myWorkManager.cancelAllWorkByTag("go4lunch");
    }


}