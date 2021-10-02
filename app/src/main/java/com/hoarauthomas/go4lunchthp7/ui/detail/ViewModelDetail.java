package com.hoarauthomas.go4lunchthp7.ui.detail;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;
import com.hoarauthomas.go4lunchthp7.BuildConfig;
import com.hoarauthomas.go4lunchthp7.model.FirestoreUser;
import com.hoarauthomas.go4lunchthp7.model.NearbySearch.RestaurantPojo;
import com.hoarauthomas.go4lunchthp7.model.PlaceDetails.ResultPlaceDetail;
import com.hoarauthomas.go4lunchthp7.repository.FirebaseAuthRepository;
import com.hoarauthomas.go4lunchthp7.repository.FirestoreRepository;
import com.hoarauthomas.go4lunchthp7.repository.RestaurantsRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;

public class ViewModelDetail extends ViewModel {

    private final FirebaseAuthRepository myFirebaseAuth;
    private final RestaurantsRepository myRestaurantRepository;
    private final FirestoreRepository myFirestoreRepository;

    //for ui update
    private final MediatorLiveData<ViewStateDetail> myScreenDetailMediator = new MediatorLiveData<>();

    //for update the placeId to show
    private final MutableLiveData<String> placeIdRequest = new MutableLiveData<>(null);

    public ViewModelDetail(

            FirebaseAuthRepository myAuthRepository,
            RestaurantsRepository myRestaurantRepository,
            FirestoreRepository myFirestoreRepository) {

        this.myFirebaseAuth = myAuthRepository;
        this.myRestaurantRepository = myRestaurantRepository;
        this.myFirestoreRepository = myFirestoreRepository;

        LiveData<FirebaseUser> myUserFirebaseFromRepo = this.myFirebaseAuth.getFirebaseAuthUserFromRepo();
        LiveData<List<RestaurantPojo>> myRestaurantsListFromRepo = this.myRestaurantRepository.getMyRestaurantsList();
        LiveData<ResultPlaceDetail> myRestaurantDetailFromRepo = this.myRestaurantRepository.getMyRestaurantDetail();
        LiveData<List<FirestoreUser>> myWorkMatesListFromRepo = this.myFirestoreRepository.getFirestoreWorkmates();
        LiveData<FirestoreUser> myUserFirestore = this.myFirestoreRepository.getWorkmateFromRepo();

        //user from firestore
        myScreenDetailMediator.addSource(myUserFirestore, firestoreUser -> {
            if (firestoreUser == null) return;
            logicWork(
                    myRestaurantsListFromRepo.getValue(),
                    myWorkMatesListFromRepo.getValue(),
                    myRestaurantDetailFromRepo.getValue(),
                    myUserFirebaseFromRepo.getValue(),
                    placeIdRequest.getValue(),
                    firestoreUser);
        });

        //user from firebase
        myScreenDetailMediator.addSource(myUserFirebaseFromRepo, firebaseUser -> {
            if (myUserFirebaseFromRepo.getValue() != null) return;
            ViewModelDetail.this.logicWork(
                    myRestaurantsListFromRepo.getValue(),
                    myWorkMatesListFromRepo.getValue(),
                    myRestaurantDetailFromRepo.getValue(),
                    firebaseUser,
                    placeIdRequest.getValue(),
                    myUserFirestore.getValue());
        });

        //workmatesList from firestore
        myScreenDetailMediator.addSource(myWorkMatesListFromRepo, firestoreUsers -> {
            if (firestoreUsers.isEmpty()) return;
            logicWork(myRestaurantsListFromRepo.getValue(),
                    firestoreUsers,
                    myRestaurantDetailFromRepo.getValue(),
                    myUserFirebaseFromRepo.getValue(),
                    placeIdRequest.getValue(),
                    myUserFirestore.getValue());
        });

        //restaurants from nearbysearch
        myScreenDetailMediator.addSource(myRestaurantsListFromRepo, restaurantPojo -> {
            if (myRestaurantsListFromRepo.getValue() == null) return;
            ViewModelDetail.this.logicWork(
                    restaurantPojo,
                    myWorkMatesListFromRepo.getValue(),
                    myRestaurantDetailFromRepo.getValue(),
                    myUserFirebaseFromRepo.getValue(),
                    placeIdRequest.getValue(),
                    myUserFirestore.getValue());
        });

        //restaurant details from place details
        myScreenDetailMediator.addSource(myRestaurantDetailFromRepo, resultDetailRestaurant -> {
            if (myRestaurantDetailFromRepo.getValue() == null) return;
            ViewModelDetail.this.logicWork(
                    myRestaurantsListFromRepo.getValue(),
                    myWorkMatesListFromRepo.getValue(),
                    resultDetailRestaurant,
                    myUserFirebaseFromRepo.getValue(),
                    placeIdRequest.getValue(),
                    myUserFirestore.getValue());
        });

        //placeId to show on UI
        myScreenDetailMediator.addSource(placeIdRequest, s -> {
            if (placeIdRequest.getValue() == null) return;
            myRestaurantRepository.setPlaceId(s);
        });

    }

