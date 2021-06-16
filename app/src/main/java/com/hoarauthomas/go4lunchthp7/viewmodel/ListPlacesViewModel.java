package com.hoarauthomas.go4lunchthp7.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hoarauthomas.go4lunchthp7.model.pojo.Place;
import com.hoarauthomas.go4lunchthp7.repository.PlaceRepository;


import java.util.List;


//https://medium.com/@kashifo/4-steps-to-mvvm-in-android-java-b05fb4148523
public class ListPlacesViewModel extends ViewModel {


    private PlaceRepository myrepo;

    private MutableLiveData<List<Place>> mutableLiveData;

    public ListPlacesViewModel() {
        myrepo = new PlaceRepository();
    }

    public LiveData<List<Place>> getMyPlaces() {
        if (mutableLiveData == null) {
          //  mutableLiveData = myrepo.requestRepos();
        }

        return mutableLiveData;

    }


}
