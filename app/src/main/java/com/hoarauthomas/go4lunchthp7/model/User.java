package com.hoarauthomas.go4lunchthp7.model;

import android.media.Image;

import androidx.annotation.Nullable;


//for Firestore
public class User {

    private String uid;
    private String username;
  //  private Boolean isMentor;
    @Nullable
    private String urlPicture;
    private String favoriteRestaurant;



    public User() { }

    public User(String uid, String username, String restaurant) {
        this.uid = uid;
        this.username = username;
        this.urlPicture = urlPicture;
        this.favoriteRestaurant = restaurant;
   //     this.isMentor = false;
    }

    // --- GETTERS ---
    public String getUid() { return uid; }
    public String getUsername() { return username; }
    public String getUrlPicture() { return urlPicture; }
    public String getFavoriteRestaurant() {     return favoriteRestaurant;    }

    //   public Boolean getIsMentor() { return isMentor; }


    // --- SETTERS ---
    public void setUsername(String username) { this.username = username; }
    public void setUid(String uid) { this.uid = uid; }
    public void setUrlPicture(String urlPicture) { this.urlPicture = urlPicture; }
    public void setFavoriteRestaurant(String favoriteRestaurant) {        this.favoriteRestaurant = favoriteRestaurant;    }

    // public void setIsMentor(Boolean mentor) { isMentor = mentor; }
}

