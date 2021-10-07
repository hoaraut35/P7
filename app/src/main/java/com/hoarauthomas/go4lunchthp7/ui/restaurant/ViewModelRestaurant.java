package com.hoarauthomas.go4lunchthp7.ui.restaurant;

import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
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

public class ViewModelRestaurant extends ViewModel {

    private final MediatorLiveData<ViewStateRestaurant> myViewStateRestaurantMediator = new MediatorLiveData<>();

    public ViewModelRestaurant(
            PositionRepository myPositionRepository,
            RestaurantsRepository myRestaurantRepository,
            FirestoreRepository myFirestoreRepository,
            SharedRepository mySharedRepository,
            PlaceAutocompleteRepository placeAutocompleteRepository) {

        LiveData<LatLng> myPositionLatLng = myPositionRepository.getLocationLatLgnLiveData();
        LiveData<List<RestaurantPojo>> myRestaurantsList = myRestaurantRepository.getMyRestaurantsList();
        LiveData<List<FirestoreUser>> myWorkMatesList = myFirestoreRepository.getFirestoreWorkmates();
        LiveData<PlaceAutocomplete> myPlacesId = placeAutocompleteRepository.getPlaces();
        LiveData<Boolean> reloadMap = mySharedRepository.getReload();

        myViewStateRestaurantMediator.addSource(myPositionLatLng, latLng -> {
            if (latLng == null) return;
            logicWork(myRestaurantsList.getValue(),
                    myWorkMatesList.getValue(),
                    latLng,
                    myPlacesId.getValue(),
                    reloadMap.getValue());
        });

        //source
        myViewStateRestaurantMediator.addSource(reloadMap, aBoolean -> {
            if (aBoolean == null) return;
            logicWork(myRestaurantsList.getValue(),
                    myWorkMatesList.getValue(),
                    myPositionLatLng.getValue(),
                    myPlacesId.getValue(),
                    aBoolean);
        });

        //source
        myViewStateRestaurantMediator.addSource(myPlacesId, placeAutocomplete -> {
            if (placeAutocomplete == null) return;
            logicWork(myRestaurantsList.getValue(),
                    myWorkMatesList.getValue(),
                    //myPosition.getValue(),
                    myPositionLatLng.getValue(),
                    placeAutocomplete,
                    reloadMap.getValue());
        });

        //source
        myViewStateRestaurantMediator.addSource(myRestaurantsList, restaurantPojo -> {
            if (restaurantPojo == null || restaurantPojo.isEmpty()) return;
            ViewModelRestaurant.this.logicWork(
                    restaurantPojo,
                    myWorkMatesList.getValue(),
                    myPositionLatLng.getValue(),
                    myPlacesId.getValue(),
                    reloadMap.getValue());
        });


        //source
        myViewStateRestaurantMediator.addSource(myWorkMatesList, firestoreUsers -> {
            if (firestoreUsers == null) return;
            ViewModelRestaurant.this.logicWork(myRestaurantsList.getValue(),
                    firestoreUsers,
                    //myPosition.getValue(),
                    myPositionLatLng.getValue(),
                    myPlacesId.getValue(),
                    reloadMap.getValue());
        });


    }

    //logic work for data
    private void logicWork(List<RestaurantPojo> restaurants,
                           List<FirestoreUser> workMates,
                           //@Nullable Location myPosition,
                           LatLng myLatLng,
                           PlaceAutocomplete myPlaceAuto,
                           Boolean refresh) {

        //if (restaurants == null || workMates == null || myPosition == null) return;
        if (restaurants == null || workMates == null || myLatLng == null) return;

        //normal mode
        if (refresh) {

            List<RestaurantPojo> newRestaurantsList = new ArrayList<>();
            RestaurantPojo myRestaurant;

            //get distance
            for (int i = 0; i < restaurants.size(); i++) {

                //myRestaurant = new RestaurantPojo();
                myRestaurant = restaurants.get(i);

                //modifier la distance du restaurant dans le pojo
                LatLng restaurantPos = new LatLng(restaurants.get(i).getGeometry().getLocation().getLat(), restaurants.get(i).getGeometry().getLocation().getLng());
                //LatLng userPos = new LatLng(myPosition.getLatitude(), myPosition.getLongitude());
                LatLng userPos = myLatLng;
                //float getDistance;
                float getDistance = distanceBetween(restaurantPos, userPos);
                int distance = Math.round(getDistance);
                myRestaurant.setMyDistance(Integer.toString(distance));

                int count = 0;

                //workmates number for an restaurant
                for (int j = 0; j < workMates.size(); j++) {

                    if (restaurants.get(i).getPlaceId().equals(workMates.get(j).getFavoriteRestaurant())) {
                        count = count + 1;
                    }
                }

                myRestaurant.setMyNumberOfWorkmates(Integer.toString(count));
                newRestaurantsList.add(myRestaurant);
            }

            myViewStateRestaurantMediator.setValue(new ViewStateRestaurant(newRestaurantsList));

        } else {

            //autocomplete mode
            if (myPlaceAuto != null) {

                List<RestaurantPojo> newPredictionRestaurantList = new ArrayList<>();

                for (RestaurantPojo myRestaurant : restaurants) {
                    //if restaurant = prediction then we work
                    for (Prediction myPrediction : myPlaceAuto.getPredictions()) {
                        if (myRestaurant.getPlaceId().equals(myPrediction.getPlaceId())) {
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