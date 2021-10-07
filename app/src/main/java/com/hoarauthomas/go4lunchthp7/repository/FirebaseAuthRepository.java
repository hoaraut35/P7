package com.hoarauthomas.go4lunchthp7.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseAuthRepository {

    public final FirebaseAuth myFireBaseAuthInstance;

    private final MutableLiveData<FirebaseUser> myUser = new MutableLiveData<>();
    private final MutableLiveData<Boolean> myUserState = new MutableLiveData<>();

    public FirebaseAuthRepository(FirebaseAuth firebaseAuthInstance) {
        this.myFireBaseAuthInstance = firebaseAuthInstance;
        setupAuthentificationListener();
    }

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

    public LiveData<Boolean> getFirebaseAuthUserStateFromRepo() {
        return myUserState;
    }

    public LiveData<FirebaseUser> getFirebaseAuthUserFromRepo() {
        return myUser;
    }

    public void logOut() {
        myFireBaseAuthInstance.signOut();
    }

}