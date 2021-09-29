package com.hoarauthomas.go4lunchthp7.repository;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.work.Data;
import androidx.work.WorkManager;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;

import com.hoarauthomas.go4lunchthp7.R;
import com.hoarauthomas.go4lunchthp7.workmanager.WorkManagerTest;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

public class AlarmRepository {

    FirebaseAuthRepository myFirebase;
    WorkManager myWorkManager;
    Context myContext;

    public AlarmRepository(Context context) {
        this.myContext = context;
        this.myWorkManager = WorkManager.getInstance(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setAlarm(String uid) {

        Toast.makeText(myContext.getApplicationContext(), R.string.notification_enable_msg, Toast.LENGTH_SHORT).show();


//        Log.i("[ALARME]","User uid for alarm : " + myFirebase.myFireBaseAuthInstance.getCurrentUser().getUid());
        //for production
        // LocalTime alarmTime = LocalTime.of(12, 00);

        //set starting time for test
        LocalDateTime actuel = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        LocalTime alarmTime = actuel.toLocalTime();
        alarmTime = alarmTime.plusMinutes(1);

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
        PeriodicWorkRequest myPeriodicWorkRequest = new PeriodicWorkRequest.Builder(WorkManagerTest.class, 15, TimeUnit.MINUTES)
                //first work is delayed after we use the repeatinterval 24h
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