package com.hoarauthomas.go4lunchthp7.ui.detail;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
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
        myScreenDetailMediator.addSource(myRestaurantsList, (List<RestaurantPojo> restaurantPojo) -> {
            if (myRestaurantsList == null) return;
            logicWork(restaurantPojo, myWorkMatesList.getValue(), myRestaurantDetail.getValue(), myUserFromRepo.getValue(), placeIdRequest.getValue());
        });

        //observe detail of restaurant
        myScreenDetailMediator.addSource(myRestaurantDetail, (ResultDetailRestaurant resultDetailRestaurant) ->

        {
            if (myRestaurantDetail == null) return;
            logicWork(myRestaurantsList.getValue(), myWorkMatesList.getValue(), resultDetailRestaurant, myUserFromRepo.getValue(), placeIdRequest.getValue());
        })
        ;

        //observe workmates list
        myScreenDetailMediator.addSource(myWorkMatesList, workmates -> {
            if (myWorkMatesList == null) return;
            logicWork(myRestaurantsList.getValue(), workmates, myRestaurantDetail.getValue(), myUserFromRepo.getValue(), placeIdRequest.getValue());
        });

        //observe user
        myScreenDetailMediator.addSource(myUserFromRepo, firebaseUser -> logicWork(myRestaurantsList.getValue(), myWorkMatesList.getValue(), myRestaurantDetail.getValue(), firebaseUser, placeIdRequest.getValue()));

        myScreenDetailMediator.addSource(placeIdRequest, s -> {
            if (myUserFromRepo == null) return;
            myRestaurantRepository.setPlaceId(s);
            //logicWork(myRestaurantsList.getValue(), myWorkMatesList.getValue(), myRestaurantDetail.getValue(), myUserFromRepo.getValue(), s);
        });

    }

    //logic method for mediatorLiveData
    private void logicWork(@Nullable List<RestaurantPojo> restaurantsList, @Nullable List<User> workmatesList, @Nullable ResultDetailRestaurant Restaurantdetail, @Nullable FirebaseUser myUserBase, String placeIdRequested) {
        //create an viewstate detail for ui
        ViewStateDetail myScreen = new ViewStateDetail();

        //recherche durestaurant à afficher sur l'écran
        for (int x = 0; x < restaurantsList.size(); x++) {

            //recherche du restaurant en fonction de l'id recherché (clic sur map, liste restau ou liste workmates
            if (restaurantsList.get(x).getPlaceId().equals(placeIdRequested)) {


                Log.i("[FIRESTORE]", "restaurant trouvé dans la liste : " + restaurantsList.get(x).getName());

                //extraction photo
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

                //get photo
                try {
                    myScreen.setUrlPhoto(restaurantsList.get(x).getPhotos().get(0).getPhotoReference());
                } catch (Exception e) {
                    //charger une photo de substitution
                }

                //get phone number
                try {
                    myScreen.setCall(Restaurantdetail.getFormattedPhoneNumber());
                } catch (Exception e) {
                    myScreen.setCall("inconnu");
                }


                //get url web
                try {
                    myScreen.setWebsite(Restaurantdetail.getUrl());
                } catch (Exception e) {
                    myScreen.setWebsite("");
                }


                //if we have workmates then ...
                List<User> myWorkMatesDetailList = new ArrayList<>();

                for (User myWorkMate : workmatesList) {

                    if (myWorkMate.getFavoriteRestaurant().equals(placeIdRequested)) {
                        myWorkMatesDetailList.add(myWorkMate);
                    }

                }

                myFirestoreDatabaseRepository.getFirestore().document().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {


                    }
                });

                //extraction firestore
                myFirestoreDatabaseRepository.getFirestore().document(myUserBase.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        if (documentSnapshot.exists()) {

                            Log.i("[FIRESTORE]", "username : " + documentSnapshot.getData().get("username").toString());
                            Log.i("[FIRESTORE]", "uid : " + documentSnapshot.getData().get("uid").toString());
                            Log.i("[FIRESTORE]", "restaurant favorite :" + documentSnapshot.getData().get("favoriteRestaurant").toString());
                            Log.i("[FIRESTORE]", "restaurant liked " + documentSnapshot.getData().get("restaurant_liked").toString());

                            if (documentSnapshot.getData().get("favoriteRestaurant").equals(placeIdRequested)) {
                                Log.i("[FIRESTORE]", "Le restaurant est déjà favoris");
                                myScreen.setFavorite(true);
                                // mettre le bouton fav en true;

                            } else {
                                Log.i("[FIRESTORE]", "Le restaurant n'est pas encorefavoris !!");
                                myScreen.setFavorite(false);
                                //mettre le bouton fav en false
                            }

                            //extract liked

                            List<String> group = (List<String>) documentSnapshot.get("restaurant_liked");

                            //pas bon car trouve le favori au lieu du liked ...
                            if (group.contains(placeIdRequested)) {

                                Log.i("[FIRESTORE]", "Le restaurant est liké");
                                myScreen.setLiked(true);
                                //mettre bouton en jaune
                            } else {
                                Log.i("[FIRESTORE]", "Le restaurant n'est pas encore liké !!");
                                myScreen.setLiked(false);
                                //mettre bouton en rouge
                            }


                            myScreen.setListWorkMates(myWorkMatesDetailList);


                            Log.i("[FIRESTORE]", "Détail object viewstate : " + " fav : " + myScreen.getFavorite() + " lik:" + myScreen.getLiked());
                            myScreenDetailMediator.setValue(myScreen);

                        }
                    }
                });
            }
        }
    }

    public void setPlaceId(String placeId) {
        placeIdRequest.setValue(placeId);
    }

    public String getPlaceId() {
        return placeIdRequest.getValue();
    }

    public FirebaseUser getCurrentUser() {
        return myFirestoreDatabaseRepository.getCurrentUser();
    }

    public LiveData<ViewStateDetail> getMediatorScreen() {
        return myScreenDetailMediator;
    }

    public void addLikedRestaurant(String uid, String myPlaces) {
        myFirestoreDatabaseRepository.addLikedRestaurant(uid, myPlaces);
    }

    public void deleteLikedRestaurant(String uid, String placeId) {
        myFirestoreDatabaseRepository.deleteLikedRestaurant(uid, placeId);
    }

    public void addtFavRestaurant(String uid, String placeId) {
        myFirestoreDatabaseRepository.addFavRestaurant(uid, placeId);
    }

    public void deleteFavRestaurant(String uid, String placeId) {
        myFirestoreDatabaseRepository.deleteFavRestaurant(uid);
    }

}