package com.hoarauthomas.go4lunchthp7.viewmodel;

import android.location.Location;
import android.util.Log;

import androidx.arch.core.util.Function;
import androidx.collection.ArraySet;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseUser;

import com.hoarauthomas.go4lunchthp7.model.firestore.User;
import com.hoarauthomas.go4lunchthp7.permissions.PermissionChecker;
import com.hoarauthomas.go4lunchthp7.pojo.Result;
import com.hoarauthomas.go4lunchthp7.repository.AuthRepository;
import com.hoarauthomas.go4lunchthp7.repository.PositionRepository;
import com.hoarauthomas.go4lunchthp7.repository.RestaurantsRepository;
import com.hoarauthomas.go4lunchthp7.repository.WorkMatesRepository;

import java.util.ArrayList;
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
    private final WorkMatesRepository myWorkMatesSource;
    private final PermissionChecker myPermissionChecker;

    //Add livedata and mutableLiveData here...
    private MutableLiveData<FirebaseUser> myUserVM;
    private MutableLiveData<Boolean> myUserStateVM;
    private LiveData<Location> myPositionVM;

    //list of workmates
    private List<User> myWorkMatesList = new ArrayList<>();
    private List<Result> myRestaurantList = new ArrayList<>();


    //mediatorLiveData
    private final MediatorLiveData<Result> myListOfRestaurantWithColour = new MediatorLiveData<>();

    
    private LiveData<List<Result>> placesResponseLiveData;

    private String myActualRestaurant;
    private FirebaseUser myFUser;


    private Double Long, Lat;


    //constructor to get one instance of each object, called by ViewModelFactory
    public ViewModelGo4Lunch(
            AuthRepository authRepository,
            RestaurantsRepository placeRepository,
            PositionRepository positionRepository,
            WorkMatesRepository workMatesRepository,
            PermissionChecker permissionChecker) {

        //for authentification...
        this.myAuthSource = authRepository;
        //user
        this.myUserVM = myAuthSource.getUserFromRepo();
        //connected or not connected...
        this.myUserStateVM = myAuthSource.getUserStateFromRepo();

        //this for permission
        this.myPermissionChecker = permissionChecker;

        //for position...
        this.myLocationSource = positionRepository;
        this.myPositionVM = Transformations.map(myLocationSource.getLocationLiveData(), new Function<Location, Location>() {
            @Override
            public Location apply(Location input) {
                if (input == null) {
                    //TODO: return a position by defaut
                    return null;
                } else {
                    return input;
                }
            }
        });

        //for workmates...
        this.myWorkMatesSource = workMatesRepository;

        //restaurant details ...
        this.myRestaurantsSource = placeRepository;

        this.placesResponseLiveData = myRestaurantsSource.getAllRestaurants(Long, Lat);



    }



    //publish to activity or fragment
    public LiveData<Location> getMyPosition() {
        Log.i("[RESTAURANT]", "Appel fonction ds le ViewModel... getMyPosition");
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

    //update user position
    public void updateLngLat(Double Long, Double Lat) {
        Log.i("[RESTAURANT]", "Update position in restaurant request ..." + Long + " " + Lat);
        this.Long = Long;
        this.Lat = Lat;
        myRestaurantsSource.UpdateLngLat(Long, Lat);
        this.placesResponseLiveData = myRestaurantsSource.getAllRestaurants(Long, Lat);
    }

    public LatLng getMyLastPosition() {
        return new LatLng(this.Lat, this.Long);
    }


    //----------------------------------------------------------------------------------------------


    //publish method to activity for
    public MutableLiveData<FirebaseUser> getMyCurrentUser() {
        return myUserVM;
    }

    //publish method to activity... to log out work fine
    public void logOut() {
        myAuthSource.logOutFromRepo();
    }

    //publish method to activity... (logged or not) work fine
    public MutableLiveData<Boolean> getMyUserState() {
        return myUserStateVM;
    }


    //----------------------------------------------------------------------------------------------


    //these methods are published to activity or fragments ...






    //this method must be modified to include color of ùmarker
    public LiveData<List<Result>> getRestaurants() {
        Log.i("[RESTAURANT]", "getRestaurant in ViewModelm " + this.Long + this.Lat);
        return placesResponseLiveData;
    }

    public void setActualRestaurant(String tag) {
        this.myActualRestaurant = tag;
        Log.i("[MAP]", "Restaurant actuel SET: " + tag);

    }

    public String getActualRestaurant() {
        Log.i("[MAP]", "Restaurant actuel gET: " + this.myActualRestaurant);
        return this.myActualRestaurant;

    }


    //----------------------------------------------------------------------------------------------
    //FIRESTORE


    //publish to UI...
    public LiveData<List<User>> getAllWorkMates() {
        Log.i("[WORK]", "in VM get all work mates...");

        return myWorkMatesSource.getAllWorkMates();
    }

    public LiveData<List<User>> getAllWorkMatesByRestaurant(){
        return myWorkMatesSource.getAllWorkMatesByRestaurant("id");
    }


    //Create user to Firestore
    public void createUser() {
        this.myWorkMatesSource.createUser();
    }


    public void addNewRestaurant(String name) {
        this.myWorkMatesSource.addRestaurant(name);
    }


    public Result getRestaurantDetail(String restaurant_id) {





        return null;

    }

    //Event from UI when user click on like
    public void onLikeClicked() {
    }

    //Update list of workmates in vm
    public void updateWorkMatesList(List<User> users) {

        this.myWorkMatesList.clear();
        this.myWorkMatesList.addAll(users);
    }

    public User getWorkMAteById(String workmateId){

        int i =0;

        for (i = 0 ; i < this.myWorkMatesList.size(); i++)
        {
            if (this.myWorkMatesList.get(i).getUid().equals(workmateId)){
                break;

            }
        }

        return this.myWorkMatesList.get(i);

    }

    //update list of restaurant in vm
    public void updateRestaurantList(List<Result> restaurants) {
        this.myRestaurantList.clear();
        this.myRestaurantList.addAll(restaurants);
    }

    public List<Result> mergeRestauWithFavRestau()
    {
        for (int i=0; i< this.myRestaurantList.size(); i++){
            for (int y=0; y <myWorkMatesList.size();y++){
                if (myRestaurantList.get(i).getPlaceId().equals(myWorkMatesList.get(y).getFavoriteRestaurant())){
                    //update marker color here .... and retuirn the list to fragment map ?
                    myRestaurantList.get(i).setIcon("green");

                }else
                {
                    myRestaurantList.get(i).setIcon("red");
                }
            }
        }
        return myRestaurantList;
    }



}


