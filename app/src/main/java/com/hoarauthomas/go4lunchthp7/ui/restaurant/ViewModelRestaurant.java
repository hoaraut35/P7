package com.hoarauthomas.go4lunchthp7.ui.restaurant;

import android.location.Location;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;
import com.hoarauthomas.go4lunchthp7.Prediction;
import com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo;
import com.hoarauthomas.go4lunchthp7.repository.FirestoreRepository;
import com.hoarauthomas.go4lunchthp7.model.FirestoreUser;
import com.hoarauthomas.go4lunchthp7.repository.PositionRepository;
import com.hoarauthomas.go4lunchthp7.repository.RestaurantsRepository;
import com.hoarauthomas.go4lunchthp7.ui.SharedViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class ViewModelRestaurant extends ViewModel {

    private PositionRepository myPositionRepository;
    private RestaurantsRepository myRestaurantRepository;
    private FirestoreRepository myFirestoreRepository;
    private LiveData<Location> myPosition;
    private SharedViewModel mySharedRepository;
    private final MediatorLiveData<ViewStateRestaurant> myViewStateRestaurantMediator = new MediatorLiveData<>();

    public ViewModelRestaurant(PositionRepository myPositionRepository, RestaurantsRepository myRestaurantRepository, FirestoreRepository myFirestoreRepository, SharedViewModel mySharedRepository) {

        this.myPositionRepository = myPositionRepository;
        this.myRestaurantRepository = myRestaurantRepository;
        this.myFirestoreRepository = myFirestoreRepository;
        this.mySharedRepository = mySharedRepository;
        myPosition = myPositionRepository.getLocationLiveData();
        LiveData<List<com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo>> myRestaurantsList = this.myRestaurantRepository.getMyRestaurantsList();
        LiveData<List<FirestoreUser>> myWorkMatesList = this.myFirestoreRepository.getFirestoreWorkmates();

        myViewStateRestaurantMediator.addSource(myPosition, position -> {
            if (position == null) return;
            myRestaurantRepository.UpdateLngLat(position.getLongitude(), position.getLatitude());
            logicWork(myRestaurantsList.getValue(), myWorkMatesList.getValue(), position);
        });

        myViewStateRestaurantMediator.addSource(myRestaurantsList, restaurantPojos -> {
            if (restaurantPojos == null || restaurantPojos.isEmpty()) return;
            logicWork(restaurantPojos, myWorkMatesList.getValue(), myPosition.getValue());
        });

        myViewStateRestaurantMediator.addSource(myWorkMatesList, firestoreUsers -> {
            if (firestoreUsers == null) return;
            logicWork(myRestaurantsList.getValue(), firestoreUsers, myPosition.getValue());
        });

    }

    private void logicWork(List<com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo> restaurants, List<FirestoreUser> workMates, @Nullable Location myPosition) {

        if (restaurants == null || workMates == null || myPosition == null) return;

        List<com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo> newList = new ArrayList<>();
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