package com.hoarauthomas.go4lunchthp7.ui.detail;

import android.location.Location;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
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

    private MutableLiveData<String> myPlaceId =  new MutableLiveData<>();

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
                logicWork(null, restaurantPojos,myWorkMatesList.getValue(),myPlaceId.getValue());
            }
        });

        myViewStateDetailMediator.addSource(myWorkMatesList, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                Log.i("[DETAIL]", "Event Detail liste workmates");
                logicWork(null,myRestaurantsList.getValue(),users,myPlaceId.getValue());
            }
        });


        myViewStateDetailMediator.addSource(myPlaceId, new Observer<String>() {
            @Override
            public void onChanged(String s) {

                Log.i("[MONDETAIL]", "Event Detail placeifd"+ s);
                logicWork(null,myRestaurantsList.getValue(),myWorkMatesList.getValue(),s);
            }
        });



    }

    //**********************************************************************************************
    // Logic work
    //**********************************************************************************************
    private void logicWork(@Nullable Location position, @Nullable List<RestaurantPojo> restaurants, @Nullable List<User> workmates, @Nullable String placeId) {
        List<User> workMatesTag = new ArrayList<>();

        RestaurantPojo newRestau;

        //selection restaurant
        if (placeId != null){

            //verification id
            if (!placeId.isEmpty()){

                if (restaurants != null){

                    for (int i =0; i<restaurants.size();i++){

                        //restaurant trouvé
                        if (restaurants.get(i).getPlaceId().equals(placeId)){
                            newRestau = restaurants.get(i);
                            myViewStateDetailMediator.setValue(new ViewStateDetail(newRestau.getName(),"test","","",true,true,workMatesTag));
                        }

                    /*for (int z=0;z<workmates.size();z++){

                        if (restaurants.get(i).getPlaceId().equals(workmates.get(z).getFavoriteRestaurant())){
                            workMatesTag.add(workmates.get(z));

                        }
                    }

                     */

                    }
                }



            }
        }



        if (restaurants == null || workmates == null){
            return;
        }


        if (!restaurants.isEmpty() && !workmates.isEmpty()){

            if (placeId != null ){


                Log.i("[MONDETAIL]","Identification du restaurant : " + placeId);

                //check all restaurant



            }


            int i;
            int z;






        }else
        {
            myViewStateDetailMediator.setValue(new ViewStateDetail("null","test","","",true,true,workMatesTag));
        }


    }
    //**********************************************************************************************
    // End of logic work
    //**********************************************************************************************




    public void setPlaceId(String placeId){
        myPlaceId.setValue(placeId);
    }




    //public livedata to publish in viexwstate
    public LiveData<ViewStateDetail> getMediatorLiveData() {
        return myViewStateDetailMediator;
    }
}



