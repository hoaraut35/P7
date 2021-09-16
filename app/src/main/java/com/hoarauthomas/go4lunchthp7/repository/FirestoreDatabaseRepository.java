package com.hoarauthomas.go4lunchthp7.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.hoarauthomas.go4lunchthp7.model.firestore.User;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FirestoreDatabaseRepository {

    //added
    private final CollectionReference myBase;
    private static final String COLLECTION_NAME = "users";

    private MutableLiveData<List<User>> myWorkMatesListMedia = new MutableLiveData<>();

    /**
     * constructor called by injection
     */
    public FirestoreDatabaseRepository() {
        this.myBase = FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
        getRestaurantFromFirestore();
      //  setupListenerOnCollection();
    }

    /**
     * get data from firebase
     * @return
     */
    private MutableLiveData<List<User>> getRestaurantFromFirestore()
    {

        myBase.get().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Log.i("[WORK]","Fail to access workmates");
                myWorkMatesListMedia.setValue(null);
            }
        });

        myBase.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                List<User> allWorkMates = new ArrayList<>();

                for (DocumentSnapshot docs : queryDocumentSnapshots) {

                    User myUser = new User();

                    if (docs.get("uid") != null) {
                        myUser.setUid(docs.get("uid").toString());
                    }

                    if (docs.get("urlPicture") != null) {
                        myUser.setUrlPicture(docs.get("urlPicture").toString());
                    }

                    if (docs.get("username") != null) {
                        myUser.setUsername(docs.get("username").toString());
                    }

                    if (docs.get("favoriteRestaurant") != null) {
                        myUser.setFavoriteRestaurant(docs.get("favoriteRestaurant").toString());
                    }

                    //TODO: coorect this
                    if(docs.get("restaurant_liked") != null){

                        List<String> mylist = new ArrayList<>();
                        mylist.addAll((List<String>)docs.get("restaurant_liked"));
                        myUser.setRestaurant_liked(mylist);
                    }

                    allWorkMates.add(myUser);

                }

                myWorkMatesListMedia.setValue(allWorkMates);

            }

        });

        return myWorkMatesListMedia;

    }

    private void setupListenerOnCollection() {

        CollectionReference collectionReference = myBase;

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {

                if (error != null)
                {
                    Log.i("[FIRE]","Erreur " , error );
                    return;
                }

                if (value != null && !value.isEmpty()){

                    getRestaurantFromFirestore();
                }


                /*

                for (DocumentChange dc : value.getDocumentChanges()){

                    switch (dc.getType()){
                        case ADDED:
                            Log.i("[FIRE]","add " );
                            getRestaurantFromFirestore();
                            break;
                        case MODIFIED:
                            Log.i("[FIRE]","modif "  );
                            getRestaurantFromFirestore();
                            break;
                        case REMOVED:
                            Log.i("[FIRE]","remove "  );
                            getRestaurantFromFirestore();
                            break;

                    }
                }

                 */

                Log.i("[FIRE]","Event on databse ...");
            }
        });

    }

    public MutableLiveData<List<User>> getAllWorkMatesListFromRepo()
    {
     //   return myWorkMatesListMedia;
    return    getRestaurantFromFirestore();
    }

    public Task<DocumentSnapshot> getWorkMates(String uid) {
        if (uid != null) {
            return this.myBase.document(uid).get();

        } else {
            return null;
        }
    }

 /*   public void updateFavorite(String place_id) {
        if (place_id != null) {
            this.getUsersCollection().document(uid).update(FAVORITE_RESTAURANT, place_id);
        }
    }

  */

    /*public void addRestaurant(String name) {
        this.getUsersCollection().document().update(TAG_RESTAURANT, name);
    }

     */




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

                    if (documentSnapshot.exists()){
                        Log.i("[CREATE]","deja existant");
                    }else
                    {
                        Log.i("[CREATE]","a créer");
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


    public Task<DocumentSnapshot> checkUser()
    {
        String uid = this.getCurrentUserUID();
        if (uid!= null){
            return this.myBase.document(uid).get();
        }else
        {
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

    public void getDetailPlaceWithId(String placeIdRequested) {

//        CollectionReference users = myBase.document().collection("users");
//
//        Query query =
//        Task<QuerySnapshot> myQuery = this.myBase..getDoc.document(getCurrentUserUID()).get();
//
    }
}