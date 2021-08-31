package com.hoarauthomas.go4lunchthp7;

import android.location.Location;

import com.google.firebase.auth.FirebaseUser;
import com.hoarauthomas.go4lunchthp7.model.firestore.User;
import com.hoarauthomas.go4lunchthp7.model.placedetails2.MyDetailRestaurant;
import com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo;

import java.util.List;

import javax.annotation.Nullable;

public class ViewStateMain {

    private Location myLocation;

    public void setMyActualUser(FirebaseUser myActualUser) {
        this.myActualUser = myActualUser;
    }

    private FirebaseUser myActualUser;
    private List<com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo> myRestaurantsList;
    private List<User> myWorkMatesList;
    private MyDetailRestaurant myDetailRestaurant;
    private String myRestaurantFavorite;

    private Boolean myLogState;

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

    //**********************************************************************************************

    public ViewStateMain(
            FirebaseUser user,
            Boolean bool,

            @Nullable String restaurant) {
        //this.myRestaurantFavorite = myRestaurantFavorite;
        //this.myLocation = myLocation;
        // this.myRestaurantsList = myRestaurantPojoList;
        this.myWorkMatesList = myWorkMatesList;
        //this.getMyActualUser() = user;
        this.myLogState = bool;
        this.myRestaurantFavorite = restaurant;
    }

    public String getMyRestaurantFavorite() {
        return myRestaurantFavorite;
    }

    public void setMyRestaurantFavorite(String myRestaurantFavorite) {
        this.myRestaurantFavorite = myRestaurantFavorite;
    }

    public ViewStateMain(@Nullable FirebaseUser user) {
        this.myActualUser = user;
    }

    public ViewStateMain(@Nullable Boolean myLogState) {
        this.myLogState = myLogState;
    }


    // public ViewStateMain(@Nullable FirebaseUser user, Boolean myLogState, String id){
    public ViewStateMain(@Nullable FirebaseUser user, Boolean myLogState) {
        //this.myRestaurantFavorite = id;
        this.myActualUser = user;
        this.myLogState = myLogState;
    }

    public Boolean getMyLogState() {
        return myLogState;
    }

    //**********************************************************************************************

    public void setMyLogState(Boolean myLogState) {
        this.myLogState = myLogState;
    }

    public ViewStateMain(MyDetailRestaurant monrestau) {
        this.myDetailRestaurant = monrestau;
    }

    public ViewStateMain(Location MyLocation) {
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
}
