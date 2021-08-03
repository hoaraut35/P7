package com.hoarauthomas.go4lunchthp7.repository;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class WorkMatesRepository {

    //Fields in FireStore
    private static final String COLLECTION_NAME = "users";
    private static final String FAVORITE_RESTAURANT = "fav_restaurant_id";
    private static final String TAG_RESTAURANT = "restaurant_liked";


    private String uid;

    public WorkMatesRepository() {
    }


    private CollectionReference getUsersCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }


    //----------------------------------------------------------------------------------------------
    public Task<DocumentSnapshot> getWorkMates(String uid) {
        if (uid != null) {
            return this.getUsersCollection().document(uid).get();

        }else
        {
            return null;
        }
    }

    public void updateFavorite(String place_id) {
        if (place_id != null) {
            this.getUsersCollection().document(uid).update(FAVORITE_RESTAURANT, place_id);
        }
    }

    public void addRestaurant(String name)
    {
        this.getUsersCollection().document().update(TAG_RESTAURANT,name);
    }

    public void removeRestaurant()
    {

    }

}