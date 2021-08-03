package com.hoarauthomas.go4lunchthp7.api;


import com.hoarauthomas.go4lunchthp7.Result2;
import com.hoarauthomas.go4lunchthp7.model.pojo.Place;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

//This is the contract with the Place Search API and Place Details API

public interface GooglePlaceApi {

    //without rxjava
    // for Place Search...
    @GET("place/nearbysearch/json?rankby=distance&type=restaurant")
    Call<Place> getNearbyPlaces(
            @Query("key") String key,
            @Query("location") String myLocation);

    //for Place Details...
    @GET("place/details/json?fields=formatted_phone_number,url,rating,opening_hours")
    Call<Result2> getPlaceDetails(
            @Query("key") String key,
            @Query("place_id") String myPlaceId);

    //**********************************************************************************************

    //with rxjava
    // for Place Search...
    @GET("place/nearbysearch/json?rankby=distance&type=restaurant")
    Observable<Place> getNearbyPlacesRx(
            @Query("key") String key,
            @Query("location") String myLocation);

    //for Place Details...
    @GET("place/details/json?fields=formatted_phone_number,url,rating,opening_hours")
    Observable<Result2> getPlaceDetailsRx(
            @Query("key") String key,
            @Query("place_id") String myPlaceId);

}
