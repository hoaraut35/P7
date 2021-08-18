package com.hoarauthomas.go4lunchthp7.viewmodel;

import android.location.Location;
import android.util.Log;

import com.google.firebase.auth.FirebaseUser;
import com.hoarauthomas.go4lunchthp7.model.firestore.User;
import com.hoarauthomas.go4lunchthp7.model.placedetails2.MyDetailRestaurant;
import com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo;

import java.util.List;

import javax.annotation.Nullable;

public class MainViewState {

    private Location myLocation;
    private FirebaseUser myActualUser;
    private List<com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo> myRestaurantsList;
    private List<User> myWorkMatesList;
    private MyDetailRestaurant myDetailRestaurant;

    //**********************************************************************************************

    public List<User> getMyWorkMatesList() {
        return myWorkMatesList;
    }

    public void setMyWorkMatesList(List<User> myWorkMatesList) {
        this.myWorkMatesList = myWorkMatesList;
    }

    public Location getMyLocation() {
        return myLocation;
    }

    public void setMyLocation(Location myLocation) {
        this.myLocation = myLocation;
    }

    public List<RestaurantPojo> getMyRestaurantsList() {
        return myRestaurantsList;
    }

    public void setMyRestaurantsList(List<RestaurantPojo> myRestaurantsList) {
        this.myRestaurantsList = myRestaurantsList;
    }

    public MainViewState(@Nullable Location myLocation, @Nullable List<com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo> myRestaurantPojoList, @Nullable List<User> myWorkMatesList) {

        this.myLocation = myLocation;
        this.myRestaurantsList = myRestaurantPojoList;
        this.myWorkMatesList = myWorkMatesList;


    }

    public MainViewState(MyDetailRestaurant monrestau) {
        this.myDetailRestaurant = monrestau;
    }

    public MainViewState(Location MyLocation) {
        this.myLocation = MyLocation;
    }




    public MyDetailRestaurant getMyDetailRestaurant() {
        return myDetailRestaurant;
    }

    public void setMyDetailRestaurant(MyDetailRestaurant myDetailRestaurant) {
        this.myDetailRestaurant = myDetailRestaurant;
    }

    public Location getLocation() {
        return myLocation;
    }

    public void setLocation(Location location) {
        this.myLocation = location;
    }

    public FirebaseUser getMyActualUser() {
        return myActualUser;
    }

    public void setMyActualUser(FirebaseUser myActualUser) {
        this.myActualUser = myActualUser;
    }
}
