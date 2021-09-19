package com.hoarauthomas.go4lunchthp7.repository;

import java.util.List;

public class FirestoreUser {

    String favoriteRestaurant;
    List<String> restaurant_liked;
    String uid;
    String urlPicture;
    String username;


    public FirestoreUser() {

    }

    public FirestoreUser(String myFavoriteRestaurant, List<String> myLikedRestaurant, String myUID, String myAvatar, String myUserName) {
        this.favoriteRestaurant = myFavoriteRestaurant;
        this.restaurant_liked = myLikedRestaurant;
        this.uid = myUID;
        this.urlPicture = myAvatar;
        this.username = myUserName;
    }

    public String getFavoriteRestaurant() {
        return favoriteRestaurant;
    }

    public void setFavoriteRestaurant(String favoriteRestaurant) {
        this.favoriteRestaurant = favoriteRestaurant;
    }

    public List<String> getRestaurant_liked() {
        return restaurant_liked;
    }

    public void setRestaurant_liked(List<String> restaurant_liked) {
        this.restaurant_liked = restaurant_liked;
    }

    public String getMyUID() {
        return uid;
    }

    public void setMyUID(String myUID) {
        this.uid = myUID;
    }

    public String getUrlPicture() {
        return urlPicture;
    }

    public void setUrlPicture(String urlPicture) {
        this.urlPicture = urlPicture;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
