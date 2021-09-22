package com.hoarauthomas.go4lunchthp7.repository;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseAuthentificationRepository {

    public FirebaseAuth myFireBaseAuthInstance;
    private FirebaseUser myFireBaseUser;

    private MutableLiveData<FirebaseUser> myUser = new MutableLiveData<>();
    private MutableLiveData<Boolean> myUserState = new MutableLiveData<>();

    /**
     * called by depandence injection
     * @param firebaseAuthInstance
     */
    public FirebaseAuthentificationRepository(FirebaseAuth firebaseAuthInstance) {
        this.myFireBaseAuthInstance = firebaseAuthInstance;
        setupAuthentificationListener();
    }

    /**
     * setup listener for authentification event
     */
    public void setupAuthentificationListener() {
        myFireBaseAuthInstance.addAuthStateListener(firebaseAuth -> {
            if (firebaseAuth.getCurrentUser() != null) {
                myUser.setValue(firebaseAuth.getCurrentUser());
                myUserState.setValue(true);
            } else {
                myUser.setValue(null);
                myUserState.setValue(false);
            }
        });
    }

    //to get login state
    public LiveData<Boolean> getLoginState() {
        return myUserState;
    }

    //to publish
    public LiveData<Boolean> getLoggedOutLiveDataNew() {
        return myUserState;
    }

    //to get user object
    public LiveData<FirebaseUser> getUserLiveDataNew() {
        return myUser;
    }

    //to logout
    public void logOut(Context context) {

        myFireBaseAuthInstance.signOut();
       /* myFireBaseAuthInstance.removeAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            }
        });

        */
    }

}
