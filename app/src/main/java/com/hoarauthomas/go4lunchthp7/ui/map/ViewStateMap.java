package com.hoarauthomas.go4lunchthp7.ui.map;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.hoarauthomas.go4lunchthp7.model.NearbySearch.RestaurantPojo;


import java.util.List;

public class ViewStateMap {

    public Location myPosition;
    private LatLng myLatLng;

    public List<com.hoarauthomas.go4lunchthp7.model.NearbySearch.RestaurantPojo> myRestaurantsList;

    /**
     * Constructor for the ViewStateMap object
     * @param position return the position of user
     * @param restaurants return the list of restaurant
     */
    public ViewStateMap(Location position, List<com.hoarauthomas.go4lunchthp7.model.NearbySearch.RestaurantPojo> restaurants) {
        myPosition = position;
        myLatLng = new LatLng(position.getLatitude(), position.getLongitude());
        myRestaurantsList = restaurants;
    }

    /**
     * getter for list of restaurant
     * @return list of restaurant
     */
    public List<RestaurantPojo> getMyRestaurantsList() {
        return myRestaurantsList;
    }

    /**
     * setter of restaurant
     * @param myRestaurantsList set the restaurant list
     */
    public void setMyRestaurantsList(List<RestaurantPojo> myRestaurantsList) {
        this.myRestaurantsList = myRestaurantsList;
    }

    /**
     * getter for location
     * @return get the location
     */
    public Location getMyPosition() {
        return myPosition;
    }

    /**
     * setter for position
     * @param myPosition set the position
     */
    public void setMyPosition(Location myPosition) {
        this.myPosition = myPosition;
    }

    /**
     * getter LatLng
     * @return get LatLng
     */
    public LatLng getMyLatLng() {
        return myLatLng;
    }

    /**
     * setter LatLng
     * @param myLatLng set LatLng
     */
    public void setMyLatLng(LatLng myLatLng) {
        this.myLatLng = myLatLng;
    }

}