    //logic work method for mediatorLiveData
    private void logicWork(
            @Nullable List<RestaurantPojo> restaurantsListFromNearbySearch,
            @Nullable List<FirestoreUser> workmatesListFromFirestore,
            @Nullable ResultPlaceDetail RestaurantDetailFromPlaceDetails,
            @Nullable FirebaseUser myUserFromFirebase,
            String placeIdRequestedFromUI,
            FirestoreUser myUserFromFirestore) {

        if (workmatesListFromFirestore == null ||
                restaurantsListFromNearbySearch == null ||
                myUserFromFirebase == null ||
                RestaurantDetailFromPlaceDetails == null ||
                myUserFromFirestore == null)
            return;

        //create an ViewState detail object for ui
        ViewStateDetail myScreen = new ViewStateDetail();

        //list of workmates
        List<FirestoreUser> myWorkMatesDetailList = new ArrayList<>();
        for (FirestoreUser myItem : workmatesListFromFirestore) {
            if (myItem.getFavoriteRestaurant().equals(placeIdRequestedFromUI)) {
                myWorkMatesDetailList.add(myItem);
            }
        }
        myScreen.setListWorkMates(myWorkMatesDetailList);

        //?
        myScreen.setWorkmate(myUserFromFirebase.getUid());
        myScreen.setPlaceId(placeIdRequestedFromUI);


        //setup favorite
        if (myUserFromFirestore.getFavoriteRestaurant().equals(placeIdRequestedFromUI)) {
            myScreen.setFavorite(true);
        } else {
            myScreen.setFavorite(false);
        }

        //setup btn like
        if (myUserFromFirestore.getRestaurant_liked().contains(placeIdRequestedFromUI)) {
            myScreen.setLiked(true);
        } else {
            myScreen.setLiked(false);
        }

        //setup general data in view object
        for (RestaurantPojo myItem : restaurantsListFromNearbySearch) {

            if (myItem.getPlaceId().equals(placeIdRequestedFromUI)) {

                //title
                myScreen.setTitle(myItem.getName());

                //address
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

                if (RestaurantDetailFromPlaceDetails.getFormattedPhoneNumber() != null) {
                    myScreen.setPhoneNumber(RestaurantDetailFromPlaceDetails.getFormattedPhoneNumber());
                }


                //website
                if (RestaurantDetailFromPlaceDetails.getUrl() != null) {
                    myScreen.setWebSite(RestaurantDetailFromPlaceDetails.getUrl());
                }

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
        myFirestoreRepository.updateLikeRestaurant(mLike, mPlaceId, mWorkmate);
    }

    public void getUserPlaceFavoriteToShow() {
        setPlaceId(Objects.requireNonNull(myFirestoreRepository.getWorkmateFromRepo().getValue()).getFavoriteRestaurant());
    }
}