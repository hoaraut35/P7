package com.hoarauthomas.go4lunchthp7.repository;

import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hoarauthomas.go4lunchthp7.BuildConfig;
import com.hoarauthomas.go4lunchthp7.PlaceAutocomplete;
import com.hoarauthomas.go4lunchthp7.Prediction;
import com.hoarauthomas.go4lunchthp7.api.GooglePlaceApi;
import com.hoarauthomas.go4lunchthp7.api.RetrofitRequest;
import com.hoarauthomas.go4lunchthp7.model.PlaceDetails.PlaceDetailsFinal;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaceAutocompleteRepository {

    //variable
    private final GooglePlaceApi service;
    private final MutableLiveData<PlaceAutocomplete> myPlaceAutoCompleteListSingle = new MutableLiveData<>();

    private final MutableLiveData<List<PlaceDetailsFinal>> myPlaceDataList = new MutableLiveData<>();

    //constructor
    public PlaceAutocompleteRepository() {
        service = RetrofitRequest.getRetrofitInstance().create(GooglePlaceApi.class);
    }


    //getter setter
    public List<Prediction> myList = new ArrayList<>();

    public void setMyList(List<Prediction> myList) {
        this.myList = myList;
    }

    //method to get autocomplete
   /* public void getPlaceAutocompleteSingle(String textSearch, Location position) {

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

    */


    //method to get autocomplete with directly a list of PlaceDEtail
    public void getAutocompleteDataToPlaceDetailList(String textSearch, Location position) {

        String positionStr;

        if (!Double.isNaN(position.getLatitude()) && !Double.isNaN(position.getLongitude()) && textSearch != null && textSearch.length() > 3) {

            positionStr = position.getLatitude() + "," + position.getLongitude();

            service.getPlaceAutocomplete(BuildConfig.MAPS_API_KEY, textSearch, positionStr).enqueue(new Callback<PlaceAutocomplete>() {
                @Override
                public void onResponse(@NonNull Call<PlaceAutocomplete> call, @NonNull Response<PlaceAutocomplete> response) {

                    List<PlaceDetailsFinal> myList = new ArrayList<>();

                    if (response.isSuccessful()) {

                      //  Log.i("[AUTOCOMPLETE]", "Number of prediction(s) : " + response.body().getPredictions().size());

                        for (Prediction myPrediction : response.body().getPredictions()) {

                            service.getPlaceWithAllDetails(BuildConfig.MAPS_API_KEY, myPrediction.getPlaceId()).enqueue(new Callback<PlaceDetailsFinal>() {
                                @Override
                                public void onResponse(Call<PlaceDetailsFinal> call, Response<PlaceDetailsFinal> response) {

                                  //  Log.i("[AUTOCOMPLETE]", "Working on the : " + myPrediction.getDescription());

                                    if (response.isSuccessful()) {
                                        myList.add(response.body());
                                        myPlaceDataList.setValue(myList);
                                       // Log.i("[AUTOCOMPLETE]", "New size of list result for ui  : " + myList.size());
                                    }
                                }

                                @Override
                                public void onFailure(Call<PlaceDetailsFinal> call, Throwable t) {
                                  //  Log.i("[AUTOCOMPLETE]", "Fail on iterate result");
                                }
                            });
                        }

                        //set mutablelivedata
                        myPlaceDataList.setValue(myList);
                     //   Log.i("[AUTOCOMPLETE]", "update mutablelivedata for vm  :" + myList.size());


                    }


                }

                @Override
                public void onFailure(@NonNull Call<PlaceAutocomplete> call, @NonNull Throwable t) {
                    myPlaceDataList.setValue(null);
                   // Log.i("[AUTOCOMPLETE]", "Fail on autocomplete request...");
                }
            });

        } else {
            myPlaceDataList.setValue(null);
        }
    }

   /* public MutableLiveData<PlaceAutocomplete> getPlaces() {
        return myPlaceAutoCompleteListSingle;
    }

    */


    public LiveData<List<PlaceDetailsFinal>> getPlacesForAutocomplete() {
        Log.i("[AUTOCOMPLETE]", "retourn data prediction for vm :" + myList.size());
        return myPlaceDataList;
    }

    public void setResult(List<Prediction> myList) {
        setMyList(myList);
    }
}
