package com.hoarauthomas.go4lunchthp7.viewmodel;

import android.location.Location;

import com.google.firebase.auth.FirebaseUser;
import com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo;

import java.util.List;

import javax.annotation.Nullable;

public class MainViewState {

    private Location myLocation;
    private FirebaseUser myActualUser;
    private List<com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo> myRestaurantsList;

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

    public MainViewState(@Nullable Location myLocation, @Nullable List<com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo> myRestaurantPojoList){

        this.myLocation = myLocation;
        this.myRestaurantsList = myRestaurantPojoList;
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
