package com.hoarauthomas.go4lunchthp7.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.hoarauthomas.go4lunchthp7.model.GitHubRepo;
import com.hoarauthomas.go4lunchthp7.model.Place;
import com.hoarauthomas.go4lunchthp7.retrofit.ApiRetrofitService;
import com.hoarauthomas.go4lunchthp7.retrofit.GooglePlacesInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


//TODO:https://medium.com/@kashifo/4-steps-to-mvvm-in-android-java-b05fb4148523
public class PlaceRepository {

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
