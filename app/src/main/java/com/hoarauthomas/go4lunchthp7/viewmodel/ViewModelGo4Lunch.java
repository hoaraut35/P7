package com.hoarauthomas.go4lunchthp7.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.facebook.internal.Mutable;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hoarauthomas.go4lunchthp7.MainActivity;
import com.hoarauthomas.go4lunchthp7.api.UserHelper;
import com.hoarauthomas.go4lunchthp7.model.pojo.Result;
import com.hoarauthomas.go4lunchthp7.repositories.LocationRepository;
import com.hoarauthomas.go4lunchthp7.repositories.RestaurantsRepository;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executor;

//This class is for the business logic
//We expose only Livedata but we must use Mutable livedata to modify livedata value
//Do not use reference to a view or lifecycle here !!!!

//https://medium.com/@kashifo/4-steps-to-mvvm-in-android-java-b05fb4148523

public class ViewModelGo4Lunch extends ViewModel {

    //add here repositories here ...
    private final RestaurantsRepository myPlaceSource;
    private LocationRepository myLocationSource;
    private final Executor executor;

    //data
    private final LiveData<List<Result>> placesResponseLiveData;
    private final LiveData<Location> responseLocation;

    //constructor for viewmodel
    public ViewModelGo4Lunch(RestaurantsRepository placeRepository, LocationRepository locationRepository, Executor executor) {
        this.myPlaceSource = new RestaurantsRepository();
        this.myLocationSource = new LocationRepository(null);
        this.placesResponseLiveData = myPlaceSource.getAllPlaces();
        this.responseLocation = myLocationSource.getMyLocation();
        this.executor = executor;
    }

    //this method is use by MainActivity ...
    public LiveData<List<Result>> getRestaurants() {
        return placesResponseLiveData;
    }

    //this method is use by MainActivity ...
    public LiveData<Location> getMyPosition() { return responseLocation;}



    //**********************************************************************************************
    // Security section
    //**********************************************************************************************

    protected FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    protected Boolean isCurrentUserLogged() {
        return (this.getCurrentUser() != null);
    }

    private MutableLiveData<Boolean> logged = new MutableLiveData<>();

    public LiveData<Boolean> isLogged() {
        checkSecurity();
        return logged;
    }

    public Boolean checkSecurity() {

        if (!isCurrentUserLogged()) {
            request_login();
            return false;
        } else {
            request_user_info();
            return true;
        }

    }

    public void request_login() {
        logged.setValue(false);
    }

    public void request_user_info() {
        logged.setValue(true);
    }

    //**********************************************************************************************
    // End of Security section
    //**********************************************************************************************


    //Add user to Firestore
    public void createuser() {
        if (this.getCurrentUser() != null) {

            String uid = this.getCurrentUser().getUid();
            String username = this.getCurrentUser().getDisplayName();
            String urlPicture = (this.getCurrentUser().getPhotoUrl() != null) ? this.getCurrentUser().getPhotoUrl().toString() : null;
            Log.i("[THOMAS]", "Add user to Firestore ... " + uid + " " + username + " " + urlPicture);

            UserHelper.createUser(uid, username, "OC Pizza").addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.i("[THOMAS]", "Erreur CREATE Firestore " + e.getMessage());
                }
            });
        }


    }


}
