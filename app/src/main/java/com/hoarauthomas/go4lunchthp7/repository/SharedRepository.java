package com.hoarauthomas.go4lunchthp7.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class SharedRepository {

    private final MutableLiveData<Integer> myZoom = new MutableLiveData<>();

    public MutableLiveData<Boolean> reloadMap = new MutableLiveData<>(true);

    public SharedRepository() {
    }

    public void setZoom(Integer myZoom) {
        this.myZoom.setValue(myZoom);
    }

    public LiveData<Integer> getMyZoom() {
        return myZoom;
    }

    public boolean setReloadMap(Boolean bool){
        reloadMap.setValue(bool);
        return false;
    }

    //used for relaod initial data on map or list of restaurant
    public LiveData<Boolean> getReload() {
        return reloadMap;
    }

}
