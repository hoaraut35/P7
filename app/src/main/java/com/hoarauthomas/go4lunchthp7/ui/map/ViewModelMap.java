package com.hoarauthomas.go4lunchthp7.ui.map;

import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hoarauthomas.go4lunchthp7.PlaceAutocomplete;
import com.hoarauthomas.go4lunchthp7.Prediction;
import com.hoarauthomas.go4lunchthp7.model.FirestoreUser;
import com.hoarauthomas.go4lunchthp7.model.NearbySearch.RestaurantPojo;
import com.hoarauthomas.go4lunchthp7.permissions.PermissionChecker;
import com.hoarauthomas.go4lunchthp7.repository.FirestoreRepository;
import com.hoarauthomas.go4lunchthp7.repository.PlaceAutocompleteRepository;
import com.hoarauthomas.go4lunchthp7.repository.PositionRepository;
import com.hoarauthomas.go4lunchthp7.repository.RestaurantsRepository;
import com.hoarauthomas.go4lunchthp7.repository.SharedRepository;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class ViewModelMap extends ViewModel {

    private final RestaurantsRepository myRestaurantRepository;
    private final SharedRepository mySharedRepository;

    private final MutableLiveData<Integer> myZoomLive = new MutableLiveData<>();
    private final LiveData<Location> myPosition;

    private final MediatorLiveData<ViewStateMap> myViewStateMapMediator = new MediatorLiveData<>();

    public ViewModelMap(PermissionChecker myPermission,
                        PositionRepository myPositionRepository,
                        RestaurantsRepository myRestaurantsRepository,
                        FirestoreRepository myFirestoreRepository,
                        SharedRepository mySharedRepository,
                        PlaceAutocompleteRepository placeAutocompleteRepository) {
        //init repository
        this.myRestaurantRepository = myRestaurantsRepository;
        this.mySharedRepository = mySharedRepository;

        //zoom parameter
        LiveData<Integer> myZoom = this.mySharedRepository.getMyZoom();

        //start position of map
        myPosition = myPositionRepository.getLocationLiveData();

        //start restaurants list
        LiveData<List<com.hoarauthomas.go4lunchthp7.model.NearbySearch.RestaurantPojo>> myRestaurantsList = this.myRestaurantRepository.getMyRestaurantsList();

        //start list of workmates
        LiveData<List<FirestoreUser>> myWorkMatesList = myFirestoreRepository.getFirestoreWorkmates();

        //list of prediction place id ???????????????????
        //LiveData<List<String>> myListOfPredictionPlaceId = this.mySharedRepository.getMyRestaurantList();
        LiveData<PlaceAutocomplete> myPlacesId = placeAutocompleteRepository.getPlaces();
        LiveData<Boolean> reloadMap = this.mySharedRepository.getReload();

        myViewStateMapMediator.addSource(reloadMap, aBoolean -> {
            if (reloadMap == null) return;
            logicWork(myPosition.getValue(),
                    myRestaurantsList.getValue(),
                    myWorkMatesList.getValue(),
                    myPlacesId.getValue(),
                    aBoolean);
        });

        myViewStateMapMediator.addSource(myPlacesId, placeAutocomplete -> {
            if (placeAutocomplete == null) return;
            logicWork(myPosition.getValue(),
                    myRestaurantsList.getValue(),
                    myWorkMatesList.getValue(),
                    placeAutocomplete,
                    reloadMap.getValue());
        });

        myViewStateMapMediator.addSource(myZoom, integer -> {
            if (integer == null) return;
            myZoomLive.setValue(integer);
        });

        //add listener for new position
        myViewStateMapMediator.addSource(myPosition, position -> {
            if (position == null) return;

               myRestaurantRepository.setNewLatLngPositionFromGPS(position.getLongitude(), position.getLatitude());

        });

        //add listener for list of restaurants
        myViewStateMapMediator.addSource(myRestaurantsList, (List<RestaurantPojo> restaurantsList) -> {
            if (restaurantsList != null) {
                logicWork(myPosition.getValue(),
                        restaurantsList,
                        myWorkMatesList.getValue(),
                        myPlacesId.getValue(),
                        reloadMap.getValue());
            }
        });

        //add listener for workmates list
        myViewStateMapMediator.addSource(myWorkMatesList, workmates -> {
            if (workmates != null) {
                logicWork(myPosition.getValue(),
                        myRestaurantsList.getValue(),
                        workmates,
                        myPlacesId.getValue(),
                        reloadMap.getValue());
            }
        });
    }


    private void logicWork(@Nullable Location position,
                           @Nullable List<RestaurantPojo> restaurants,
                           @Nullable List<FirestoreUser> workmates,
                           @Nullable PlaceAutocomplete myPlacesId,
                           Boolean reload) {

        if (position == null || restaurants == null || workmates == null) return;


        if (reload) {

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

                //we set the ViewState for ui
                myViewStateMapMediator.setValue(new ViewStateMap(position, newRestaurantList));

            }//fin si

        } else {

            //mode autocomplete
            if (myPlacesId != null) {

                List<RestaurantPojo> newPredictionRestaurantList = new ArrayList<>();

                for (RestaurantPojo myRestaurant : restaurants) {
                    for (Prediction myPrediction : myPlacesId.getPredictions()) {
                        if (myRestaurant.getPlaceId().equals(myPrediction.getPlaceId())) {
                            newPredictionRestaurantList.add(myRestaurant);
                        }
                    }
                }

                myViewStateMapMediator.setValue(new ViewStateMap(position, newPredictionRestaurantList));

            }
        }
    }

    public LiveData<ViewStateMap> ViewStateForMapUI() {
        return myViewStateMapMediator;
    }

    public LiveData<Integer> getMyZoom() {
        return myZoomLive;
    }

}