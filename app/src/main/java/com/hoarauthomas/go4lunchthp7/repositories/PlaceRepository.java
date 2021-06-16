package com.hoarauthomas.go4lunchthp7.repositories;


import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hoarauthomas.go4lunchthp7.api.GooglePlacesInterface;
import com.hoarauthomas.go4lunchthp7.model.pojo.Place;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//TODO:https://medium.com/@kashifo/4-steps-to-mvvm-in-android-java-b05fb4148523

//https://github.com/NinoDLC/MVVM_Clean_Archi_Java/blob/master/app/src/main/java/fr/delcey/mvvm_clean_archi_java/data/interwebs/WeatherRepository.java

public class PlaceRepository {

    private List<String> allPlaces = new ArrayList<>();


    private GooglePlacesInterface service;

    public MutableLiveData<List<String>> getPlaces() {

        final MutableLiveData<List<String>> mutableLiveData = new MutableLiveData<>();

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

                        Double lat = response.body().getResults().get(i).getGeometry().getLocation().getLat();
                        Double lng = response.body().getResults().get(i).getGeometry().getLocation().getLng();


                        Log.i("[THOMAS]","coordonne["+ i + "] " + lat + " " + lng );

                        String placeName = response.body().getResults().get(i).getName();

                        allPlaces.add(placeName);



                    }



                    //TODO: set value or putvalue? extract data to place class
                    //mutableLiveData.setValue(response.body());

                   // return allPlaces.toArray();
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
