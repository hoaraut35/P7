package com.hoarauthomas.go4lunchthp7.repository;


import android.util.Log;
import android.util.LruCache;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bumptech.glide.util.ExceptionPassthroughInputStream;
import com.google.android.gms.maps.model.LatLng;
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

public class RestaurantsRepository {

    private final GooglePlaceApi service;

    private  List<RestaurantPojo> allRestaurants = new ArrayList<>();

    public MutableLiveData<ResultPlaceDetail> myRestaurantDetailByPlaceId = new MutableLiveData<>();

    private final MutableLiveData<List<RestaurantPojo>> listOfRestaurantWithLongLat = new MutableLiveData<>(null);
    private final MutableLiveData<PlaceDetailsFinal> restauDetailObj = new MutableLiveData<>();
    private final MutableLiveData<PlaceDetailsFinal> restauDetailObj2 = new MutableLiveData<>();
    private final MutableLiveData<String> placeId = new MutableLiveData<String>();
    private final MutableLiveData<ResultPlaceDetail> myTest = new MutableLiveData<>();

    private final LruCache<String, PlaceDetailsFinal> myCache = new LruCache<>(2_000);

    public RestaurantsRepository() {
        service = RetrofitRequest.getRetrofitInstance().create(GooglePlaceApi.class);
    }

    public void setNewLatLngPositionFromGPS2(LatLng myLatLng){
        listOfRestaurantWithLongLat.setValue(getAllRestaurants(myLatLng.longitude,myLatLng.latitude).getValue());
    }

    public PlaceDetailsFinal getPlaceDetail(String placeId){

        //Call<PlaceDetailsFinal> myCallAPI = service.getPlaceWithAllDetails2(BuildConfig.MAPS_API_KEY,placeId);
        Call<PlaceDetailsFinal> myCallAPI = service.getPlaceWithAllDetails(BuildConfig.MAPS_API_KEY,placeId);

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

    public LiveData<List<RestaurantPojo>> getAllRestaurants(@Nullable Double Long, @Nullable Double Lat) {

        String myPositionStr = Lat + "," + Long;
        Log.i("[MAP]", "[REPOSITORY RESTAURANT] : Ma position : " + myPositionStr);

        service.getNearbyPlaces(BuildConfig.MAPS_API_KEY, myPositionStr)

                .enqueue(new Callback<Place>() {
                    @Override
                    public void onResponse(Call<Place> call, Response<Place> response) {

                        if (response.body() != null) {

                            allRestaurants.clear();

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
