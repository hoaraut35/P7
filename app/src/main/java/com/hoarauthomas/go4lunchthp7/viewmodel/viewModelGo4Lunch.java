package com.hoarauthomas.go4lunchthp7.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hoarauthomas.go4lunchthp7.data.UserHelper;
import com.hoarauthomas.go4lunchthp7.model.pojo.Result;
import com.hoarauthomas.go4lunchthp7.repositories.RestaurantsRepository;


import java.util.List;
import java.util.concurrent.Executor;

//Business logic
//share data between multiple fragment (list workmates etc)

//https://medium.com/@kashifo/4-steps-to-mvvm-in-android-java-b05fb4148523

public class viewModelGo4Lunch extends ViewModel {


    //add here repositories ...
    private RestaurantsRepository myPlaceSource;

    //protected
    protected FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }


    //constructor for viewmodel
    public viewModelGo4Lunch(RestaurantsRepository placeRepository, Executor executor) {
        this.myPlaceSource = new RestaurantsRepository();
        //this.executor = executor;
    }


    public LiveData<List<Result>> getRestaurants() {
        return myPlaceSource.getPlaces();

    }

    public LiveData<List<Result>> getSecurity() {
        return myPlaceSource.getPlaces();

    }

    public LiveData<List<Result>> getWorkMates() {
        return myPlaceSource.getPlaces();

    }


    //Add user to Firestore
    public void createuser( ) {
        if (this.getCurrentUser() != null) {

            String uid = this.getCurrentUser().getUid();
            String username = this.getCurrentUser().getDisplayName();
            String urlPicture = (this.getCurrentUser().getPhotoUrl() != null) ? this.getCurrentUser().getPhotoUrl().toString() : null;
            Log.i("[THOMAS]", "Add user to Firestore ... " + uid + " " +username + " " + urlPicture);

            UserHelper.createUser(uid, username).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.i("[THOMAS]","erreur create firestore" + e.getMessage());
                }
            });
            }


        }



}
