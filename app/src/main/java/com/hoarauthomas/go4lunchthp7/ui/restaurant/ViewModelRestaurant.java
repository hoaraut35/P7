package com.hoarauthomas.go4lunchthp7.ui.restaurant;

import android.location.Location;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.hoarauthomas.go4lunchthp7.model.firestore.User;
import com.hoarauthomas.go4lunchthp7.repository.PositionRepository;
import com.hoarauthomas.go4lunchthp7.repository.RestaurantsRepository;
import com.hoarauthomas.go4lunchthp7.repository.WorkMatesRepository;
import com.hoarauthomas.go4lunchthp7.ui.map.ViewStateMap;

import java.util.List;

public class ViewModelRestaurant extends ViewModel {


    private PositionRepository myPositionRepository;
    private RestaurantsRepository myRestaurantRepository;
    private WorkMatesRepository myWorkMatesRepository;

    private LiveData<Location> myPosition;

    private final MediatorLiveData<ViewStateRestaurant> myViewStateRestaurantMediator = new MediatorLiveData<>();

    public ViewModelRestaurant(PositionRepository myPositionRepository, RestaurantsRepository myRestaurantRepository, WorkMatesRepository myWorkMatesRepository) {
        this.myPositionRepository = myPositionRepository;
        this.myRestaurantRepository = myRestaurantRepository;
        this.myWorkMatesRepository = myWorkMatesRepository;

        myPosition = myPositionRepository.getLocationLiveData();
        LiveData<List<com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo>> myRestaurantsList = this.myRestaurantRepository.getMyRestaurantsList();
        LiveData<List<User>> myWorkMatesList = this.myWorkMatesRepository.getAllWorkMates();


        myViewStateRestaurantMediator.addSource(myPosition, new Observer<Location>() {
            @Override
            public void onChanged(Location position) {
                Log.i("[LIST]", "Event position");
                if (position != null) {
                    Log.i("[LIST]", "Position trouvée" + position.getLatitude() + position.getLongitude());
                    myRestaurantRepository.UpdateLngLat(position.getLongitude(), position.getLatitude());
                }
                //logicWork(position,myRestaurantsList.getValue());
            }
        });

        myViewStateRestaurantMediator.addSource(myRestaurantsList, new Observer<List<com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo>>() {
            @Override
            public void onChanged(List<com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo> restaurantPojos) {
                Log.i("LIST]", "Event restaurants");
                if (restaurantPojos != null) {
                    Log.i("[LIST]", "Liste restaura" + restaurantPojos.size());
                        logicWork( restaurantPojos);
                }

            }
        });

        myViewStateRestaurantMediator.addSource(myWorkMatesList, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                Log.i("[LIST]", "Event workmates list...");
                if (users != null) {
                    Log.i("[LIST]", "Liste workmates" + users.size());
                    //   logicWork(myPosition.getValue(), myRestaurantsList.getValue(), users);
                }

            }
        });

    }

    private void logicWork(List<com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo> restaurants){

        myViewStateRestaurantMediator.setValue(new ViewStateRestaurant(restaurants));

    }


    public LiveData<ViewStateRestaurant> getMediatorLiveData() {
        return myViewStateRestaurantMediator;
    }

}
