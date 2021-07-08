package com.hoarauthomas.go4lunchthp7.repository;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class LocationRepository {

    private static final int LOCATION_REQUEST_INTERVAL_MS = 10_000;
    private static final float SMALLEST_DISPLACEMENT_THRESHOLD_METER = 25;
    private final FusedLocationProviderClient fusedLocationProviderClient;
    private final Context myContext;
    private LocationCallback callback;
    private final MutableLiveData<android.location.Location> locationMutableLiveData = new MutableLiveData<>();

    //constructor
    public LocationRepository(FusedLocationProviderClient fusedLocationProviderClient, Context context) {
        Log.i("[THOMAS]","- Appel Repository Location");
        this.fusedLocationProviderClient = fusedLocationProviderClient;
        this.myContext = context;
    }

    //publish to viewmodel
    public LiveData<Location> getLocationLiveData() {
        return locationMutableLiveData;
    }

    //TODO: permission

    public void startLocationRequest() {
        if (callback == null) {
            callback = new LocationCallback() {
                @Override
                public void onLocationResult(@NonNull LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    android.location.Location location = locationResult.getLastLocation();
                    locationMutableLiveData.setValue(location);
                }



            };
        }

        fusedLocationProviderClient.removeLocationUpdates(callback);

        if (ActivityCompat.checkSelfPermission(myContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(myContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(
                LocationRequest.create()
                        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                        .setSmallestDisplacement(SMALLEST_DISPLACEMENT_THRESHOLD_METER)
                        .setInterval(LOCATION_REQUEST_INTERVAL_MS),
                callback,
                Looper.getMainLooper()
        );
    }

    public void stopLocationRequest() {
        fusedLocationProviderClient.removeLocationUpdates(callback);
    }

}
