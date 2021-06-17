package com.hoarauthomas.go4lunchthp7.data;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hoarauthomas.go4lunchthp7.model.User;

//crud for users
public class FireBaseHelper {


    private static final String COLLECTION_NAME = "users";

    // --- COLLECTION REFERENCE ---

    public static CollectionReference getUsersCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // --- CREATE ---

    public static Task<Void> createUser(String uid, String username, String urlPicture) {
        User userToCreate = new User(uid, username);
        return FireBaseHelper.getUsersCollection().document(uid).set(userToCreate);
    }

    // --- GET ---

    public static Task<DocumentSnapshot> getUser(String uid){
        return FireBaseHelper.getUsersCollection().document(uid).get();
    }

    // --- UPDATE ---

    public static Task<Void> updateUsername(String username, String uid) {
        return FireBaseHelper.getUsersCollection().document(uid).update("username", username);
    }

    public static Task<Void> updateIsMentor(String uid, Boolean isMentor) {
        return FireBaseHelper.getUsersCollection().document(uid).update("isMentor", isMentor);
    }

    // --- DELETE ---

    public static Task<Void> deleteUser(String uid) {
        return FireBaseHelper.getUsersCollection().document(uid).delete();
    }

}
