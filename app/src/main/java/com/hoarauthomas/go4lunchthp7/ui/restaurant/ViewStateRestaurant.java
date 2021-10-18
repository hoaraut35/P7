package com.hoarauthomas.go4lunchthp7.ui.restaurant;

import com.hoarauthomas.go4lunchthp7.model.NearbySearch.RestaurantPojo;
import com.hoarauthomas.go4lunchthp7.model.PlaceDetails.ResultPlaceDetail;

import java.util.ArrayList;
import java.util.List;

public class ViewStateRestaurant {

    private final List<RestaurantPojo> myRestaurantList;

    private List<ResultPlaceDetail> myAutocompleteList = new ArrayList<>();



    public ViewStateRestaurant(List<RestaurantPojo> myRestaurantList,List<ResultPlaceDetail> myAutocompleteList) {
        this.myRestaurantList = myRestaurantList;
        this.myAutocompleteList = myAutocompleteList;
    }


    public List<RestaurantPojo> getMyRestaurantList() {
        return myRestaurantList;
    }

    public List<ResultPlaceDetail> getMyAutocompleteList() { return myAutocompleteList;}

}
