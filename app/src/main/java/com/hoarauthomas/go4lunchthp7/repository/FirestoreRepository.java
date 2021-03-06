package com.hoarauthomas.go4lunchthp7.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.hoarauthomas.go4lunchthp7.model.FirestoreUser;
import com.hoarauthomas.go4lunchthp7.model.firestore.User;

import java.util.ArrayList;
import java.util.List;

public class FirestoreRepository {

    private final CollectionReference myBase;
    private static final String COLLECTION_NAME = "users";

    private final MutableLiveData<List<FirestoreUser>> myWorkmatesListFromFirestore = new MutableLiveData<>(null);
    private final MutableLiveData<FirestoreUser> myWorkmateFromRepo = new MutableLiveData<>();
    private final MutableLiveData<FirestoreUser> myActualUser = new MutableLiveData<>(null);

    public FirestoreRepository() {
        this.myBase = FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
        setupListenerOnCollection();
    }

    public void setupListeners() {
        setupListenerOnCollection();
        getUserActual();

        if (getCurrentUser() != null) {
            setupListenerWorkmateFromFirestoreRepo();
        }

    }

    public void setupListenerWorkmateFromFirestoreRepo() {

        myBase.document(getCurrentUser().getUid()).addSnapshotListener((value, error) -> {
            if (error != null) {
                Log.i("[FIRESTORE]", "Error on listener for user");
            }
            if (value != null && value.exists()) {
                myWorkmateFromRepo.setValue(value.toObject(FirestoreUser.class));
            }
        });
    }


    public void getUserActual() {

        myBase.document(getCurrentUser().getUid()).addSnapshotListener((value, error) -> {

            if (value != null && value.exists()) {
                Log.i("[USER]", "Actual user in firestore " + value.get("username"));
                myActualUser.setValue(value.toObject(FirestoreUser.class));
            } else {
                Log.i("[USER]", "Actual user in firestore error");
                myActualUser.setValue(null);
            }

        });
    }

    public LiveData<FirestoreUser> getPublicUSerFirestore() {
        return myActualUser;
    }

    public MutableLiveData<FirestoreUser> getWorkmateFromRepo() {
        return myWorkmateFromRepo;
    }

    private void setupListenerOnCollection() {
        myBase.addSnapshotListener((value, error) -> {
            if (error != null) {
                Log.i("[FIRESTORE]", "Error setupListenerOnCollection() " + error);
            }
            if (value != null && !value.isEmpty()) {
                Log.i("[FIRESTORE]", "Event sur la base ");

                List<FirestoreUser> allWorkMates = new ArrayList<>();

                for (DocumentSnapshot workmateIterate : value.getDocuments()) {
                    allWorkMates.add(workmateIterate.toObject(FirestoreUser.class));
                }
                myWorkmatesListFromFirestore.setValue(allWorkMates);
            }
        });
    }

    public Task<DocumentSnapshot> getMyUserByUIDFromFirestore(String uid) {
        return myBase.document(uid).get();
    }

    public Task<QuerySnapshot> getAllUsersByPlaceIdFromFirestore(String restaurantId) {
        return myBase.whereEqualTo("favoriteRestaurant", restaurantId).get();
    }

    public LiveData<List<FirestoreUser>> getFirestoreWorkmates() {
        return myWorkmatesListFromFirestore;
    }

    public void createUser() {
        myBase.document(getCurrentUser().getUid()).get().addOnSuccessListener(documentSnapshot -> {
            if (!documentSnapshot.exists()) {
                FirebaseUser myWorkmateToWrite = getCurrentUser();
                String urlPicture = (myWorkmateToWrite.getPhotoUrl() != null ? myWorkmateToWrite.getPhotoUrl().toString() : null);
                String username = (myWorkmateToWrite.getDisplayName() != null ? myWorkmateToWrite.getDisplayName() : null);
                String uid = (myWorkmateToWrite.getUid() != null ? myWorkmateToWrite.getUid() : null);
                String restaurant = "";
                List<String> restaurant_liked = new ArrayList<>();

                User userToCreate = new User(uid, username, urlPicture, restaurant, restaurant_liked);

                myBase.document(myWorkmateToWrite.getUid()).set(userToCreate)
                        .addOnSuccessListener(unused -> Log.i("[FIRESTORE]", "Write user in database"))

                        .addOnFailureListener(e -> Log.i("[FIRESTORE]", "Error on writing new user to database with createUSer()"));

            }
        });
    }

    public FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public void updateFavRestaurant(Boolean mFavorite, String mPlaceId, String mWorkmate) {

        Log.i("", "" + mFavorite + mPlaceId + mWorkmate);
        if (!mFavorite) {

            myBase.document(mWorkmate).update("favoriteRestaurant", mPlaceId)
                    .addOnSuccessListener(unused -> Log.i("[FIRESTORE]", "Add du favorite ok"))
                    .addOnFailureListener(e -> Log.i("[FIRESTORE]", "Add favorite impossible : " + e));


        } else {
            myBase.document(mWorkmate).update("favoriteRestaurant", "")

                    .addOnSuccessListener(unused -> Log.i("[FIRESTORE]", "Suppression du favorite ok"))
                    .addOnFailureListener(e -> Log.i("[FIRESTORE]", "Suppression favorite impossible : " + e));

        }

    }

    public void updateLikeRestaurant(Boolean mLike, String mPlaceId, String mWorkmate) {

        if (!mLike) {

            myBase.document(mWorkmate).update("restaurant_liked", FieldValue.arrayUnion(mPlaceId))
                    .addOnSuccessListener(unused -> Log.i("[FIRESTORE]", "Add de like sur restaurant ok"))
                    .addOnFailureListener(e -> Log.i("[FIRESTORE]", "Add du like fail "));
        } else {
            myBase.document(mWorkmate).update("restaurant_liked", FieldValue.arrayRemove(mPlaceId))
                    .addOnSuccessListener(unused -> Log.i("[FIRESTORE]", "Remove like ok"))
                    .addOnFailureListener(e -> Log.i("[FIRESTORE]", "Remove like restaurant fail"));
        }
    }

}