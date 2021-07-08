package com.hoarauthomas.go4lunchthp7.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.LocationServices;
import com.hoarauthomas.go4lunchthp7.MainApplication;
import com.hoarauthomas.go4lunchthp7.repository.AuthentificationRepository;
import com.hoarauthomas.go4lunchthp7.repository.LocationRepository;
import com.hoarauthomas.go4lunchthp7.repository.RestaurantsRepository;

//TODO: erreur try to generify ?

//https://medium.com/koderlabs/viewmodel-with-viewmodelprovider-factory-the-creator-of-viewmodel-8fabfec1aa4f
//ViewModelProvider.Factory is responsible to create the instance of ViewModels

public class ViewModelFactory implements ViewModelProvider.Factory {

    private static ViewModelFactory myViewModelFactory;

    //Add repository here...
    private final AuthentificationRepository authentificationRepository;
    private final LocationRepository locationRepository;
    private final RestaurantsRepository restaurantsRepository;

    //Get an instance of ViewModelFactory ... see pattern singleton
    public static ViewModelFactory getInstance() {
        Log.i("[THOMAS]", "[VIEWMODELFACTORY INIT]");
        if (myViewModelFactory == null) {
            synchronized (ViewModelFactory.class) {
                if (myViewModelFactory == null) {
                    Log.i("[THOMAS]", "[VIEWMODELFACTORY NEW OBJECT]");
                    Application application = MainApplication.getApplication();
                    myViewModelFactory = new ViewModelFactory(
                            new AuthentificationRepository(),
                            new RestaurantsRepository(),
                            new LocationRepository(LocationServices.getFusedLocationProviderClient(application), application)
                    );
                } else {
                    Log.i("[THOMAS]", "[VIEWMODELFACTORY OBJECT ALREADY EXIST]");
                }

            }
        }

        return myViewModelFactory;
    }

    //constructor of ViewModelFactory ...
    private ViewModelFactory(AuthentificationRepository authentificationRepository, RestaurantsRepository restaurantsRepository, LocationRepository locationRepository) {
        this.authentificationRepository = authentificationRepository;
        this.locationRepository = locationRepository;
        this.restaurantsRepository = restaurantsRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ViewModelGo4Lunch.class)) {
            return (T) new ViewModelGo4Lunch(this.authentificationRepository, this.restaurantsRepository, this.locationRepository);
            //return (T) new ViewModelGo4Lunch();
        }
        throw new IllegalArgumentException("[V M F] Unknow ViewModel class");
    }
}
