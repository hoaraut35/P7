package com.hoarauthomas.go4lunchthp7.repository;


import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hoarauthomas.go4lunchthp7.BuildConfig;

import com.hoarauthomas.go4lunchthp7.Result2;
import com.hoarauthomas.go4lunchthp7.api.GooglePlaceApi;
import com.hoarauthomas.go4lunchthp7.api.RetrofitRequest;
import com.hoarauthomas.go4lunchthp7.model.pojo.Place;
import com.hoarauthomas.go4lunchthp7.pojo.Result;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//TODO:https://medium.com/@kashifo/4-steps-to-mvvm-in-android-java-b05fb4148523

//https://github.com/NinoDLC/MVVM_Clean_Archi_Java/blob/master/app/src/main/java/fr/delcey/mvvm_clean_archi_java/data/interwebs/WeatherRepository.java

public class RestaurantsRepository {

    //this is api service class
    private final GooglePlaceApi service;

    //this is the list for add all iteration in a list to send after in mutable
    //for the fisrt api query...
    private final List<Result> allRestaurants = new ArrayList<>();
    //for the second api query...
    private final List<Result2> allRestauranntsDetails = new ArrayList<>();

    private Double Long, Lat;

    //this is the constructor for repository
    public RestaurantsRepository() {
        Log.i("[THOMAS]", "- Appel Repository Restaurants");
        service = RetrofitRequest.getRetrofitInstance().create(GooglePlaceApi.class);
    }

    public void UpdateLngLat(Double Long, Double Lat) {
        Log.i("[RESTAURANT]", "Repository restaurant position " + Lat + Long);
        this.Long = Long;
        this.Lat = Lat;
    }


    //this is livedata to publish detail restaurant to viewmodel
  /*  public LiveData<List<Result2>> getAllDetailForRestaurant(){

    }

   */

    //this is livedata to publish to viewmodel
    public LiveData<List<Result>> getAllRestaurants(Double Long, Double Lat) {


        final MutableLiveData<List<Result>> data = new MutableLiveData<>();

        //service.getNearbyPlaces(BuildConfig.MAPS_API_KEY, 1000)
        Log.i("[RESTAURANT]", "getAllRestaurant " + this.Long + Lat);


        String myPositionStr = Lat+","+Long;
        Log.i("[RESTAURANT]", "myLocation" + myPositionStr);

        service.getNearbyPlaces(BuildConfig.MAPS_API_KEY, myPositionStr)
        //service.getNearbyPlaces(BuildConfig.MAPS_API_KEY)

                .enqueue(new Callback<Place>() {
                    @Override
                    public void onResponse(Call<Place> call, Response<Place> response) {

                        if (response.body() != null) {

                            //iterate all results ...
                            for (int i = 0; i < response.body().getResults().size(); i++) {
                                Log.i("[RESTAURANT]", "Repository Restaurants, getAllRestaurants : " + response.body().getResults().size());

                                allRestaurants.add(response.body().getResults().get(i));
                                data.postValue(allRestaurants);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Place> call, Throwable t) {
                        Log.i("[RESTAURANT]", "[REPOSITORY FAIL] Erreur repository place ! ");
                        data.postValue(null);
                    }
                });

        return data;
    }

}
