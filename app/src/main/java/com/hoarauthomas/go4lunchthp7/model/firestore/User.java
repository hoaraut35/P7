package com.hoarauthomas.go4lunchthp7.model.firestore;

import androidx.annotation.Nullable;

import java.util.List;


//for Firestore
public class User {

    private String uid;
    private String username;
    private String urlPicture;
    private String favoriteRestaurant;
    private List<String> restaurant_liked;


    public User() {
    }

    public User(String uid, String username, String urlPicture, @Nullable String favoriteRestaurant, List<String> restaurant_liked) {
        this.uid = uid;
        this.username = username;
        this.urlPicture = urlPicture;
        this.favoriteRestaurant = favoriteRestaurant;
        this.restaurant_liked = restaurant_liked;

    }

    // --- GETTERS ---
    public String getUid() {
        return uid;
    }

    public String getUsername() {
        return username;
    }

    public List<String> getRestaurant_liked() {
        return restaurant_liked;
    }


    public String getUrlPicture() {
        return urlPicture;
    }

    public String getFavoriteRestaurant() {
        return favoriteRestaurant;
    }

    public List<String> getLikedRestaurant() {
        return restaurant_liked;
    }


    // --- SETTERS ---
    public void setUsername(String username) {
        this.username = username;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setUrlPicture(String urlPicture) {
        this.urlPicture = urlPicture;
    }

    public void setFavoriteRestaurant(String favoriteRestaurant) {
        this.favoriteRestaurant = favoriteRestaurant;
    }

    public void setRestaurant_liked(List<String> restaurant_liked) {
        this.restaurant_liked = restaurant_liked;
    }


}

