package com.hoarauthomas.go4lunchthp7.ui.restaurant;

import com.hoarauthomas.go4lunchthp7.model.firestore.User;
import com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo;

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
