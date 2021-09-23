package com.hoarauthomas.go4lunchthp7.ui.map;

import android.annotation.SuppressLint;
import android.location.Location;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
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

    private PermissionChecker myPermission;
    private PositionRepository myPositionRepository;
    private RestaurantsRepository myRestaurantRepository;
    private FirestoreRepository myFirestoreRepository;
    private SharedRepository mySharedRepository;
    private PlaceAutocompleteRepository myPlaceAutocompleteRepository;

    private MutableLiveData<Integer> myZoomLive = new MutableLiveData<>();
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
     * @param placeAutocompleteRepository
     */
    public ViewModelMap(PermissionChecker myPermission, PositionRepository myPositionRepository, RestaurantsRepository myRestaurantsRepository, FirestoreRepository myFirestoreRepository, SharedRepository mySharedRepository, PlaceAutocompleteRepository placeAutocompleteRepository) {
        //init repository
        this.myPermission = myPermission;
        this.myPositionRepository = myPositionRepository;
        this.myRestaurantRepository = myRestaurantsRepository;
        this.myFirestoreRepository = myFirestoreRepository;
        this.mySharedRepository = mySharedRepository;
        this.myPlaceAutocompleteRepository = placeAutocompleteRepository;

        //zoom parameter
        LiveData<Integer> myZoom = this.mySharedRepository.getMyZoom();

        //start position of map
        myPosition = myPositionRepository.getLocationLiveData();

        //start restaurants list
        LiveData<List<com.hoarauthomas.go4lunchthp7.model.NearbySearch.RestaurantPojo>> myRestaurantsList = this.myRestaurantRepository.getMyRestaurantsList();

        //start list of workmates
        LiveData<List<FirestoreUser>> myWorkMatesList = this.myFirestoreRepository.getFirestoreWorkmates();

        //list of prediction place id ???????????????????
        LiveData<List<String>> myListOfPredictionPlaceId = this.mySharedRepository.getMyRestaurantList();


        LiveData<PlaceAutocomplete> myPlacesId = this.myPlaceAutocompleteRepository.getPlaces();


        LiveData<Boolean> reloadMap = this.mySharedRepository.getReload();


        myViewStateMapMediator.addSource(reloadMap, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (reloadMap == null) return;
                logicWork(myPosition.getValue(),
                        myRestaurantsList.getValue(),
                        myWorkMatesList.getValue(),
                        myPlacesId.getValue(),
                        aBoolean);
            }
        });

        myViewStateMapMediator.addSource(myPlacesId, new Observer<PlaceAutocomplete>() {
            @Override
            public void onChanged(PlaceAutocomplete placeAutocomplete) {
                if (placeAutocomplete == null) return;
                logicWork(myPosition.getValue(),
                        myRestaurantsList.getValue(),
                        myWorkMatesList.getValue(),
                        placeAutocomplete,
                        reloadMap.getValue());
            }
        });

        myViewStateMapMediator.addSource(myZoom, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (integer == null) return;
                myZoomLive.setValue(integer);

            }
        });

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







                /*myMarkerPosition = new LatLng(restaurants.get(i).getGeometry().getLocation().getLat(), restaurants.get(i).getGeometry().getLocation().getLng());
                myMarkerOptions = new MarkerOptions();

                myMarkerOptions.position(myMarkerPosition);

                if (restaurants.get(i).getIcon().contains("rouge")) {
                    //myMarkerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    myMarkerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.baseline_unreserved_restaurant_24));
                } else {
                    //    myMarkerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                    myMarkerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.baseline_booked_restaurant_24));
                }


                myMarker = myMap.addMarker(myMarkerOptions);
                myMarker.setTag(restaurants.get(i).getPlaceId());


                allMarkers.add(new MyMarkerObject(restaurants.get(i).getPlaceId(), myMarkerPosition));

                 */


                }

                //we set the viewstate for ui
                myViewStateMapMediator.setValue(new ViewStateMap(position, newRestaurantList));

            }//fin si


        } else {
            //reset autocomplete ?
            // myPlacesId = null;
            //   mySharedRepository.setReloadMap(false);


            if (myPlacesId != null && restaurants != null) {

                List<RestaurantPojo> newPredictionRestaurantList = new ArrayList<>();

                for (RestaurantPojo myRestaurant : restaurants) {
                    for (Prediction myPrediction : myPlacesId.getPredictions()) {
                        if (myRestaurant.getPlaceId().equals(myPrediction.getPlaceId())) {
                            Log.i("[PREDIC]", "new restau to add" + myRestaurant.getName());
                            newPredictionRestaurantList.add(myRestaurant);
                        }
                    }
                }

                myViewStateMapMediator.setValue(new ViewStateMap(position, newPredictionRestaurantList));


            }


        }


    }

    @SuppressLint("MissingPermission")
    public void refresh() {
        // No GPS permission
        if (!myPermission.hasLocationPermission()) {
            myPositionRepository.stopLocationRequest();
        } else {
            myPositionRepository.startLocationRequest();
        }
    }

    public LiveData<ViewStateMap> ViewStateForMapUI() {
        return myViewStateMapMediator;
    }

    public MutableLiveData<Prediction> getPredictionFromRepository() {
        return mySharedRepository.getMyPlaceIdFromAutocomplete();
    }

    public LiveData<List<String>> getMyListPrediction() {
        return mySharedRepository.getMyRestaurantList();
    }

    public LiveData<Integer> getMyZoom() {
        return myZoomLive;
    }

}