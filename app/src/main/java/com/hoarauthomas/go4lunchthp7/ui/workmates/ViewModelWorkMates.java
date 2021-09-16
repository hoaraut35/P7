package com.hoarauthomas.go4lunchthp7.ui.workmates;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.hoarauthomas.go4lunchthp7.model.SpecialWorkMates;
import com.hoarauthomas.go4lunchthp7.model.firestore.User;
import com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo;
import com.hoarauthomas.go4lunchthp7.repository.RestaurantsRepository;
import com.hoarauthomas.go4lunchthp7.repository.FirestoreDatabaseRepository;

import java.util.ArrayList;
import java.util.List;

public class ViewModelWorkMates extends ViewModel {


    private FirestoreDatabaseRepository myFirestoreDatabaseRepository;
    private RestaurantsRepository myRestaurantRepository;
    private List<SpecialWorkMates> mySpecialWorkMatesList = new ArrayList<>();

    private final MediatorLiveData<ViewStateWorkMates> myViewStateWorkMatesMediator = new MediatorLiveData<>();
    private final RestaurantPojo myRestauResult = new RestaurantPojo();

    public ViewModelWorkMates(RestaurantsRepository myRestaurantRepository, FirestoreDatabaseRepository myFirestoreDatabaseRepository) {
        this.myRestaurantRepository = myRestaurantRepository;
        this.myFirestoreDatabaseRepository = myFirestoreDatabaseRepository;

        LiveData<List<User>> myWorkMatesList = this.myFirestoreDatabaseRepository.getAllWorkMatesListFromRepo();
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

                SpecialWorkMates myWorkMates = new SpecialWorkMates();

                myWorkMates.setAvatar(myList.get(i).getUrlPicture());
                myWorkMates.setNameOfWorkMates(myList.get(i).getUsername());

                for (int z = 0; z < myRestaurant.size(); z++) {


                    if (myRestaurant.get(z).getPlaceId() != null) {
                        if (myList.get(i).getFavoriteRestaurant().equals(myRestaurant.get(z).getPlaceId())) {
                            myWorkMates.setNameOfRestaurant( myRestaurant.get(z).getName());
                            myWorkMates.setPlaceId(myRestaurant.get(z).getPlaceId());
                        }
                    }

                }

                mySpecialWorkMatesList.add(myWorkMates);
                Log.i("[NEW]", "" + mySpecialWorkMatesList.get(i).toString());
            }

            myViewStateWorkMatesMediator.setValue(new ViewStateWorkMates(mySpecialWorkMatesList));
        }

    }

    public LiveData<ViewStateWorkMates> getMediatorLiveData() {
        return myViewStateWorkMatesMediator;
    }


}
