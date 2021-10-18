package com.hoarauthomas.go4lunchthp7.ui.map;

import com.google.android.gms.maps.model.LatLng;
import com.hoarauthomas.go4lunchthp7.model.NearbySearch.RestaurantPojo;
import com.hoarauthomas.go4lunchthp7.model.PlaceDetails.ResultPlaceDetail;

import java.util.List;

public class ViewStateMap {

    public LatLng myPosition;
    public List<com.hoarauthomas.go4lunchthp7.model.NearbySearch.RestaurantPojo> myRestaurantsList;
    public List<ResultPlaceDetail> myAutocompleteList;

    public ViewStateMap(LatLng position, List<com.hoarauthomas.go4lunchthp7.model.NearbySearch.RestaurantPojo> restaurants, List<ResultPlaceDetail> myAutocompleteListA) {
        myPosition = position;
        myRestaurantsList = restaurants;
        myAutocompleteList = myAutocompleteListA;
    }

    public LatLng getMyPosition() {
        return myPosition;
    }

    public List<RestaurantPojo> getMyRestaurantsList() {
        return myRestaurantsList;
    }

    public List<ResultPlaceDetail> getMyAutocompleteList() {
        return myAutocompleteList;
    }

}