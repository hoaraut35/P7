package com.hoarauthomas.go4lunchthp7.viewmodel;

import android.location.Location;
import android.util.Log;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;

import com.hoarauthomas.go4lunchthp7.permissions.PermissionChecker;
import com.hoarauthomas.go4lunchthp7.pojo.Result;
import com.hoarauthomas.go4lunchthp7.repository.AuthRepository;
import com.hoarauthomas.go4lunchthp7.repository.PositionRepository;
import com.hoarauthomas.go4lunchthp7.repository.RestaurantsRepository;

import java.util.List;

//This class is for the business logic
//We expose only Livedata
//Do not use reference to a view or lifecycle here !!!!
//https://medium.com/@kashifo/4-steps-to-mvvm-in-android-java-b05fb4148523

public class ViewModelGo4Lunch extends ViewModel {

    //Add repository here...
    private final AuthRepository myAuthSource;
    private final PositionRepository myLocationSource;
    private final RestaurantsRepository myRestaurantsSource;
    private final PermissionChecker myPermissionChecker;

    //Add livedata and mutableLiveData here...
    private MutableLiveData<FirebaseUser> myUserVM;
    private MutableLiveData<Boolean> myUserStateVM;
    private LiveData<Location> myPositionVM;
    private final LiveData<List<Result>> placesResponseLiveData;

    //constructor to get one instance of each object, called by ViewModelFactory
    public ViewModelGo4Lunch(AuthRepository authRepository, RestaurantsRepository placeRepository, PositionRepository positionRepository, PermissionChecker permissionChecker) {

        Log.i("[THOMAS]", "[VIEWMODELGO4LUNCH INIT]");

        //this is ok ...
        this.myAuthSource = authRepository;
        this.myUserVM = myAuthSource.getUserLiveData();
        this.myUserStateVM = myAuthSource.getMyUserState();

        //this is ok ...
        this.myLocationSource = positionRepository;
        this.myPositionVM = Transformations.map(myLocationSource.getLocationLiveData(), new Function<Location, Location>() {
            @Override
            public Location apply(Location input) {
                if (input == null) {
                    return null;//TODO: ?
                } else {
                    return input;
                }
            }
        });

        //this for permission
        this.myPermissionChecker = permissionChecker;

        //in progress...
        this.myRestaurantsSource = placeRepository;
        this.placesResponseLiveData = myRestaurantsSource.getAllRestaurants();

    }


    //----------------------------------------------------------------------------------------------


    //publish to activity or fragment
    public LiveData<Location> getMyPosition() {
        return myPositionVM;
    }


    //publish this method to activity for updat eposition...
    public void refreshPosition() {

        if (!myPermissionChecker.hasLocationPermission()) {
            myLocationSource.stopLocationRequest();
        } else {
            myLocationSource.startLocationRequest();
        }
    }


    //----------------------------------------------------------------------------------------------


    //publish method to activity for
    public MutableLiveData<FirebaseUser> getMyCurrentUser() {
        return myUserVM;
    }

    //publish method to activity... to log out work fine
    public void logOut() {
        myAuthSource.logOut();
    }

    //publish method to activity... (logged or not) work fine
    public MutableLiveData<Boolean> getMyUserState() {
        return myUserStateVM;
    }

    //----------------------------------------------------------------------------------------------


    //these methods are published to activity or fragments ...
    public LiveData<List<Result>> getRestaurants() {
        return placesResponseLiveData;
    }//add method to get restaurant from repository?


    //----------------------------------------------------------------------------------------------


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