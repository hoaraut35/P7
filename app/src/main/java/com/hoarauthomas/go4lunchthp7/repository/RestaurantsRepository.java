package com.hoarauthomas.go4lunchthp7.repository;


import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hoarauthomas.go4lunchthp7.BuildConfig;
import com.hoarauthomas.go4lunchthp7.api.GooglePlacesInterface;
import com.hoarauthomas.go4lunchthp7.api.RetrofitRequest;
import com.hoarauthomas.go4lunchthp7.model.pojo.Place;
import com.hoarauthomas.go4lunchthp7.model.pojo.Result;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//TODO:https://medium.com/@kashifo/4-steps-to-mvvm-in-android-java-b05fb4148523

//https://github.com/NinoDLC/MVVM_Clean_Archi_Java/blob/master/app/src/main/java/fr/delcey/mvvm_clean_archi_java/data/interwebs/WeatherRepository.java

public class RestaurantsRepository {

    //this is api service class
    private GooglePlacesInterface service;

    //this is the list for add all iteration in a list to sned after in mutable
    private final List<Result> allPlaces = new ArrayList<>();

    //this is the constructor for repository
    public RestaurantsRepository() {
        service = RetrofitRequest.getRetrofitInstance().create(GooglePlacesInterface.class);
    }

    //this is livedata to publish to viewmodel
    public LiveData<List<Result>> getAllPlaces() {

        final MutableLiveData<List<Result>> data = new MutableLiveData<>();

        service.getNearbyPlaces(BuildConfig.MAPS_API_KEY, 1000)
                .enqueue(new Callback<Place>() {
                    @Override
                    public void onResponse(Call<Place> call, Response<Place> response) {

                        if (response.body() != null) {

                            //iterate all results ...
                            for (int i = 0; i < response.body().getResults().size(); i++) {
                                //?
                                allPlaces.add(response.body().getResults().get(i));
                                data.postValue(allPlaces);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Place> call, Throwable t) {
                        Log.i("[THOMAS]", "[REPOSITORY FAIL] Erreur repository place ! ");
                        data.postValue(null);
                    }
                });

        return data;
    }

}
