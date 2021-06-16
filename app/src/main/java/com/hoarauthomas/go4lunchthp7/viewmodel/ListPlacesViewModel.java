package com.hoarauthomas.go4lunchthp7.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hoarauthomas.go4lunchthp7.model.User;
import com.hoarauthomas.go4lunchthp7.model.pojo.Place;
import com.hoarauthomas.go4lunchthp7.repositories.PlaceRepository;


import java.util.List;
import java.util.concurrent.Executor;


//https://medium.com/@kashifo/4-steps-to-mvvm-in-android-java-b05fb4148523
public class ListPlacesViewModel extends ViewModel {


    //add here repositories ...
    private PlaceRepository myPlaceSource;
    //private final Executor executor;

    private LiveData<Place> currentPlace;

    //constructor for viewmodel
    public ListPlacesViewModel(PlaceRepository placeRepository, Executor executor) {
        this.myPlaceSource = new PlaceRepository();
        //this.executor = executor;
    }

  /*  public LiveData<List<String>> getMyPlaces() {
        if (mutableLiveData == null) {


            //  mutableLiveData = myrepo.getPlaces();
            //  Log.i("[THOMAS]",""+ myrepo.getPlaces().getValue().toString());
        }

        return mutableLiveData;

    }

   */

    private MutableLiveData<List<Place>> mutableLiveData;


    public LiveData<List<String>> getPlaces() {
        return myPlaceSource.getPlaces();

    }
}
