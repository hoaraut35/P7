package com.hoarauthomas.go4lunchthp7.repository;

import androidx.lifecycle.MutableLiveData;

import com.hoarauthomas.go4lunchthp7.retrofit.ApiRetrofitService;
import com.hoarauthomas.go4lunchthp7.model.Place;


//https://learntodroid.com/consuming-a-rest-api-using-retrofit2-with-the-mvvm-pattern-in-android/

public class PlaceRepository {

 //   private static final ApiRetrofitService myApiService;

    private static final String BASE_URL_API = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?parameters";

    private ApiRetrofitService apiRetrofitService;

    private MutableLiveData<Place> placesResponseLiveData;






}
