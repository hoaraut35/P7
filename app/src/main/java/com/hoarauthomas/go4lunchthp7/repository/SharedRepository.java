package com.hoarauthomas.go4lunchthp7.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hoarauthomas.go4lunchthp7.Prediction;
import com.hoarauthomas.go4lunchthp7.model.NearbySearch.RestaurantPojo;

import java.util.List;

public class SharedRepository {

    private MutableLiveData<Integer> myZoom = new MutableLiveData<Integer>();
    public MutableLiveData<Prediction> myPlaceIdFromAutocomplete = new MutableLiveData<>();
    public MutableLiveData<List<String>> myRestaurantList = new MutableLiveData<>();

    public MutableLiveData<Boolean> reloadMap = new MutableLiveData<>(true);

    public MutableLiveData<Prediction> getMyPlaceIdFromAutocomplete() {
        return myPlaceIdFromAutocomplete;
    }




    public LiveData<List<String>> getMyRestaurantList() {
        return myRestaurantList;
    }

    public void setMyRestaurantList(List<String> myRestaurantList) {
        this.myRestaurantList.setValue(myRestaurantList);
    }

    public void setMyPlaceIdFromAutocomplete(Prediction myPlaceFromVM) {
        this.myPlaceIdFromAutocomplete.postValue(myPlaceFromVM);
    }

    public void setZoom(Integer myZoom) {
        Log.i("[PREF]", "Zoom setup to : " + myZoom);
        this.myZoom.setValue(myZoom);
    }

    public LiveData<Integer> getMyZoom() {
        return myZoom;
    }


    public void setReloadMap(Boolean bool){
        reloadMap.setValue(bool);
    }

    public LiveData<Boolean> getReload() {
        return reloadMap;
    }


}
