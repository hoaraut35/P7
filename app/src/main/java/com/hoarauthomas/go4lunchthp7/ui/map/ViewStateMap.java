package com.hoarauthomas.go4lunchthp7.ui.map;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class ViewStateMap {

    public Location myPosition;
    private final LatLng myLatLng;

    public List<com.hoarauthomas.go4lunchthp7.model.NearbySearch.RestaurantPojo> myRestaurantsList;

    public ViewStateMap(Location position, List<com.hoarauthomas.go4lunchthp7.model.NearbySearch.RestaurantPojo> restaurants) {
        myPosition = position;
        myLatLng = new LatLng(position.getLatitude(), position.getLongitude());
        myRestaurantsList = restaurants;
    }

    public LatLng getMyLatLng() {
        return myLatLng;
    }

}