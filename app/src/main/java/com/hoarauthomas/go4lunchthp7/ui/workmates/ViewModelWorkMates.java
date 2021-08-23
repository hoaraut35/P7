package com.hoarauthomas.go4lunchthp7.ui.workmates;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.hoarauthomas.go4lunchthp7.model.firestore.User;
import com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo;
import com.hoarauthomas.go4lunchthp7.repository.RestaurantsRepository;
import com.hoarauthomas.go4lunchthp7.repository.WorkMatesRepository;

import java.util.ArrayList;
import java.util.List;

public class ViewModelWorkMates extends ViewModel {


    private WorkMatesRepository myWorkMatesRepository;
    private RestaurantsRepository myRestaurantRepository;
    private List<SpecialWorkMate> mySpecialWorkMatesList = new ArrayList<>();

    private final MediatorLiveData<ViewStateWorkMates> myViewStateWorkMatesMediator = new MediatorLiveData<>();
    private final RestaurantPojo myRestauResult = new RestaurantPojo();


    public ViewModelWorkMates(RestaurantsRepository myRestaurantRepository, WorkMatesRepository myWorkMatesRepository) {
        this.myRestaurantRepository = myRestaurantRepository;
        this.myWorkMatesRepository = myWorkMatesRepository;

        LiveData<List<User>> myWorkMatesList = this.myWorkMatesRepository.getAllWorkMates();
        LiveData<List<com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo>> myRestaurantList = this.myRestaurantRepository.getMyRestaurantsList();

        myViewStateWorkMatesMediator.addSource(myWorkMatesList, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                Log.i("[WORKM]", "Event workmates list...");
                if (users != null) {
                    Log.i("[WORKM]", "Liste workmates" + users.size());
                    logicWork(users, myRestaurantList.getValue());
                }

            }
        });


        myViewStateWorkMatesMediator.addSource(myRestaurantList, new Observer<List<RestaurantPojo>>() {
            @Override
            public void onChanged(List<RestaurantPojo> restaurantPojos) {


                //TODO: bug passage objet restaurantr
                logicWork(myWorkMatesList.getValue(), restaurantPojos);


            }
        });


    }


    private void logicWork(List<User> myList, List<RestaurantPojo> myRestaurant) {

        if (myList != null && myRestaurant != null) {

            mySpecialWorkMatesList.clear();

            for (int i = 0; i < myList.size(); i++) {

                SpecialWorkMate myWorkMates = new SpecialWorkMate();

                myWorkMates.avatar = myList.get(i).getUrlPicture();
                myWorkMates.nameOfWorkMates = myList.get(i).getUsername();

                for (int z = 0; z < myRestaurant.size(); z++) {

                    if (myList.get(i).getFavoriteRestaurant().equals(myRestaurant.get(z).getPlaceId())) {
                        myWorkMates.nameOfRestaurant = myRestaurant.get(z).getName();
                        myWorkMates.placeId = myRestaurant.get(z).getPlaceId();
                    //    Log.i("[NEW]","identique" + myWorkMates.getNameOfRestaurant() + myWorkMates.getPlaceId());

                    } else {
                    //    myWorkMates.nameOfRestaurant = "";
                      //  myWorkMates.placeId = "";
                       // Log.i("[NEW]","identique" + myWorkMates.getNameOfRestaurant() + myWorkMates.getPlaceId());
                    }

                }

                mySpecialWorkMatesList.add(myWorkMates);
                Log.i("[NEW]","" + mySpecialWorkMatesList.get(i).toString());
            }

            myViewStateWorkMatesMediator.setValue(new ViewStateWorkMates( mySpecialWorkMatesList));
        }

    }

    public LiveData<ViewStateWorkMates> getMediatorLiveData() {
        return myViewStateWorkMatesMediator;
    }

    public static class SpecialWorkMate {

        String avatar;
        String nameOfWorkMates;
        String nameOfRestaurant;
        String placeId;

        public String getPlaceId() {
            return placeId;
        }

        public void setPlaceId(String placeId) {
            this.placeId = placeId;
        }

        public String getNameOfWorkMates() {
            return nameOfWorkMates;
        }

        public void setNameOfWorkMates(String nameOfWorkMates) {
            this.nameOfWorkMates = nameOfWorkMates;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }


        public String getNameOfRestaurant() {
            return nameOfRestaurant;
        }

        public void setNameOfRestaurant(String nameOfRestaurant) {
            this.nameOfRestaurant = nameOfRestaurant;
        }

        public SpecialWorkMate(String avatar, String name, String nameOfRestaurant, String id) {
            this.avatar = avatar;
            this.nameOfWorkMates = name;
            this.nameOfRestaurant = nameOfRestaurant;
            this.placeId = id;
        }

        public SpecialWorkMate() {

        }

    }
}
