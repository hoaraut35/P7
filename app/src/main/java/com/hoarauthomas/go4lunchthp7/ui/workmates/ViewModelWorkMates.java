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

    private final FirestoreRepository myFirestoreRepository;
    private final RestaurantsRepository myRestaurantRepository;

    private final MediatorLiveData<ViewStateWorkMates> myViewStateWorkMatesMediator = new MediatorLiveData<>();

    public ViewModelWorkMates(RestaurantsRepository myRestaurantRepository, FirestoreRepository myFirestoreRepository) {
        this.myRestaurantRepository = myRestaurantRepository;
        this.myFirestoreRepository = myFirestoreRepository;

        LiveData<List<FirestoreUser>> myWorkMatesListFromRepo = this.myFirestoreRepository.getFirestoreWorkmates();
        LiveData<List<RestaurantPojo>> myRestaurantList = this.myRestaurantRepository.getMyRestaurantsList();

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

        for (int i = 0; i < myFirestoreWorkmatesList.size(); i++) {

            SpecialWorkMates myWorkMates = new SpecialWorkMates();
            myWorkMates.setAvatar(myFirestoreWorkmatesList.get(i).getUrlPicture());
            myWorkMates.setNameOfWorkMates(myFirestoreWorkmatesList.get(i).getUsername());

            for (int z = 0; z < myRestaurant.size(); z++) {
                if (myRestaurant.get(z).getPlaceId() != null) {
                    if (myFirestoreWorkmatesList.get(i).getFavoriteRestaurant().equals(myRestaurant.get(z).getPlaceId())) {
                        myWorkMates.setNameOfRestaurant(myRestaurant.get(z).getName());
                        myWorkMates.setPlaceId(myRestaurant.get(z).getPlaceId());
                    }
                }
            }
            mySpecialWorkMatesList.add(myWorkMates);
        }

        myViewStateWorkMatesMediator.setValue(new ViewStateWorkMates(mySpecialWorkMatesList));
    }

    public LiveData<ViewStateWorkMates> getMediatorLiveData() {
        return myViewStateWorkMatesMediator;
    }

}
