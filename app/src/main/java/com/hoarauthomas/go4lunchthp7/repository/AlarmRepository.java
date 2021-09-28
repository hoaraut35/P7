package com.hoarauthomas.go4lunchthp7.repository;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.work.WorkManager;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;

import com.hoarauthomas.go4lunchthp7.workmanager.WorkManagerTest;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

public class AlarmRepository {

    WorkManager myWorkManager;
    Context myContext;

    public AlarmRepository(Context context) {
        this.myContext = context;
        this.myWorkManager = WorkManager.getInstance(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setAlarm() {

        //for production
        // LocalTime alarmTime = LocalTime.of(12, 00);

        //set starting time for test
        LocalDateTime actuel = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        LocalTime alarmTime = actuel.toLocalTime();
        alarmTime = alarmTime.plusMinutes(2);

//        LocalTime alarmTime = LocalTime.of(18, 11);

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

        //minimum repeat interval must be greather or equal to 15min see JobScheduler API
        PeriodicWorkRequest myPeriodicWorkRequest = new PeriodicWorkRequest.Builder(WorkManagerTest.class, 1, TimeUnit.DAYS)
                //first work is delayed after we use the repeatinterval 24h
                .setInitialDelay(duration.getSeconds(), TimeUnit.SECONDS)
                .addTag("go4lunch")
                .build();

        //TODO: policy ? KEEP REPLACE
        myWorkManager.enqueueUniquePeriodicWork("go4lunch", ExistingPeriodicWorkPolicy.KEEP, myPeriodicWorkRequest);

    }

    public void check_alarm(){

    }

    public void removeAlarm() {
        myWorkManager.cancelAllWorkByTag("go4lunch");
    }

}