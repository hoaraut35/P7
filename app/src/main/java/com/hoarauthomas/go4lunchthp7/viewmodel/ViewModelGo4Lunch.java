package com.hoarauthomas.go4lunchthp7.viewmodel;

import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.hoarauthomas.go4lunchthp7.api.UserHelper;

import com.hoarauthomas.go4lunchthp7.pojo.Result;
import com.hoarauthomas.go4lunchthp7.repository.AuthentificationRepository;
import com.hoarauthomas.go4lunchthp7.repository.LocationRepository;
import com.hoarauthomas.go4lunchthp7.repository.RestaurantsRepository;

import java.util.List;

//This class is for the business logic
//We expose only Livedata but we must use Mutable livedata to modify livedata value
//Do not use reference to a view or lifecycle here !!!!

//https://medium.com/@kashifo/4-steps-to-mvvm-in-android-java-b05fb4148523

public class ViewModelGo4Lunch extends ViewModel {

    //Add repository here...
    private final AuthentificationRepository myAuthentificationSource;
    private final RestaurantsRepository myRestaurantsSource;
    private final LocationRepository myLocationSource;

    //Add livedata and mutableLiveData here...
    private final LiveData<List<Result>> placesResponseLiveData;
    private final LiveData<Location> responseLocation;

    //constructor to get one instance of each object, called by ViewModelFactory
    public ViewModelGo4Lunch(AuthentificationRepository authentificationRepository, RestaurantsRepository placeRepository, LocationRepository locationRepository) {
        this.myAuthentificationSource = authentificationRepository;
        this.myRestaurantsSource = placeRepository;
        this.myLocationSource = locationRepository;
        this.placesResponseLiveData = myRestaurantsSource.getAllRestaurants();
        this.responseLocation = myLocationSource.getLocationLiveData();
    }

    //these methods are published to activity or fragments ...
    public LiveData<List<Result>> getRestaurants() {     return placesResponseLiveData; }//add method to get restaurant from repository?
    public LiveData<Location> getMyPosition() { return responseLocation;}//a&dd method to get position from repository location?
    public void refreshPosition(){
        myLocationSource.startLocationRequest();
    }






    private MutableLiveData<Boolean> logged = new MutableLiveData<>();

    public LiveData<Boolean> isLogged() {
      //  checkSecurity();
        return logged;
    }


    public FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public Boolean checkSecurity() {

        if (!myAuthentificationSource.getLoginState()){
            Log.i("[THOMAS]","VM, checkSecurity() non connecté");
         request_login();
         return false;
        }
        else
        {
            Log.i("[THOMAS]","VM, checkSecurity() connecté");
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
