package com.hoarauthomas.go4lunchthp7.repository;


import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hoarauthomas.go4lunchthp7.BuildConfig;
import com.hoarauthomas.go4lunchthp7.RestaurantDetailPojo;
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
    private final List<com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo> allRestaurants = new ArrayList<>();

    private final MutableLiveData<ResultDetailRestaurant> monDetailRestau = new MutableLiveData<>();

    private final MutableLiveData<List<com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo>> listOfRestaurantWithLongLat = new MutableLiveData<>();



    private final MutableLiveData<ResultDetailRestaurant> restauDetailObj = new MutableLiveData<>();



    private final MutableLiveData<MyDetailRestaurant> restauDetailObj2 = new MutableLiveData<>();

    private final MutableLiveData<String> placeId = new MutableLiveData<String>();


    //this is the constructor is called by factory...
    public RestaurantsRepository() {
        Log.i("[MAP]", "Repository restaurant starting singleton...");
        service = RetrofitRequest.getRetrofitInstance().create(GooglePlaceApi.class);
    }

    //**********************************************************************************************

    public LiveData<List<com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo>> getMyRestaurantsList() {
        return listOfRestaurantWithLongLat;
    }

    //update position of user in repository when a location is find
    public void UpdateLngLat(Double Long, Double Lat) {
        Log.i("[MAP]", "Repository restaurant position " + Lat + Long);
        listOfRestaurantWithLongLat.setValue(getAllRestaurants(Long, Lat).getValue());
    }

    //**********************************************************************************************

    //this is livedata is publish to viewmodel
    public LiveData<List<com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo>> getAllRestaurants(@Nullable Double Long, @Nullable Double Lat) {

        String myPositionStr = Lat + "," + Long;
        Log.i("[MAP]", "[REPOSITORY RESTAURANT] : Ma position : " + myPositionStr);

        service.getNearbyPlaces(BuildConfig.MAPS_API_KEY, myPositionStr)

                .enqueue(new Callback<Place>() {
                    @Override
                    public void onResponse(Call<Place> call, Response<Place> response) {

                        if (response.body() != null) {

                            allRestaurants.clear();
                            listOfRestaurantWithLongLat.postValue(null);

                            //iterate all results ...
                            for (int i = 0; i < response.body().getResults().size(); i++) {
                                allRestaurants.add(response.body().getResults().get(i));
                            }

                            Log.i("[RESTAURANT]", "[REPOSITORY RESTAURANT OK] Récupérationb de la liste" + allRestaurants.size());

                            listOfRestaurantWithLongLat.postValue(allRestaurants);
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

    //this livedata is publish to viewmodel... v2
    public LiveData<ResultDetailRestaurant> getRestaurantById(String restaurant_id) {

        if (restaurant_id == null || restaurant_id.isEmpty()) {
            Log.i("[MONDETAIL]","id null opu vide");
            monDetailRestau.setValue(null);
        }else{
            Log.i("[RESTAURANT]", "detail restaurant avec id " + restaurant_id);

            service.getPlaceDetails2(BuildConfig.MAPS_API_KEY, restaurant_id)
                    .enqueue(new Callback<MyDetailRestaurant>() {
                        @Override
                        public void onResponse(Call<MyDetailRestaurant> call, Response<MyDetailRestaurant> response) {
                            assert response.body() != null;

                            Log.i("[MONDETAIL]","recup detail... repository");
                     /*   Log.i("[RESTAURANT]", "DETAIL REPOSITORY : " +
                                "\n téléphone : " +
                                response.body().getResult().getFormattedPhoneNumber() +
                                "\n url : " +
                                response.body().getResult().getUrl() + response.body().getResult());

                      */
                            monDetailRestau.setValue(response.body().getResult());

                        }

                        @Override
                        public void onFailure(Call<MyDetailRestaurant> call, Throwable t) {
                            Log.i("[MONDETAIL]","Erreur sur le detail... repository");
                        }
                    });



        }
        return monDetailRestau;


    }

    public String setPlaceId(String id){

        getRestaurantById(id);
        return id;
    }

    public LiveData<ResultDetailRestaurant> getMyRestaurantDetail() {
        return monDetailRestau;
    }


}
