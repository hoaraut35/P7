package com.hoarauthomas.go4lunchthp7.ui.map;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class ViewStateMap {


    public LatLng myPosition;

    public List<com.hoarauthomas.go4lunchthp7.model.NearbySearch.RestaurantPojo> myRestaurantsList;

    public ViewStateMap(LatLng position, List<com.hoarauthomas.go4lunchthp7.model.NearbySearch.RestaurantPojo> restaurants) {
        myPosition = position;

        myRestaurantsList = restaurants;
    }

    public LatLng getMyPosition() {
        return myPosition;
    }

}