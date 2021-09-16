package com.hoarauthomas.go4lunchthp7.ui;

import com.google.firebase.auth.FirebaseUser;

public class ViewMainState {

    public Boolean LoginState;
    public String myRestaurant;
    public FirebaseUser myUser;

    public String getMyRestaurant() {
        return myRestaurant;
    }

    public void setMyRestaurant(String myRestaurant) {
        this.myRestaurant = myRestaurant;
    }

    public ViewMainState(Boolean loginState, String myRestaurant, FirebaseUser fbUser) {
        this.LoginState = loginState;
        this.myRestaurant = myRestaurant;
        this.myUser = fbUser;
    }

    public Boolean getLoginState() {
        return LoginState;
    }

    public FirebaseUser getMyUser() {
        return myUser;
    }

    public void setMyUser(FirebaseUser myUser) {
        this.myUser = myUser;
    }

    public void setLoginState(Boolean loginState) {
        LoginState = loginState;
    }
}
