package com.hoarauthomas.go4lunchthp7.repository;

import androidx.annotation.Nullable;

import java.util.List;

public class FirestoreUser {

    String uid;
    String username;
    String urlPicture;
    String favoriteRestaurant;
    List<String> restaurant_liked;

    public FirestoreUser() {}

    public FirestoreUser(@Nullable String favoriteRestaurant,  List<String> restaurant_liked, String uid,  String urlPicture,String username ) {
        this.uid = uid;
        this.username = username;
        this.urlPicture = urlPicture;
        this.favoriteRestaurant = favoriteRestaurant;
        this.restaurant_liked = restaurant_liked;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUrlPicture() {
        return urlPicture;
    }

    public void setUrlPicture(String urlPicture) {
        this.urlPicture = urlPicture;
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
}
