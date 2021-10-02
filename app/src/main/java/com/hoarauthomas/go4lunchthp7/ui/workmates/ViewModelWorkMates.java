package com.hoarauthomas.go4lunchthp7.ui.workmates;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.hoarauthomas.go4lunchthp7.model.FirestoreUser;
import com.hoarauthomas.go4lunchthp7.model.NearbySearch.RestaurantPojo;
import com.hoarauthomas.go4lunchthp7.model.WorkmatesPojoForUI;
import com.hoarauthomas.go4lunchthp7.repository.FirestoreRepository;
import com.hoarauthomas.go4lunchthp7.repository.RestaurantsRepository;

import java.util.ArrayList;
import java.util.List;

public class ViewModelWorkMates extends ViewModel {

    //for UI
    private final MediatorLiveData<ViewStateWorkMates> myViewStateWorkMatesMediator = new MediatorLiveData<>();

    //constructor
    public ViewModelWorkMates(
            RestaurantsRepository myRestaurantRepository,
            FirestoreRepository myFirestoreRepository) {

        LiveData<List<FirestoreUser>> myWorkMatesListFromRepo = myFirestoreRepository.getFirestoreWorkmates();
        LiveData<List<RestaurantPojo>> myRestaurantList = myRestaurantRepository.getMyRestaurantsList();

        myViewStateWorkMatesMediator.addSource(myWorkMatesListFromRepo, firestoreUsers -> {
            if (firestoreUsers != null) {
                logicWork(firestoreUsers, myRestaurantList.getValue());
            }
        });

        myViewStateWorkMatesMediator.addSource(myRestaurantList, restaurantPojo -> {
            if (restaurantPojo == null || restaurantPojo.isEmpty()) return;
            logicWork(myWorkMatesListFromRepo.getValue(), restaurantPojo);
        });
    }

    private void logicWork(
            List<FirestoreUser> myFirestoreWorkmatesList,
            List<RestaurantPojo> myRestaurants) {

        if (myRestaurants == null || myRestaurants.isEmpty()) return;

        if (myFirestoreWorkmatesList == null || myFirestoreWorkmatesList.isEmpty()) return;

        List<WorkmatesPojoForUI> myWorkmatesUIList = new ArrayList<>();

        for (FirestoreUser myUserLoop : myFirestoreWorkmatesList) {

            WorkmatesPojoForUI myWorkmates = new WorkmatesPojoForUI();
            myWorkmates.setAvatar(myUserLoop.getUrlPicture());
            myWorkmates.setNameOfWorkMates(myUserLoop.getUsername());

            for (RestaurantPojo myRestaurantLoop : myRestaurants) {
                if (myRestaurantLoop.getPlaceId() != null) {
                    if (myUserLoop.getFavoriteRestaurant().equals(myRestaurantLoop.getPlaceId())) {
                        myWorkmates.setNameOfRestaurant(myRestaurantLoop.getName());
                        //not visible but used for click listener
                        myWorkmates.setPlaceId(myRestaurantLoop.getPlaceId());
                    }
                }
            }
            myWorkmatesUIList.add(myWorkmates);
        }

        myViewStateWorkMatesMediator.setValue(new ViewStateWorkMates(myWorkmatesUIList));
    }

    public LiveData<ViewStateWorkMates> getMediatorLiveData() {
        return myViewStateWorkMatesMediator;
    }

}
