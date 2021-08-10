package com.hoarauthomas.go4lunchthp7.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.hoarauthomas.go4lunchthp7.BuildConfig;
import com.hoarauthomas.go4lunchthp7.Result2;
import com.hoarauthomas.go4lunchthp7.model.firestore.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorkMatesRepository {

    //Fields in FireStore
    private static final String COLLECTION_NAME = "users";
    private static final String FAVORITE_RESTAURANT = "fav_restaurant_id";
    private static final String TAG_RESTAURANT = "restaurant_liked";

    //to expose list to viewmodel
    private final List<User> allWorkMates = new ArrayList<>();

    private String uid;

    //init data on startup viewmodelfactory
    public WorkMatesRepository() {

        Log.i("[WORK]", "- Appel Repository WorkMates...");

        getUsersCollection().get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot docs : queryDocumentSnapshots) {

                    User user = new User();

                    user.setUsername(docs.get("fullName").toString());

                    allWorkMates.add(user);


                    Log.i("[WORK]", "" + allWorkMates.size());

                }
            }
        });

    }

    //this is livedata to publish detail of restaurant to the viewmodel ...
    public LiveData<List<User>> getAllWorkMates() {

        Log.i("[WORK]", "- getAllWorkMates from rpeo " + allWorkMates.size() + "  added ..." );

        final MutableLiveData<List<User>> data = new MutableLiveData<>();
        data.postValue(allWorkMates);
        return data;

    }



    //
    private CollectionReference getUsersCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
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

    public void createWorkMate(String uid) {
        //   FirebaseUser user = getCurrentUser();


    }
}