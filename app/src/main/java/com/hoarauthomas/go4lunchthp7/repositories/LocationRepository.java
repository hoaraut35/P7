package com.hoarauthomas.go4lunchthp7.repositories;

import android.Manifest;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.RestrictionEntry;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.facebook.appevents.ml.Utils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.hoarauthomas.go4lunchthp7.MainActivity;

import static android.provider.ContactsContract.Directory.PACKAGE_NAME;
import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class LocationRepository {

    public Context context;

    private FusedLocationProviderClient myFusedLocationProviderClient;

    //initialisation with cxontext
    public void initMyFused() {


        myFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.context);




    }



    //the constructor
    public LocationRepository(Context context){

        try{
            Log.i("[THOMAS]","Repo Context => " + context);
            this.context = context;
            //myFusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(context);




        }
        catch (Exception e){
            Log.i("[THOMAS]","Exception : " + e.getMessage());
        }


    }






    //added to stop location updates
    private void stopLocationUpdates(){
        //myFusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
    }



}
