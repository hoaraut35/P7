package com.hoarauthomas.go4lunchthp7.ui.map;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;
import com.hoarauthomas.go4lunchthp7.PlaceAutocomplete;
import com.hoarauthomas.go4lunchthp7.Prediction;
import com.hoarauthomas.go4lunchthp7.model.FirestoreUser;
import com.hoarauthomas.go4lunchthp7.model.NearbySearch.RestaurantPojo;
import com.hoarauthomas.go4lunchthp7.model.PlaceDetails.PlaceDetailsFinal;
import com.hoarauthomas.go4lunchthp7.model.PlaceDetails.ResultPlaceDetail;
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
    private final MutableLiveData<Integer> myZoomLive = new MutableLiveData<>();

    private final MediatorLiveData<ViewStateMap> myViewStateMapMediator = new MediatorLiveData<>();

    public ViewModelMap(
                        PositionRepository myPositionRepository,
                        RestaurantsRepository myRestaurantsRepository,
                        FirestoreRepository myFirestoreRepository,
                        SharedRepository mySharedRepository,
                        PlaceAutocompleteRepository placeAutocompleteRepository) {

        //init repository
        this.myRestaurantRepository = myRestaurantsRepository;

        //zoom parameter
        LiveData<Integer> myZoom = mySharedRepository.getMyZoom();

        //start position of map
        LiveData<LatLng> myPositionLatLng = myPositionRepository.getLocationLatLgnLiveData();

        //start restaurants list
        LiveData<List<com.hoarauthomas.go4lunchthp7.model.NearbySearch.RestaurantPojo>> myRestaurantsList = this.myRestaurantRepository.getMyRestaurantsList();

        //start list of workmates
        LiveData<List<FirestoreUser>> myWorkMatesList = myFirestoreRepository.getFirestoreWorkmates();


        LiveData<List<PlaceDetailsFinal>> myResultPlacesAutocomplete = placeAutocompleteRepository.getPlacesForAutocomplete();
        //origin
        //LiveData<PlaceAutocomplete> myPlacesId = placeAutocompleteRepository.getPlaces();


        LiveData<Boolean> reloadMap = mySharedRepository.getReload();

        myViewStateMapMediator.addSource(reloadMap, aBoolean -> {
            if (reloadMap == null) return;
            logicWork(myPositionLatLng.getValue(),
                    myRestaurantsList.getValue(),
                    myWorkMatesList.getValue(),
                    myResultPlacesAutocomplete.getValue(),
                    aBoolean);
        });

        myViewStateMapMediator.addSource(myResultPlacesAutocomplete, placeAutocomplete -> {
            if (placeAutocomplete == null) return;
            logicWork(myPositionLatLng.getValue(),
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
        myViewStateMapMediator.addSource(myPositionLatLng, latLng -> {
            if (latLng == null) return;
            //myRestaurantRepository.setNewLatLngPositionFromGPS(latLng.longitude, latLng.latitude);
            myRestaurantRepository.setNewLatLngPositionFromGPS2(latLng);
        });

        //add listener for list of restaurants
        myViewStateMapMediator.addSource(myRestaurantsList, (List<RestaurantPojo> restaurantsList) -> {
            if (restaurantsList != null) {
                logicWork(myPositionLatLng.getValue(),
                        restaurantsList,
                        myWorkMatesList.getValue(),
                        myResultPlacesAutocomplete.getValue(),
                        reloadMap.getValue());
            }
        });

        //add listener for workmates list
        myViewStateMapMediator.addSource(myWorkMatesList, workmates -> {
            if (workmates != null) {
                logicWork(myPositionLatLng.getValue(),
                        myRestaurantsList.getValue(),
                        workmates,
                        myResultPlacesAutocomplete.getValue(),
                        reloadMap.getValue());
            }
        });
    }

    private void logicWork(
            //@Nullable Location position,
            LatLng position,
            @Nullable List<RestaurantPojo> restaurants,
            @Nullable List<FirestoreUser> workmates,
            @Nullable List<PlaceDetailsFinal> myPlacesAutocomplete,
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
                myViewStateMapMediator.setValue(new ViewStateMap(position, newRestaurantList, null));

            }//fin si

        } else {

            //mode autocomplete
            if (myPlacesAutocomplete != null && !myPlacesAutocomplete.isEmpty()) {


                Log.i("[AUTOCOMPLETE]", "In VM we have actualy " + myPlacesAutocomplete.size() + " results " );


                List<ResultPlaceDetail> myListForUI = new ArrayList<>();
                ResultPlaceDetail myPlaceDetail;

                for (PlaceDetailsFinal myPlace : myPlacesAutocomplete) {
                    Log.i("[AUTOCOMPLETE]", "Places :" + myPlace.getResult().getName());

                    myPlaceDetail = myPlace.getResult();

                    for (int z = 0; z < workmates.size(); z++) {

                        //restaurant already liked
                        if (myPlace.getResult().getPlaceId().equals(workmates.get(z).getFavoriteRestaurant())) {
                            myPlaceDetail.setIcon("vert");
                            break;
                        }
                        //restaurant is not favorite
                        myPlaceDetail.setIcon("rouge");
                    }



                    myListForUI.add(myPlaceDetail);
                }

               myViewStateMapMediator.setValue(new ViewStateMap(position,null, myListForUI));

            }
        }
    }

    public LiveData<ViewStateMap> getViewStateForMapUI() {
        return myViewStateMapMediator;
    }

    public LiveData<Integer> getMyZoom() {
        return myZoomLive;
    }

}