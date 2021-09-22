package com.hoarauthomas.go4lunchthp7.ui.workmates;

import com.hoarauthomas.go4lunchthp7.model.SpecialWorkMates;
import com.hoarauthomas.go4lunchthp7.model.firestore.User;
import com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo;

import java.util.ArrayList;
import java.util.List;

public class ViewStateWorkMates {

    List<SpecialWorkMates> mySpecialWorkMAtes = new ArrayList<>();
    List<User> myWorkMatesList;
    List<com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo> myRestaurantList;
    RestaurantPojo myRestauResult;

    public List<User> getMyWorkMatesList() {
        return myWorkMatesList;
    }

    public void setMyWorkMatesList(List<User> myWorkMatesList) {
        this.myWorkMatesList = myWorkMatesList;
    }

    public List<RestaurantPojo> getMyRestaurantList() {
        return myRestaurantList;
    }

    public void setMyRestaurantList(List<RestaurantPojo> myRestaurantList) {
        this.myRestaurantList = myRestaurantList;
    }

    public RestaurantPojo getMyRestauResult() {
        return myRestauResult;
    }

    public void setMyRestauResult(RestaurantPojo myRestauResult) {
        this.myRestauResult = myRestauResult;
    }

    public ViewStateWorkMates(List<SpecialWorkMates> mySpecial) {
        this.mySpecialWorkMAtes = mySpecial;
    }

    public List<SpecialWorkMates> getMySpecialWorkMAtes() {
        return mySpecialWorkMAtes;
    }

    public void setMySpecialWorkMAtes(List<SpecialWorkMates> mySpecialWorkMAtes) {
        this.mySpecialWorkMAtes = mySpecialWorkMAtes;
    }
}
