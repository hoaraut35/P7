package com.hoarauthomas.go4lunchthp7.repository;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import java.util.UUID;

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

    }

}
