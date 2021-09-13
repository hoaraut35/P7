package com.hoarauthomas.go4lunchthp7.factory;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.hoarauthomas.go4lunchthp7.MainApplication;
import com.hoarauthomas.go4lunchthp7.permissions.PermissionChecker;
import com.hoarauthomas.go4lunchthp7.repository.AlarmRepository;
import com.hoarauthomas.go4lunchthp7.repository.FirebaseAuthRepository;
import com.hoarauthomas.go4lunchthp7.repository.PlaceAutocompleteRepository;
import com.hoarauthomas.go4lunchthp7.repository.PositionRepository;
import com.hoarauthomas.go4lunchthp7.repository.RestaurantsRepository;
import com.hoarauthomas.go4lunchthp7.repository.WorkMatesRepository;
import com.hoarauthomas.go4lunchthp7.ui.ViewModelMain;
import com.hoarauthomas.go4lunchthp7.ui.detail.ViewModelDetail;
import com.hoarauthomas.go4lunchthp7.ui.map.ViewModelMap;
import com.hoarauthomas.go4lunchthp7.ui.restaurant.ViewModelRestaurant;
import com.hoarauthomas.go4lunchthp7.ui.workmates.ViewModelWorkMates;

//Dependance injection with factory pattern
//https://medium.com/koderlabs/viewmodel-with-viewmodelprovider-factory-the-creator-of-viewmodel-8fabfec1aa4f
//ViewModelProvider.Factory is responsible to create the instance of ViewModels, one for all application

public class ViewModelFactory implements ViewModelProvider.Factory {

    //ViewModelFactory
    private static ViewModelFactory myViewModelFactory;

    //Add repository here...
    private final FirebaseAuthRepository firebaseAuthRepository;
    private final PositionRepository positionRepository;
    private final RestaurantsRepository restaurantsRepository;
    private final WorkMatesRepository workMatesRepository;
    private final PermissionChecker permissionChecker;
    private final PlaceAutocompleteRepository placeAutocompleteRepository;
    private final AlarmRepository alarmRepository;

    //Get an instance of ViewModelFactory ... see pattern singleton
    public static ViewModelFactory getInstance() {
        Log.i("[FACTORY]", "[VIEWMODELFACTORY INIT]");
        if (myViewModelFactory == null) {
            synchronized (ViewModelFactory.class) {
                if (myViewModelFactory == null) {
                    Log.i("[FACTORY]", "[VIEWMODELFACTORY NEW OBJECT]");
                    Application application = MainApplication.getApplication();
                    myViewModelFactory = new ViewModelFactory(
                            new PermissionChecker(application),
                            new FirebaseAuthRepository(FirebaseAuth.getInstance()),
                            new RestaurantsRepository(),
                            new PositionRepository(LocationServices.getFusedLocationProviderClient(application)),
                            new WorkMatesRepository(),
                            new PlaceAutocompleteRepository(),
                            new AlarmRepository(application)
                    );
                } else {
                    Log.i("[FACTORY]", "[VIEWMODELFACTORY OBJECT ALREADY EXIST]");
                }
            }
        }

        return myViewModelFactory;
    }

    //constructor of ViewModelFactory ...
    private ViewModelFactory(
            @NonNull PermissionChecker permissionChecker,
            FirebaseAuthRepository firebaseAuthRepository,
            @NonNull RestaurantsRepository restaurantsRepository,
            PositionRepository positionRepository,
            WorkMatesRepository workMatesRepository,
            PlaceAutocompleteRepository placeAutocompleteRepository,
            AlarmRepository alarmRepository
    ) {
        this.permissionChecker = permissionChecker;
        this.firebaseAuthRepository = firebaseAuthRepository;
        this.positionRepository = positionRepository;
        this.restaurantsRepository = restaurantsRepository;
        this.workMatesRepository = workMatesRepository;
        this.placeAutocompleteRepository = placeAutocompleteRepository;
        this.alarmRepository = alarmRepository;

    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        if (modelClass.isAssignableFrom(ViewModelMain.class)) {
            return (T) new ViewModelMain(
                    firebaseAuthRepository,
                    workMatesRepository,
                    placeAutocompleteRepository,
                    positionRepository,
                    alarmRepository);

        } else if (modelClass.isAssignableFrom(ViewModelMap.class)) {
            return (T) new ViewModelMap(
                    permissionChecker,
                    positionRepository,
                    restaurantsRepository,
                    workMatesRepository);

        } else if (modelClass.isAssignableFrom(ViewModelDetail.class)) {
            return (T) new ViewModelDetail(
                    firebaseAuthRepository,
                    restaurantsRepository,
                    workMatesRepository);

        } else if (modelClass.isAssignableFrom(ViewModelRestaurant.class)) {
            return (T) new ViewModelRestaurant(
                    positionRepository,
                    restaurantsRepository,
                    workMatesRepository);

        } else if (modelClass.isAssignableFrom(ViewModelWorkMates.class)) {
            return (T) new ViewModelWorkMates(
                    restaurantsRepository,
                    workMatesRepository);
        }

        throw new IllegalArgumentException("[V M F] Unknow ViewModel class");
    }
}
