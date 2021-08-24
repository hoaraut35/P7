package com.hoarauthomas.go4lunchthp7.api;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hoarauthomas.go4lunchthp7.model.firestore.User;

import java.util.List;

//crud for users
public class UserHelper {

    private static final String COLLECTION_NAME = "users";

    // --- COLLECTION REFERENCE ---

    public static CollectionReference getUsersCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // --- CREATE ---

    public static Task<Void> createUser(String uid, String username, String avatar, String restaurant, List<String> restaurant_liked) {
        User userToCreate = new User(uid, username, avatar, restaurant, restaurant_liked);
        return UserHelper.getUsersCollection().document(uid).set(userToCreate);
    }

    // --- GET ---

    public static Task<DocumentSnapshot> getUser(String uid) {
        return UserHelper.getUsersCollection().document(uid).get();
    }


    // --- UPDATE ---

    public static Task<Void> updateUsername(String username, String uid) {
        return UserHelper.getUsersCollection().document(uid).update("username", username);
    }

    public static Task<Void> updateIsMentor(String uid, Boolean isMentor) {
        return UserHelper.getUsersCollection().document(uid).update("isMentor", isMentor);
    }


    public static Task<Void> updateFavRestaurant(String uid, String placeId) {
        return UserHelper.getUsersCollection().document(uid).update("favoriteRestaurant", placeId);
    }


    //TODO : tableau favoris
    public static Task<Void> addLikedRestaurant(String uid, String placeId) {


        return UserHelper.getUsersCollection().document(uid).update("restaurant_liked", placeId);
    }

    public static Task<Void> getLikedRestaurant(String uid) {
        // DocumentReference docRef = UserHelper.getUsersCollection().document(uid);
        return null;
    }


    // --- DELETE ---

    public static Task<Void> deleteUser(String uid) {
        return UserHelper.getUsersCollection().document(uid).delete();
    }

}
