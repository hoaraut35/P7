package com.hoarauthomas.go4lunchthp7.viewmodel;

import android.location.Location;

import com.google.firebase.auth.FirebaseUser;

public class MainViewState {

    private Location myLocation;
    private FirebaseUser myActualUser;

    public MainViewState(Location myLocation){
        this.myLocation = myLocation;
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
