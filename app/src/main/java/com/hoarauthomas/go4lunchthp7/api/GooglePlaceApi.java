package com.hoarauthomas.go4lunchthp7.api;


import com.hoarauthomas.go4lunchthp7.model.pojo.Place;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

//This is the contract with the API...

public interface GooglePlaceApi {

    @GET("place/nearbysearch/json?location=48.11198,%20-1.67429&rankby=distance&type=restaurant")
    Call<Place> getNearbyPlaces(
            @Query("key") String key);
            //@Query("radius") int radius);
         //   @Query("location") Double latitude);,

}
