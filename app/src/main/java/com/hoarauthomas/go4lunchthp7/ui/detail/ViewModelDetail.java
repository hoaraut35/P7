package com.hoarauthomas.go4lunchthp7.ui.detail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;
import com.hoarauthomas.go4lunchthp7.api.UserHelper;
import com.hoarauthomas.go4lunchthp7.model.firestore.User;
import com.hoarauthomas.go4lunchthp7.model.placedetails2.ResultDetailRestaurant;
import com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo;
import com.hoarauthomas.go4lunchthp7.repository.FirebaseAuthentificationRepository;
import com.hoarauthomas.go4lunchthp7.repository.FirestoreDatabaseRepository;
import com.hoarauthomas.go4lunchthp7.repository.RestaurantsRepository;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class ViewModelDetail extends ViewModel {

    /**
     * we set repository here
     */
    private final RestaurantsRepository myRestaurantRepository;
    private final FirestoreDatabaseRepository myFirestoreDatabaseRepository;

    /**
     * this ViewState for UI
     */
    private final MediatorLiveData<ViewStateDetail> myScreenDetailMediator = new MediatorLiveData<>();

    //placeId selected for detail
    // private String placeIdGen = null;


    private MutableLiveData<String> placeIdRequest = new MutableLiveData<>(null);

    //constructor
    public ViewModelDetail(FirebaseAuthentificationRepository myAuthRepository, RestaurantsRepository myRestaurantRepository, FirestoreDatabaseRepository myFirestoreDatabaseRepository) {

        //get actual user authentification
        LiveData<FirebaseUser> myUserFromRepo = myAuthRepository.getUserLiveDataNew();

        this.myRestaurantRepository = myRestaurantRepository;
        //get restaurant list
        LiveData<List<RestaurantPojo>> myRestaurantsList = this.myRestaurantRepository.getMyRestaurantsList();
        //get restaurant detail
        LiveData<ResultDetailRestaurant> myRestaurantDetail = this.myRestaurantRepository.getMyRestaurantDetail();

        this.myFirestoreDatabaseRepository = myFirestoreDatabaseRepository;
        //get workmates list
        LiveData<List<User>> myWorkMatesList = this.myFirestoreDatabaseRepository.getAllWorkMatesListFromRepo();

        //observe for list of restaurant
        myScreenDetailMediator.addSource(myRestaurantsList, restaurantPojo -> logicWork(restaurantPojo, myWorkMatesList.getValue(), myRestaurantDetail.getValue(), myUserFromRepo.getValue(), placeIdRequest.getValue()));

        //observe detail of restaurant
        myScreenDetailMediator.addSource(myRestaurantDetail, resultDetailRestaurant -> logicWork(myRestaurantsList.getValue(), myWorkMatesList.getValue(), resultDetailRestaurant, myUserFromRepo.getValue(), placeIdRequest.getValue()));

        //observe workmates list
        myScreenDetailMediator.addSource(myWorkMatesList, workmates -> logicWork(myRestaurantsList.getValue(), workmates, myRestaurantDetail.getValue(), myUserFromRepo.getValue(), placeIdRequest.getValue()));

        //observe user
        myScreenDetailMediator.addSource(myUserFromRepo, firebaseUser -> logicWork(myRestaurantsList.getValue(), myWorkMatesList.getValue(), myRestaurantDetail.getValue(), firebaseUser, placeIdRequest.getValue()));

        myScreenDetailMediator.addSource(placeIdRequest, s -> {

            myRestaurantRepository.setPlaceId(s);
            //logicWork(myRestaurantsList.getValue(), myWorkMatesList.getValue(), myRestaurantDetail.getValue(), myUserFromRepo.getValue(), s);
        });

    }

    //logic method for mediatorLiveData
    private void logicWork(@Nullable List<RestaurantPojo> restaurantsList, @Nullable List<User> workmatesList, @Nullable ResultDetailRestaurant Restaurantdetail, @Nullable FirebaseUser myUserBase, String placeIdRequested) {


        ViewStateDetail myScreen = new ViewStateDetail();

        //if we have null data we cancel work
        if (restaurantsList == null || workmatesList == null || Restaurantdetail == null || myUserBase == null || placeIdRequested == null) {
            return;
        } else {
            //search the restaurant attached to user
            outMasterLoop:
            for (int x = 0; x < restaurantsList.size(); x++) {

                //we' get the restaurant to work with it
                if (restaurantsList.get(x).getPlaceId().equals(placeIdRequested)) {


                    //get photo
                    try {
                        myScreen.setUrlPhoto(restaurantsList.get(x).getPhotos().get(0).getPhotoReference());
                    } catch (Exception e) {
                        //charger une photo de substitution
                    }

                    //get titre restaurant
                    myScreen.setTitle(restaurantsList.get(x).getName());

                    //get address
                    myScreen.setAddress(restaurantsList.get(x).getVicinity());

                    //get rating
                    try {
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

                    //get favorite ansliked
                    if (workmatesList.size() > 0) {


                        //we scan list of workmates
                        for (int i = 0; i < workmatesList.size(); i++) {

                            //restaurant is favorite, update button
                            // if (workmatesList.get(i).getUid().equals(myUserBase.getUid())) {

                            //check if favorite
                            //myScreen.setFavorite(workmates.get(i).getFavoriteRestaurant().indexOf(placeIdGen)>0);


                            if (workmatesList.get(i).getFavoriteRestaurant() != null && !workmatesList.get(i).getFavoriteRestaurant().isEmpty()) {

                                if (workmatesList.get(i).getFavoriteRestaurant().equals(placeIdRequested)) {

                                    myScreen.setFavorite(true);
                                    break;
                                }


                            } else {
                                myScreen.setFavorite(false);
                            }


                            // }
                        }

                        //check liked

                        outDoubleLoop:
                        for (int i = 0; i < workmatesList.size(); i++) {

                            //we have liked restaurant
                            if (workmatesList.get(i).getRestaurant_liked() != null && !workmatesList.get(i).getRestaurant_liked().isEmpty()) {


                                if (workmatesList.get(i).getUid().equals(myUserBase.getUid())){

                                    List<String> myTempRestaurant = new ArrayList<>(workmatesList.get(i).getRestaurant_liked());
//
                                    for (int z = 0; z < myTempRestaurant.size(); z++) {

                                        if (workmatesList.get(i).getRestaurant_liked().get(z).equals(placeIdRequested)) {
                                            myScreen.setLiked(true);
                                            break outDoubleLoop;
                                        } else
                                            myScreen.setLiked(false);
                                    }

                                }



                            /*if (myScreen.getLiked() != null) {
                                if (myScreen.getLiked()) {
                                    break;
                                }

                            }

                             */

                            } else {
                                myScreen.setLiked(false);
                            }

                        }

                    } else {
                        //no data from workmates set bool by default
                        myScreen.setFavorite(false);
                        myScreen.setLiked(false);

                    }

                    //get phone number
                    myScreen.setCall(Restaurantdetail.getFormattedPhoneNumber());

                    //get url web
                    myScreen.setWebsite(Restaurantdetail.getUrl());

                    //if we have workmates then ...
                    List<User> myWorkMatesDetailList = new ArrayList<>();

                    for (User myWorkMate : workmatesList) {

                        if (myWorkMate.getFavoriteRestaurant().equals(placeIdRequested)) {
                            myWorkMatesDetailList.add(myWorkMate);
                        }

                    }
                    myScreen.setListWorkMates(myWorkMatesDetailList);

                    break outMasterLoop;
                }


            }

            //set mediator
            myScreenDetailMediator.setValue(myScreen);
            //
        }


    }
//**********************************************************************************************
// End of logic work
//**********************************************************************************************

    //setup place id before open detail activity
    public void setPlaceId(String placeId) {
        //placeIdGen = myRestaurantRepository.setPlaceId(placeId);
        // myRestaurantRepository.setPlaceId(placeId);
        placeIdRequest.setValue(placeId);
    }

    //get placeId before open detail activity
    public String getPlaceId() {
        return placeIdRequest.getValue();
    }

    public FirebaseUser getCurrentUser() {
        return myFirestoreDatabaseRepository.getCurrentUser();
    }

    //publish data to UI
    public LiveData<ViewStateDetail> getMediatorScreen() {
        return myScreenDetailMediator;
    }

    //like a restaurant

    /**
     * add liked restaurant to firestore
     *
     * @param uid
     * @param myPlaces
     */
    public void addLikedRestaurant(String uid, String myPlaces) {

        UserHelper.addRestaurantLiked(uid,myPlaces);
        //UserHelper.addLikedRestaurant(uid, myPlaces);
    }

    /**
     * delete liked restaurant
     *
     * @param uid
     * @param placeId
     */
    public void deleteLikedRestaurant(String uid, String placeId) {
        UserHelper.deleteLikedRestaurant(uid, placeId);
    }

    //set a favorite restaurant

    /**
     * add restaurant to favorite
     *
     * @param uid     user id
     * @param placeId place id
     */
    public void addtFavRestaurant(String uid, String placeId) {
        UserHelper.addFavRestaurant(uid, placeId);
    }

    /**
     * delete a favorite restaurant
     *
     * @param uid user id
     */
    //delete a favorite restaurant
    public void deleteFavRestaurant(String uid) {
        UserHelper.deleteFavRestaurant(uid);
    }

}