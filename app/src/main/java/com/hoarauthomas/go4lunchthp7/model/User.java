package com.hoarauthomas.go4lunchthp7.model;

import android.media.Image;

public class User {

    private Image imgAvatar;
    private String strFirstName;
    private String strLastName;

    public User(String strFirstName, String strLastName, Image imgAvatar) {
        this.strFirstName = strFirstName;
        this.strLastName = strLastName;
        this.imgAvatar = imgAvatar;
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
}
