package com.hoarauthomas.go4lunchthp7.factory;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.hoarauthomas.go4lunchthp7.MainApplication;
import com.hoarauthomas.go4lunchthp7.permissions.PermissionChecker;
import com.hoarauthomas.go4lunchthp7.repository.AlarmRepository;
import com.hoarauthomas.go4lunchthp7.repository.FirebaseAuthRepository;
import com.hoarauthomas.go4lunchthp7.repository.FirestoreRepository;
import com.hoarauthomas.go4lunchthp7.repository.PlaceAutocompleteRepository;
import com.hoarauthomas.go4lunchthp7.repository.PositionRepository;
import com.hoarauthomas.go4lunchthp7.repository.RestaurantsRepository;
import com.hoarauthomas.go4lunchthp7.repository.SharedRepository;
import com.hoarauthomas.go4lunchthp7.ui.ViewModelMain;
import com.hoarauthomas.go4lunchthp7.ui.detail.ViewModelDetail;
import com.hoarauthomas.go4lunchthp7.ui.map.ViewModelMap;
import com.hoarauthomas.go4lunchthp7.ui.restaurant.ViewModelRestaurant;
import com.hoarauthomas.go4lunchthp7.ui.workmates.ViewModelWorkMates;

/**
 * https://medium.com/koderlabs/viewmodel-with-viewmodelprovider-factory-the-creator-of-viewmodel-8fabfec1aa4f
 * ViewModelProvider.Factory is responsible to create the instance of ViewModels, one for all application
 */

public class ViewModelFactory implements ViewModelProvider.Factory {

    /**
     * ViewModelFactory object
     */
    private static ViewModelFactory myViewModelFactory;

    /**
     * Add all repositories here
     */

    private final FirebaseAuthRepository firebaseAuthRepository;
    private final PositionRepository positionRepository;
    private final RestaurantsRepository restaurantsRepository;
    private final FirestoreRepository firestoreRepository;
    private final PermissionChecker permissionChecker;
    private final PlaceAutocompleteRepository placeAutocompleteRepository;
    private final AlarmRepository alarmRepository;
    private final SharedRepository sharedRepository;

    public static ViewModelFactory getInstance() {
        if (myViewModelFactory == null) {
            synchronized (ViewModelFactory.class) {
                if (myViewModelFactory == null) {
                    Application application = MainApplication.getApplication();
                    myViewModelFactory = new ViewModelFactory(
                            new PermissionChecker(application),
                            new FirebaseAuthRepository(FirebaseAuth.getInstance()),
                            new RestaurantsRepository(),
                            new PositionRepository(LocationServices.getFusedLocationProviderClient(application)),
                            new FirestoreRepository(),
                            new PlaceAutocompleteRepository(),
                            new AlarmRepository(application),
                            new SharedRepository()
                    );
                }
            }
        }
        return myViewModelFactory;
    }

    private ViewModelFactory(
            PermissionChecker permissionChecker,
            FirebaseAuthRepository firebaseAuthRepository,
            RestaurantsRepository restaurantsRepository,
            PositionRepository positionRepository,
            FirestoreRepository firestoreRepository,
            PlaceAutocompleteRepository placeAutocompleteRepository,
            AlarmRepository alarmRepository,
            SharedRepository sharedRepository

    ) {
        this.permissionChecker = permissionChecker;
        this.firebaseAuthRepository = firebaseAuthRepository;
        this.positionRepository = positionRepository;
        this.restaurantsRepository = restaurantsRepository;
        this.firestoreRepository = firestoreRepository;
        this.placeAutocompleteRepository = placeAutocompleteRepository;
        this.alarmRepository = alarmRepository;
        this.sharedRepository = sharedRepository;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        if (modelClass.isAssignableFrom(ViewModelMain.class)) {
            return (T) new ViewModelMain(
                    permissionChecker,
                    firebaseAuthRepository,
                    firestoreRepository,
                    placeAutocompleteRepository,
                    positionRepository,
                    alarmRepository,
                    sharedRepository
                    );

        } else if (modelClass.isAssignableFrom(ViewModelMap.class)) {
            return (T) new ViewModelMap(
                    positionRepository,
                    restaurantsRepository,
                    firestoreRepository,
                    sharedRepository,
                    placeAutocompleteRepository
            );

        } else if (modelClass.isAssignableFrom(ViewModelDetail.class)) {
            return (T) new ViewModelDetail(
                    firebaseAuthRepository,
                    restaurantsRepository,
                    firestoreRepository

            );

        } else if (modelClass.isAssignableFrom(ViewModelRestaurant.class)) {
            return (T) new ViewModelRestaurant(
                    positionRepository,
                    restaurantsRepository,
                    firestoreRepository,
                    sharedRepository,
                    placeAutocompleteRepository);

        } else if (modelClass.isAssignableFrom(ViewModelWorkMates.class)) {
            return (T) new ViewModelWorkMates(
                    restaurantsRepository,
                    firestoreRepository);
        }

        throw new IllegalArgumentException("[V M F] Unknow ViewModel class");
    }
}