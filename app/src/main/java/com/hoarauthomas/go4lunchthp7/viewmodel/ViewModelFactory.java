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
import com.hoarauthomas.go4lunchthp7.repository.WorkMatesRepository;
import com.hoarauthomas.go4lunchthp7.ui.detail.ViewModelDetail;
import com.hoarauthomas.go4lunchthp7.ui.map.ViewModelMap;
import com.hoarauthomas.go4lunchthp7.ui.restaurant.ViewModelRestaurant;

//https://medium.com/koderlabs/viewmodel-with-viewmodelprovider-factory-the-creator-of-viewmodel-8fabfec1aa4f
//ViewModelProvider.Factory is responsible to create the instance of ViewModels, one for all application

public class ViewModelFactory implements ViewModelProvider.Factory {

    private static ViewModelFactory myViewModelFactory;

    //Add repository here...
    private final AuthRepository authRepository;
    private final PositionRepository positionRepository;
    private final RestaurantsRepository restaurantsRepository;
    private final WorkMatesRepository workMatesRepository;
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
                            new WorkMatesRepository(),
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
    private ViewModelFactory(AuthRepository authRepository, RestaurantsRepository restaurantsRepository, PositionRepository positionRepository, WorkMatesRepository workMatesRepository, PermissionChecker permissionChecker) {
        this.authRepository = authRepository;
        this.positionRepository = positionRepository;
        this.restaurantsRepository = restaurantsRepository;
        this.workMatesRepository = workMatesRepository;
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
                    workMatesRepository,
                    permissionChecker);
        }

        if(modelClass.isAssignableFrom(ViewModelMap.class)){
            return(T) new ViewModelMap(
                    positionRepository,
                    restaurantsRepository

            );

        }

        if(modelClass.isAssignableFrom(ViewModelDetail.class)){

        }
        if(modelClass.isAssignableFrom(ViewModelRestaurant.class)){

        }



        throw new IllegalArgumentException("[V M F] Unknow ViewModel class");
    }
}
