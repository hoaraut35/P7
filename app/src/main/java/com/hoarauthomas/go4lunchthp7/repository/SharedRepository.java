package com.hoarauthomas.go4lunchthp7.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hoarauthomas.go4lunchthp7.Prediction;

public class SharedRepository {



    public MutableLiveData<Integer> myZoom = new MutableLiveData<Integer>();

    public MutableLiveData<Prediction> myPlaceIdFromAutocomplete = new MutableLiveData<>();
    public MutableLiveData<com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo> myRestaurantList = new MutableLiveData<>();


    /**
     * public method to get prediction in repository, for observe by sample
     *
     * @return
     */
    public MutableLiveData<Prediction> getMyPlaceIdFromAutocomplete() {
        return myPlaceIdFromAutocomplete;
    }

    /**
     * public method to set prediction in repository
     *
     * @param myPlaceFromVM
     */
    public void setMyPlaceIdFromAutocomplete(Prediction myPlaceFromVM) {
        this.myPlaceIdFromAutocomplete.postValue(myPlaceFromVM);
    }

    public void setZoom(Integer myZoom) {
        this.myZoom.setValue(myZoom);
    }

    public LiveData<Integer> getMyZoom() {
        return myZoom;
    }
}
