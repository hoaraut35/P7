package com.hoarauthomas.go4lunchthp7.workmanager;

import android.content.Context;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkRequest;

import com.hoarauthomas.go4lunchthp7.MainApplication;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class AlarmManager extends AppCompatActivity {

    androidx.work.WorkManager myWorkManager;

    public void getAlarmManager(Context context) {
        this.myWorkManager = androidx.work.WorkManager.getInstance(context);
    }

    public void setAlarm() {

        //Determine the format to work
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        //Determine actual datetime
        LocalDateTime dateactu = LocalDateTime.now();
        Log.i("[JOB]", "Actual date and time : " + dateactu.format(formatter));

        //Determine the target date and time to execute request
        LocalDate dateToStart = LocalDate.now();
        LocalTime timeToStart = LocalTime.parse("12:00:00");
        LocalDateTime fullDateTimeToStart = LocalDateTime.of(dateToStart, timeToStart);
        Log.i("[JOB]", "Target date and time : " + fullDateTimeToStart.format(formatter).toString());

        //extract initial delay to construct work request after
        long minutes = ChronoUnit.MINUTES.between(fullDateTimeToStart, dateactu);
        Log.i("[JOB]", "Extraction initial delay for work request : " + Long.toString(minutes) + " min");

        //on peut creer l'alrme
        if (minutes <= 0) {

            //first we cancel all job with tag popup12h00
            myWorkManager.cancelAllWorkByTag("popup12h00");

            //create request
            PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(
                    WorkManagerTest.class,
                    15 ,
                    TimeUnit.MINUTES)
                    .addTag("popup12h00")
                    .build();

            //send request
            myWorkManager.enqueue(periodicWorkRequest);

           /* //second create a new work
            WorkRequest newLoadWork = new OneTimeWorkRequest.Builder(WorkManagerTest.class)
                    .setInitialDelay(Math.abs((int) minutes), TimeUnit.MINUTES)
                    .addTag("popup12h00")
                    .build();

            myWorkManager.enqueue(newLoadWork);

            */

        }
        //le temps est dépassé
        else {
            //first we cancel all job with tag popup12h00
            myWorkManager.cancelAllWorkByTag("popup12h00");

            Log.i("[JOB]", "Demarrage job j+1 ");

            //create request
            PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(
                    WorkManagerTest.class,
                    15 ,
                    TimeUnit.MINUTES)
                    .addTag("popup12h00")
                    .build();

            //send request
            myWorkManager.enqueue(periodicWorkRequest);

            /*
            //second create a new work
            WorkRequest newLoadWork = new OneTimeWorkRequest.Builder(WorkManagerTest.class)
                  //  .setInitialDelay(Math.abs((int) minutes) + 1440, TimeUnit.MINUTES)//+1j
                    .setInitialDelay(10, TimeUnit.MINUTES)//+1j
                    .addTag("popup12h00")
                    .build();

            myWorkManager.enqueue(newLoadWork);

             */
        }


    }


//    public void getAlarmProgress(){
//
//
//        myWorkManager.getWorkInfoByIdLiveData(UUID.fromString("popup12h00"))
//                .observe(getLifecycle(), new Observer<WorkInfo>() {
//                    @Override
//                    public void onChanged(WorkInfo workInfo) {
//
//                    }
//                });
//
//    }


}
