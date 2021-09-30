package com.hoarauthomas.go4lunchthp7.ui.restaurant;

import com.hoarauthomas.go4lunchthp7.model.NearbySearch.RestaurantPojo;

import java.util.List;

public class ViewStateRestaurant {

    private final List<RestaurantPojo> myRestaurantList;

    public ViewStateRestaurant(List<RestaurantPojo> myRestaurantList) {
        this.myRestaurantList = myRestaurantList;
    }

    public List<RestaurantPojo> getMyRestaurantList() {
        return myRestaurantList;
    }

}
