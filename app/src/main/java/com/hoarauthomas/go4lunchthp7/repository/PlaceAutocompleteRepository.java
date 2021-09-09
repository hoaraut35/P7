package com.hoarauthomas.go4lunchthp7.repository;

import android.location.Location;

import androidx.lifecycle.MutableLiveData;

import com.hoarauthomas.go4lunchthp7.BuildConfig;
import com.hoarauthomas.go4lunchthp7.PlaceAutocomplete;
import com.hoarauthomas.go4lunchthp7.api.GooglePlaceApi;
import com.hoarauthomas.go4lunchthp7.api.RetrofitRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaceAutocompleteRepository {

    //for service
    private final GooglePlaceApi service;

    //for data
    private MutableLiveData<PlaceAutocomplete> myPlaceAutocompleteList = new MutableLiveData<>();

    //constructor
    public PlaceAutocompleteRepository() {
        service = RetrofitRequest.getRetrofitInstance().create(GooglePlaceApi.class);
        //getPlaceAutocomplete("pizza");
    }

    public MutableLiveData<PlaceAutocomplete> getPlaceAutocomplete(String textSearch, Location position) {
        String positionstr =null;

        if (!Double.isNaN(position.getLatitude()) && !Double.isNaN(position.getLongitude())){

            positionstr = position.getLatitude() +  "," + position.getLongitude();
        }

        if (textSearch != null && textSearch.length() > 3) {


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

            return myPlaceAutocompleteList;

        }else
        {
            myPlaceAutocompleteList.setValue(null);
            return myPlaceAutocompleteList;
        }

    }


}
