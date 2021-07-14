package com.hoarauthomas.go4lunchthp7.api;


import android.location.Location;
import android.util.Log;

import com.hoarauthomas.go4lunchthp7.model.pojo.Place;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

//This is the contract with the API Places...

public interface GooglePlaceApi {

    @GET("place/nearbysearch/json?rankby=distance&type=restaurant")
    Call<Place> getNearbyPlaces(
            @Query("key") String key,
            @Query("location") String myLocation);

}
