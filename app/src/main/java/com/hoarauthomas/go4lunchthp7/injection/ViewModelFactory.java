package com.hoarauthomas.go4lunchthp7.injection;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.hoarauthomas.go4lunchthp7.repositories.PlaceRepository;
import com.hoarauthomas.go4lunchthp7.viewmodel.ListPlacesViewModel;

import java.util.concurrent.Executor;

//TODO: add here others repository...

public class ViewModelFactory implements ViewModelProvider.Factory {

    //regroup all repositories here ...
    private final PlaceRepository placeRepository;
    private final Executor executor;

    //constructor to soulager MainAcitivty
    public ViewModelFactory(PlaceRepository placeRepository, Executor executor) {
        this.placeRepository = placeRepository;
        this.executor = executor;
    }


    //see Dagger 2 for information
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ListPlacesViewModel.class)) {
            return (T) new ListPlacesViewModel(placeRepository, executor);
        }
        throw new IllegalArgumentException("Unknow ViewModel class");
    }
}
