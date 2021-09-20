package com.hoarauthomas.go4lunchthp7.ui.detail;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.hoarauthomas.go4lunchthp7.model.placedetails2.ResultDetailRestaurant;
import com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo;
import com.hoarauthomas.go4lunchthp7.repository.FirebaseAuthentificationRepository;
import com.hoarauthomas.go4lunchthp7.repository.FirestoreDatabaseRepository;
import com.hoarauthomas.go4lunchthp7.repository.FirestoreUser;
import com.hoarauthomas.go4lunchthp7.repository.RestaurantsRepository;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class ViewModelDetail extends ViewModel {

    private final FirebaseAuthentificationRepository myFirebaseAuth;
    private final RestaurantsRepository myRestaurantRepository;
    private final FirestoreDatabaseRepository myFirestoreDatabaseRepository;

    private final MediatorLiveData<ViewStateDetail> myScreenDetailMediator = new MediatorLiveData<>();

    private MutableLiveData<String> placeIdRequest = new MutableLiveData<>(null);

    //constructor
    public ViewModelDetail(
            FirebaseAuthentificationRepository myAuthRepository,
            RestaurantsRepository myRestaurantRepository,
            FirestoreDatabaseRepository myFirestoreDatabaseRepository) {

        this.myFirebaseAuth = myAuthRepository;
        this.myRestaurantRepository = myRestaurantRepository;
        this.myFirestoreDatabaseRepository = myFirestoreDatabaseRepository;

        //firebase auth user
        LiveData<FirebaseUser> myUserFromRepo = this.myFirebaseAuth.getUserLiveDataNew();
        //google api
        LiveData<List<RestaurantPojo>> myRestaurantsListFromRepo = this.myRestaurantRepository.getMyRestaurantsList();
        //google api
        LiveData<ResultDetailRestaurant> myRestaurantDetailFromRepo = this.myRestaurantRepository.getMyRestaurantDetail();
        //firestore
        LiveData<List<FirestoreUser>> myWorkMatesListFromRepo = this.myFirestoreDatabaseRepository.getFirestoreWorkmates();

        myScreenDetailMediator.addSource(myWorkMatesListFromRepo, new Observer<List<FirestoreUser>>() {
            @Override
            public void onChanged(List<FirestoreUser> firestoreUsers) {
                if (firestoreUsers == null) return;
                logicWork(myRestaurantsListFromRepo.getValue(), firestoreUsers, myRestaurantDetailFromRepo.getValue(), myUserFromRepo.getValue(), placeIdRequest.getValue());
            }
        });

        myScreenDetailMediator.addSource(myRestaurantsListFromRepo, new Observer<List<RestaurantPojo>>() {
            @Override
            public void onChanged(List<RestaurantPojo> restaurantPojo) {
                if (myRestaurantsListFromRepo == null) return;
                ViewModelDetail.this.logicWork(restaurantPojo, myWorkMatesListFromRepo.getValue(), myRestaurantDetailFromRepo.getValue(), myUserFromRepo.getValue(), placeIdRequest.getValue());
            }
        });

        myScreenDetailMediator.addSource(myRestaurantDetailFromRepo, new Observer<ResultDetailRestaurant>() {
            @Override
            public void onChanged(ResultDetailRestaurant resultDetailRestaurant) {
                if (myRestaurantDetailFromRepo == null) return;
                ViewModelDetail.this.logicWork(myRestaurantsListFromRepo.getValue(), myWorkMatesListFromRepo.getValue(), resultDetailRestaurant, myUserFromRepo.getValue(), placeIdRequest.getValue());
            }
        })
        ;

        myScreenDetailMediator.addSource(myUserFromRepo, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if (myUserFromRepo == null) return;
                ViewModelDetail.this.logicWork(myRestaurantsListFromRepo.getValue(), myWorkMatesListFromRepo.getValue(), myRestaurantDetailFromRepo.getValue(), firebaseUser, placeIdRequest.getValue());
            }
        });

        myScreenDetailMediator.addSource(placeIdRequest, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (placeIdRequest == null) return;
                myRestaurantRepository.setPlaceId(s);
                //logicWork(myRestaurantsList.getValue(), myWorkMatesList.getValue(), myRestaurantDetail.getValue(), myUserFromRepo.getValue(), s);
            }
        });

    }

    //logic method for mediatorLiveData
    private void logicWork(@Nullable List<RestaurantPojo> restaurantsList, @Nullable List<FirestoreUser> workmatesList, @Nullable ResultDetailRestaurant Restaurantdetail, @Nullable FirebaseUser myUserBase, String placeIdRequested) {


        //create an ViewState detail object for ui
        ViewStateDetail myScreen = new ViewStateDetail();

        //list of workmates
        List<FirestoreUser> myWorkMatesDetailList = new ArrayList<>();

        //extract all users with the same restaurant favorite
      /*  myFirestoreDatabaseRepository.getFirestore().whereEqualTo("favoriteRestaurant", placeIdRequested).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for (QueryDocumentSnapshot myUser : queryDocumentSnapshots) {
                    Log.i("[GOOD]", "extract workmates" + myUser.get("username"));

                    myWorkMatesDetailList.add(myUser.toObject(FirestoreUser.class));
                    //new FirestoreUser((myUser.get("username").toString()));
                }

                myScreen.setListWorkMates(myWorkMatesDetailList);



            }
        });*/



        myFirestoreDatabaseRepository.getFirestore().document(myFirestoreDatabaseRepository.getCurrentUserUID()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@androidx.annotation.Nullable DocumentSnapshot value, @androidx.annotation.Nullable FirebaseFirestoreException error) {


                for (RestaurantPojo myRestaurantIterate : restaurantsList) {
                    if (myRestaurantIterate.getPlaceId().equals(placeIdRequested)) {
                        Log.i("[GOOD]", "Recherche restaurant à afficher trouvé  : " + myRestaurantIterate.getName());

                        //title
                        myScreen.setTitle(myRestaurantIterate.getName());

                        //adress
                        myScreen.setAddress(myRestaurantIterate.getVicinity());

                        //photo
                        try {
                            myScreen.setUrlPhoto(myRestaurantIterate.getPhotos().get(0).getPhotoReference());
                        } catch (Exception e) {

                        }

                        //phone
                        // if (!Strings.isNullOrEmpty(Restaurantdetail.getFormattedPhoneNumber())){
                        //   myScreen.setCall(Restaurantdetail.getFormattedPhoneNumber());
                        // }

                        //set url
                        // if (!Strings.isNullOrEmpty(Restaurantdetail.getUrl())){
                        //   myScreen.setWebsite(Restaurantdetail.getUrl());
                        // }

                        break;
                    }
                }

                if (value == null) return;

                Log.i("[GOOD]", "Extraction environnement : ");
                Log.i("[GOOD]", " - utilisateur connecté : " + value.get("username") + " " + value.get("uid"));
                Log.i("[GOOD]", " - restaurant favoris : " + value.get("favoriteRestaurant"));
                Log.i("[GOOD]", " - restaurant affiché : " + placeIdRequested);

                //favorite btn setup
                if (value.get("favoriteRestaurant").equals(placeIdRequested)) {
                    Log.i("[GOOD]", " - [FAVORIS] = TRUE");
                    myScreen.setFavorite(true);
                } else {
                    Log.i("[GOOD]", " - [FAVORIS] = FALSE");
                    myScreen.setFavorite(false);
                }

                //liked button setup
                if (value.getData().get("restaurant_liked").toString().contains(placeIdRequested)) {
                    Log.i("[GOOD]", " - [LIKED] = TRUE");
                    myScreen.setLiked(true);
                } else {
                    Log.i("[GOOD]", " - [LIKED] = FALSE " + value.getData().get("restaurant_liked").toString());
                    myScreen.setLiked(false);
                }





                //set empty list for recycler else it crash
                myScreen.setListWorkMates(myWorkMatesDetailList);


                //final result for viewstate object
                myScreenDetailMediator.setValue(myScreen);


            }
        });


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


        //List<String> group = (List<String>) documentSnapshot.get("restaurant_liked");

        //if we have workmates then ...
        //     List<FirestoreUser> myWorkMatesDetailList = new ArrayList<>();
        //List<String> myWorkMatesDetailList = new ArrayList<>();


                        /*    List<User> myFirestoreUser = (List) myFirestoreDatabaseRepository.getFirestore().get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                    List<User> myUserf = (List)queryDocumentSnapshots.getDocuments();
                                    Log.i("[list]",""+ myUserf.size());
                                }
                            });

                         */

        //  myWorkMatesDetailList.clear();
        //pour chaque utilisateur
                     /*       for (int z = 0; z < workmatesList.size(); z++) {

                                //pour chaque restaurant
                                //  for (int y = 0; y < restaurantsList.size(); y++) {


                                //if (workmatesList.get(z).getFavoriteRestaurant().equals(getPlaceId()restaurantsList.get(y).getPlaceId())) {
                                if (workmatesList.get(z).getFavoriteRestaurant().equals(placeIdRequested)) {
                                    myWorkMatesDetailList.add(workmatesList.get(z));
                                    // }
                                    //   if (workmatesList.get(z).getFavoriteRestaurant().equals(placeIdRequested)) {

                                    //            User test = new User();
                                    //                              test.setUsername("place id : " + placeIdRequested + " wokr place : " + workmatesList.get(z).getFavoriteRestaurant());
                                    //                                myWorkMatesDetailList.add(test);
                                    //myWorkMatesDetailList.add("place id : " + placeIdRequested + " wokr place : " + workmatesList.get(z).getFavoriteRestaurant());


                                }
                            }

                      */


        //     List<String> group = (List<String>) documentSnapshot.get("restaurant_liked");


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

    public void addLikedRestaurant(String uid, String myPlaces) throws InterruptedException {
        myFirestoreDatabaseRepository.addLikedRestaurant(uid, myPlaces);
    }

    public void deleteLikedRestaurant(String uid, String placeId) throws InterruptedException {
        myFirestoreDatabaseRepository.deleteLikedRestaurant(uid, placeId);
    }

    public void addtFavRestaurant(String uid, String placeId) throws InterruptedException {
        myFirestoreDatabaseRepository.addFavRestaurant(uid, placeId);
    }

    public void deleteFavRestaurant(String uid, String placeId) throws InterruptedException {
        myFirestoreDatabaseRepository.deleteFavRestaurant(uid);
    }

}