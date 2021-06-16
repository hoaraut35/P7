package com.hoarauthomas.go4lunchthp7.repository;


import com.hoarauthomas.go4lunchthp7.api.GooglePlacesInterface;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//TODO:https://medium.com/@kashifo/4-steps-to-mvvm-in-android-java-b05fb4148523
//https://github.com/NinoDLC/MVVM_Clean_Archi_Java/blob/master/app/src/main/java/fr/delcey/mvvm_clean_archi_java/data/interwebs/WeatherRepository.java
public class PlaceRepository {


    private GooglePlacesInterface service;

    public PlaceRepository() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/maps/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(GooglePlacesInterface.class);
    }






/*
    public MutableLiveData<List<Place>> requestRepos() {
        final MutableLiveData<List<Place>> mutableLiveData = new MutableLiveData<>();


        Retrofit retrofit = new Retrofit.Builder()
                //work fine for test github
                //.baseUrl("https://api.github.com/")
                .baseUrl("https://maps.googleapis.com/maps/api/place/nearbysearch/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Create a REST
        //work fine for github test
        //ApiRetrofitService service = retrofit.create(ApiRetrofitService.class);
        GooglePlacesInterface service = retrofit.create(GooglePlacesInterface.class);


        //Fetch a list of the Github repositories
        Call<Place> call = service.placesForLocation("AIzaSyDzUUJlN7hmetd7MtQR5s5TTzWiO4dwpC",10,"48.114700,-1.679400");

        Log.i("[THOMAS]","in repository place");

        //Execute th e call asynchronolously
      /*  call.enqueue(new Callback<List<Place>>() {
            @Override
            public void onResponse(Call<List<Place>> call, Response<List<Place>> response) {

                Log.i("[THOMAS]","retour repository "+ response.body().size());
                mutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Place>> call, Throwable t) {
                Log.i("[THOMAS]","erreur in repository places");
            }
        });


       */

    //return mutableLiveData;
    //}


}
