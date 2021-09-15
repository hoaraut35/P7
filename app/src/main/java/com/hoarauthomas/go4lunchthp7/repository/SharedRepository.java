package com.hoarauthomas.go4lunchthp7.repository;

import androidx.lifecycle.MutableLiveData;

import com.facebook.internal.Mutable;
import com.hoarauthomas.go4lunchthp7.PlaceAutocomplete;
import com.hoarauthomas.go4lunchthp7.Prediction;
import com.hoarauthomas.go4lunchthp7.SingleLiveEvent;

public class SharedRepository {

    public MutableLiveData<Prediction> myPlaceIdFromAutocomplete = new MutableLiveData<>(null);

    public MutableLiveData<com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo> myRestaurantList = new MutableLiveData<>();


    /**
     * public method to get prediction in repository, for observe by sample
     * @return
     */
    public MutableLiveData<Prediction> getMyPlaceIdFromAutocomplete() {
        return myPlaceIdFromAutocomplete;
    }

    /**
     * public method to set prediction in repository
     * @param myPlaceFromVM
     */
    public void setMyPlaceIdFromAutocomplete(Prediction myPlaceFromVM) {
        if (myPlaceFromVM != null){
            this.myPlaceIdFromAutocomplete.postValue(myPlaceFromVM);
        }

    }
}
