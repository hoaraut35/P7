package com.hoarauthomas.go4lunchthp7.viewmodel;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.LocationServices;
import com.hoarauthomas.go4lunchthp7.MainActivity;
import com.hoarauthomas.go4lunchthp7.MainApplication;
import com.hoarauthomas.go4lunchthp7.repository.LocationRepositoryNew;
import com.hoarauthomas.go4lunchthp7.repository.RestaurantsRepository;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private static ViewModelFactory myViewModelFactory;

    private final LocationRepositoryNew locationRepository;
    private final RestaurantsRepository restaurantsRepository;

    public static ViewModelFactory getInstance() {
        if (myViewModelFactory == null) {
            synchronized (ViewModelFactory.class) {
                if (myViewModelFactory == null) {
                    Application application = MainApplication.getApplication();
                    Log.i("[THOMAS]","Context factory : "+ application);

                    myViewModelFactory = new ViewModelFactory(
                            new RestaurantsRepository(),
                            new LocationRepositoryNew(LocationServices.getFusedLocationProviderClient(application))
                    );
                }
            }
        }
        return myViewModelFactory;
    }

    private ViewModelFactory(RestaurantsRepository restaurantsRepository, LocationRepositoryNew locationRepository) {
        this.locationRepository = locationRepository;
        this.restaurantsRepository = restaurantsRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ViewModelGo4Lunch.class)) {
            return (T) new ViewModelGo4Lunch(
                    this.restaurantsRepository,
                    this.locationRepository
            );
        }
        throw new IllegalArgumentException("Unknow ViewModel class");
    }
}
