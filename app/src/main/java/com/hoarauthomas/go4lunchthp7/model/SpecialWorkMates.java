package com.hoarauthomas.go4lunchthp7.model;

public class SpecialWorkMates {

    String avatar;
    String nameOfWorkMates;
    String nameOfRestaurant;
    String placeId;

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getNameOfWorkMates() {
        return nameOfWorkMates;
    }

    public void setNameOfWorkMates(String nameOfWorkMates) {
        this.nameOfWorkMates = nameOfWorkMates;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }


    public String getNameOfRestaurant() {
        return nameOfRestaurant;
    }

    public void setNameOfRestaurant(String nameOfRestaurant) {
        this.nameOfRestaurant = nameOfRestaurant;
    }

    public SpecialWorkMates(String avatar, String name, String nameOfRestaurant, String id) {
        this.avatar = avatar;
        this.nameOfWorkMates = name;
        this.nameOfRestaurant = nameOfRestaurant;
        this.placeId = id;
    }

    public SpecialWorkMates() {

    }

}

