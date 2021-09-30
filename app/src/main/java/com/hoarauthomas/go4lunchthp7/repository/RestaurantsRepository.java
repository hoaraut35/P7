package com.hoarauthomas.go4lunchthp7.repository;


import android.util.Log;
import android.util.LruCache;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bumptech.glide.util.ExceptionPassthroughInputStream;
import com.hoarauthomas.go4lunchthp7.model.NearbySearch.RestaurantPojo;
import com.hoarauthomas.go4lunchthp7.BuildConfig;
import com.hoarauthomas.go4lunchthp7.api.GooglePlaceApi;
import com.hoarauthomas.go4lunchthp7.api.RetrofitRequest;
import com.hoarauthomas.go4lunchthp7.model.NearbySearch.Place;
import com.hoarauthomas.go4lunchthp7.model.PlaceDetails.PlaceDetailsFinal;
import com.hoarauthomas.go4lunchthp7.model.PlaceDetails.ResultPlaceDetail;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;
import javax.xml.transform.Result;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//TODO:https://medium.com/@kashifo/4-steps-to-mvvm-in-android-java-b05fb4148523

//https://github.com/NinoDLC/MVVM_Clean_Archi_Java/blob/master/app/src/main/java/fr/delcey/mvvm_clean_archi_java/data/interwebs/WeatherRepository.java

public class RestaurantsRepository {

    //this is api service class
    private final GooglePlaceApi service;

    //this is the list for add all iteration in a list to send after in mutable
    private  List<RestaurantPojo> allRestaurants = new ArrayList<>();

    public MutableLiveData<ResultPlaceDetail> myRestaurantDetailByPlaceId = new MutableLiveData<>();

    private final MutableLiveData<List<RestaurantPojo>> listOfRestaurantWithLongLat = new MutableLiveData<>();
    private final MutableLiveData<PlaceDetailsFinal> restauDetailObj = new MutableLiveData<>();
    private final MutableLiveData<PlaceDetailsFinal> restauDetailObj2 = new MutableLiveData<>();
    private final MutableLiveData<String> placeId = new MutableLiveData<String>();
    private final MutableLiveData<ResultPlaceDetail> myTest = new MutableLiveData<>();

    private final LruCache<String, PlaceDetailsFinal> myCache = new LruCache<>(2_000);

    public RestaurantsRepository() {
        service = RetrofitRequest.getRetrofitInstance().create(GooglePlaceApi.class);
    }



    //update position of user in repository when a location is find
    public void setNewLatLngPositionFromGPS(Double Long, Double Lat) {
        Log.i("[MAP]", "Repository restaurant position " + Lat + Long);
        listOfRestaurantWithLongLat.setValue(getAllRestaurants(Long, Lat).getValue());
    }

    //for alarm place detail
    public PlaceDetailsFinal getPlaceDetail(String placeId){

        Call<PlaceDetailsFinal> myCallAPI = service.getPlaceWithAllDetails2(BuildConfig.MAPS_API_KEY,placeId);

        try{
            Log.i("[ALARME]","Starting call synchrone for details ..." );
            Response<PlaceDetailsFinal> response = myCallAPI.execute();
            PlaceDetailsFinal apiResponse = response.body();
            return apiResponse;
        }
        catch (Exception ex){
            Log.i("[ALARME]","Extraction synchrone détail restaurant"  + ex.getMessage());
            return null;
        }

    }

    //this is livedata is publish to viewmodel
    public LiveData<List<RestaurantPojo>> getAllRestaurants(@Nullable Double Long, @Nullable Double Lat) {

        String myPositionStr = Lat + "," + Long;
        Log.i("[MAP]", "[REPOSITORY RESTAURANT] : Ma position : " + myPositionStr);

        //TODO: cache

        service.getNearbyPlaces(BuildConfig.MAPS_API_KEY, myPositionStr)

                .enqueue(new Callback<Place>() {
                    @Override
                    public void onResponse(Call<Place> call, Response<Place> response) {

                        if (response.body() != null) {

                            allRestaurants.clear();
                            //listOfRestaurantWithLongLat.postValue(null);

                            for (int i = 0; i < response.body().getResults().size(); i++) {
                                allRestaurants.add(response.body().getResults().get(i));
                            }

                            Log.i("[RESTAURANT]", "[REPOSITORY RESTAURANT OK] Récupérationb de la liste" + allRestaurants.size() + response.body().getResults());

                            listOfRestaurantWithLongLat.postValue(allRestaurants);
                        }
                    }

                    @Override
                    public void onFailure(Call<Place> call, Throwable t) {
                        Log.i("[RESTAURANT]", "[REPOSITORY FAIL] Erreur repository place ! " + t.getMessage());
                        listOfRestaurantWithLongLat.postValue(null);
                    }
                });

        return listOfRestaurantWithLongLat;
    }

    public void getRestaurantById(String placeId) {

        PlaceDetailsFinal existing = myCache.get(placeId);

        //check cache
        if (existing != null) {
            myRestaurantDetailByPlaceId.setValue(existing.getResult());
            Log.i("[CACHE]", "cache used !!");
        } else {

            //no cache, no placeId request
            if (placeId == null || placeId.isEmpty()) {
                Log.i("[MONDETAIL]", "id null opu vide");
                myRestaurantDetailByPlaceId.setValue(null);
            } else {

                Log.i("[RESTAURANT]", "detail restaurant avec id " + placeId);

                service.getPlaceWithAllDetails(BuildConfig.MAPS_API_KEY,placeId)
                        .enqueue(new Callback<PlaceDetailsFinal>() {
                            @Override
                            public void onResponse(Call<PlaceDetailsFinal> call, Response<PlaceDetailsFinal> response) {

                                if (response.isSuccessful()){
                                    myRestaurantDetailByPlaceId.setValue(response.body().getResult());

                                    Log.i("[DETAILS]","" + response.body().getResult().getName());
                                }
                            }

                            @Override
                            public void onFailure(Call<PlaceDetailsFinal> call, Throwable t) {
                                myRestaurantDetailByPlaceId.setValue(null);
                            }
                        });


            }




        }


      //  return myRestaurantDetailByPlaceId;
    }

    public void setPlaceId(String id) {
        this.getRestaurantById(id);
    }

    public LiveData<ResultPlaceDetail> getMyRestaurantDetail() {
        return myRestaurantDetailByPlaceId;
    }

    public LiveData<List<RestaurantPojo>> getMyRestaurantsList() {
        return listOfRestaurantWithLongLat;
    }
}
