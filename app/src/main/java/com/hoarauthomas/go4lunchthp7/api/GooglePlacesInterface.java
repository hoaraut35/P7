package com.hoarauthomas.go4lunchthp7.api;


import com.hoarauthomas.go4lunchthp7.model.pojo.Place;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GooglePlacesInterface {

    @GET("place/nearbysearch/json?location=48.11198,%20-1.67429&type=restaurant")
    Call<Place> getNearbyPlaces(
            @Query("key") String key,
            @Query("radius") int radius);
       //     @Query("latidute") String latitude,
         //   @Query("longitude") String longitude);
        //&rankby=distance
}
