package com.hoarauthomas.go4lunchthp7.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.LocationServices;
import com.hoarauthomas.go4lunchthp7.MainApplication;
import com.hoarauthomas.go4lunchthp7.permissions.PermissionChecker;
import com.hoarauthomas.go4lunchthp7.repository.AuthRepository;
import com.hoarauthomas.go4lunchthp7.repository.PositionRepository;
import com.hoarauthomas.go4lunchthp7.repository.RestaurantsRepository;

//TODO: erreur try to generify ?

//https://medium.com/koderlabs/viewmodel-with-viewmodelprovider-factory-the-creator-of-viewmodel-8fabfec1aa4f
//ViewModelProvider.Factory is responsible to create the instance of ViewModels

public class ViewModelFactory implements ViewModelProvider.Factory {

    private static ViewModelFactory myViewModelFactory;

    //Add repository here...
    private final AuthRepository authRepository;
    private final PositionRepository positionRepository;
    private final RestaurantsRepository restaurantsRepository;
    private final PermissionChecker permissionChecker;

    //Get an instance of ViewModelFactory ... see pattern singleton
    public static ViewModelFactory getInstance() {
        Log.i("[THOMAS]", "[VIEWMODELFACTORY INIT]");
        if (myViewModelFactory == null) {
            synchronized (ViewModelFactory.class) {
                if (myViewModelFactory == null) {
                    Log.i("[THOMAS]", "[VIEWMODELFACTORY NEW OBJECT]");
                    Application application = MainApplication.getApplication();
                    myViewModelFactory = new ViewModelFactory(
                            new AuthRepository(),
                            new RestaurantsRepository(),
                            new PositionRepository(LocationServices.getFusedLocationProviderClient(application)),
                            new PermissionChecker(application)
                    );
                } else {
                    Log.i("[THOMAS]", "[VIEWMODELFACTORY OBJECT ALREADY EXIST]");
                }

            }
        }

        return myViewModelFactory;
    }

    //constructor of ViewModelFactory ...
    private ViewModelFactory(AuthRepository authRepository, RestaurantsRepository restaurantsRepository, PositionRepository positionRepository, PermissionChecker permissionChecker) {
        this.authRepository = authRepository;
        this.positionRepository = positionRepository;
        this.restaurantsRepository = restaurantsRepository;
        this.permissionChecker = permissionChecker;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ViewModelGo4Lunch.class)) {
            return (T) new ViewModelGo4Lunch(
                    authRepository,
                    restaurantsRepository,
                    positionRepository,
                    permissionChecker);
        }
        throw new IllegalArgumentException("[V M F] Unknow ViewModel class");
    }
}