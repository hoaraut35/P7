package com.hoarauthomas.go4lunchthp7.repositories;


import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hoarauthomas.go4lunchthp7.data.api.GooglePlacesInterface;
import com.hoarauthomas.go4lunchthp7.model.pojo.Place;
import com.hoarauthomas.go4lunchthp7.model.pojo.Result;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//TODO:https://medium.com/@kashifo/4-steps-to-mvvm-in-android-java-b05fb4148523

//https://github.com/NinoDLC/MVVM_Clean_Archi_Java/blob/master/app/src/main/java/fr/delcey/mvvm_clean_archi_java/data/interwebs/WeatherRepository.java

public class RestaurantsRepository {

    private List<Result> allPlaces = new ArrayList<>();

    public LiveData<List<Result>> getAllPlaces() {
        return  getPlaces();
    }





    private GooglePlacesInterface service;

    public MutableLiveData<List<Result>> getPlaces() {

        final MutableLiveData<List<Result>> mutableLiveData = new MutableLiveData<>();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/maps/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(GooglePlacesInterface.class);

        service.getNearbyPlaces("AIzaSyDzUUJlN7hmetd7MtQR5s5TTzWiO4dwpCA", 1000).enqueue(new Callback<Place>() {
            @Override
            public void onResponse(Call<Place> call, Response<Place> response) {

                Log.i("[THOMAS]", "[REPOSITORY PLACES] Actual number size : " + response.body().getResults().size());

                if (response.isSuccessful() && response.body() != null) {

                    //loop to add marker on map for everybody result
                    for (int i = 0; i < response.body().getResults().size(); i++){
                       allPlaces.add(response.body().getResults().get(i));
                    }

                    Log.i("[THOMAS]","Repository resultats restaurants " + allPlaces.size() );
                    mutableLiveData.postValue(allPlaces);
               }
            }

            @Override
            public void onFailure(Call<Place> call, Throwable t) {
                Log.i("[THOMAS]", "Erreur repository place ! ");
            }
        });

        return mutableLiveData;

    }








}
