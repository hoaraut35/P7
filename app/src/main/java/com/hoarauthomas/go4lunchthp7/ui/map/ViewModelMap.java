package com.hoarauthomas.go4lunchthp7.ui.map;

import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo;
import com.hoarauthomas.go4lunchthp7.repository.PositionRepository;
import com.hoarauthomas.go4lunchthp7.repository.RestaurantsRepository;

import java.util.List;

import javax.annotation.Nullable;

public class ViewModelMap extends ViewModel {

    private RestaurantsRepository myRestaurantRepository;

    private final MediatorLiveData<ViewStateMap> myViewStateMapMediator = new MediatorLiveData<>();


    public ViewModelMap(PositionRepository myPositionRepository, RestaurantsRepository myRestaurantsRepository) {

        this.myRestaurantRepository = myRestaurantsRepository;
        LiveData<Location> myPosition = myPositionRepository.getmyPositionFromRepo();
        LiveData<List<com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo>> myRestaurantsList = this.myRestaurantRepository.getMyRestaurantsList();

        myViewStateMapMediator.addSource(myPosition, new Observer<Location>() {
            @Override
            public void onChanged(Location position) {
                logicWork(position, myRestaurantsList.getValue());
            }
        });

        myViewStateMapMediator.addSource(myRestaurantsList, new Observer<List<RestaurantPojo>>() {
            @Override
            public void onChanged(List<RestaurantPojo> restaurantPojos) {
                logicWork(myPosition.getValue(), restaurantPojos);
            }
        });

    }

    private void logicWork(@Nullable Location position, @Nullable List<RestaurantPojo> restaurants) {

        if (position == null) {
            return;
        }

        myRestaurantRepository.getAllRestaurants(position.getLongitude(), position.getLatitude());

        if (position != null && restaurants != null){
            myViewStateMapMediator.setValue(new ViewStateMap(position, restaurants));
        }

    }


}
