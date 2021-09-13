package com.hoarauthomas.go4lunchthp7.repository;

import static java.util.jar.Pack200.Unpacker.PROGRESS;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.hoarauthomas.go4lunchthp7.workmanager.WorkManagerTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class AlarmRepository {

    androidx.work.WorkManager myWorkManager;
    Context myContext;




    public AlarmRepository(Context context) {
        this.myContext = context;
        this.myWorkManager = androidx.work.WorkManager.getInstance(context);
    }

    //get an alarm
    public void getAlarm(String idAlarm){

        myWorkManager
                .getWorkInfoByIdLiveData(UUID.fromString(idAlarm))
                .observe((LifecycleOwner) myContext, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {

                        if (workInfo != null){
                            Data progress = workInfo.getProgress();
                          //  int value = progress.getInt(progress,0);

                            //otherqs
                        }

                    }
                });

    }







    //set alarm
    public void setAlarm(){

        //Determine the format to work
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        //Determine actual datetime
        LocalDateTime dateactu = LocalDateTime.now();
        Log.i("[JOB]","Actual date and time : " + dateactu.format(formatter));

        //Determine the target date and time to execute request
        LocalDate dateToStart = LocalDate.now();
        LocalTime timeToStart = LocalTime.parse("12:00:00");
        LocalDateTime fullDateTimeToStart = LocalDateTime.of(dateToStart,timeToStart);
        Log.i("[JOB]","Target date and time : " + fullDateTimeToStart.format(formatter).toString());

        //extract initial delay to construct work request after
        long minutes = ChronoUnit.MINUTES.between(fullDateTimeToStart,dateactu);
        Log.i("[JOB]","Extraction initial delay for work request : " + Long.toString(minutes) + " min");


        //on peut creer l'alrme
        if (minutes < 0 ){


            //first we cancel all job with tag popup12h00
            androidx.work.WorkManager.getInstance(myContext).cancelAllWorkByTag("popup12h00");

            //second create a new work
            WorkRequest newLoadWork = new OneTimeWorkRequest.Builder(WorkManagerTest.class)
                    .setInitialDelay(Math.abs((int)minutes), TimeUnit.MINUTES)
                    .addTag("popup12h00")
                    .build();

            androidx.work.WorkManager.getInstance(myContext).enqueue(newLoadWork);

            //periodic mode
            // PeriodicWorkRequest newLoadPeriodicWork = new PeriodicWorkRequest.Builder(WorkManagerTest.class,
            //        15, TimeUnit.MINUTES)
            // //constrains
            //.build();
        }
        //le temps est dépassé
        else
        {
            //first we cancel all job with tag popup12h00
            androidx.work.WorkManager.getInstance(myContext).cancelAllWorkByTag("popup12h00");

            //second create a new work
            WorkRequest newLoadWork = new OneTimeWorkRequest.Builder(WorkManagerTest.class)
                    .setInitialDelay(Math.abs((int)minutes) + 1440, TimeUnit.MINUTES)
                    .addTag("popup12h00")
                    .build();

            androidx.work.WorkManager.getInstance(myContext).enqueue(newLoadWork);

            //nothing to do
            //j+1 ?
        }

    }

    public void removeAlarm() {
    }


    public void getAlarmState(String idalarm){
//        myWorkManager.getWorkInfoByIdLiveData(UUID.fromString(idalarm))
//                .observe(, new Observer<WorkInfo>() {
//                    @Override
//                    public void onChanged(WorkInfo workInfo) {
//
//                        if (workInfo != null){
//
//                            Data progress = workInfo.getProgress();
//                            int val = progress.getInt(PROGRESS,0);
//                            Log.i("[JOB]","progression" +val);
//                        }
//                    }
//                });
//    }
}

}
