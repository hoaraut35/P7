package com.hoarauthomas.go4lunchthp7.model;

import java.util.List;

public class MyUser {

    String myAvatar;
    String myName;
    String myFavoriteRestaurantId;
    List<String> myLikedRestaurant;

    public MyUser(String myAvatar, String myName, String myFavoriteRestaurantId, List<String> myLikedRestaurant) {
        this.myAvatar = myAvatar;
        this.myName = myName;
        this.myFavoriteRestaurantId = myFavoriteRestaurantId;
        this.myLikedRestaurant = myLikedRestaurant;
    }

    public String getMyAvatar() {
        return myAvatar;
    }

    public void setMyAvatar(String myAvatar) {
        this.myAvatar = myAvatar;
    }

    public String getMyName() {
        return myName;
    }

    public void setMyName(String myName) {
        this.myName = myName;
    }

    public String getMyFavoriteRestaurantId() {
        return myFavoriteRestaurantId;
    }

    public void setMyFavoriteRestaurantId(String myFavoriteRestaurantId) {
        this.myFavoriteRestaurantId = myFavoriteRestaurantId;
    }

    public List<String> getMyLikedRestaurant() {
        return myLikedRestaurant;
    }

    public void setMyLikedRestaurant(List<String> myLikedRestaurant) {
        this.myLikedRestaurant = myLikedRestaurant;
    }
}
