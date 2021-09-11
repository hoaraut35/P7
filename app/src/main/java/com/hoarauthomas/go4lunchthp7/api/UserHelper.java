package com.hoarauthomas.go4lunchthp7.api;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.hoarauthomas.go4lunchthp7.model.firestore.User;

import java.util.List;

//crud for users
public class UserHelper {

    private static final String COLLECTION_NAME = "users";

    // --- COLLECTION REFERENCE ---

    public static void getListenerOnUser(String uid) {

        DocumentReference docRef = FirebaseFirestore.getInstance().document(uid).collection(COLLECTION_NAME).document(uid);

        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable DocumentSnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {

                if (error != null) {

                    return;
                }

                if (value != null && value.exists()) {
                    Log.i("FIRE", "Event on user" + value.getData());
                } else {
                    Log.i("FIRE", "Event null on user firestore");
                }

            }
        });

    }


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
    public static void addLikedRestaurant(String uid, String placeId) {

        //get user collection
        Task<DocumentSnapshot> myDoc = getUsersCollection().document(uid).get();


        //add listener
        myDoc.addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                List<String> restaurant_liked_list = (List<String>) myDoc.getResult().get("restaurant_liked");
                restaurant_liked_list.add(placeId);
                Log.i("[LOGIN]", "restaura liké" + myDoc.getResult().get("restaurant_liked") + restaurant_liked_list.size());
                //UserHelper.getUsersCollection().document(uid).collection("restaurant_liked").add(temp);
                UserHelper.getUsersCollection().document(uid).update("restaurant_liked", FieldValue.arrayUnion(placeId));
            }
        });

        myDoc.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                    //
            }
        });

      //  return true;
        //    return null;
    }

    public static Task<Void> getLikedRestaurant(String uid) {
        // DocumentReference docRef = UserHelper.getUsersCollection().document(uid);
        return null;
    }


    // --- DELETE ---

    public static Task<Void> deleteUser(String uid) {
        return UserHelper.getUsersCollection().document(uid).delete();
    }


    public static Task<Void> deleteFavRestaurant(String uid) {
        return UserHelper.getUsersCollection().document(uid).update("favoriteRestaurant", "");
    }

    public static Task<Void> deleteLikedRestaurant(String uid, String placeId) {
        return UserHelper.getUsersCollection().document(uid).update("restaurant_liked", FieldValue.arrayRemove(placeId));

    }
}
