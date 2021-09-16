package com.hoarauthomas.go4lunchthp7.ui.restaurant;

import android.location.Location;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;
import com.hoarauthomas.go4lunchthp7.Prediction;
import com.hoarauthomas.go4lunchthp7.model.firestore.User;
import com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo;
import com.hoarauthomas.go4lunchthp7.repository.PositionRepository;
import com.hoarauthomas.go4lunchthp7.repository.RestaurantsRepository;
import com.hoarauthomas.go4lunchthp7.repository.SharedRepository;
import com.hoarauthomas.go4lunchthp7.repository.FirestoreDatabaseRepository;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class ViewModelRestaurant extends ViewModel {


    private PositionRepository myPositionRepository;
    private RestaurantsRepository myRestaurantRepository;
    private FirestoreDatabaseRepository myFirestoreDatabaseRepository;

    private LiveData<Location> myPosition;

    private SharedRepository mySharedRepository;


    private final MediatorLiveData<ViewStateRestaurant> myViewStateRestaurantMediator = new MediatorLiveData<>();

    public ViewModelRestaurant(PositionRepository myPositionRepository, RestaurantsRepository myRestaurantRepository, FirestoreDatabaseRepository myFirestoreDatabaseRepository, SharedRepository mySharedRepository) {
        this.myPositionRepository = myPositionRepository;
        this.myRestaurantRepository = myRestaurantRepository;
        this.myFirestoreDatabaseRepository = myFirestoreDatabaseRepository;
        this.mySharedRepository = mySharedRepository;

        myPosition = myPositionRepository.getLocationLiveData();
        LiveData<List<com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo>> myRestaurantsList = this.myRestaurantRepository.getMyRestaurantsList();
        LiveData<List<User>> myWorkMatesList = this.myFirestoreDatabaseRepository.getAllWorkMatesList();


        myViewStateRestaurantMediator.addSource(myPosition, new Observer<Location>() {
            @Override
            public void onChanged(Location position) {
                Log.i("[LIST]", "Event position");
                if (position != null) {
                    Log.i("[LIST]", "Position trouvée" + position.getLatitude() + position.getLongitude());
                    myRestaurantRepository.UpdateLngLat(position.getLongitude(), position.getLatitude());
                }
                logicWork(myRestaurantsList.getValue(), myWorkMatesList.getValue(), position);
            }
        });

        myViewStateRestaurantMediator.addSource(myRestaurantsList, new Observer<List<com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo>>() {
            @Override
            public void onChanged(List<com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo> restaurantPojos) {
                Log.i("LIST]", "Event restaurants");
                if (restaurantPojos != null) {
                    Log.i("[LIST]", "Liste restaura" + restaurantPojos.size());
                    logicWork(restaurantPojos, myWorkMatesList.getValue(), myPosition.getValue());
                }

            }
        });

        myViewStateRestaurantMediator.addSource(myWorkMatesList, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                Log.i("[LIST]", "Event workmates list...");
                if (users != null) {
                    Log.i("[LIST]", "Liste workmates" + users.size());
                    logicWork(myRestaurantsList.getValue(), users, myPosition.getValue());
                }

            }
        });

    }

    private void logicWork(List<com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo> restaurants, List<User> workMates, @Nullable Location myPosition) {


        if (restaurants != null && myPosition != null && workMates != null && !restaurants.isEmpty() && !workMates.isEmpty()) {

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
