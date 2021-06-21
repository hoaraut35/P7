package com.hoarauthomas.go4lunchthp7.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

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

//Business logic
//share data between multiple fragment (list workmates etc)

//https://medium.com/@kashifo/4-steps-to-mvvm-in-android-java-b05fb4148523

public class ViewModelGo4Lunch extends ViewModel {

    @NonNull Resources resources;


    //add here repositories ...
    private final RestaurantsRepository myPlaceSource;
    private LocationRepository myLocationSource;

    private final LiveData<List<Result>> placesResponseLiveData;

    //constructor for viewmodel
    //public ViewModelGo4Lunch(RestaurantsRepository placeRepository, Executor executor) {
    public ViewModelGo4Lunch( RestaurantsRepository placeRepository, LocationRepository locationRepository, Executor executor) {
        this.myPlaceSource = new RestaurantsRepository();
    //    this.myLocationSource = new LocationRepository();
        this.placesResponseLiveData = myPlaceSource.getAllPlaces();
        //TODO:don't work ?
        executor = executor;
    }

    //this method is use by MainActivity ...
    public LiveData<List<Result>> getRestaurants() {
        return placesResponseLiveData;
    }


    //**********************************************************************************************
    // Log
    //**********************************************************************************************

    //protected
    protected FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    protected Boolean isCurrentUserLogged() {
        return (this.getCurrentUser() != null);
    }

    public void security() {
        if (!isCurrentUserLogged()) {
            //if not connected then request login
            //    request_login();
        } else {
            //else request user info to update ui
            //request_user_info();
        }
    }


    //**********************************************************************************************
    public void updateLocation() {

    }
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


    //**********************************************************************************************





    //**********************************************************************************************

    public void startLocation()
    {
      //  this.myLocationSource = new LocationRepository(context);
       // myLocationSource.initMyFused();
        //return "goo";
        //resources.

    }


}
