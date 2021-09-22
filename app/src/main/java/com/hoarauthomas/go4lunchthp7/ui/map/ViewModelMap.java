package com.hoarauthomas.go4lunchthp7.ui.map;

import android.annotation.SuppressLint;
import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hoarauthomas.go4lunchthp7.Prediction;
import com.hoarauthomas.go4lunchthp7.permissions.PermissionChecker;
import com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo;
import com.hoarauthomas.go4lunchthp7.repository.FirestoreRepository;
import com.hoarauthomas.go4lunchthp7.model.FirestoreUser;
import com.hoarauthomas.go4lunchthp7.repository.PositionRepository;
import com.hoarauthomas.go4lunchthp7.repository.RestaurantsRepository;
import com.hoarauthomas.go4lunchthp7.ui.SharedViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class ViewModelMap extends ViewModel {

    private PermissionChecker myPermission;
    private PositionRepository myPositionRepository;
    private RestaurantsRepository myRestaurantRepository;
    private FirestoreRepository myFirestoreRepository;
    private SharedViewModel mySharedRepository;

    private LiveData<Location> myPosition;
    private final MediatorLiveData<ViewStateMap> myViewStateMapMediator = new MediatorLiveData<>();

    /**
     * constructor called by dependance injection
     *
     * @param myPermission
     * @param myPositionRepository
     * @param myRestaurantsRepository
     * @param myFirestoreRepository
     * @param mySharedRepository
     */
    public ViewModelMap(PermissionChecker myPermission, PositionRepository myPositionRepository, RestaurantsRepository myRestaurantsRepository, FirestoreRepository myFirestoreRepository, SharedViewModel mySharedRepository) {
        //init repository
        this.myPermission = myPermission;
        this.myPositionRepository = myPositionRepository;
        this.myRestaurantRepository = myRestaurantsRepository;
        this.myFirestoreRepository = myFirestoreRepository;
        this.mySharedRepository = mySharedRepository;

        //init position
        myPosition = myPositionRepository.getLocationLiveData();
        //init list of restaurants
        LiveData<List<com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo>> myRestaurantsList = this.myRestaurantRepository.getMyRestaurantsList();
        //init list of workmates
        LiveData<List<FirestoreUser>> myWorkMatesList = this.myFirestoreRepository.getFirestoreWorkmates();

        //add listener for new position
        myViewStateMapMediator.addSource(myPosition, position -> {
            if (position != null) {
                //TODO: rename method to setNewLatLngPosition()
                myRestaurantRepository.UpdateLngLat(position.getLongitude(), position.getLatitude());
            }
        });

        //add listener for list of restaurants
        myViewStateMapMediator.addSource(myRestaurantsList, (List<RestaurantPojo> restaurantsList) -> {
            if (restaurantsList != null) {
                logicWork(myPosition.getValue(),
                        restaurantsList,
                        myWorkMatesList.getValue());
            }
        });

        //add listener for workmates list
        myViewStateMapMediator.addSource(myWorkMatesList, workmates -> {
            if (workmates != null) {
                logicWork(myPosition.getValue(), myRestaurantsList.getValue(), workmates);
            }
        });
    }

    /**
     * This is the logic work
     * @param position
     * @param restaurants
     * @param workmates
     */
    private void logicWork(@Nullable Location position, @Nullable List<RestaurantPojo> restaurants, @Nullable List<FirestoreUser> workmates) {

        //if one of three values is null then we cancel the job
        if (position == null || restaurants == null || workmates == null) return;

        //
        if (!restaurants.isEmpty() && !workmates.isEmpty()) {

            List<RestaurantPojo> newRestaurantList = new ArrayList<>();
            RestaurantPojo newRestaurantItem;

            //for the amount of result restaurants
            for (int i = 0; i < restaurants.size(); i++) {

                newRestaurantItem = restaurants.get(i);

                //check if workmates has already liked a restaurant
                for (int z = 0; z < workmates.size(); z++) {

                    //restaurant already liked
                    if (restaurants.get(i).getPlaceId().equals(workmates.get(z).getFavoriteRestaurant())) {
                        newRestaurantItem.setIcon("vert");
                        break;
                    }
                    //restaurant is not favorite
                    newRestaurantItem.setIcon("rouge");
                }

                newRestaurantList.add(newRestaurantItem);

            }

            //we set the viewstate for ui
            myViewStateMapMediator.setValue(new ViewStateMap(position, newRestaurantList));

        }

    }

    /**
     * check permissio
     */
    @SuppressLint("MissingPermission")
    public void refresh() {
        // No GPS permission
        if (!myPermission.hasLocationPermission()) {
            myPositionRepository.stopLocationRequest();
        } else {
            myPositionRepository.startLocationRequest();
        }
    }

    /**
     * return ViewState for Map fragment
     *
     * @return ViewState mediator
     */
    public LiveData<ViewStateMap> ViewStateForMapUI() {
        return myViewStateMapMediator;
    }

    /**
     * get Prediction from repository
     *
     * @return prediction from repository
     */
    public MutableLiveData<Prediction> getPredictionFromRepository() {
        return mySharedRepository.getMyPlaceIdFromAutocomplete();
    }

}