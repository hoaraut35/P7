package com.hoarauthomas.go4lunchthp7.repository;

import android.content.Context;

import androidx.annotation.NonNull;
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
        myUser.setValue(myFireBaseAuthInstance.getCurrentUser());
        //this.myUser.setValue(myFireBaseAuthInstance.getCurrentUser());
        loginListener();
    }

    public void loginListener() {

        myFireBaseAuthInstance.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if (firebaseAuth.getCurrentUser() != null) {

                    myUser.setValue(firebaseAuth.getCurrentUser());
                    myUserState.setValue(true);
                }                                                               //  Log.i("[Auth]", "Utilisateur : " + myFireBaseUser.getDisplayName());
                else {

                    myUser.setValue(null);
                    myUserState.setValue(false);

                }


            }
        });

    }

    //to update status user
    public void checkActualUserFirebase() {

        this.myFireBaseUser = myFireBaseAuthInstance.getCurrentUser();


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

    public void updateUser() {
    }
}
