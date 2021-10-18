package com.hoarauthomas.go4lunchthp7.ui.restaurant;

import android.location.Location;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;
import com.hoarauthomas.go4lunchthp7.PlaceAutocomplete;
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

public class ViewModelRestaurant extends ViewModel {

    private final RestaurantsRepository myRestaurantRepo;
    private PlaceAutocompleteRepository myPlaceAutocompleteRepository;
    private final MediatorLiveData<ViewStateRestaurant> myViewStateRestaurantMediator = new MediatorLiveData<>();

    public ViewModelRestaurant(
            PositionRepository myPositionRepository,
            RestaurantsRepository myRestaurantRepository,
            FirestoreRepository myFirestoreRepository,
            SharedRepository mySharedRepository,
            PlaceAutocompleteRepository placeAutocompleteRepository) {

        this.myRestaurantRepo = myRestaurantRepository;

        LiveData<LatLng> myPositionLatLng = myPositionRepository.getLocationLatLgnLiveData();
        LiveData<List<RestaurantPojo>> myRestaurantsList = myRestaurantRepository.getMyRestaurantsList();
        LiveData<List<FirestoreUser>> myWorkMatesList = myFirestoreRepository.getFirestoreWorkmates();


        LiveData<List<PlaceDetailsFinal>> myPlacesAutocomplete = placeAutocompleteRepository.getPlacesForAutocomplete();

        LiveData<Boolean> reloadMap = mySharedRepository.getReload();

        myViewStateRestaurantMediator.addSource(myPositionLatLng, latLng -> {
            if (latLng == null) return;
            logicWork(myRestaurantsList.getValue(),
                    myWorkMatesList.getValue(),
                    latLng,
                    myPlacesAutocomplete.getValue(),
                    reloadMap.getValue());
        });

        //source
        myViewStateRestaurantMediator.addSource(reloadMap, aBoolean -> {
            if (aBoolean == null) return;
            logicWork(myRestaurantsList.getValue(),
                    myWorkMatesList.getValue(),
                    myPositionLatLng.getValue(),
                    myPlacesAutocomplete.getValue(),
                    aBoolean);
        });

        //source
        myViewStateRestaurantMediator.addSource(myPlacesAutocomplete, placeAutocomplete -> {
            if (placeAutocomplete == null || placeAutocomplete.isEmpty()) return;

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
                    myPlacesAutocomplete.getValue(),
                    reloadMap.getValue());
        });


        //source
        myViewStateRestaurantMediator.addSource(myWorkMatesList, firestoreUsers -> {
            if (firestoreUsers == null) return;
            ViewModelRestaurant.this.logicWork(myRestaurantsList.getValue(),
                    firestoreUsers,
                    //myPosition.getValue(),
                    myPositionLatLng.getValue(),
                    myPlacesAutocomplete.getValue(),
                    reloadMap.getValue());
        });


    }

    //logic work for data
    private void logicWork(List<RestaurantPojo> restaurants,
                           List<FirestoreUser> workMates,
                           //@Nullable Location myPosition,
                           LatLng myLatLng,
                           List<PlaceDetailsFinal> myPlacesAutocompleteCombine,
                           //PlaceAutocomplete myPlaceAuto,
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

            myViewStateRestaurantMediator.setValue(new ViewStateRestaurant(newRestaurantsList, null));

        } else {

            //autocomplete mode
            if (myPlacesAutocompleteCombine != null && !myPlacesAutocompleteCombine.isEmpty()) {

                Log.i("[AUTOCOMPLETE]", "In VM we have actualy " + myPlacesAutocompleteCombine.size() + " results " );


                List<ResultPlaceDetail> myListForUI = new ArrayList<>();

                ResultPlaceDetail myPlaceDetail;

                for (PlaceDetailsFinal myPlace : myPlacesAutocompleteCombine) {
                    Log.i("[AUTOCOMPLETE]", "Places :" + myPlace.getResult().getName());

                    myPlaceDetail = myPlace.getResult();

                    //modifier la distance du restaurant dans le pojo
                    LatLng restaurantPos = new LatLng(myPlaceDetail.getGeometry().getLocation().getLat(), myPlaceDetail.getGeometry().getLocation().getLng());
                    //LatLng userPos = new LatLng(myPosition.getLatitude(), myPosition.getLongitude());
                    LatLng userPos = myLatLng;
                    float getDistance = distanceBetween(restaurantPos, userPos);
                    int distance = Math.round(getDistance);
                    myPlaceDetail.setMyDistance(Integer.toString(distance));


                    int count = 0;
                    //workmates number for an restaurant
                    for (int j = 0; j < workMates.size(); j++) {

                        if (myPlaceDetail.getPlaceId().equals(workMates.get(j).getFavoriteRestaurant())) {
                            count = count + 1;
                        }
                    }
                    myPlaceDetail.setMyNumberOfWorkmates(Integer.toString(count));

                    myListForUI.add(myPlaceDetail);
                }




                // myPlaceAutocompleteRepository.getAutocompleteDataToPlaceDetailList();

                /*
                ORIGINE
                //we iterate all result from autocomplete
                for (Prediction myPrediction : myPlaceAuto.getPredictions()) {

                    //here we must to get place detail
                   PlaceDetailsFinal myPlaceDetail =  myRestaurantRepo.getPlaceDetail(myPrediction.getPlaceId());
                    Log.i("[AUTOCOMPLETE]", "Predicion name : " + myPrediction.getDescription());

                    for (RestaurantPojo myRestaurant : restaurants) {

                      //  Log.i("[AUTOCOMPLETE]","Restaurant list actual :" + myRestaurant.getName() + myPrediction.getMatchedSubstrings().);


                            if (myRestaurant.getPlaceId().equals(myPrediction.getPlaceId())){

                        //if (myRestaurant.getName().contains(myPrediction.getStructuredFormatting().getMainText())){
                            newPredictionRestaurantList.add(myRestaurant);
                        }

                    }

                }

                 */

                myViewStateRestaurantMediator.setValue(new ViewStateRestaurant(null, myListForUI));
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