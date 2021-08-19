package com.hoarauthomas.go4lunchthp7.ui.detail;

import android.location.Location;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.hoarauthomas.go4lunchthp7.RestaurantDetailPojo;
import com.hoarauthomas.go4lunchthp7.model.firestore.User;
import com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo;
import com.hoarauthomas.go4lunchthp7.repository.RestaurantsRepository;
import com.hoarauthomas.go4lunchthp7.repository.WorkMatesRepository;
import com.hoarauthomas.go4lunchthp7.ui.map.ViewStateMap;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class ViewModelDetail extends ViewModel {

    //declare repo here...
    private RestaurantsRepository myRestaurantRepository;
    private WorkMatesRepository myWorkMatesRepository;

    //to publish data in ViewState
    private final MediatorLiveData<ViewStateDetail> myViewStateDetailMediator = new MediatorLiveData<>();

    //constructor
    public ViewModelDetail(RestaurantsRepository myRestaurantRepository,WorkMatesRepository myWorkMatesRepository){
        this.myRestaurantRepository = myRestaurantRepository;
        this.myWorkMatesRepository = myWorkMatesRepository;

        LiveData<List<com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo>> myRestaurantsList = this.myRestaurantRepository.getMyRestaurantsList();
        LiveData<List<User>> myWorkMatesList = this.myWorkMatesRepository.getAllWorkMates();

        myViewStateDetailMediator.addSource(myRestaurantsList, new Observer<List<RestaurantPojo>>() {
            @Override
            public void onChanged(List<RestaurantPojo> restaurantPojos) {
                Log.i("[DETAIL]", "Event Detail liste restaurantr");
                logicWork(null, restaurantPojos,null);
            }
        });

        myViewStateDetailMediator.addSource(myWorkMatesList, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                Log.i("[DETAIL]", "Event Detail liste workmates");
                logicWork(null,myRestaurantsList.getValue(),users);
            }
        });




    }

    //**********************************************************************************************
    // Logic work
    //**********************************************************************************************
    private void logicWork(@Nullable Location position, @Nullable List<RestaurantPojo> restaurants, @Nullable List<User> workmates) {
        List<User> workMatesTag = new ArrayList<>();

        if (restaurants == null || workmates == null){
            return;
        }

        if (!restaurants.isEmpty() && !workmates.isEmpty()){

            int i;
            int z;



            for (i=0; i<restaurants.size();i++){


                for (z=0;z<workmates.size();z++){

                    if (restaurants.get(i).getPlaceId().equals(workmates.get(z).getFavoriteRestaurant())){
                        workMatesTag.add(workmates.get(z));

                    }
                }

            }

            myViewStateDetailMediator.setValue(new ViewStateDetail("null","test","","",true,true,workMatesTag));

        }else
        {
            myViewStateDetailMediator.setValue(new ViewStateDetail("null","test","","",true,true,workMatesTag));
        }


    }
    //**********************************************************************************************
    // End of logic work
    //**********************************************************************************************




    //public livedata to publish in viexwstate
    public LiveData<ViewStateDetail> getMediatorLiveData() {
        return myViewStateDetailMediator;
    }
}



