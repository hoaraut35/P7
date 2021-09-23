package com.hoarauthomas.go4lunchthp7.repository;

import android.location.Location;

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
    private MutableLiveData<PlaceAutocomplete> myPlaceAutoCompleteListSingle = new MutableLiveData<>();

    //constructor
    public PlaceAutocompleteRepository() {
        service = RetrofitRequest.getRetrofitInstance().create(GooglePlaceApi.class);
    }


    //getter setter
    public List<Prediction> myLiost = new ArrayList<>();

    public List<Prediction> getMyList() {
        return myLiost;
    }

    public void setMyLiost(List<Prediction> myLiost) {
        this.myLiost = myLiost;
    }

    //method to get autocomplete
    public void getPlaceAutocompleteSingle(String textSearch, Location position) {

        String positionstr = null;

        if (!Double.isNaN(position.getLatitude()) && !Double.isNaN(position.getLongitude()) && textSearch != null && textSearch.length() > 3) {

            positionstr = position.getLatitude() + "," + position.getLongitude();

            service.getPlaceAutocomplete(BuildConfig.MAPS_API_KEY, textSearch, positionstr).enqueue(new Callback<PlaceAutocomplete>() {
                @Override
                public void onResponse(Call<PlaceAutocomplete> call, Response<PlaceAutocomplete> response) {
                    myPlaceAutoCompleteListSingle.setValue(response.body());
                }

                @Override
                public void onFailure(Call<PlaceAutocomplete> call, Throwable t) {
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
        setMyLiost(myList);
    }
}
