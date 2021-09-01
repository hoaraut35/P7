package com.hoarauthomas.go4lunchthp7.ui.map;

import android.annotation.SuppressLint;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.hoarauthomas.go4lunchthp7.model.firestore.User;
import com.hoarauthomas.go4lunchthp7.permissions.PermissionChecker;
import com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo;
import com.hoarauthomas.go4lunchthp7.repository.PositionRepository;
import com.hoarauthomas.go4lunchthp7.repository.RestaurantsRepository;
import com.hoarauthomas.go4lunchthp7.repository.WorkMatesRepository;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class ViewModelMap extends ViewModel {

    @NonNull
    private PermissionChecker myPermission;
    @NonNull
    private PositionRepository myPositionRepository;
    @NonNull
    private RestaurantsRepository myRestaurantRepository;
    private WorkMatesRepository myWorkMatesRepository;

    private LiveData<Location> myPosition;
    private final MediatorLiveData<ViewStateMap> myViewStateMapMediator = new MediatorLiveData<>();

    public ViewModelMap(PermissionChecker myPermission, PositionRepository myPositionRepository, RestaurantsRepository myRestaurantsRepository, WorkMatesRepository myWorkMatesRepository) {
        this.myPermission = myPermission;
        this.myPositionRepository = myPositionRepository;
        this.myRestaurantRepository = myRestaurantsRepository;
        this.myWorkMatesRepository = myWorkMatesRepository;

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

        if (position != null && restaurants != null && workmates != null && !restaurants.isEmpty() && !workmates.isEmpty()) {

            List<RestaurantPojo> newList = new ArrayList<>();
            RestaurantPojo newRestau;

            newList.clear();

            for (int i = 0; i < restaurants.size(); i++) {

                newRestau = restaurants.get(i);

                for (int z = 0; z < workmates.size(); z++) {

                    Log.i("[MAP]","id " + restaurants.get(i).getPlaceId() + " " + workmates.get(z).getFavoriteRestaurant());
                  //  newRestau.setIcon("rouge");

                    if (restaurants.get(i).getPlaceId().equals(workmates.get(z).getFavoriteRestaurant())) {
                        Log.i("[MAP]", "restaurant deja liké " + restaurants.get(i).getName());

                        newRestau.setIcon("vert");
                        break;

                    }
                        //restaurants.set(i,se
                       newRestau.setIcon("rouge");

                }

                    newList.add(newRestau);

            }

            myViewStateMapMediator.setValue(new ViewStateMap(position, newList));

        }

    }
    //**********************************************************************************************
    // End of logic work
    //**********************************************************************************************

    @SuppressLint("MissingPermission")
    public void refresh() {
        // No GPS permission
        if (!myPermission.hasLocationPermission()) {
            myPositionRepository.stopLocationRequest();
        } else {
            myPositionRepository.startLocationRequest();
        }
    }

    public LiveData<ViewStateMap> getMediatorLiveData() {
        return myViewStateMapMediator;
    }

}
