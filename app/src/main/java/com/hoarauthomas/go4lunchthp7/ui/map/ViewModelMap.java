package com.hoarauthomas.go4lunchthp7.ui.map;

import android.annotation.SuppressLint;
import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.hoarauthomas.go4lunchthp7.Prediction;
import com.hoarauthomas.go4lunchthp7.model.firestore.User;
import com.hoarauthomas.go4lunchthp7.permissions.PermissionChecker;
import com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo;
import com.hoarauthomas.go4lunchthp7.repository.PositionRepository;
import com.hoarauthomas.go4lunchthp7.repository.RestaurantsRepository;
import com.hoarauthomas.go4lunchthp7.repository.SharedRepository;
import com.hoarauthomas.go4lunchthp7.repository.WorkMatesRepository;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class ViewModelMap extends ViewModel {

    private PermissionChecker myPermission;
    private PositionRepository myPositionRepository;
    private RestaurantsRepository myRestaurantRepository;
    private WorkMatesRepository myWorkMatesRepository;
    private SharedRepository mySharedRepository;

    private LiveData<Location> myPosition;
    private final MediatorLiveData<ViewStateMap> myViewStateMapMediator = new MediatorLiveData<>();

   // private final MutableLiveData<String> myPositionFromAutoComplete = new MutableLiveData<>();

   // private SingleLiveEvent<Prediction> myPositionFromAutoSingleMode = new SingleLiveEvent<>();

    public ViewModelMap(PermissionChecker myPermission, PositionRepository myPositionRepository, RestaurantsRepository myRestaurantsRepository, WorkMatesRepository myWorkMatesRepository, SharedRepository mySharedRepository) {
        this.myPermission = myPermission;
        this.myPositionRepository = myPositionRepository;
        this.myRestaurantRepository = myRestaurantsRepository;
        this.myWorkMatesRepository = myWorkMatesRepository;
        this.mySharedRepository = mySharedRepository;

        myPosition = myPositionRepository.getLocationLiveData();
        LiveData<List<com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo>> myRestaurantsList = this.myRestaurantRepository.getMyRestaurantsList();
        LiveData<List<User>> myWorkMatesList = this.myWorkMatesRepository.getAllWorkMatesList();

        myViewStateMapMediator.addSource(myPosition, new Observer<Location>() {
            @Override
            public void onChanged(Location position) {
                //   Log.i("[MAP]", "Event position");
                if (position != null) {
                    //   Log.i("[MAP]", "Position trouvée" + position.getLatitude() + position.getLongitude());
                    myRestaurantRepository.UpdateLngLat(position.getLongitude(), position.getLatitude());
                }
                //logicWork(position,myRestaurantsList.getValue());
            }
        });

        myViewStateMapMediator.addSource(myRestaurantsList, new Observer<List<RestaurantPojo>>() {
            @Override
            public void onChanged(List<RestaurantPojo> restaurantPojos) {
                //   Log.i("[MAP]", "Event restaurants");
                if (restaurantPojos != null) {
                    // Log.i("[MAP]", "Liste restaura" + restaurantPojos.size());
                    logicWork(myPosition.getValue(), restaurantPojos, myWorkMatesList.getValue());
                }

            }
        });

        myViewStateMapMediator.addSource(myWorkMatesList, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                //    Log.i("[MAP]", "Event workmates list...");
                if (users != null) {
                    //   Log.i("[MAP]", "Liste workmates" + users.size());
                    logicWork(myPosition.getValue(), myRestaurantsList.getValue(), users);
                }
            }
        });
    }

    //**********************************************************************************************
    // Logic work
    //**********************************************************************************************
    private void logicWork(@Nullable Location position, @Nullable List<RestaurantPojo> restaurants, @Nullable List<User> workmates) {

        if (position == null || restaurants == null || workmates == null) return;

        if (!restaurants.isEmpty() && !workmates.isEmpty()) {

            List<RestaurantPojo> newRestaurantList = new ArrayList<>();
            RestaurantPojo newRestaurantItem;

            for (int i = 0; i < restaurants.size(); i++) {

                newRestaurantItem = restaurants.get(i);

                //check if workmates has already liked a restaurant
                for (int z = 0; z < workmates.size(); z++) {

                    //restaurant already liked
                    if (restaurants.get(i).getPlaceId().equals(workmates.get(z).getFavoriteRestaurant())) {
                        newRestaurantItem.setIcon("vert");
                        break;
                    }
                    //restaurants.set(i,se
                    newRestaurantItem.setIcon("rouge");

                }

                newRestaurantList.add(newRestaurantItem);

            }

            myViewStateMapMediator.setValue(new ViewStateMap(position, newRestaurantList));

        }

    }
    //**********************************************************************************************
    // End of logic work
    //**********************************************************************************************

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
     * @return ViewState mediator
     */
    public LiveData<ViewStateMap> ViewStateForMapUI()
    {
        return myViewStateMapMediator;
    }

    /**
     * get Prediction from repository
     * @return prediction from repository
     */
    public MutableLiveData<Prediction> getPredictionFromRepository() {
        return mySharedRepository.getMyPlaceIdFromAutocomplete();
    }

}