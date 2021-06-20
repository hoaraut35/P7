package com.hoarauthomas.go4lunchthp7.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hoarauthomas.go4lunchthp7.api.UserHelper;
import com.hoarauthomas.go4lunchthp7.model.pojo.Result;
import com.hoarauthomas.go4lunchthp7.repositories.RestaurantsRepository;


import java.util.List;
import java.util.concurrent.Executor;

//Business logic
//share data between multiple fragment (list workmates etc)

//https://medium.com/@kashifo/4-steps-to-mvvm-in-android-java-b05fb4148523

public class ViewModelGo4Lunch extends ViewModel {

    //add here repositories ...
    private final RestaurantsRepository myPlaceSource;
    private final LiveData<List<Result>> placesResponseLiveData;

    //constructor for viewmodel
    public ViewModelGo4Lunch(RestaurantsRepository placeRepository, Executor executor) {
        this.myPlaceSource = new RestaurantsRepository();
        this.placesResponseLiveData = myPlaceSource.getAllPlaces();
        //TODO:don't work ?
        executor = executor;
    }

    //this method is use by MainActivity ...
    public LiveData<List<Result>> getRestaurants() {
        return placesResponseLiveData;
    }

    //**********************************************************************************************

    //protected
    protected FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    protected Boolean isCurrentUserLogged() {
        return (this.getCurrentUser() != null);
    }






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
