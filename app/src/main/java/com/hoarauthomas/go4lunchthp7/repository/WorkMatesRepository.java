package com.hoarauthomas.go4lunchthp7.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.hoarauthomas.go4lunchthp7.model.firestore.User;

import java.util.ArrayList;
import java.util.List;

//get data from repository

public class WorkMatesRepository {

    //Collections in Firestore...
    private static final String COLLECTION_NAME = "users";

    //Fields in collection ...
    private static final String FAVORITE_RESTAURANT = "fav_restaurant_id";
    private static final String TAG_RESTAURANT = "restaurant_liked";

    //to expose list to viewmodel
    private final List<User> allWorkMates = new ArrayList<>();


    private String uid;

    //get an instance from firestore ...
    private CollectionReference getUsersCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    //init data on startup viewmodelfactory
    public WorkMatesRepository() {

        Log.i("[WORK]", "- Appel du repository WorkMates ...");

        getUsersCollection().get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for (DocumentSnapshot docs : queryDocumentSnapshots) {

                    User myUser = new User();

                    if (docs.get("urlPicture").toString() != null) {
                        Log.i("[WORK]", "Avatar : " + docs.get("urlPicture").toString());
                        myUser.setUrlPicture(docs.get("urlPicture").toString());
                    }
                    if (docs.get("username").toString() != null) {
                        Log.i("[WORK]", "Nom complet : " + docs.get("username").toString());
                        myUser.setUsername(docs.get("username").toString());
                    }
                    if (docs.get("favoriteRestaurant").toString() != null) {
                        Log.i("[WORK]", "Restaurant favoris : " + docs.get("favoriteRestaurant").toString());
                        myUser.setFavoriteRestaurant(docs.get("favoriteRestaurant").toString());
                    }

                    allWorkMates.add(myUser);

                }

                Log.i("[WORK]", "Total utilisateur du système : " + allWorkMates.size());

            }
        });
    }

    //this is livedata to publish detail of restaurant to the viewmodel ...
    public LiveData<List<User>> getAllWorkMates() {
        Log.i("[WORK]", "- getAllWorkMates from repo " + allWorkMates.size() + "  added ...");
        final MutableLiveData<List<User>> data = new MutableLiveData<>();
        data.postValue(allWorkMates);
        return data;
    }


    //----------------------------------------------------------------------------------------------
    //CRUD

    public Task<DocumentSnapshot> getWorkMates(String uid) {
        if (uid != null) {
            return this.getUsersCollection().document(uid).get();

        } else {
            return null;
        }
    }

    public void updateFavorite(String place_id) {
        if (place_id != null) {
            this.getUsersCollection().document(uid).update(FAVORITE_RESTAURANT, place_id);
        }
    }

    public void addRestaurant(String name) {
        this.getUsersCollection().document().update(TAG_RESTAURANT, name);
    }

    public void removeRestaurant() {

    }


    public FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public void createUser() {


        FirebaseUser user = getCurrentUser();
        Log.i("[FIRE]", "Create user from workmates > " + user.getUid() + " " + user.getDisplayName() + " " + user.getPhotoUrl());

        if (user != null) {

            String urlPicture = (user.getPhotoUrl() != null ? user.getPhotoUrl().toString() : null);
            String username = (user.getDisplayName() != null ? user.getDisplayName().toString() : null);
            String uid = (user.getUid() != null ? user.getUid().toString() : null);

            User userToCreate = new User(uid, username, urlPicture);

            Task<DocumentSnapshot> userData = getUserData();

            //if user already exist alors update data...
            userData.addOnSuccessListener(documentSnapshot -> {

                if (documentSnapshot.contains("favoriteRestaurant")) {
                    userToCreate.setFavoriteRestaurant("cool");

                } else if (documentSnapshot.contains("urlPicture")) {
                    userToCreate.setUrlPicture(urlPicture);

                } else if (documentSnapshot.contains("username")) {
                    userToCreate.setUsername(username);
                }

                this.getUsersCollection().document(uid).set(userToCreate);
            });


        }
    }

    public Task<DocumentSnapshot> getUserData() {
        String uid = this.getCurrentUserUID();
        if (uid != null) {
            return this.getUsersCollection().document(uid).get();
        } else {
            return null;
        }
    }

    public String getCurrentUserUID() {
        FirebaseUser user = getCurrentUser();
        return (user != null) ? user.getUid() : null;
    }


    public LiveData<List<User>> getAllWorkMatesByRestaurant(String id) {

     /*   getUsersCollection().get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for (DocumentSnapshot docs : queryDocumentSnapshots) {

                    User user = new User();

                    //  if (docs.get("urlPicture").toString() != null){
                    //  user.setUrlPicture(docs.get("urlPicture").toString());
                    //  }


//                    user.setUsername(docs.get("fullName").toString());

                    //    user.setFavoriteRestaurant((docs.get("favoriteRestaurant").toString()));

                  //  allWorkMatesBy.add(user);



                    Log.i("[WORK]", "" + allWorkMates.size());

                }
            }


        });

      */
        //return (LiveData<List<User>>) allWorkMates;
        return null;

    }
}