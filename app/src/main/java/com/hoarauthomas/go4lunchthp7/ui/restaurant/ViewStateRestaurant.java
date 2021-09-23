package com.hoarauthomas.go4lunchthp7.ui.restaurant;

import com.hoarauthomas.go4lunchthp7.model.NearbySearch.RestaurantPojo;
import com.hoarauthomas.go4lunchthp7.model.firestore.User;


import java.util.List;

public class ViewStateRestaurant {


    private List<RestaurantPojo> myRestaurantList;
    private List<User> myWorkMatesList;

    public ViewStateRestaurant(List<RestaurantPojo> myRestaurantList) {
        this.myRestaurantList = myRestaurantList;
    }

    public List<RestaurantPojo> getMyRestaurantList() {
        return myRestaurantList;
    }

    public void setMyRestaurantList(List<RestaurantPojo> myRestaurantList) {
        this.myRestaurantList = myRestaurantList;
    }
}
