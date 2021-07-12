package com.hoarauthomas.go4lunchthp7.repository;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.hoarauthomas.go4lunchthp7.permissions.PermissionChecker;

import org.jetbrains.annotations.NotNull;

//Only to get data and publish it to ViewModel ...
public class PositionRepository {

    private static final int LOCATION_REQUEST_INTERVAL_MS = 10_000;
    private static final float SMALLEST_DISPLACEMENT_THRESHOLD_METER = 25;

    private final FusedLocationProviderClient fusedLocationProviderClient;

    private final MutableLiveData<Location> locationMutableLiveData = new MutableLiveData<>();

    private LocationCallback myCallback;


    public PositionRepository(@NonNull FusedLocationProviderClient fusedLocationProviderClient) {
        Log.i("[LOCATION]", "- Appel Repository Position");
        this.fusedLocationProviderClient = fusedLocationProviderClient;
    }

    public LiveData<Location> getLocationLiveData() {
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
                        Log.i("[LOCATION]", "position introuvable");
                    } else {
                        Log.i("[LOCATION]", "position trouvée");
                    }

                    locationMutableLiveData.setValue(location);
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

