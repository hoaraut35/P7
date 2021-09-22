package com.hoarauthomas.go4lunchthp7.ui.workmates;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.hoarauthomas.go4lunchthp7.model.SpecialWorkMates;
import com.hoarauthomas.go4lunchthp7.model.firestore.User;
import com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo;
import com.hoarauthomas.go4lunchthp7.repository.FirestoreRepository;
import com.hoarauthomas.go4lunchthp7.repository.FirestoreUser;
import com.hoarauthomas.go4lunchthp7.repository.RestaurantsRepository;

import java.util.ArrayList;
import java.util.List;

public class ViewModelWorkMates extends ViewModel {

    /**
     * declare repository here...
     */
    private FirestoreRepository myFirestoreRepository;
    private RestaurantsRepository myRestaurantRepository;
    private List<SpecialWorkMates> mySpecialWorkMatesList = new ArrayList<>();

    /**
     * for ui
     */
    private final MediatorLiveData<ViewStateWorkMates> myViewStateWorkMatesMediator = new MediatorLiveData<>();
    private final RestaurantPojo myRestauResult = new RestaurantPojo();

    public ViewModelWorkMates(RestaurantsRepository myRestaurantRepository, FirestoreRepository myFirestoreRepository) {
        this.myRestaurantRepository = myRestaurantRepository;
        this.myFirestoreRepository = myFirestoreRepository;

        LiveData<List<FirestoreUser>> myWorkMatesListFromRepo = this.myFirestoreRepository.getFirestoreWorkmates();
        LiveData<List<com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo>> myRestaurantList = this.myRestaurantRepository.getMyRestaurantsList();

        myViewStateWorkMatesMediator.addSource(myWorkMatesListFromRepo, firestoreUsers -> {
            if (firestoreUsers == null) return;
            logicWork(firestoreUsers, myRestaurantList.getValue());
        });

        myViewStateWorkMatesMediator.addSource(myRestaurantList, restaurantPojos -> {
            if (restaurantPojos == null || restaurantPojos.isEmpty() ) return;
            logicWork(myWorkMatesListFromRepo.getValue(), restaurantPojos);
        });
    }

    private void logicWork(List<FirestoreUser> myFirestoreWorkmatesList, List<RestaurantPojo> myRestaurant) {

        if (myRestaurant == null || myRestaurant.isEmpty() ) return;
        if (myFirestoreWorkmatesList == null || myFirestoreWorkmatesList.isEmpty()) return;

        mySpecialWorkMatesList.clear();

        //pour l'ensemble des utilisateurs foirestore
        for (int i = 0; i < myFirestoreWorkmatesList.size(); i++) {

            //on créé un specialworkmates pour l'affichage
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
            Log.i("[NEW]", "" + mySpecialWorkMatesList.get(i).toString());
        }

        myViewStateWorkMatesMediator.setValue(new ViewStateWorkMates(mySpecialWorkMatesList));
    }

    public LiveData<ViewStateWorkMates> getMediatorLiveData() {
        return myViewStateWorkMatesMediator;
    }

}
