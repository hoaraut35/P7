package com.hoarauthomas.go4lunchthp7.injection;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.hoarauthomas.go4lunchthp7.MainActivity;
import com.hoarauthomas.go4lunchthp7.repositories.LocationRepository;
import com.hoarauthomas.go4lunchthp7.repositories.RestaurantsRepository;
import com.hoarauthomas.go4lunchthp7.viewmodel.ViewModelGo4Lunch;

import java.util.concurrent.Executor;

//TODO: add here others repository... see Dagger 2
//Android Architecture Components

public class ViewModelFactory implements ViewModelProvider.Factory {

    //regroup all repositories here ...
    private final RestaurantsRepository placeRepository;
    private final LocationRepository locationRepository;
    //add others repository here...

    //for separated thread...
    private final Executor executor;

    //constructor to soulager MainAcitivty
   public ViewModelFactory(RestaurantsRepository placeRepository, LocationRepository locationRepository, Executor executor) {
    //public ViewModelFactory(RestaurantsRepository placeRepository, LocationRepository locationRepository, Executor executor) {
        this.placeRepository = placeRepository;
        this.locationRepository = locationRepository;

        //add other repository here...
        this.executor = executor;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ViewModelGo4Lunch.class)) {
            return (T) new ViewModelGo4Lunch( placeRepository, locationRepository,  executor);
        }
        throw new IllegalArgumentException("Unknow ViewModel class");
    }
}
