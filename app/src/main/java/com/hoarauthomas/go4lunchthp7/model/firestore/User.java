package com.hoarauthomas.go4lunchthp7.model.firestore;

import android.media.Image;

import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;


//for Firestore
public class User {

    private String uid;
    private String username;
  //  private Boolean isMentor;
    @Nullable
    private String urlPicture;
    private String favoriteRestaurant;


    private LatLng myPos;



    public User() { }

    public User(String uid, String username, String restaurant) {
        this.uid = uid;
        this.username = username;
        this.urlPicture = urlPicture;
        this.favoriteRestaurant = restaurant;
   //     this.isMentor = false;
    }


    public LatLng getLatLongUser(){return myPos;}




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

    public void setLatLongUser(LatLng latLongUser){this.myPos = latLongUser;}

    // public void setIsMentor(Boolean mentor) { isMentor = mentor; }
}

