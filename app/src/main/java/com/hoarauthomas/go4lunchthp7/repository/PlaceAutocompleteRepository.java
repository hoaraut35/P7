package com.hoarauthomas.go4lunchthp7.repository;

import android.location.Location;

import androidx.lifecycle.MutableLiveData;

import com.hoarauthomas.go4lunchthp7.BuildConfig;
import com.hoarauthomas.go4lunchthp7.PlaceAutocomplete;
import com.hoarauthomas.go4lunchthp7.SingleLiveEvent;
import com.hoarauthomas.go4lunchthp7.api.GooglePlaceApi;
import com.hoarauthomas.go4lunchthp7.api.RetrofitRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Single;

public class PlaceAutocompleteRepository {

    //for service
    private final GooglePlaceApi service;

    //for data
    private MutableLiveData<PlaceAutocomplete> myPlaceAutocompleteList = new MutableLiveData<>();
    private SingleLiveEvent<PlaceAutocomplete> myPlaceAutoCompleteListSingle;

    //constructor
    public PlaceAutocompleteRepository() {
        service = RetrofitRequest.getRetrofitInstance().create(GooglePlaceApi.class);
    }

    public void getPlaceAutocomplete(String textSearch, Location position) {

        String positionstr =null;

        if (!Double.isNaN(position.getLatitude()) && !Double.isNaN(position.getLongitude()) && textSearch != null && textSearch.length() > 3){

            positionstr = position.getLatitude() +  "," + position.getLongitude();

            service.getPlaceAutocomplete(BuildConfig.MAPS_API_KEY, textSearch, positionstr).enqueue(new Callback<PlaceAutocomplete>() {
                @Override
                public void onResponse(Call<PlaceAutocomplete> call, Response<PlaceAutocomplete> response) {
                    myPlaceAutocompleteList.setValue(response.body());
                }

                @Override
                public void onFailure(Call<PlaceAutocomplete> call, Throwable t) {
                    myPlaceAutocompleteList.setValue(null);
                }
            });

        }else
        {
            myPlaceAutocompleteList.setValue(null);
        }

    }

    public void getPlaceAutocompleteSingle(String textSearch, Location position) {

        String positionstr =null;

        if (!Double.isNaN(position.getLatitude()) && !Double.isNaN(position.getLongitude()) && textSearch != null && textSearch.length() > 3){

            positionstr = position.getLatitude() +  "," + position.getLongitude();

            service.getPlaceAutocomplete(BuildConfig.MAPS_API_KEY, textSearch, positionstr).enqueue(new Callback<PlaceAutocomplete>() {
                @Override
                public void onResponse(Call<PlaceAutocomplete> call, Response<PlaceAutocomplete> response) {
                    myPlaceAutoCompleteListSingle.setValue(response.body());
                    //
                }

                @Override
                public void onFailure(Call<PlaceAutocomplete> call, Throwable t) {
                    myPlaceAutoCompleteListSingle.setValue(null);
                }
            });

        }else
        {
            myPlaceAutoCompleteListSingle.setValue(null);
        }

    }

    public MutableLiveData<PlaceAutocomplete> getMyPlaceAutocompleteListForVM() {
        return myPlaceAutocompleteList;
    }

    public SingleLiveEvent<PlaceAutocomplete> getMyPlaceAutocompleteListForVMSingle() {

        return myPlaceAutoCompleteListSingle;
    }
}
