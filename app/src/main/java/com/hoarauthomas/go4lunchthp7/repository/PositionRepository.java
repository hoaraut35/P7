package com.hoarauthomas.go4lunchthp7.repository;

import android.location.Location;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.model.LatLng;

//to get position when changed

public class PositionRepository {

    //parameters
    private static final int LOCATION_REQUEST_INTERVAL_MS = 10_000;
    private static final float SMALLEST_DISPLACEMENT_THRESHOLD_METER = 100;

    //for object
    private final FusedLocationProviderClient fusedLocationProviderClient;

    //To update data

    private final MutableLiveData<Location> locationMutableLiveData = new MutableLiveData<>(null);
    private final MutableLiveData<LatLng> myLatLngLiveData = new MutableLiveData<>(null);

    //for callback of FusedLocationProviderClient
   // private LocationCallback locationCallback;

    //constructor called by viewmodelfactory to get an instance...
    public PositionRepository(@NonNull FusedLocationProviderClient fusedLocationProviderClient) {
        this.fusedLocationProviderClient = fusedLocationProviderClient;
    }

    //publish to viewModel
    public LiveData<Location> getLocationLiveData() {
        return locationMutableLiveData;
    }

    //publish to viewmodel with latlgn
    public LiveData<LatLng> getLocationLatLgnLiveData(){return myLatLngLiveData;}

    //public method to start listener position
    @RequiresPermission(anyOf = {"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"})
    public void startLocationRequest() {
        LocationRequest request = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(LOCATION_REQUEST_INTERVAL_MS)
                .setSmallestDisplacement(SMALLEST_DISPLACEMENT_THRESHOLD_METER);

        fusedLocationProviderClient.requestLocationUpdates(request, locationCallback, Looper.getMainLooper());

    }

    private final LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);

            if (locationResult != null && locationResult.getLastLocation()!= null){
                myLatLngLiveData.setValue(new LatLng(locationResult.getLastLocation().getLatitude(),locationResult.getLastLocation().getLongitude()));
                locationMutableLiveData.setValue(locationResult.getLastLocation());
            }
        }
    };

    //public method to stop listener when permission is not granted
    public void stopLocationRequest() {
        if (locationCallback != null) {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        }
    }
}

