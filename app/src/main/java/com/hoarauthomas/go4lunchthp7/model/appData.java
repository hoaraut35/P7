package com.hoarauthomas.go4lunchthp7.model;

import com.hoarauthomas.go4lunchthp7.model.firestore.User;
import com.hoarauthomas.go4lunchthp7.pojo.Result;

import java.util.List;

public class appData {


    //For local user
    String nameUser;
    String userRestaurantId;

    //For workmates
    List<User> workmatesList;

    //For restaurant
    List<com.hoarauthomas.go4lunchthp7.pojo.Result> listRestaurantFromApi;










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

    public List<Result> getListRestaurantFromApi() {
        return listRestaurantFromApi;
    }

    public void setListRestaurantFromApi(List<Result> listRestaurantFromApi) {
        this.listRestaurantFromApi = listRestaurantFromApi;
    }







}
