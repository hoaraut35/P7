package com.hoarauthomas.go4lunchthp7.repository;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkRequest;

import com.hoarauthomas.go4lunchthp7.R;
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
    Notification myNotification = new Notification();
    NotificationManagerCompat notificationManagerCompat;

    /**
     * constructor
     */
    public AlarmRepository(Context context) {
        this.myContext = context;
        this.myWorkManager = androidx.work.WorkManager.getInstance(context);
       // setupNotification("Go4Lunch","Démonstration notification");
    }

    /**
     *  Setup channel for notification
     */
    public void setupChannel(){
        notificationManagerCompat = NotificationManagerCompat.from(myContext);
        notificationManagerCompat.notify(1,myNotification);
    }

    /**
     * make a notification
     * @param title title of notifica&tion
     * @param content content of notification
     */
    public void setupNotification(String title, String content){

        String CHANNEL_ID = "123";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            CharSequence name = "Go4Lunch";
            String description = "Channel for Go4Lunch";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(myContext, null);
//            notificationManager.createNotificationChannel(channel);

        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(myContext, "123")
                .setSmallIcon(R.drawable.ic_logo)
                .setContentTitle("Go4Lunch from dowork")
                .setContentText("Message de test")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);







        myNotification = new NotificationCompat.Builder(myContext,"Go4Lunch")
                .setContentText(content)
                .setContentTitle(title)
                .setSmallIcon(R.drawable.ic_logo)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .build();

        setupChannel();
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

    /**
     * Stop all work with the tag parameter
     */
    public void removeAlarm() {
        myWorkManager.cancelAllWorkByTag("popup12h00");
    }





    /**
     * Enable or disable notification with state parameter from viewmodel
     * @param state
     */
    public void setNotification(Boolean state) {

        if (state){
            setupNotification("Go4Lunch","contenu");    
        }else
        {
            removeNotification();
        }

    }

    /**
     * Remove all notification with tag id
     */
    private void removeNotification() {
        myWorkManager.cancelAllWorkByTag("popup12h00");
    }

}
