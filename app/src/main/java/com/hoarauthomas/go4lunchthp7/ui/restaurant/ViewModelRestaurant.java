package com.hoarauthomas.go4lunchthp7.ui.restaurant;

import android.location.Location;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
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

    //repositories here ...
    //private final PositionRepository myPositionRepository;
    //private final RestaurantsRepository myRestaurantRepository;
    //private final FirestoreRepository myFirestoreRepository;
    //private final SharedRepository mySharedRepository;
    //private final PlaceAutocompleteRepository myPlaceAutocompleteRepository;

    //mediatorlivedata for UI update
    private final MediatorLiveData<ViewStateRestaurant> myViewStateRestaurantMediator = new MediatorLiveData<>();

    // viewmodel constructor
    public ViewModelRestaurant(PositionRepository myPositionRepository,
                               RestaurantsRepository myRestaurantRepository,
                               FirestoreRepository myFirestoreRepository,
                               SharedRepository mySharedRepository,
                               PlaceAutocompleteRepository placeAutocompleteRepository) {

        //PositionRepository myPositionRepository;
        //this.myRestaurantRepository = myRestaurantRepository;
        //this.myFirestoreRepository = myFirestoreRepository;
        //his.mySharedRepository = mySharedRepository;
        //this.myPlaceAutocompleteRepository = placeAutocompleteRepository;

        //get data from repositories
        LiveData<Location> myPosition = myPositionRepository.getLocationLiveData();
        LiveData<List<RestaurantPojo>> myRestaurantsList = myRestaurantRepository.getMyRestaurantsList();
        LiveData<List<FirestoreUser>> myWorkMatesList = myFirestoreRepository.getFirestoreWorkmates();
        LiveData<PlaceAutocomplete> myPlacesId = placeAutocompleteRepository.getPlaces();
        LiveData<Boolean> reloadMap = mySharedRepository.getReload();

        //source
        myViewStateRestaurantMediator.addSource(reloadMap, aBoolean -> {
            if (aBoolean == null) return;
            logicWork(myRestaurantsList.getValue(), myWorkMatesList.getValue(), myPosition.getValue(), myPlacesId.getValue(), aBoolean);
        });

        //source
        myViewStateRestaurantMediator.addSource(myPlacesId, placeAutocomplete -> {
            if (placeAutocomplete == null) return;
            logicWork(myRestaurantsList.getValue(), myWorkMatesList.getValue(), myPosition.getValue(), placeAutocomplete, reloadMap.getValue());
        });

        //source
        myViewStateRestaurantMediator.addSource(myPosition, position -> {
            if (position == null) return;
            myRestaurantRepository.setNewLatLngPositionFromGPS(position.getLongitude(), position.getLatitude());
            logicWork(myRestaurantsList.getValue(), myWorkMatesList.getValue(), position, myPlacesId.getValue(), reloadMap.getValue());
        });

        //source
        myViewStateRestaurantMediator.addSource(myRestaurantsList, restaurantPojos -> {
            if (restaurantPojos == null || restaurantPojos.isEmpty()) return;
            logicWork(restaurantPojos, myWorkMatesList.getValue(), myPosition.getValue(), myPlacesId.getValue(), reloadMap.getValue());
        });

        //source
        myViewStateRestaurantMediator.addSource(myWorkMatesList, firestoreUsers -> {
            if (firestoreUsers == null) return;
            logicWork(myRestaurantsList.getValue(), firestoreUsers, myPosition.getValue(), myPlacesId.getValue(), reloadMap.getValue());
        });

    }

    //logic work for data
    private void logicWork(List<RestaurantPojo> restaurants,
                           List<FirestoreUser> workMates,
                           @Nullable Location myPosition,
                           PlaceAutocomplete myPlaceAuto,
                           Boolean refresh) {

        if (restaurants == null || workMates == null || myPosition == null) return;

        if (refresh) {

            List<RestaurantPojo> newRestaurantsList = new ArrayList<>();
            RestaurantPojo myRestaurant;

            //calculer distance
            for (int i = 0; i < restaurants.size(); i++) {

                myRestaurant = new RestaurantPojo();
                myRestaurant = restaurants.get(i);

                //modifier la distance du restaurant dans le pojo
                LatLng restaurantPos = new LatLng(restaurants.get(i).getGeometry().getLocation().getLat(), restaurants.get(i).getGeometry().getLocation().getLng());
                LatLng userPos = new LatLng(myPosition.getLatitude(), myPosition.getLongitude());
                //float getDistance;
                float getDistance = distanceBetween(restaurantPos, userPos);
                int distance = Math.round(getDistance);
                myRestaurant.setMyDistance(Integer.toString(distance));

                int compteur = 0;

                //workmates number for an restaurant
                for (int j = 0; j < workMates.size(); j++) {

                    if (restaurants.get(i).getPlaceId().equals(workMates.get(j).getFavoriteRestaurant())) {
                        compteur = compteur + 1;
                    }
                }

                myRestaurant.setMyNumberOfWorkmates(Integer.toString(compteur));
                newRestaurantsList.add(myRestaurant);
            }

            myViewStateRestaurantMediator.setValue(new ViewStateRestaurant(newRestaurantsList));


        } else {

            if (myPlaceAuto != null) {

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

    private float distanceBetween(LatLng first, LatLng second) {
        float[] distance = new float[1];
        Location.distanceBetween(first.latitude, first.longitude, second.latitude, second.longitude, distance);
        return distance[0];
    }

    public LiveData<ViewStateRestaurant> getRestaurantsViewUI() {
        return myViewStateRestaurantMediator;
    }

}