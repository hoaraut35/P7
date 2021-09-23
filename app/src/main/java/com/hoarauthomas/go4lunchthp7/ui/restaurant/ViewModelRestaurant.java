package com.hoarauthomas.go4lunchthp7.ui.restaurant;

import android.location.Location;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;
import com.hoarauthomas.go4lunchthp7.PlaceAutocomplete;
import com.hoarauthomas.go4lunchthp7.Prediction;
import com.hoarauthomas.go4lunchthp7.model.FirestoreUser;
import com.hoarauthomas.go4lunchthp7.model.NearbySearch.RestaurantPojo;
import com.hoarauthomas.go4lunchthp7.repository.FirestoreRepository;
import com.hoarauthomas.go4lunchthp7.repository.PlaceAutocompleteRepository;
import com.hoarauthomas.go4lunchthp7.repository.PositionRepository;
import com.hoarauthomas.go4lunchthp7.repository.RestaurantsRepository;
import com.hoarauthomas.go4lunchthp7.repository.SharedRepository;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class ViewModelRestaurant extends ViewModel {

    private PositionRepository myPositionRepository;
    private RestaurantsRepository myRestaurantRepository;
    private FirestoreRepository myFirestoreRepository;
    private LiveData<Location> myPosition;
    private SharedRepository mySharedRepository;
    private PlaceAutocompleteRepository myPlaceAutocompleteRepository;


    private final MediatorLiveData<ViewStateRestaurant> myViewStateRestaurantMediator = new MediatorLiveData<>();

    public ViewModelRestaurant(PositionRepository myPositionRepository, RestaurantsRepository myRestaurantRepository, FirestoreRepository myFirestoreRepository, SharedRepository mySharedRepository, PlaceAutocompleteRepository placeAutocompleteRepository) {

        this.myPositionRepository = myPositionRepository;
        this.myRestaurantRepository = myRestaurantRepository;
        this.myFirestoreRepository = myFirestoreRepository;
        this.mySharedRepository = mySharedRepository;
        this.myPlaceAutocompleteRepository = placeAutocompleteRepository;

        myPosition = myPositionRepository.getLocationLiveData();
        LiveData<List<com.hoarauthomas.go4lunchthp7.model.NearbySearch.RestaurantPojo>> myRestaurantsList = this.myRestaurantRepository.getMyRestaurantsList();
        LiveData<List<FirestoreUser>> myWorkMatesList = this.myFirestoreRepository.getFirestoreWorkmates();


        //for autocomplete
        LiveData<PlaceAutocomplete> myPlacesId = this.myPlaceAutocompleteRepository.getPlaces();
        LiveData<Boolean> reloadMap = this.mySharedRepository.getReload();


        myViewStateRestaurantMediator.addSource(reloadMap, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (reloadMap == null) return;
                logicWork(myRestaurantsList.getValue(), myWorkMatesList.getValue(), myPosition.getValue(), myPlacesId.getValue(), aBoolean);
            }
        });

        myViewStateRestaurantMediator.addSource(myPlacesId, new Observer<PlaceAutocomplete>() {
            @Override
            public void onChanged(PlaceAutocomplete placeAutocomplete) {
                if (myPlacesId == null) return;
                logicWork(myRestaurantsList.getValue(), myWorkMatesList.getValue(), myPosition.getValue(), placeAutocomplete, reloadMap.getValue());
            }
        });


        myViewStateRestaurantMediator.addSource(myPosition, position -> {
            if (position == null) return;
            myRestaurantRepository.UpdateLngLat(position.getLongitude(), position.getLatitude());
            logicWork(myRestaurantsList.getValue(), myWorkMatesList.getValue(), position, myPlacesId.getValue(), reloadMap.getValue());
        });

        myViewStateRestaurantMediator.addSource(myRestaurantsList, restaurantPojos -> {
            if (restaurantPojos == null || restaurantPojos.isEmpty()) return;
            logicWork(restaurantPojos, myWorkMatesList.getValue(), myPosition.getValue(), myPlacesId.getValue(), reloadMap.getValue());
        });

        myViewStateRestaurantMediator.addSource(myWorkMatesList, firestoreUsers -> {
            if (firestoreUsers == null) return;
            logicWork(myRestaurantsList.getValue(), firestoreUsers, myPosition.getValue(), myPlacesId.getValue(), reloadMap.getValue());
        });

    }

    private void logicWork(List<com.hoarauthomas.go4lunchthp7.model.NearbySearch.RestaurantPojo> restaurants,
                           List<FirestoreUser> workMates,
                           @Nullable Location myPosition,
                           PlaceAutocomplete myPlaceAuto,
                           Boolean refresh) {

        if (restaurants == null || workMates == null || myPosition == null) return;


        if (refresh) {


            List<RestaurantPojo> newList = new ArrayList<>();
            RestaurantPojo myRestau;


            //calculer distance
            for (int i = 0; i < restaurants.size(); i++) {


                myRestau = new RestaurantPojo();
                myRestau = restaurants.get(i);


                //modifier la distance du restaurant dans le pojo
                LatLng restauPos = new LatLng(restaurants.get(i).getGeometry().getLocation().getLat(), restaurants.get(i).getGeometry().getLocation().getLng());
                LatLng userPos = new LatLng(myPosition.getLatitude(), myPosition.getLongitude());

                float getDistance;
                getDistance = distanceBetween(restauPos, userPos);

                int distance = Math.round(getDistance);

                myRestau.setMyDistance(Integer.toString(distance));

                int compteur = 0;

                //workmates number for an restaurant
                for (int j = 0; j < workMates.size(); j++) {

                    if (restaurants.get(i).getPlaceId().equals(workMates.get(j).getFavoriteRestaurant())) {
                        Log.i("[compteur]", "un collegue sur le restaur " + workMates.get(j).getUsername() + " " + restaurants.get(i).getName());

                        compteur = compteur + 1;
                    } else {
                        Log.i("[compteur]", "compteur = " + restaurants.get(i).getPlaceId() + " " + workMates.get(j).getFavoriteRestaurant());
                    }
                }


                myRestau.setMyNumberOfWorkmates(Integer.toString(compteur));

                newList.add(myRestau);
            }

            myViewStateRestaurantMediator.setValue(new ViewStateRestaurant(newList));


        } else {
            if (myPlaceAuto != null && restaurants != null) {


                List<RestaurantPojo> newPredictionRestaurantList = new ArrayList<>();

                for (RestaurantPojo myRestaurant : restaurants) {

                    //si restauran = prediction on traite
                    for (Prediction myPrediction : myPlaceAuto.getPredictions()) {

                        if (myRestaurant.getPlaceId().equals(myPrediction.getPlaceId())) {

                            Log.i("[PREDIC]", "new restau to add" + myRestaurant.getName());
                            newPredictionRestaurantList.add(myRestaurant);


                        }
                    }


                }

                myViewStateRestaurantMediator.setValue(new ViewStateRestaurant(newPredictionRestaurantList));


            }


        }


    }

    public float distanceBetween(LatLng first, LatLng second) {
        float[] distance = new float[1];
        Location.distanceBetween(first.latitude, first.longitude, second.latitude, second.longitude, distance);
        return distance[0];
    }

    public LiveData<ViewStateRestaurant> getMediatorLiveData() {
        return myViewStateRestaurantMediator;
    }

    public MutableLiveData<Prediction> getPredictionFromVM() {
        return mySharedRepository.getMyPlaceIdFromAutocomplete();
    }
}