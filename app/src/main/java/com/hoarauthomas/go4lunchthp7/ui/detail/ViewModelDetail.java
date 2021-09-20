package com.hoarauthomas.go4lunchthp7.ui.detail;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.hoarauthomas.go4lunchthp7.model.placedetails2.ResultDetailRestaurant;
import com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo;
import com.hoarauthomas.go4lunchthp7.repository.FirebaseAuthentificationRepository;
import com.hoarauthomas.go4lunchthp7.repository.FirestoreRepository;
import com.hoarauthomas.go4lunchthp7.repository.FirestoreUser;
import com.hoarauthomas.go4lunchthp7.repository.RestaurantsRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

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
      //  LiveData<FirestoreUser> myWorkmateFromRepo = this.myFirestoreRepository.getWorkmateFromFirestoreRepo();

    /*    myScreenDetailMediator.addSource(myWorkmateFromRepo, firestoreUser -> {
            if (firestoreUser == null) return;
            logicWork(myRestaurantsListFromRepo.getValue(), myWorkMatesListFromRepo.getValue(), myRestaurantDetailFromRepo.getValue(), myUserFromRepo.getValue(), placeIdRequest.getValue(), myWorkmateFromRepo.getValue());
        });

     */

        myScreenDetailMediator.addSource(myWorkMatesListFromRepo, firestoreUsers -> {
            if (firestoreUsers == null) return;
            logicWork(myRestaurantsListFromRepo.getValue(), firestoreUsers, myRestaurantDetailFromRepo.getValue(), myUserFromRepo.getValue(), placeIdRequest.getValue());
        });

        myScreenDetailMediator.addSource(myRestaurantsListFromRepo, restaurantPojo -> {
            if (myRestaurantsListFromRepo == null) return;
            ViewModelDetail.this.logicWork(restaurantPojo, myWorkMatesListFromRepo.getValue(), myRestaurantDetailFromRepo.getValue(), myUserFromRepo.getValue(), placeIdRequest.getValue());
        });

        myScreenDetailMediator.addSource(myRestaurantDetailFromRepo, resultDetailRestaurant -> {
            if (myRestaurantDetailFromRepo == null) return;
            ViewModelDetail.this.logicWork(myRestaurantsListFromRepo.getValue(), myWorkMatesListFromRepo.getValue(), resultDetailRestaurant, myUserFromRepo.getValue(), placeIdRequest.getValue());
        })
        ;

        myScreenDetailMediator.addSource(myUserFromRepo, firebaseUser -> {
            if (myUserFromRepo == null) return;
            ViewModelDetail.this.logicWork(myRestaurantsListFromRepo.getValue(), myWorkMatesListFromRepo.getValue(), myRestaurantDetailFromRepo.getValue(), firebaseUser, placeIdRequest.getValue());
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
            String placeIdRequested){
         //   @Nullable FirestoreUser myWorkmate) {


     //   if (myWorkmate == null ) return;


        //create an ViewState detail object for ui
        ViewStateDetail myScreen = new ViewStateDetail();

        //list of workmates
        List<FirestoreUser> myWorkMatesDetailList = new ArrayList<>();

        for (FirestoreUser myItem : workmatesList){
            if (myItem.getFavoriteRestaurant().equals(placeIdRequested)){
                myWorkMatesDetailList.add(myItem);
            }
        }

        myScreen.setListWorkMates(myWorkMatesDetailList);

        //
        for (RestaurantPojo myItem : restaurantsList){
            if (myItem.getPlaceId().equals(placeIdRequested)){

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

        /*Log.i("[GOOD]", "Extraction environnement : ");
        Log.i("[GOOD]", " - utilisateur connecté : " + value.get("username") + " " + value.get("uid"));
        Log.i("[GOOD]", " - restaurant favoris : " + value.get("favoriteRestaurant"));
        Log.i("[GOOD]", " - restaurant affiché : " + placeIdRequested);

         */


      /*  if (myFirestoreRepository.getWorkmateFromFirestoreRepo().getValue().getFavoriteRestaurant().equals(placeIdRequested)){
       //if (myWorkmate.getFavoriteRestaurant().equals(placeIdRequested)){
            myScreen.setFavorite(true);
       }else
        {
         //   myScreen.setFavorite(false);
        }

       */


     //   if (myWorkmate.getRestaurant_liked().contains(placeIdRequested)){
            myScreen.setLiked(true);
       // }else
       // {
          //  myScreen.setLiked(false);
        //}


        //favorite btn setup
       /* if (value.get("favoriteRestaurant").equals(placeIdRequested)) {
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

        */





        //set empty list for recycler else it crash
        myScreen.setListWorkMates(myWorkMatesDetailList);


        //final result for viewstate object
        myScreenDetailMediator.setValue(myScreen);






        //move to repository !
    /*    myFirestoreRepository.getFirestore().whereEqualTo("favoriteRestaurant",placeIdRequested).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@androidx.annotation.Nullable QuerySnapshot value, @androidx.annotation.Nullable FirebaseFirestoreException error) {

                if (value == null || value.isEmpty()) {
                    myScreen.setListWorkMates(myWorkMatesDetailList);
                    return;
                }

                myWorkMatesDetailList.clear();

                for (int i =0; i<value.size();i++){

                    Log.i("[GOOD]", "extract workmates" + value.getDocuments().get(i).get("username"));
                    myWorkMatesDetailList.add(value.getDocuments().get(i).toObject(FirestoreUser.class));
                }
                myScreen.setListWorkMates(myWorkMatesDetailList);

            }
        });

     */

        //move to repository!
/*        myFirestoreRepository.getFirestore().document(myFirestoreRepository.getCurrentUserUID()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
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






 */




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

    public void addtFavRestaurant(String uid, String placeId) throws InterruptedException {
        myFirestoreRepository.addFavRestaurant(uid, placeId);
    }

    public void deleteFavRestaurant(String uid, String placeId) throws InterruptedException {
        myFirestoreRepository.deleteFavRestaurant(uid);
    }

}