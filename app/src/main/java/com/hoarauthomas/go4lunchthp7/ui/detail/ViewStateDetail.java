package com.hoarauthomas.go4lunchthp7.ui.detail;

import com.hoarauthomas.go4lunchthp7.RestaurantDetailPojo;
import com.hoarauthomas.go4lunchthp7.model.firestore.User;
import com.hoarauthomas.go4lunchthp7.model.placedetails2.ResultDetailRestaurant;
import com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class ViewStateDetail {

    com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo myRestaurantObject;
    ResultDetailRestaurant myRestaurantDetailObject;

    String myTitle;

    public RestaurantPojo getMyRestaurantObject() {
        return myRestaurantObject;
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



    String Adresse;
    String telephone;
    String website;
    Boolean like;
    Boolean Favoris;
    List<User> myWorkMatesTag = new ArrayList<>();

    public ViewStateDetail(RestaurantPojo myRestaurant, ResultDetailRestaurant myDetailRestaurant, String myTitle, String adresse, String telephone, String website, Boolean like, Boolean favoris, List<User> myWorkMatesTag) {
        this.myTitle = myTitle;
        Adresse = adresse;
        this.telephone = telephone;
        this.website = website;
        this.like = like;
        Favoris = favoris;
        this.myWorkMatesTag = myWorkMatesTag;

        this.myRestaurantObject = myRestaurant;
        this.myRestaurantDetailObject = myDetailRestaurant;

    }

    public List<User> getMyWorkMatesTag() {
        return myWorkMatesTag;
    }

    public void setMyWorkMatesTag(List<User> myWorkMatesTag) {
        this.myWorkMatesTag = myWorkMatesTag;
    }

    public String getMyTitle() {
        return myTitle;
    }

    public void setMyTitle(String myTitle) {
        this.myTitle = myTitle;
    }

    public String getAdresse() {
        return Adresse;
    }

    public void setAdresse(String adresse) {
        Adresse = adresse;
    }

    public String getTelephone() {
        return telephone;
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






}
