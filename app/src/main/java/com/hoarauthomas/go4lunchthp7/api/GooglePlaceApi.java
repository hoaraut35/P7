package com.hoarauthomas.go4lunchthp7.api;

import com.hoarauthomas.go4lunchthp7.PlaceAutocomplete;
import com.hoarauthomas.go4lunchthp7.model.NearbySearch.Place;
import com.hoarauthomas.go4lunchthp7.model.PlaceDetails.PlaceDetailsFinal;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GooglePlaceApi {

    @GET("place/nearbysearch/json?rankby=distance&type=restaurant")
    Call<Place> getNearbyPlaces(
            @Query("key") String key,
            @Query("location") String myLocation);

    @GET("place/details/json?")
    Call<PlaceDetailsFinal> getPlaceWithAllDetails(
            @Query("key") String key,
            @Query("place_id") String myPlaceId);

    @GET("place/details/json?")
    Call<PlaceDetailsFinal> getPlaceWithAllDetails2(
            @Query("key") String key,
            @Query("place_id") String myPlaceId);

    @GET("place/autocomplete/json?types=establishment&radius=1000&components=country:fr&language=fr")
    Call<PlaceAutocomplete> getPlaceAutocomplete(
            @Query("key") String key,
            @Query("input") String input,
            @Query("location") String location);

}
