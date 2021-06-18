package com.hoarauthomas.go4lunchthp7.data.api;


import com.hoarauthomas.go4lunchthp7.model.pojo.Place;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GooglePlacesInterface {

    //Use https://www.jsonschema2pojo.org/ to convert json to pojo class

    //Sample https://maps.googleapis.com/maps/api/place/nearbysearch/json?key=AIzaSyCKxZQFg6PZiBL9kHASYdFWt6A0Ai2mAZw&location=48.114700,-1.679400&radius=10
    //to get all restaurants...

    @GET("place/nearbysearch/json?location=48.11198,%20-1.67429&type=restaurant")
    Call<Place> getNearbyPlaces(
            @Query("key") String key,
            @Query("radius") int radius);

    //Sample https://maps.googleapis.com/maps/api/place/nearbysearch/json?key=AIzaSyCKxZQFg6PZiBL9kHASYdFWt6A0Ai2mAZw&location=48.114700,-1.679400&radius=10
    //to get a photo from restaurant reference... with size adapted

    @GET("place/photo?")
    Call<Place> getPhotoFromNearby(
            @Query("key") String key,
            @Query("maxHeight") String height,
            @Query("mawWidth") String width,
            @Query("reference") String reference);

}
