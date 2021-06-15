package com.hoarauthomas.go4lunchthp7.api;

import com.hoarauthomas.go4lunchthp7.model.Place;
import com.hoarauthomas.go4lunchthp7.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;


//TODO: création d'une classe interface pour Retrofit
public interface ApiClient {

    //@GET("https://maps.googleapis.com/maps/api/place/nearbysearch/json?AIzaSyBj7uLS8r6m1-BitmJynXP1yM_Dc-usJ2U");
    Call<List<Place>> getPlaces();

}
