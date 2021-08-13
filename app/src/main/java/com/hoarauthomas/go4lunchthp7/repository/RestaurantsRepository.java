package com.hoarauthomas.go4lunchthp7.repository;


import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hoarauthomas.go4lunchthp7.BuildConfig;
import com.hoarauthomas.go4lunchthp7.RestaurantDetailPojo;
import com.hoarauthomas.go4lunchthp7.api.GooglePlaceApi;
import com.hoarauthomas.go4lunchthp7.api.RetrofitRequest;
import com.hoarauthomas.go4lunchthp7.model.pojo.Place;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//TODO:https://medium.com/@kashifo/4-steps-to-mvvm-in-android-java-b05fb4148523

//https://github.com/NinoDLC/MVVM_Clean_Archi_Java/blob/master/app/src/main/java/fr/delcey/mvvm_clean_archi_java/data/interwebs/WeatherRepository.java

public class RestaurantsRepository {

    //this is api service class
    private final GooglePlaceApi service;

    //this is the list for add all iteration in a list to send after in mutable
    private List<com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo> allRestaurants = new ArrayList<>();
    private final List<RestaurantDetailPojo> allRestaurantsDetails = new ArrayList<>();


    final MutableLiveData<List<com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo>> data = new MutableLiveData<>();


    private Double Long, Lat;



    //this is the constructor is called by factory...
    public RestaurantsRepository() {
        service = RetrofitRequest.getRetrofitInstance().create(GooglePlaceApi.class);
    }

    //update position of user in repository when a location is find
    public void UpdateLngLat(Double Long, Double Lat) {
        Log.i("[RESTAURANT]", "Repository restaurant position " + Lat + Long);
        this.Long = Long;
        this.Lat = Lat;
        data.setValue(getAllRestaurants(Long,Lat).getValue());
    }

    //this is livedata to publish detail of restaurant to the viewmodel ...
    public LiveData<RestaurantDetailPojo> getAllDetailForRestaurant(String myPlaceId) {

        final MutableLiveData<RestaurantDetailPojo> data = new MutableLiveData<>();

        service.getPlaceDetails(BuildConfig.MAPS_API_KEY, myPlaceId)
                .enqueue(new Callback<RestaurantDetailPojo>() {


                    @Override
                    public void onResponse(Call<RestaurantDetailPojo> call, Response<RestaurantDetailPojo> response) {
                        if (response.body() != null) {

                            Log.i("[RESTAURANT]", "Repository Restaurants, getDetailrestaurant : " + response.body().getFormattedPhoneNumber());
                            data.postValue(response.body());

                        }
                    }

                    @Override
                    public void onFailure(Call<RestaurantDetailPojo> call, Throwable t) {
                        data.postValue(null);
                    }
                });
        ;
        return data;

    }


    //this is livedata to publish to viewmodel
    public LiveData<List<com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo>> getAllRestaurants(@Nullable Double Long, @Nullable Double Lat) {



        //service.getNearbyPlaces(BuildConfig.MAPS_API_KEY, 1000)
        Log.i("[RESTAURANT]", "getAllRestaurant " + this.Long + Lat);


        String myPositionStr = Lat + "," + Long;
        Log.i("[RESTAURANT]", "myLocation" + myPositionStr);

        service.getNearbyPlaces(BuildConfig.MAPS_API_KEY, myPositionStr)

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
