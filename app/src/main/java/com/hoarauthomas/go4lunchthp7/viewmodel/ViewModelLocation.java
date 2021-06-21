package com.hoarauthomas.go4lunchthp7.viewmodel;

import android.content.Context;

import androidx.lifecycle.ViewModel;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.hoarauthomas.go4lunchthp7.model.pojo.Location;
import com.hoarauthomas.go4lunchthp7.repositories.LocationRepository;

public class ViewModelLocation extends ViewModel {

    public LocationRepository locationRepository;



    public void init()
    {
     //  locationRepository.initMyFused();

    }

    public ViewModelLocation(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    /*  public ViewModelLocation(Context context,FusedLocationProviderClient locationProviderClient){
            this.locationRepository = new LocationRepository(context, locationProviderClient);
        }

       */
    // alternatively using a setter
    public void setFusedLocationProviderClient(FusedLocationProviderClient locationProvider) {
   //    locationRepository = new LocationRepository(context, locationProvider);
    }






}
