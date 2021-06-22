package com.hoarauthomas.go4lunchthp7.injection;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.hoarauthomas.go4lunchthp7.repositories.LocationRepository;
import com.hoarauthomas.go4lunchthp7.repositories.RestaurantsRepository;
import com.hoarauthomas.go4lunchthp7.viewmodel.ViewModelGo4Lunch;

import java.util.concurrent.Executor;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private final RestaurantsRepository placeRepository;
    private final LocationRepository locationRepository;
    private final Executor executor;

    public ViewModelFactory(RestaurantsRepository placeRepository, LocationRepository locationRepository, Executor executor) {
        this.placeRepository = placeRepository;
        this.locationRepository = locationRepository;
        this.executor = executor;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ViewModelGo4Lunch.class)) {
            return (T) new ViewModelGo4Lunch(placeRepository, locationRepository, executor);
        }
        throw new IllegalArgumentException("Unknow ViewModel class");
    }
}
