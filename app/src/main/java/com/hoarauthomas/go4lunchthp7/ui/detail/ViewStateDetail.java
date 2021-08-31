package com.hoarauthomas.go4lunchthp7.ui.detail;

import com.hoarauthomas.go4lunchthp7.model.MyUser;
import com.hoarauthomas.go4lunchthp7.model.SpecialWorkMates;
import com.hoarauthomas.go4lunchthp7.model.placedetails2.ResultDetailRestaurant;
import com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo;

import java.util.ArrayList;
import java.util.List;

public class ViewStateDetail {


    String Adresse;
    String telephone;
    String website;
    Boolean like;
    Boolean Favoris;
    MyUser myUser;
    List<SpecialWorkMates> myWorkMatesTag;
    //= new ArrayList<>();


    com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo myRestaurantObject;
    ResultDetailRestaurant myRestaurantDetailObject;


    public RestaurantPojo getMyRestaurantObject() {
        return myRestaurantObject;
    }


    public ViewStateDetail(RestaurantPojo myRestaurant, ResultDetailRestaurant myDetailRestaurant, List<SpecialWorkMates> myWorkMatesTag, Boolean fav, Boolean like,MyUser myUser) {
        this.myRestaurantObject = myRestaurant;
        this.myRestaurantDetailObject = myDetailRestaurant;
        this.myWorkMatesTag = myWorkMatesTag;
        this.like = like;
        this.Favoris = fav;
        this.myUser = myUser;
    }




    public List<SpecialWorkMates> getMyWorkMatesTag() {
        return myWorkMatesTag;
    }

    public void setMyWorkMatesTag(List<SpecialWorkMates> myWorkMatesTag) {
        this.myWorkMatesTag = myWorkMatesTag;
    }


    public String getTelephone() {
        return telephone;
    }

    public MyUser getMyUser() {
        return myUser;
    }

    public void setMyUser(MyUser myUser) {
        this.myUser = myUser;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Boolean getLike() {
        return like;
    }

    public void setLike(Boolean like) {
        this.like = like;
    }

    public Boolean getFavoris() {
        return Favoris;
    }

    public void setFavoris(Boolean favoris) {
        Favoris = favoris;
    }



    public void setMyRestaurantObject(RestaurantPojo myRestaurantObject) {
        this.myRestaurantObject = myRestaurantObject;
    }

    public ResultDetailRestaurant getMyRestaurantDetailObject() {
        return myRestaurantDetailObject;
    }

    public void setMyRestaurantDetailObject(ResultDetailRestaurant myRestaurantDetailObject) {
        this.myRestaurantDetailObject = myRestaurantDetailObject;
    }
}
