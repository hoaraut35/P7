package com.hoarauthomas.go4lunchthp7.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

//Only to get data from Firestore, store and publish user and user state to ViewModel ...

public class FirebaseAuthentificationRepository {

    public FirebaseAuth myFireBaseAuthInstance;

    private FirebaseUser myFireBaseUser;

    private MutableLiveData<FirebaseUser> myUser = new MutableLiveData<>();
    private MutableLiveData<Boolean> myUserState = new MutableLiveData<>();

    public FirebaseAuthentificationRepository(FirebaseAuth firebaseAuthInstance) {
        this.myFireBaseAuthInstance = firebaseAuthInstance;
        this.myUser.setValue(myFireBaseAuthInstance.getCurrentUser());
        checkActualUserFirebase();
        //
    }

    //to update status user
    public void checkActualUserFirebase() {

        this.myFireBaseUser = myFireBaseAuthInstance.getCurrentUser();

        if (myFireBaseUser == null) {
            myUser.setValue(null);
            myUserState.setValue(false);
        } else {
            myUser.setValue(myFireBaseUser);
            myUserState.setValue(true);
        }
    }

    //to get login state
    public LiveData<Boolean> getLoginState() {
        return myUserState;
    }

    public MutableLiveData<Boolean> getLoggedOutLiveDataNew() {
        return myUserState;
    }

    //to get user object
    public LiveData<FirebaseUser> getUserLiveDataNew() {
        return myUser;
    }

    public void logOut(Context context) {
        myFireBaseAuthInstance.signOut();
        checkActualUserFirebase();
    }

    public LiveData<FirebaseUser> getUserFromVM() {
        return null;// myFireBaseAuthInstance.getCurrentUser();
    }
}
