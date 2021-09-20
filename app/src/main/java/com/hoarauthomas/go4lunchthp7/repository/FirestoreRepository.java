package com.hoarauthomas.go4lunchthp7.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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

    private CollectionReference myBase = null;
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
        getWorkmateFromFirestoreRepo();
        setupListenerOnCollection();
    }

    /**
     * get an user from firestore
     */
    public LiveData<FirestoreUser> getWorkmateFromFirestoreRepo() {
        myBase.document(getCurrentUserUID()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    DocumentSnapshot myWorkmate = task.getResult();

                    if (myWorkmate.exists()) {
                        myWorkmateFromRepo.setValue(myWorkmate.toObject(FirestoreUser.class));

                        //return myWorkmate.toObject(FirestoreUser.class);
                    } else {
                        Log.i("[FIRESTORE]", "Error getWorkmateFromFiresotre on user object");
                    }
                } else {
                    Log.i("[FIRESTORE]", "Error getWorkmateFromFiresotre on result data empty");
                }
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("[FIRESTORE]", "Error getWorkmateFromFiresotre on result data empty" + e);
            }
        });

        return myWorkmateFromRepo;
    }

    /**
     * return a user for viewmodel
     *
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
     * this is the listener for all collection (to detect new favoreite)
     */
    private void setupListenerOnCollection() {

        myBase.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if (error != null) {
                    Log.i("[FIRESTORE]", "Error setupListenerOnCollection() " + error);
                }

                if (value != null && !value.isEmpty()) {
                    getAllWorkmatesFromFirestoreRepo();
                }
            }
        });
    }


    /**
     * create a new user in Firestore
     *
     * @return
     */
    public void createUser() {

        FirebaseUser myWorkmateToWrite = getCurrentUser();

        String urlPicture = (myWorkmateToWrite.getPhotoUrl() != null ? myWorkmateToWrite.getPhotoUrl().toString() : null);
        String username = (myWorkmateToWrite.getDisplayName() != null ? myWorkmateToWrite.getDisplayName() : null);
        String uid = (myWorkmateToWrite.getUid() != null ? myWorkmateToWrite.getUid() : null);
        String restaurant = "";
        List<String> restaurant_liked = new ArrayList<>();

        User userToCreate = new User(uid, username, urlPicture, restaurant, restaurant_liked);

        myBase.document(myWorkmateToWrite.getUid()).set(userToCreate).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.i("[FIRESTORE]", "Ecriture utilisateur dans la base ok");
            }
        })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("[FIRESTORE]", "Error on wrtiting new user to dabase with createUSer()");
                    }
                });

    }

    public FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public String getCurrentUserUID() {
        FirebaseUser user = getCurrentUser();
        return (user != null) ? user.getUid() : null;
    }

    //CRUD

    public List<QuerySnapshot> getWorkmatesByPlaceId(String placeId){
        myBase.whereEqualTo("favorite_restaurant",placeId).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error!=null){
                    Log.i("[FIRESTORE]","Error on getWorkmatesByPlaceId()");
                }else {

                  //  return value.getDocuments();



                }



            }
        });

        return null;
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


    public Task<DocumentSnapshot> getUser(String uid) {
        return myBase.document(uid).get();
    }


    public CollectionReference getFirestore() {
        return this.myBase;
    }

    public Task<DocumentSnapshot> getUserFirestoreFromRepo(String uid) {
        return getUser(uid);
    }


}