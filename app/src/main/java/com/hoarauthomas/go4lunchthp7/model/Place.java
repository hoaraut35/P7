package com.hoarauthomas.go4lunchthp7.model;



import android.media.Image;

//This is a Model Class for API Google Places
public class Place {

    private String name;
    private int distance;
    private Image image;
    private String type;
    private String adress;
    private int numberOfWorkmates;
    private String timetoopen;
    private String avis;


    public String getName() {
        return name;
    }
}
