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
import com.hoarauthomas.go4lunchthp7.model.pojo.Result;
import com.hoarauthomas.go4lunchthp7.repository.LocationRepository;
import com.hoarauthomas.go4lunchthp7.repository.RestaurantsRepository;

import java.util.List;

//This class is for the business logic
//We expose only Livedata but we must use Mutable livedata to modify livedata value
//Do not use reference to a view or lifecycle here !!!!

//https://medium.com/@kashifo/4-steps-to-mvvm-in-android-java-b05fb4148523

public class ViewModelGo4LunchNew extends ViewModel {

    private final RestaurantsRepository myPlaceSource;
    private final LocationRepository myLocationSource;

    private final LiveData<List<Result>> placesResponseLiveData;
    private final LiveData<Location> responseLocation;

    public ViewModelGo4LunchNew(RestaurantsRepository placeRepository, LocationRepository locationRepository) {
        this.myPlaceSource = new RestaurantsRepository();
        this.myLocationSource = new LocationRepository(null);//TODO: context ?
        this.placesResponseLiveData = myPlaceSource.getAllPlaces();
        this.responseLocation = myLocationSource.getMyLocation();
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
