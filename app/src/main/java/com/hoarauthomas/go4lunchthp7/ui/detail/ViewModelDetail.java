package com.hoarauthomas.go4lunchthp7.ui.detail;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;
import com.hoarauthomas.go4lunchthp7.model.placedetails2.ResultDetailRestaurant;
import com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo;
import com.hoarauthomas.go4lunchthp7.repository.FirebaseAuthentificationRepository;
import com.hoarauthomas.go4lunchthp7.repository.FirestoreRepository;
import com.hoarauthomas.go4lunchthp7.repository.FirestoreUser;
import com.hoarauthomas.go4lunchthp7.repository.RestaurantsRepository;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class ViewModelDetail extends ViewModel {

    //repositories
    private final FirebaseAuthentificationRepository myFirebaseAuth;
    private final RestaurantsRepository myRestaurantRepository;
    private final FirestoreRepository myFirestoreRepository;

    //for ui
    private final MediatorLiveData<ViewStateDetail> myScreenDetailMediator = new MediatorLiveData<>();

    //used to know the placeid requested
    private MutableLiveData<String> placeIdRequest = new MutableLiveData<>(null);

    //constructor
    public ViewModelDetail(

            FirebaseAuthentificationRepository myAuthRepository,
            RestaurantsRepository myRestaurantRepository,
            FirestoreRepository myFirestoreRepository) {

        this.myFirebaseAuth = myAuthRepository;
        this.myRestaurantRepository = myRestaurantRepository;
        this.myFirestoreRepository = myFirestoreRepository;


        //firebase auth user
        LiveData<FirebaseUser> myUserFromRepo = this.myFirebaseAuth.getUserLiveDataNew();

        //google api
        LiveData<List<RestaurantPojo>> myRestaurantsListFromRepo = this.myRestaurantRepository.getMyRestaurantsList();

        //google api
        LiveData<ResultDetailRestaurant> myRestaurantDetailFromRepo = this.myRestaurantRepository.getMyRestaurantDetail();

        //firestore
        LiveData<List<FirestoreUser>> myWorkMatesListFromRepo = this.myFirestoreRepository.getFirestoreWorkmates();


        //user from firestore
        LiveData<FirestoreUser> myWorkmateFromRepo = this.myFirestoreRepository.getWorkmateFromRepo();




        myScreenDetailMediator.addSource(myWorkmateFromRepo, firestoreUser -> {
            if (firestoreUser == null) return;
            logicWork(myRestaurantsListFromRepo.getValue(), myWorkMatesListFromRepo.getValue(), myRestaurantDetailFromRepo.getValue(), myUserFromRepo.getValue(), placeIdRequest.getValue(), firestoreUser);
        });


        myScreenDetailMediator.addSource(myWorkMatesListFromRepo, firestoreUsers -> {
            if (firestoreUsers == null) return;
            logicWork(myRestaurantsListFromRepo.getValue(), firestoreUsers, myRestaurantDetailFromRepo.getValue(), myUserFromRepo.getValue(), placeIdRequest.getValue(), myWorkmateFromRepo.getValue());
        });

        myScreenDetailMediator.addSource(myRestaurantsListFromRepo, restaurantPojo -> {
            if (myRestaurantsListFromRepo == null) return;
            ViewModelDetail.this.logicWork(restaurantPojo, myWorkMatesListFromRepo.getValue(), myRestaurantDetailFromRepo.getValue(), myUserFromRepo.getValue(), placeIdRequest.getValue(), myWorkmateFromRepo.getValue());
        });

        myScreenDetailMediator.addSource(myRestaurantDetailFromRepo, resultDetailRestaurant -> {
            if (myRestaurantDetailFromRepo == null) return;
            ViewModelDetail.this.logicWork(myRestaurantsListFromRepo.getValue(), myWorkMatesListFromRepo.getValue(), resultDetailRestaurant, myUserFromRepo.getValue(), placeIdRequest.getValue(), myWorkmateFromRepo.getValue());
        })
        ;

        myScreenDetailMediator.addSource(myUserFromRepo, firebaseUser -> {
            if (myUserFromRepo == null) return;
            ViewModelDetail.this.logicWork(myRestaurantsListFromRepo.getValue(), myWorkMatesListFromRepo.getValue(), myRestaurantDetailFromRepo.getValue(), firebaseUser, placeIdRequest.getValue(), myWorkmateFromRepo.getValue());
        });

        myScreenDetailMediator.addSource(placeIdRequest, s -> {
            if (placeIdRequest == null) return;
            myRestaurantRepository.setPlaceId(s);
        });

    }

    //logic method for mediatorLiveData
    private void logicWork(
            @Nullable List<RestaurantPojo> restaurantsList,
            @Nullable List<FirestoreUser> workmatesList,
            @Nullable ResultDetailRestaurant Restaurantdetail,
            @Nullable FirebaseUser myUserBase,
            String placeIdRequested,
            FirestoreUser myUser) {

        if (workmatesList == null || restaurantsList == null || myUserBase == null || Restaurantdetail == null || myUser == null)
            return;

        Log.i("[FIRESTORE]","Utilisateur datbase : " + myUser.getUsername() +myUser.getUid());


        //create an ViewState detail object for ui
        ViewStateDetail myScreen = new ViewStateDetail();

        //list of workmates
        List<FirestoreUser> myWorkMatesDetailList = new ArrayList<>();

        for (FirestoreUser myItem : workmatesList) {
            if (myItem.getFavoriteRestaurant().equals(placeIdRequested)) {
                myWorkMatesDetailList.add(myItem);
            }
        }

        myScreen.setWorkmate(myUser.getUid());
        myScreen.setListWorkMates(myWorkMatesDetailList);
        myScreen.setPlaceId(placeIdRequested);

        if (myUser.getFavoriteRestaurant().equals(placeIdRequested)) {
            myScreen.setFavorite(true);
        } else {
            myScreen.setFavorite(false);
        }





       /* if (myItem.getMyUID().equals(myUserBase.getUid())) {




        //
        for (RestaurantPojo myItem : restaurantsList) {

            if (myItem.getPlaceId().equals(placeIdRequested)) {

                //title
                myScreen.setTitle(myItem.getName());

                //adress
                myScreen.setAddress(myItem.getVicinity());

                //photo
                try {
                    myScreen.setUrlPhoto(myItem.getPhotos().get(0).getPhotoReference());
                } catch (Exception e) {

                }


                break;
            }
        }

        Log.i("[FIRESTORE]", "Extraction environnement : ");
        Log.i("[FIRESTORE]", " - utilisateur connecté : " + myUserBase.getDisplayName() + " " + myUserBase.getUid());
      //  Log.i("[FIRESTORE]", " - restaurant favoris : " + myFirestoreRepository.getWorkmateFromFirestoreRepo().getValue().getFavoriteRestaurant());
        Log.i("[FIRESTORE]", " - restaurant affiché : " + placeIdRequested);




     /*   if (myUser.getRestaurant_liked().contains(placeIdRequested)) {
            myScreen.setLiked(true);
        } else {
            myScreen.setLiked(false);
        }

      */

        //set empty list for recycler else it crash
        myScreen.setListWorkMates(myWorkMatesDetailList);

        //final result for viewstate object
        myScreenDetailMediator.setValue(myScreen);


        //get rating
             /*   try {
                    //on google we have a rating from 1 to 5 but we want 1 to 3...
                    //Double ratingDouble = map(restaurants.get(x).getRating(), 1.0, 5.0, 1.0, 3.0);
                    double ratingDouble = (restaurantsList.get(x).getRating() - 1.0) * (3.0 - 1.0) / (5.0 - 1.0) + 1.0;
                    //private Double map(double value, double in_min, double in_max, double out_min, double out_max) {
                    //return (value - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;

                    int ratingInt = (int) Math.round(ratingDouble);

                    if (ratingInt == 1) {
                        myScreen.setRating(1);
                    } else if (ratingInt == 2) {
                        myScreen.setRating(2);
                    } else if (ratingInt == 3) {
                        myScreen.setRating(3);
                    }
                } catch (Exception e) {
                    myScreen.setRating(0);
                }

              */


    }

    public void setPlaceId(String placeId) {
        placeIdRequest.setValue(placeId);
    }

    public String getPlaceId() {
        return placeIdRequest.getValue();
    }

    public FirebaseUser getCurrentUser() {
        return myFirestoreRepository.getCurrentUser();
    }

    public LiveData<ViewStateDetail> getMediatorScreen() {
        return myScreenDetailMediator;
    }

    public void addLikedRestaurant(String uid, String myPlaces) throws InterruptedException {
        myFirestoreRepository.addLikedRestaurant(uid, myPlaces);
    }

    public void deleteLikedRestaurant(String uid, String placeId) throws InterruptedException {
        myFirestoreRepository.deleteLikedRestaurant(uid, placeId);
    }

    /**
     * add or remove a fovorite restaurant
     * @param mFavorite
     * @param mPlaceId
     * @param mWorkmate
     */

    public void updateFavRestaurant(Boolean mFavorite, String mPlaceId, String mWorkmate) {
        myFirestoreRepository.updateFavRestaurant(mFavorite,mPlaceId,mWorkmate);
    }
}