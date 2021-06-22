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
    private FusedLocationProviderClient fusedLocationProviderClient;

    private LocationCallback callback;

    private MutableLiveData<android.location.Location> locationMutableLiveData = new MutableLiveData<>();

    public LocationRepository(FusedLocationProviderClient fusedLocationProviderClient){
        this.fusedLocationProviderClient = fusedLocationProviderClient;
    }

    public LiveData<Location> getLocationLiveData(){return locationMutableLiveData;}

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
