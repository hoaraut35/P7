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
import com.google.firebase.firestore.QuerySnapshot;
import com.hoarauthomas.go4lunchthp7.model.firestore.User;

import java.util.List;

//crud for users
public class UserHelper {

    private static final String COLLECTION_NAME = "users";

    // --- COLLECTION REFERENCE ---

//    public static void getListenerOnUser(String uid) {
//
//        DocumentReference docRef = FirebaseFirestore.getInstance().document(uid).collection(COLLECTION_NAME).document(uid);
//
//        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable DocumentSnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
//
//                if (error != null) {
//
//                    return;
//                }
//
//                if (value != null && value.exists()) {
//                    Log.i("FIRE", "Event on user" + value.getData());
//
//
//
//
//                } else {
//                    Log.i("FIRE", "Event null on user firestore");
//                }
//
//            }
//        });
//
//    }


    public static CollectionReference getUsersCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    public static Task<Void> createUser(String uid, String username, String avatar, String restaurant, List<String> restaurant_liked) {
        User userToCreate = new User(uid, username, avatar, restaurant, restaurant_liked);
        return UserHelper.getUsersCollection().document(uid).set(userToCreate);
    }

    public static Task<DocumentSnapshot> getUser(String uid) {
        return UserHelper.getUsersCollection().document(uid).get();
    }









    public static Task<Void> addRestaurantLiked(String uid, String place){


        return UserHelper.getUsersCollection().document(uid).update("restaurant_liked",FieldValue.arrayUnion(place));
    }

    public static void addLikedRestaurant(String uid, String placeId) {



        //get workmate document
        Task<DocumentSnapshot> myWorkMateDocument = getUsersCollection().document(uid).get();

        //add listener to this request
        myWorkMateDocument.addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {


                if (documentSnapshot.contains("restaurant_liked")){

                //    documentSnapshot.toObject()get

                    //documentSnapshot.get("restaurant_liked").
                        }




//                List<String> myRestaurantsListUser = documentSnapshot.getData().get("restaurant_liked");

         //       List<String> restaurant_liked_list = (List<String>) myWorkMateDocument.getResult().get("restaurant_liked");
           //     restaurant_liked_list.add(placeId);
             //   Log.i("[LOGIN]", "restaura liké" + myWorkMateDocument.getResult().get("restaurant_liked") + restaurant_liked_list.size());
                //UserHelper.getUsersCollection().document(uid).collection("restaurant_liked").add(temp);
                UserHelper.getUsersCollection().document(uid).update("restaurant_liked", FieldValue.arrayUnion(placeId));
            }
        });


        myWorkMateDocument.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                    //
            }
        });


       // myWorkMateDocument.getResult()

    }

    public static Task<Void> getLikedRestaurant(String uid) {
        // DocumentReference docRef = UserHelper.getUsersCollection().document(uid);
        return null;
    }


    /**
     * update favorite restaurant
     * @param uid user id
     * @param placeId place id
     * @return
     */
    public static Task<Void> addFavRestaurant(String uid, String placeId) {
        return UserHelper.getUsersCollection().document(uid).update("favoriteRestaurant", placeId);
    }

    /**
     * delete a restaurant in firesotree database
     * @param uid
     * @return
     */
    public static Task<Void> deleteFavRestaurant(String uid) {
        return UserHelper.getUsersCollection().document(uid).update("favoriteRestaurant", "");
    }

    /**
     * delete liked restaurant in firestore database
     * @param uid user id
     * @param placeId restaurant id
     * @return
     */
    public static Task<Void> deleteLikedRestaurant(String uid, String placeId) {
        return UserHelper.getUsersCollection().document(uid).update("restaurant_liked", FieldValue.arrayRemove(placeId));
    }
}
