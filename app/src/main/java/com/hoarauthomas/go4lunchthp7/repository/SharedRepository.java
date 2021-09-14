package com.hoarauthomas.go4lunchthp7.repository;

import androidx.lifecycle.MutableLiveData;

import com.facebook.internal.Mutable;

public class SharedRepository {

    public MutableLiveData myPlaceIdFromAutocomplete = new MutableLiveData();





    public MutableLiveData getMyPlaceIdFromAutocomplete() {
        return myPlaceIdFromAutocomplete;
    }

    public void setMyPlaceIdFromAutocomplete(MutableLiveData myPlaceIdFromAutocomplete) {
        this.myPlaceIdFromAutocomplete = myPlaceIdFromAutocomplete;
    }
}
