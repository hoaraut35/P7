package com.hoarauthomas.go4lunchthp7.viewmodel;

import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.hoarauthomas.go4lunchthp7.api.UserHelper;

import com.hoarauthomas.go4lunchthp7.pojo.Result;
import com.hoarauthomas.go4lunchthp7.repository.AuthentificationRepository;
import com.hoarauthomas.go4lunchthp7.repository.LocationRepository;
import com.hoarauthomas.go4lunchthp7.repository.RestaurantsRepository;

import java.util.List;
import java.util.MissingFormatArgumentException;

//This class is for the business logic
//We expose only Livedata but we must use Mutable livedata to modify livedata value
//Do not use reference to a view or lifecycle here !!!!

//https://medium.com/@kashifo/4-steps-to-mvvm-in-android-java-b05fb4148523

public class ViewModelGo4Lunch extends ViewModel {

    //Add repository here...
    private  AuthentificationRepository myAuthentificationSource;
    private final RestaurantsRepository myRestaurantsSource;
    private final LocationRepository myLocationSource;

    //Add livedata and mutableLiveData here...
    private final LiveData<List<Result>> placesResponseLiveData;
    private final LiveData<Location> responseLocation;

    private MutableLiveData<FirebaseUser> myUserVM;
    private MutableLiveData<Boolean> responseAuthentification;

    //constructor to get one instance of each object, called by ViewModelFactory
    public ViewModelGo4Lunch(AuthentificationRepository authentificationRepository, RestaurantsRepository placeRepository, LocationRepository locationRepository) {
        //public ViewModelGo4Lunch() {
        Log.i("[THOMAS]", "[VIEWMODELGO4LUNCH INIT]");

        //this is ok...
        this.myAuthentificationSource = authentificationRepository;
        this.myUserVM = myAuthentificationSource.getUserLiveData();
        this.responseAuthentification = myAuthentificationSource.getMyUserState();

        //in progress...
        this.myRestaurantsSource = placeRepository;
        this.placesResponseLiveData = myRestaurantsSource.getAllRestaurants();

        //in progress...
        this.myLocationSource = locationRepository;
        this.responseLocation = myLocationSource.getLocationLiveData();
    }

    //these methods are published to activity or fragments ...
    public LiveData<List<Result>> getRestaurants() {
        return placesResponseLiveData;
    }//add method to get restaurant from repository?

    public LiveData<Location> getMyPosition() {
        return responseLocation;
    }//a&dd method to get position from repository location?

    public void refreshPosition() {
        myLocationSource.startLocationRequest();
    }








   /* public LiveData<Boolean> checkSecurity(String data) {

        //return responseAuthentification;
        final MutableLiveData<Boolean> dataresult = new MutableLiveData<>();
        dataresult.setValue(myAuthentificationSource.getCurrentLoginState(data).getValue());
        Log.i("[THOMAS]", "Check security from " + data + " with result : " + myAuthentificationSource.getCurrentLoginState(data).getValue());
        return dataresult;
    }

    */

    /*public void securityUpdate(){
        final MutableLiveData<Boolean> data = new MutableLiveData<>();

        //responseAuthentification.
        //responseAuthentification.setValue(myAuthentificationSource.getCurrentLoginState("").getValue());

    }

     */




    //publish method to activity for
    public MutableLiveData<FirebaseUser> getMyCurrentUser(){
        return myUserVM;
    }

    //publish method to activity... to log out
    public void logOut(){
        myAuthentificationSource.logOut();
    }

    //publish method to activity... (logged or not)
    public MutableLiveData<Boolean> getMyUserState(){
        return responseAuthentification;
    }


/*

    //Add user to Firestore
    public void createuser() {
        if (this.myUserVM != null) {

            String uid = this.myUserVM.getUid();
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

 */


}
