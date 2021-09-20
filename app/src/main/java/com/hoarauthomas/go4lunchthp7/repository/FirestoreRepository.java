package com.hoarauthomas.go4lunchthp7.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.hoarauthomas.go4lunchthp7.model.firestore.User;

import java.util.ArrayList;
import java.util.List;

public class FirestoreRepository {

    private final CollectionReference myBase;
    private static final String COLLECTION_NAME = "users";
    int millis = 1000;

    /**
     * used to update livedate before publish
     */
    private MutableLiveData<FirestoreUser> myWorkmateFromRepo = new MutableLiveData<>();
    private MutableLiveData<List<FirestoreUser>> myWorkmatesListFromFirestore = new MutableLiveData<>();

    /**
     * constructor called by injection
     */
    public FirestoreRepository() {
        this.myBase = FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
        getAllWorkmatesFromFirestoreRepo();
        setupListenerOnCollection();
    }

    /**
     * get an user from firestore
     */
    private void getWorkmateFromFirestoreRepo() {
        myBase.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<FirestoreUser> myList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        FirestoreUser myFirestoreUsersList = document.toObject(FirestoreUser.class);
                        myList.add(myFirestoreUsersList);
                    }
                } else {
                    Log.i("[FIRESTORE]", "Error on getWorkmateFromFirestoreRepo " + task.getException());
                }
            }
        });
    }

    /**
     * return a user for viewmodel
     * @return
     */

    public LiveData<FirestoreUser> getWorkmateFromRepo() {
        return myWorkmateFromRepo;
    }

    /**
     * get all workmates from firestore
     */
    private void getAllWorkmatesFromFirestoreRepo() {
        myBase.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    List<FirestoreUser> allWorkMates = new ArrayList<>();

                    for (QueryDocumentSnapshot workmateIterate : task.getResult()) {
                        allWorkMates.add(workmateIterate.toObject(FirestoreUser.class));
                    }

                    myWorkmatesListFromFirestore.setValue(allWorkMates);
                } else {
                    myWorkmatesListFromFirestore.setValue(null);
                    Log.i("[FIRESTORE]", "Error on getWorkmatesFromFirestore " + task.getException());
                }
            }
        });
    }

    /**
     * publish th elist of workmates for viewmodel
     *
     * @return
     */

    public LiveData<List<FirestoreUser>> getFirestoreWorkmates() {
        return myWorkmatesListFromFirestore;
    }


    /**
     * thizsis the listener for all collection (to detect new favoreite)
     */
    private void setupListenerOnCollection() {

        myBase.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if (error != null){
                    Log.i("[FIRESTORE]","Error setupListenerOnCollection() " + error);
                }

                if (value != null && !value.isEmpty()){
                    getAllWorkmatesFromFirestoreRepo();
                }
            }
        });
    }



    public Task<DocumentSnapshot> getWorkMates(String uid) {
        if (uid != null) {
            return this.myBase.document(uid).get();

        } else {
            return null;
        }
    }


    public FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }


    public void createUser() {

        FirebaseUser user = getCurrentUser();

        if (user != null) {
            String urlPicture = (user.getPhotoUrl() != null ? user.getPhotoUrl().toString() : null);
            String username = (user.getDisplayName() != null ? user.getDisplayName() : null);
            String uid = (user.getUid() != null ? user.getUid() : null);
            String restaurant = "";
            List<String> restaurant_liked = new ArrayList<>();

            User userToCreate = new User(uid, username, urlPicture, restaurant, restaurant_liked);

            //si utilisateur existant ?
            Task<DocumentSnapshot> future = this.myBase.document(getCurrentUserUID()).get();
            future.addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                    if (documentSnapshot.exists()) {
                        Log.i("[CREATE]", "deja existant");
                    } else {
                        Log.i("[CREATE]", "a créer");
                        myBase.document(uid).set(userToCreate);

                    }


                }
            });


            Task<DocumentSnapshot> userData = getUserData();



          /*      if (documentSnapshot.contains("uid")) ){
                    Log.i("[CREATE]","a creer");
                }else
                {
                    Log.i("[CREATE]","deja la");

                }

           */


               /* if (documentSnapshot.contains("favoriteRestaurant")) {
                //    userToCreate.setFavoriteRestaurant(restaurant);

                } else if (documentSnapshot.contains("urlPicture")) {
                    userToCreate.setUrlPicture(urlPicture);

                } else if (documentSnapshot.contains("username")) {
                    userToCreate.setUsername(username);
                } else if (documentSnapshot.contains("restaurant_liked")) {
        //            userToCreate.setRestaurant_liked(restaurant_liked);
                }

                */


            //
            //  });


        }


    }


    public Task<DocumentSnapshot> checkUser() {
        String uid = this.getCurrentUserUID();
        if (uid != null) {
            return this.myBase.document(uid).get();
        } else {
            return null;
        }

    }

    public Task<DocumentSnapshot> getUserData() {
        String uid = this.getCurrentUserUID();
        if (uid != null) {
            return this.myBase.document(uid).get();
        } else {
            return null;
        }
    }

    public String getCurrentUserUID() {
        FirebaseUser user = getCurrentUser();
        return (user != null) ? user.getUid() : null;
    }

    public Task<Void> addLikedRestaurant(String uid, String placeId) throws InterruptedException {
        myFirestorePause();
        return myBase.document(uid).update("restaurant_liked", FieldValue.arrayUnion(placeId));
    }

    public Task<Void> addFavRestaurant(String uid, String placeId) throws InterruptedException {
        myFirestorePause();
        return myBase.document(uid).update("favoriteRestaurant", placeId);
    }

    public Task<Void> deleteFavRestaurant(String uid) throws InterruptedException {
        myFirestorePause();
        return myBase.document(uid).update("favoriteRestaurant", "");
    }

    public Task<Void> deleteLikedRestaurant(String uid, String placeId) throws InterruptedException {
        myFirestorePause();
        return myBase.document(uid).update("restaurant_liked", FieldValue.arrayRemove(placeId));
    }

    private void myFirestorePause() throws InterruptedException {
        Thread.sleep(millis);
    }

    public Task<DocumentSnapshot> getUserFromDB(String uid) {


        return myBase.document(uid).get();
    }


    public Task<DocumentSnapshot> getUser(String uid) {
        return myBase.document(uid).get();
    }


   /* public static Task<Void> createUser(String uid, String username, String avatar, String restaurant, List<String> restaurant_liked) {
        User userToCreate = new User(uid, username, avatar, restaurant, restaurant_liked);
        return UserHelper.getUsersCollection().document(uid).set(userToCreate);
    }

    */

    public CollectionReference getFirestore() {
        return this.myBase;
    }

    public Task<DocumentSnapshot> getUserFirestoreFromRepo(String uid) {

        return getUser(uid);
    }

    public void reload() {
        //getWorkMatesFromFirestoreRepo();
        getWorkmateFromFirestoreRepo();

    }

    public void updateUserSystem() {
        //getAllWorkMatesListFromRepo();
        getFirestoreWorkmates();
    }
}