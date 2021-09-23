package com.hoarauthomas.go4lunchthp7.repository;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.hoarauthomas.go4lunchthp7.BuildConfig;
import com.hoarauthomas.go4lunchthp7.PlaceAutocomplete;
import com.hoarauthomas.go4lunchthp7.Prediction;
import com.hoarauthomas.go4lunchthp7.api.GooglePlaceApi;
import com.hoarauthomas.go4lunchthp7.api.RetrofitRequest;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaceAutocompleteRepository {

    //variable
    private final GooglePlaceApi service;
    private final MutableLiveData<PlaceAutocomplete> myPlaceAutoCompleteListSingle = new MutableLiveData<>();

    //constructor
    public PlaceAutocompleteRepository() {
        service = RetrofitRequest.getRetrofitInstance().create(GooglePlaceApi.class);
    }


    //getter setter
    public List<Prediction> myList = new ArrayList<>();

    public List<Prediction> getMyList() {
        return myList;
    }

    public void setMyList(List<Prediction> myList) {
        this.myList = myList;
    }

    //method to get autocomplete
    public void getPlaceAutocompleteSingle(String textSearch, Location position) {

        String positionStr;

        if (!Double.isNaN(position.getLatitude()) && !Double.isNaN(position.getLongitude()) && textSearch != null && textSearch.length() > 3) {

            positionStr = position.getLatitude() + "," + position.getLongitude();

            service.getPlaceAutocomplete(BuildConfig.MAPS_API_KEY, textSearch, positionStr).enqueue(new Callback<PlaceAutocomplete>() {
                @Override
                public void onResponse(@NonNull Call<PlaceAutocomplete> call, @NonNull Response<PlaceAutocomplete> response) {
                    myPlaceAutoCompleteListSingle.setValue(response.body());
                }

                @Override
                public void onFailure(@NonNull Call<PlaceAutocomplete> call, @NonNull Throwable t) {
                    myPlaceAutoCompleteListSingle.setValue(null);
                }
            });

        } else {
            myPlaceAutoCompleteListSingle.setValue(null);
        }

    }

    public MutableLiveData<PlaceAutocomplete> getPlaces() {
        return myPlaceAutoCompleteListSingle;
    }

    public void setResult(List<Prediction> myList) {
        setMyList(myList);
    }
}
