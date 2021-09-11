package com.hoarauthomas.go4lunchthp7.ui.detail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;
import com.hoarauthomas.go4lunchthp7.api.UserHelper;
import com.hoarauthomas.go4lunchthp7.model.firestore.User;
import com.hoarauthomas.go4lunchthp7.model.placedetails2.ResultDetailRestaurant;
import com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo;
import com.hoarauthomas.go4lunchthp7.repository.FirebaseAuthRepository;
import com.hoarauthomas.go4lunchthp7.repository.RestaurantsRepository;
import com.hoarauthomas.go4lunchthp7.repository.WorkMatesRepository;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class ViewModelDetail extends ViewModel {

    //declare repo here...
    private final RestaurantsRepository myRestaurantRepository;
    private final WorkMatesRepository myWorkMatesRepository;

    //ViewState for ui
    private final MediatorLiveData<ScreenDetailModel> myScreenDetailMediator = new MediatorLiveData<>();

    //placeId selected for detail
    private String placeIdGen = null;

    //constructor
    public ViewModelDetail(FirebaseAuthRepository myAuthRepository, RestaurantsRepository myRestaurantRepository, WorkMatesRepository myWorkMatesRepository) {

        //get actual user authentification
        LiveData<FirebaseUser> myUserFromRepo = myAuthRepository.getUserLiveDataNew();

        this.myRestaurantRepository = myRestaurantRepository;
        //get restaurant list
        LiveData<List<RestaurantPojo>> myRestaurantsList = this.myRestaurantRepository.getMyRestaurantsList();
        //get restaurant detail
        LiveData<ResultDetailRestaurant> myRestaurantDetail = this.myRestaurantRepository.getMyRestaurantDetail();

        this.myWorkMatesRepository = myWorkMatesRepository;
        //get workmates list
        LiveData<List<User>> myWorkMatesList = this.myWorkMatesRepository.getAllWorkMatesList();

        //observe for list of restaurant
        myScreenDetailMediator.addSource(myRestaurantsList, restaurantPojo -> logicWork(restaurantPojo, myWorkMatesList.getValue(), myRestaurantDetail.getValue(), myUserFromRepo.getValue()));

        //observe detail of restaurant
        myScreenDetailMediator.addSource(myRestaurantDetail, resultDetailRestaurant -> logicWork(myRestaurantsList.getValue(), myWorkMatesList.getValue(), resultDetailRestaurant, myUserFromRepo.getValue()));

        //observe workmates list
        myScreenDetailMediator.addSource(myWorkMatesList, workmates -> logicWork(myRestaurantsList.getValue(), workmates, myRestaurantDetail.getValue(), myUserFromRepo.getValue()));

        //observe user
        myScreenDetailMediator.addSource(myUserFromRepo, firebaseUser -> logicWork(myRestaurantsList.getValue(), myWorkMatesList.getValue(), myRestaurantDetail.getValue(), firebaseUser));

    }

    //logic method for mediatorLiveData
    private void logicWork(@Nullable List<RestaurantPojo> restaurants, @Nullable List<User> workmates, @Nullable ResultDetailRestaurant detail, @Nullable FirebaseUser myUserBase) {

        //if we have null data we abord
        if (restaurants == null || workmates == null || detail == null ||myUserBase == null) return;

        //if (restaurants != null) {

            for (int x = 0; x < restaurants.size(); x++) {

                //if the list of restaurant contains the desired restaurant
                if (restaurants.get(x).getPlaceId().equals(placeIdGen)) {

                    ScreenDetailModel myScreen;

                    myScreen = new ScreenDetailModel();

                    //get photo
                    try {
                        myScreen.setUrlPhoto(restaurants.get(x).getPhotos().get(0).getPhotoReference());
                    } catch (Exception e) {
                        //charger une photo de substitution
                    }

                    //get titre restaurant
                    myScreen.setTitle(restaurants.get(x).getName());

                    //get address
                    myScreen.setAddress(restaurants.get(x).getVicinity());

                    //get rating
                    try {
                        //on google we have a rating from 1 to 5 but we want 1 to 3...
                        //Double ratingDouble = map(restaurants.get(x).getRating(), 1.0, 5.0, 1.0, 3.0);
                        double ratingDouble = (restaurants.get(x).getRating() - 1.0) * (3.0 - 1.0) / (5.0 - 1.0) + 1.0;
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

                    //get favorite
                    //if (workmates != null) {

                        if (workmates.size() > 0) {

                            for (int i = 0; i < workmates.size(); i++) {

                                //restaurant is favorite, update button
                                if (myUserBase != null && workmates.get(i).getUid().equals(myUserBase.getUid())) {

                                    //check if favorite
                                    //myScreen.setFavorite(workmates.get(i).getFavoriteRestaurant().indexOf(placeIdGen)>0);

                                    if (workmates.get(i).getFavoriteRestaurant().equals(placeIdGen)) {
                                        myScreen.setFavorite(true);
                                        // break;
                                    } else {
                                        myScreen.setFavorite(false);
                                    }

                                    //get list of liked restaurants


                                    //TODO: check if list is empty or null
                                    if (workmates.get(i).getRestaurant_liked().size() > 0) {

                                        List<String> myTempRestaurant = new ArrayList<>(workmates.get(i).getRestaurant_liked());
                                        //set bool to true or false if placeId exist in list
                                        myScreen.setLiked(myTempRestaurant.indexOf(placeIdGen) > 0);

                                    } else {
                                        myScreen.setLiked(false);
                                    }

                                    break;

                                } else {
                                    //no data from workmates set bool by default
                                    myScreen.setFavorite(false);
                                    myScreen.setLiked(false);

                                }

                            }

                        } else {
                            //no data from workmates set bool by default
                            myScreen.setFavorite(false);
                            myScreen.setLiked(false);
                        }

//                    } else {
//                        //no data from workmates set bool by default
//                        myScreen.setFavorite(false);
//                        myScreen.setLiked(false);
//                    }

                    //if we have the detail then ...
                    if (detail != null) {

                        //get phone number
                        myScreen.setCall(detail.getFormattedPhoneNumber());

                        //get url web
                        myScreen.setWebsite(detail.getUrl());
                    }

                    //if we have workmates then ...
                    List<User> myWorkMatesDetailList = new ArrayList<>();

                    if (workmates != null) {

                        for (User myWorkMate : workmates) {
                            //if detail have a workmate
                            if (myWorkMate.getFavoriteRestaurant().equals(placeIdGen)) {
                                myWorkMatesDetailList.add(myWorkMate);
                            }

                        }

                        myScreen.setListWorkMates(myWorkMatesDetailList);

                    }

                    myScreenDetailMediator.setValue(myScreen);

                    //add break here?

                }

            }

        //}fin si

    }
    //**********************************************************************************************
    // End of logic work
    //**********************************************************************************************

    //setup place id before open detail activity
    public void setPlaceId(String placeId) {
        placeIdGen = myRestaurantRepository.setPlaceId(placeId);
    }

    //get placeId before open detail activity
    public String getPlaceId() {
        return placeIdGen;
    }

    public FirebaseUser getCurrentUser() {
        return myWorkMatesRepository.getCurrentUser();
    }

    //publish data to UI
    public LiveData<ScreenDetailModel> getMediatorScreen() {
        return myScreenDetailMediator;
    }

    //like a restaurant
    public void adLikedRestaurant(String uid, String myPlaces) {



        UserHelper.addLikedRestaurant(uid, myPlaces);
    }

    //delete a liked restaurant
    public void deleteLikedRestaurant(String uid, String placeId) {
        UserHelper.deleteLikedRestaurant(uid, placeId);
    }

    //set a favorite restaurant
    public void setFavRestaurant(String uid, String placeId) {
        UserHelper.updateFavRestaurant(uid, placeId);
    }

    //delete a favorite restaurant
    public void deleteFavRestaurant(String uid) {
        UserHelper.deleteFavRestaurant(uid);
    }

}