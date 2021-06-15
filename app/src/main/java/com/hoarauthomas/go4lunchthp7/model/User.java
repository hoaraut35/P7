package com.hoarauthomas.go4lunchthp7.model;

import android.media.Image;

public class User {

    private Image imgAvatar;
    private String strFirstName;
    private String strLastName;

    public User(String strFirstName, String strLastName) {
        this.strFirstName = strFirstName;
        this.strLastName = strLastName;
        //TODO:add avatar later
        //this.imgAvatar = imgAvatar;
    }

    public Image getImgAvatar() {
        return imgAvatar;
    }

    public String getStrFirstName() {
        return strFirstName;
    }

    public String getStrLastName() {
        return strLastName;
    }

    public void setImgAvatar(Image imgAvatar) {
        this.imgAvatar = imgAvatar;
    }

    public void setStrFirstName(String strFirstName) {
        this.strFirstName = strFirstName;
    }

    public void setStrLastName(String strLastName) {
        this.strLastName = strLastName;
    }
}
