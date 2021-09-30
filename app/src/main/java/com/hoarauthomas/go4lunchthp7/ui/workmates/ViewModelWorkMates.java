package com.hoarauthomas.go4lunchthp7.ui.workmates;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.hoarauthomas.go4lunchthp7.model.FirestoreUser;
import com.hoarauthomas.go4lunchthp7.model.NearbySearch.RestaurantPojo;
import com.hoarauthomas.go4lunchthp7.model.SpecialWorkMates;
import com.hoarauthomas.go4lunchthp7.repository.FirestoreRepository;
import com.hoarauthomas.go4lunchthp7.repository.RestaurantsRepository;

import java.util.ArrayList;
import java.util.List;

public class ViewModelWorkMates extends ViewModel {

    private final MediatorLiveData<ViewStateWorkMates> myViewStateWorkMatesMediator = new MediatorLiveData<>();

    public ViewModelWorkMates(
            RestaurantsRepository myRestaurantRepository,
            FirestoreRepository myFirestoreRepository) {

        LiveData<List<FirestoreUser>> myWorkMatesListFromRepo = myFirestoreRepository.getFirestoreWorkmates();
        LiveData<List<RestaurantPojo>> myRestaurantList = myRestaurantRepository.getMyRestaurantsList();

        myViewStateWorkMatesMediator.addSource(myWorkMatesListFromRepo, firestoreUsers -> {
            if (firestoreUsers == null) return;
            logicWork(firestoreUsers, myRestaurantList.getValue());
        });

        myViewStateWorkMatesMediator.addSource(myRestaurantList, restaurantPojo -> {
            if (restaurantPojo == null || restaurantPojo.isEmpty() ) return;
            logicWork(myWorkMatesListFromRepo.getValue(), restaurantPojo);
        });
    }

    private void logicWork(List<FirestoreUser> myFirestoreWorkmatesList, List<RestaurantPojo> myRestaurant) {

        if (myRestaurant == null || myRestaurant.isEmpty() ) return;
        if (myFirestoreWorkmatesList == null || myFirestoreWorkmatesList.isEmpty()) return;

        List<SpecialWorkMates> mySpecialWorkMatesList = new ArrayList<>();


       for (FirestoreUser myUserLoop : myFirestoreWorkmatesList){

            SpecialWorkMates myWorkmates = new SpecialWorkMates();
            myWorkmates.setAvatar(myUserLoop.getUrlPicture());
            myWorkmates.setNameOfWorkMates(myUserLoop.getUsername());

            for (RestaurantPojo myRestaurantLoop : myRestaurant){
                if (myRestaurantLoop.getPlaceId() != null){
                    if (myUserLoop.getFavoriteRestaurant().equals(myRestaurantLoop.getPlaceId())){
                        myWorkmates.setNameOfRestaurant(myRestaurantLoop.getName());
                        myWorkmates.setPlaceId(myRestaurantLoop.getPlaceId());
                    }
                }
            }

            mySpecialWorkMatesList.add(myWorkmates);

        }

        myViewStateWorkMatesMediator.setValue(new ViewStateWorkMates(mySpecialWorkMatesList));
    }

    public LiveData<ViewStateWorkMates> getMediatorLiveData() {
        return myViewStateWorkMatesMediator;
    }

}
