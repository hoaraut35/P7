package com.hoarauthomas.go4lunchthp7.api;


import com.hoarauthomas.go4lunchthp7.RestaurantDetailPojo;
import com.hoarauthomas.go4lunchthp7.model.placedetails2.MyDetailRestaurant;
import com.hoarauthomas.go4lunchthp7.model.pojo.Place;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

//This is the contract with the Place Search API and Place Details API

public interface GooglePlaceApi {

    // for Restaurants search ...
    @GET("place/nearbysearch/json?rankby=distance&type=restaurant")
    Call<Place> getNearbyPlaces(
            @Query("key") String key,
            @Query("location") String myLocation);

    //for restaurant details ...
    @GET("place/details/json?fields=formatted_phone_number,url,rating,opening_hours")
    Call<RestaurantDetailPojo> getPlaceDetails(
            @Query("key") String key,
            @Query("place_id") String myPlaceId);

    //for restaurant details ...
    @GET("place/details/json?fields=formatted_phone_number,url,rating,opening_hours")
    Call<MyDetailRestaurant> getRestaurantDetails(
            @Query("key") String key,
            @Query("place_id") String myPlaceId);

}
