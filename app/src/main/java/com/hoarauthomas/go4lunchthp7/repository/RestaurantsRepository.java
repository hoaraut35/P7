package com.hoarauthomas.go4lunchthp7.repository;


import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hoarauthomas.go4lunchthp7.BuildConfig;
import com.hoarauthomas.go4lunchthp7.api.GooglePlaceApi;
import com.hoarauthomas.go4lunchthp7.api.RetrofitRequest;
import com.hoarauthomas.go4lunchthp7.model.placedetails2.MyDetailRestaurant;
import com.hoarauthomas.go4lunchthp7.model.placedetails2.ResultDetailRestaurant;
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
    final MutableLiveData<List<com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo>> listOfRestaurantWithLongLat = new MutableLiveData<>();
    private final MutableLiveData<ResultDetailRestaurant> restauDetailObj = new MutableLiveData<>();
    private ResultDetailRestaurant monDetailRestau = new ResultDetailRestaurant();
    private final MutableLiveData<MyDetailRestaurant> restauDetailObj2 = new MutableLiveData<>();

    //**********************************************************************************************

    //this is the constructor is called by factory...
    public RestaurantsRepository() {
        service = RetrofitRequest.getRetrofitInstance().create(GooglePlaceApi.class);
    }

    //**********************************************************************************************

    //update position of user in repository when a location is find
    public void UpdateLngLat(Double Long, Double Lat) {
        Log.i("[RESTAURANT]", "Repository restaurant position " + Lat + Long);
        listOfRestaurantWithLongLat.setValue(getAllRestaurants(Long, Lat).getValue());
    }

    //**********************************************************************************************

    //this is livedata is publish to viewmodel
    public LiveData<List<com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo>> getAllRestaurants(@Nullable Double Long, @Nullable Double Lat) {

        String myPositionStr = Lat + "," + Long;
        Log.i("[RESTAURANT]", "[REPOSITORY RESTAURANT : Ma position : " + myPositionStr);

        service.getNearbyPlaces(BuildConfig.MAPS_API_KEY, myPositionStr)

                .enqueue(new Callback<Place>() {
                    @Override
                    public void onResponse(Call<Place> call, Response<Place> response) {

                        if (response.body() != null) {

                            allRestaurants.clear();

                            //iterate all results ...
                            for (int i = 0; i < response.body().getResults().size(); i++) {
                                Log.i("[RESTAURANT]", "Repository Restaurants, getAllRestaurants : " + response.body().getResults().size());
                                allRestaurants.add(response.body().getResults().get(i));
                                listOfRestaurantWithLongLat.postValue(allRestaurants);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Place> call, Throwable t) {
                        Log.i("[RESTAURANT]", "[REPOSITORY FAIL] Erreur repository place ! " + t.getMessage());
                        listOfRestaurantWithLongLat.postValue(null);
                    }
                });

        return listOfRestaurantWithLongLat;
    }

    //**********************************************************************************************

    //this livedata is publish to viewmodel...
    public LiveData<ResultDetailRestaurant> getRestaurantById(String restaurant_id) {

        Log.i("[DETAIL]", "restaurant_id restaur from repo " + restaurant_id);

        service.getPlaceDetails(BuildConfig.MAPS_API_KEY, restaurant_id)
                .enqueue(new Callback<ResultDetailRestaurant>() {
                    @Override
                    public void onResponse(Call<ResultDetailRestaurant> call, Response<ResultDetailRestaurant> response) {

                        Log.i("[DETAIL]", "DETAIL REPO : " + response.body().getUrl() + response.body().getFormattedPhoneNumber() + response.body());
                        restauDetailObj.setValue(response.body());
                    }

                    @Override
                    public void onFailure(Call<ResultDetailRestaurant> call, Throwable t) {

                    }
                });

        return restauDetailObj;
    }

    //**********************************************************************************************

    //this livedata is publish to viewmodel...
    public ResultDetailRestaurant getRestaurantById2(String restaurant_id) {

        Log.i("[DETAIL]", "restaurant_id restaur from repo " + restaurant_id);

        service.getPlaceDetails2(BuildConfig.MAPS_API_KEY, restaurant_id)
                .enqueue(new Callback<MyDetailRestaurant>() {
                    @Override
                    public void onResponse(Call<MyDetailRestaurant> call, Response<MyDetailRestaurant> response) {
                        Log.i("[DETAIL]", "DETAIL REPOSITORY : " +
                                response.body().getResult().getFormattedPhoneNumber() +
                                " url : " +
                                response.body().getResult().getUrl() + response.body().getResult());
                        monDetailRestau = response.body().getResult();

                    }

                    @Override
                    public void onFailure(Call<MyDetailRestaurant> call, Throwable t) {

                    }
                });

        return monDetailRestau;

    }
}
