package com.hoarauthomas.go4lunchthp7.model;

import com.hoarauthomas.go4lunchthp7.model.firestore.User;

import java.util.List;

public class AppMap {


    //For local user
    String nameUser;
    String userRestaurantId;

    //For workmates
    List<User> workmatesList;

    //For restaurant
    List<com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo> listRestaurantFromApi;










    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public String getUserRestaurantId() {
        return userRestaurantId;
    }

    public void setUserRestaurantId(String userRestaurantId) {
        this.userRestaurantId = userRestaurantId;
    }

    public List<com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo> getListRestaurantFromApi() {
        return listRestaurantFromApi;
    }

    public void setListRestaurantFromApi(List<com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo> listRestaurantFromApi) {
        this.listRestaurantFromApi = listRestaurantFromApi;
    }







}
