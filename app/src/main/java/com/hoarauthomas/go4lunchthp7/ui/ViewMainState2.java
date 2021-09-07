package com.hoarauthomas.go4lunchthp7.ui;

public class ViewMainState2 {

    public Boolean LoginState;
    public String myRestaurant;

    public String getMyRestaurant() {
        return myRestaurant;
    }

    public void setMyRestaurant(String myRestaurant) {
        this.myRestaurant = myRestaurant;
    }

    public ViewMainState2(Boolean loginState, String myRestaurant) {
        LoginState = loginState;
        this.myRestaurant = myRestaurant;
    }

    public Boolean getLoginState() {
        return LoginState;
    }

    public void setLoginState(Boolean loginState) {
        LoginState = loginState;
    }
}
