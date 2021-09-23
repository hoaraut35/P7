package com.hoarauthomas.go4lunchthp7.repository;


import android.util.Log;
import android.util.LruCache;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
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


    /**
     * for detail restaurant publish, must be observe by viewmodel
     */
    public MutableLiveData<ResultPlaceDetail> myRestaurantDetailByPlaceId = new MutableLiveData<>();


    private final MutableLiveData<List<RestaurantPojo>> listOfRestaurantWithLongLat = new MutableLiveData<>();
    private final MutableLiveData<PlaceDetailsFinal> restauDetailObj = new MutableLiveData<>();
    private final MutableLiveData<PlaceDetailsFinal> restauDetailObj2 = new MutableLiveData<>();
    private final MutableLiveData<String> placeId = new MutableLiveData<String>();


    /**
     * trying to use cache with placeDetail API
     */
    private final LruCache<String, PlaceDetailsFinal> myCache = new LruCache<>(2_000);

    //this is the constructor is called by factory...
    public RestaurantsRepository() {
        Log.i("[MAP]", "Repository restaurant starting singleton...");
        service = RetrofitRequest.getRetrofitInstance().create(GooglePlaceApi.class);
    }

    //**********************************************************************************************

    public LiveData<List<RestaurantPojo>> getMyRestaurantsList() {
        return listOfRestaurantWithLongLat;
    }

    //update position of user in repository when a location is find
    public void UpdateLngLat(Double Long, Double Lat) {
        Log.i("[MAP]", "Repository restaurant position " + Lat + Long);
        listOfRestaurantWithLongLat.setValue(getAllRestaurants(Long, Lat).getValue());
    }

    //**********************************************************************************************

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

    //**********************************************************************************************


    /**
     * get the detail for a restaurant with placeId
     * @param placeId
     * @return
     */
    public LiveData<ResultPlaceDetail> getRestaurantById(String placeId) {

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
                        .enqueue(new Callback<ResultPlaceDetail>() {
                                     @Override
                                     public void onResponse(Call<ResultPlaceDetail> call, Response<ResultPlaceDetail> response) {

                                         if (response.isSuccessful()){
                                             myRestaurantDetailByPlaceId.setValue(response.body());
                                         }
                                     }

                                     @Override
                                     public void onFailure(Call<ResultPlaceDetail> call, Throwable t) {
                                        myRestaurantDetailByPlaceId.setValue(null);
                                     }
                                 });


            }


        }
        return myRestaurantDetailByPlaceId;
    }

    /**
     * update place id request
     *
     * @param id
     * @return
     */
    public void setPlaceId(String id) {
        this.getRestaurantById(id);
        //return id;
    }

    public LiveData<ResultPlaceDetail> getMyRestaurantDetail() {
        return myRestaurantDetailByPlaceId;
    }

}
