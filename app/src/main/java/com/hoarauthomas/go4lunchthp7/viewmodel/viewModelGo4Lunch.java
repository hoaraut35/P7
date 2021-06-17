package com.hoarauthomas.go4lunchthp7.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.hoarauthomas.go4lunchthp7.model.pojo.Result;
import com.hoarauthomas.go4lunchthp7.repositories.RestaurantsRepository;


import java.util.List;
import java.util.concurrent.Executor;

//Business logic
//share data between multiple fragment (list workmates etc)

//https://medium.com/@kashifo/4-steps-to-mvvm-in-android-java-b05fb4148523

public class viewModelGo4Lunch extends ViewModel {


    //add here repositories ...
    private RestaurantsRepository myPlaceSource;


    //constructor for viewmodel
    public viewModelGo4Lunch(RestaurantsRepository placeRepository, Executor executor) {
        this.myPlaceSource = new RestaurantsRepository();
        //this.executor = executor;
    }


    public LiveData<List<Result>> getRestaurants() {
        return myPlaceSource.getPlaces();

    }

    public LiveData<List<Result>> getSecurity() {
        return myPlaceSource.getPlaces();

    }

    public LiveData<List<Result>> getWorkMates() {
        return myPlaceSource.getPlaces();

    }


}
