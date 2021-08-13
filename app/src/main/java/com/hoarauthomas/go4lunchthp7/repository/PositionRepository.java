package com.hoarauthomas.go4lunchthp7.repository;

import android.location.Location;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;

import org.jetbrains.annotations.NotNull;

//Only to get data and publish it to ViewModel ...

public class PositionRepository {

    //setting for position request
    private static final int LOCATION_REQUEST_INTERVAL_MS = 10_000;
    private static final float SMALLEST_DISPLACEMENT_THRESHOLD_METER = 25;
    //
    private final FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback myCallback;

    //Mutable Livedat...
    private final MutableLiveData<Location> locationMutableLiveData = new MutableLiveData<>();

    //constructor called by factory
    public PositionRepository(@NonNull FusedLocationProviderClient fusedLocationProviderClient) {
        this.fusedLocationProviderClient = fusedLocationProviderClient;
    }

    //publish to VM...
    public LiveData<Location> getmyPositionFromRepo() {
        return locationMutableLiveData;
    }

    @RequiresPermission(anyOf = {"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"})
    public void startLocationRequest() {
        if (myCallback == null) {
            myCallback = new LocationCallback() {

                @Override
                public void onLocationResult(@NonNull @NotNull LocationResult locationResult) {
                    super.onLocationResult(locationResult);

                    Location location = locationResult.getLastLocation();

                    if (location == null) {
                        //TODO:add default location if no result from fragment parameter by sample
                        Log.i("[LOCATION]", "position introuvable donc par défaut");
                    } else {
                        Log.i("[LOCATION]", "position trouvée");
                        locationMutableLiveData.setValue(location);
                    }
                }
            };
        }

        fusedLocationProviderClient.removeLocationUpdates(myCallback);

        fusedLocationProviderClient.requestLocationUpdates(LocationRequest.create()
                        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                        .setSmallestDisplacement(SMALLEST_DISPLACEMENT_THRESHOLD_METER)
                        .setInterval(LOCATION_REQUEST_INTERVAL_MS),
                myCallback,
                Looper.getMainLooper());
    }

    public void stopLocationRequest() {
        if (myCallback != null) {
            fusedLocationProviderClient.removeLocationUpdates(myCallback);
        }
    }

}

