package com.hoarauthomas.go4lunchthp7.data.api;

import com.hoarauthomas.go4lunchthp7.model.pojo.Place;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

//https://www.jsonschema2pojo.org/

public interface GooglePlacesInterface {

    //https://maps.googleapis.com/maps/api/place/nearbysearch/json?key=AIzaSyCKxZQFg6PZiBL9kHASYdFWt6A0Ai2mAZw&location=48.114700,-1.679400&radius=10

    @GET("place/nearbysearch/json?location=48.11198,%20-1.67429&type=restaurant")
    Call<Place> getNearbyPlaces(@Query("key") String key, @Query("radius") int radius);

}
