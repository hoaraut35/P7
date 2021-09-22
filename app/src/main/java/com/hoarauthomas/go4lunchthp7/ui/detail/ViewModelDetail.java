package com.hoarauthomas.go4lunchthp7.ui.detail;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;
import com.hoarauthomas.go4lunchthp7.BuildConfig;
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
        LiveData<FirebaseUser> myUserAuth = this.myFirebaseAuth.getUserLiveDataNew();

        //google api
        LiveData<List<RestaurantPojo>> myRestaurantsListFromRepo = this.myRestaurantRepository.getMyRestaurantsList();

        //google api
        LiveData<ResultDetailRestaurant> myRestaurantDetailFromRepo = this.myRestaurantRepository.getMyRestaurantDetail();

        //firestore
        LiveData<List<FirestoreUser>> myWorkMatesListFromRepo = this.myFirestoreRepository.getFirestoreWorkmates();

        //user from firestore
        LiveData<FirestoreUser> myUserFirestore = this.myFirestoreRepository.getWorkmateFromRepo();


        //dans la base firestore
        myScreenDetailMediator.addSource(myUserFirestore, firestoreUser -> {
            if (firestoreUser == null) return;
            logicWork(
                    myRestaurantsListFromRepo.getValue(),
                    myWorkMatesListFromRepo.getValue(),
                    myRestaurantDetailFromRepo.getValue(),
                    myUserAuth.getValue(),
                    placeIdRequest.getValue(),
                    firestoreUser);
        });

        //authentification
        myScreenDetailMediator.addSource(myUserAuth, firebaseUser -> {
            if (myUserAuth == null) return;
            ViewModelDetail.this.logicWork(
                    myRestaurantsListFromRepo.getValue(),
                    myWorkMatesListFromRepo.getValue(),
                    myRestaurantDetailFromRepo.getValue(),
                    firebaseUser,
                    placeIdRequest.getValue(),
                    myUserFirestore.getValue());
        });


        myScreenDetailMediator.addSource(myWorkMatesListFromRepo, firestoreUsers -> {
            if (firestoreUsers == null) return;
            logicWork(myRestaurantsListFromRepo.getValue(),
                    firestoreUsers,
                    myRestaurantDetailFromRepo.getValue(),
                    myUserAuth.getValue(),
                    placeIdRequest.getValue(),
                    myUserFirestore.getValue());
        });

        myScreenDetailMediator.addSource(myRestaurantsListFromRepo, restaurantPojo -> {
            if (myRestaurantsListFromRepo == null) return;
            ViewModelDetail.this.logicWork(
                    restaurantPojo,
                    myWorkMatesListFromRepo.getValue(),
                    myRestaurantDetailFromRepo.getValue(),
                    myUserAuth.getValue(),
                    placeIdRequest.getValue(),
                    myUserFirestore.getValue());
        });

        myScreenDetailMediator.addSource(myRestaurantDetailFromRepo, resultDetailRestaurant -> {
            if (myRestaurantDetailFromRepo == null) return;
            ViewModelDetail.this.logicWork(
                    myRestaurantsListFromRepo.getValue(),
                    myWorkMatesListFromRepo.getValue(),
                    resultDetailRestaurant,
                    myUserAuth.getValue(),
                    placeIdRequest.getValue(),
                    myUserFirestore.getValue());
        })
        ;

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
            @Nullable FirebaseUser myUserFireAuth,
            String placeIdRequested,
            FirestoreUser myUserFirtestore) {

        if (workmatesList == null || restaurantsList == null || myUserFireAuth == null || Restaurantdetail == null || myUserFirtestore == null)
            return;

        Log.i("[FIRESTORE]", "Utilisateur datbase : " + myUserFirtestore.getUsername() + myUserFirtestore.getUid());


        //create an ViewState detail object for ui
        ViewStateDetail myScreen = new ViewStateDetail();

        //list of workmates
        List<FirestoreUser> myWorkMatesDetailList = new ArrayList<>();
        for (FirestoreUser myItem : workmatesList) {
            if (myItem.getFavoriteRestaurant().equals(placeIdRequested)) {
                myWorkMatesDetailList.add(myItem);
            }
        }
        myScreen.setListWorkMates(myWorkMatesDetailList);

        //?
        myScreen.setWorkmate(myUserFireAuth.getUid());
        myScreen.setPlaceId(placeIdRequested);


        //setup favorite
        if (myUserFirtestore.getFavoriteRestaurant().equals(placeIdRequested)) {
            myScreen.setFavorite(true);
        } else {
            myScreen.setFavorite(false);
        }

        //setup btn like
        if (myUserFirtestore.getRestaurant_liked().contains(placeIdRequested)) {
            myScreen.setLiked(true);
        } else {
            myScreen.setLiked(false);
        }

        //debug
        Log.i("[FIRESTORE]", " - utilisateur connecté : " + myUserFireAuth.getDisplayName());
        Log.i("[FIRESOTRE]", "  - utilisateur bdd : " + myUserFirtestore.getUsername());
        Log.i("[FIRESTORE]", " - restaurant favoris : " + myUserFirtestore.getFavoriteRestaurant());
        Log.i("[FIRESTORE]", " - restaurant affiché : " + placeIdRequested);

        //setup general data in view object
        for (RestaurantPojo myItem : restaurantsList) {

            if (myItem.getPlaceId().equals(placeIdRequested)) {

                //title
                myScreen.setTitle(myItem.getName());

                //adress
                myScreen.setAddress(myItem.getVicinity());

                //get photo
                try {
                    String query = "https://maps.googleapis.com/maps/api/place/photo?key=" +
                            BuildConfig.MAPS_API_KEY + "&photoreference=" +
                            myItem.getPhotos().get(0).getPhotoReference() + "&maxheight=157&maxwidth=157";
                    myScreen.setUrlPhoto(query);


                } catch (Exception e) {
                    Log.i("[IMAGE]", "Exception : " + e.getMessage());
                }

                //rating
                try {
                    //on google we have a rating from 1 to 5 but we want 1 to 3...
                    //Double ratingDouble = map(restaurants.get(x).getRating(), 1.0, 5.0, 1.0, 3.0);
                    double ratingDouble = (myItem.getRating() - 1.0) * (3.0 - 1.0) / (5.0 - 1.0) + 1.0;
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

                //phone
                myScreen.setPhoneNumber(Restaurantdetail.getFormattedPhoneNumber());

                //website
                myScreen.setWebSite(Restaurantdetail.getUrl());

                break;
            }
        }

        //update viewstate object
        myScreenDetailMediator.setValue(myScreen);

    }

    public void setPlaceId(String placeId) {
        placeIdRequest.setValue(placeId);
    }

    public String getPlaceId() {
        return placeIdRequest.getValue();
    }

    public LiveData<ViewStateDetail> getMediatorScreen() {
        return myScreenDetailMediator;
    }

    public void updateFavoriteRestaurant(Boolean mFavorite, String mPlaceId, String mWorkmate) {
        myFirestoreRepository.updateFavRestaurant(mFavorite, mPlaceId, mWorkmate);
    }

    public void updateLikeRestaurant(Boolean mLike, String mPlaceId, String mWorkmate) {
        myFirestoreRepository.updateLikeRestaurant(mLike,mPlaceId,mWorkmate);
    }

    public void getUserPlaceFavoriteToShow() {
        setPlaceId(myFirestoreRepository.getWorkmateFromRepo().getValue().getFavoriteRestaurant());
    }
}