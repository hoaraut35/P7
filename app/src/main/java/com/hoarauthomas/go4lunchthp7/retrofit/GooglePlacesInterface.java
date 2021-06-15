package com.hoarauthomas.go4lunchthp7.retrofit;

import com.hoarauthomas.go4lunchthp7.model.GitHubRepo;
import com.hoarauthomas.go4lunchthp7.model.Place;

import java.util.List;
import java.util.Queue;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GooglePlacesInterface {

    //https://maps.googleapis.com/maps/api/place/nearbysearch/json?key=AIzaSyCKxZQFg6PZiBL9kHASYdFWt6A0Ai2mAZw&location=48.114700,-1.679400&radius=10

    //This is a sample request for Google Places...
    @GET("nearbysearch/json?sensor=true&location=48.11198,%20-1.67429")

    Call<Place> getNearbyPlaces(@Query("key") String key, @Query("radius") int radius);
    //Call<List<Place>> placesForLocation(@Query("radius") String radius);

}
