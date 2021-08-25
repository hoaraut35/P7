package com.hoarauthomas.go4lunchthp7.ui.map;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo;

import java.util.List;

public class ViewStateMap {

    public Location myPosition;
    private LatLng myLatLng;
    public List<com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo> myRestaurantsList;

    public LatLng getMyLatLng() {
        return myLatLng;
    }

    public void setMyLatLng(LatLng myLatLng) {
        this.myLatLng = myLatLng;
    }

    public ViewStateMap(Location position, List<com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo> restaurants) {
        myPosition = position;

        myLatLng = new LatLng(position.getLatitude(), position.getLongitude());
        myRestaurantsList = restaurants;
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
