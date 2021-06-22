package com.hoarauthomas.go4lunchthp7.repository;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class LocationRepository {

    public Context context;
    private FusedLocationProviderClient myFusedLocationProviderClient;


    //private LocationCallback mLocationCallback;
    //private Location mCurrentLocation;
    //private LocationRequest mLocationRequest;
    public Location location;


    private MutableLiveData<Location> lastPosition = new MutableLiveData<>();




    public LiveData<Location> getMyLocation() {

        //data.setValue(location);
       // Log.i("[THOMAS]", "set location repo livedat a" + this.location.getLongitude());

        return lastPosition;
    }


    //the constructor with context for Locaztionervice
    public LocationRepository(Context context) {

        try {
            Log.i("[THOMAS]", "REPO LOCATION : Affichage du context => " + context);

            //get the context
            this.context = context;

            //to get an instance
            myFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);

            //to check permission
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                //get last position
                myFusedLocationProviderClient.getLastLocation()
                        .addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                Log.i("[THOMAS]", "REPO Get the last know position : " + "Lat: " + location.getLatitude() + " Long: " + location.getLongitude());
                                location.set(location);
                                lastPosition.setValue(location);
                            }
                        });
            } else {
                Log.i("[THOMAS]", "REPO LOCATION Permissions fail ... ");
                //get last position
                myFusedLocationProviderClient.getLastLocation()
                        .addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                Log.i("[THOMAS]", "REPO Get the last know position : " + "Lat: " + location.getLatitude() + " Long: " + location.getLongitude());
                                location.set(location);
                                lastPosition.setValue(location);
                            }
                        });
            }






          /*  SettingsClient mSettingsClient = LocationServices.getSettingsClient(context);

            mLocationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(@NonNull LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    mCurrentLocation = locationResult.getLocations().get(0);

                    if (mCurrentLocation != null) {
                        Log.i("[THOMAS]", "REPO Longitude : " + mCurrentLocation.getLongitude());
                        Log.i("[THOMAS]", "REPO Latitude : " + mCurrentLocation.getLatitude());

                    } else {
                        Log.i("[THOMAS]", "REPO position nulle. !!!");
                    }


                }


            };

            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(5000);
            mLocationRequest.setFastestInterval(5000);
            mLocationRequest.setNumUpdates(3);

            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
            builder.addLocationRequest(mLocationRequest);
            LocationSettingsRequest mLocationSet = builder.build();

            //Task<LocationSettingsResponse> locationResponse = mSettingsClient.checkLocationSettings(mLocationSet);

            Log.i("[THOMAS]", "REPO LOCATION : Fin de traitement .");
*/
        } catch (Exception e) {
            Log.i("[THOMAS]", "Exception to get last position : " + e.getMessage());
        }


    }


    //added to stop location updates only in request location updates mode
    private void stopLocationUpdates() {
        //    myFusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
    }


}
