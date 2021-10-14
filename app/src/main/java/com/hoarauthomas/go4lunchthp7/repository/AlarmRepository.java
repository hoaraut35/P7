package com.hoarauthomas.go4lunchthp7.repository;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;

import com.hoarauthomas.go4lunchthp7.R;
import com.hoarauthomas.go4lunchthp7.workmanager.WorkManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

public class AlarmRepository {

    final androidx.work.WorkManager myWorkManager;
    Context myContext;

    public AlarmRepository(Context context) {
        this.myContext = context;
        this.myWorkManager = androidx.work.WorkManager.getInstance(context);
    }

    public void setAlarm(String uid) {

        Toast.makeText(myContext.getApplicationContext(), R.string.notification_enable_msg, Toast.LENGTH_SHORT).show();

        //TODO:must be disable for testing
        //LocalTime alarmTime = LocalTime.of(12, 00);

        //TODO: must be enable for testing
        LocalDateTime actual = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        LocalTime alarmTime = actual.toLocalTime();
        alarmTime = alarmTime.plusMinutes(1);

        Log.i("[ALARM]", "Alarm time :" + alarmTime.toString());

        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        LocalTime nowTime = now.toLocalTime();

        Log.i("[ALARM]", "Time now : " + nowTime.toString());

        if (nowTime == alarmTime || nowTime.isAfter(alarmTime)) {
            now = now.plusDays(1);
            Log.i("[ALARM]", "Add one day to delay if the time is passed : " + now.toString());
        }

        now = now.withHour(alarmTime.getHour()).withMinute(alarmTime.getMinute());
        Duration duration = Duration.between(LocalDateTime.now(), now);
        Log.i("[ALARM]", "Load work in : " + duration.getSeconds() + " sec");

        //minimum repeat interval must be more  or equal than 15min see JobScheduler API
        PeriodicWorkRequest myPeriodicWorkRequest = new PeriodicWorkRequest.Builder(WorkManager.class, 15, TimeUnit.MINUTES)
                //first work is delayed after we use the repeat interval 24h
                .setInitialDelay(duration.getSeconds(), TimeUnit.SECONDS)
                .setInputData(
                        new Data.Builder()
                                .putString("uid", uid)
                                .build()
                )
                .addTag("go4lunch")
                .build();

        myWorkManager.enqueueUniquePeriodicWork("go4lunch", ExistingPeriodicWorkPolicy.REPLACE, myPeriodicWorkRequest);

    }

    public void removeAlarm() {
        myWorkManager.cancelAllWorkByTag("go4lunch");
        Toast.makeText(myContext.getApplicationContext(), R.string.notification_disable_msg, Toast.LENGTH_SHORT).show();
    }

}