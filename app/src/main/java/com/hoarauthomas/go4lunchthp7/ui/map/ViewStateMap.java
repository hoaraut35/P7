package com.hoarauthomas.go4lunchthp7.ui.map;

import android.location.Location;

import com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo;

import java.util.List;

public class ViewStateMap {

    public Location myPosition;
    public List<com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo> myRestaurantsList;

    public ViewStateMap(Location position, List<com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo> restaurants) {
        myPosition = position;
        myRestaurantsList = restaurants;
    }

    public ViewStateMap(Location position) {
        myPosition = position;

    }

    public List<RestaurantPojo> getMyRestaurantsList() {
        return myRestaurantsList;
    }

    public void setMyRestaurantsList(List<RestaurantPojo> myRestaurantsList) {
        this.myRestaurantsList = myRestaurantsList;
    }

    public Location getMyPosition() {
        return myPosition;
    }

    public void setMyPosition(Location myPosition) {
        this.myPosition = myPosition;
    }
}
