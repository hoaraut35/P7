package com.hoarauthomas.go4lunchthp7.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

//Only to get data from Firestore, store and publish user and user state to ViewModel ...

public class FirebaseAuthRepository {

    private FirebaseAuth myAuth;
    private MutableLiveData<FirebaseUser> myUser = new MutableLiveData<>();
    private MutableLiveData<Boolean> myUserState = new MutableLiveData<>();

    public FirebaseAuthRepository(FirebaseAuth firebaseAuth) {
        this.myAuth = firebaseAuth;
        Log.i("[Auth]","Get an instance of Firebase Auth..." + this.myAuth.getUid() + this.myAuth.getCurrentUser().getDisplayName());
      //  this.myAuth = FirebaseAuth.getInstance();
        checkUser();
    }

    public void checkUser() {
        Log.i("[Auth]","Check current user ...");
        if (myAuth.getCurrentUser() != null) {
            Log.i("[Auth]","User log in"+myAuth.getCurrentUser().getDisplayName());
            myUser.setValue(myAuth.getCurrentUser());
            myUserState.setValue(true);
        } else {
            Log.i("[Auth]","User not log in");
            myUser.setValue(null);
            myUserState.setValue(false);
        }
    }

    public MutableLiveData<FirebaseUser> getUserLiveDataNew(){

        return myUser;
    }

    public MutableLiveData<Boolean> getLoggedOutLiveDataNew(){

        return myUserState;
    }

    public void logOut(){
        myAuth.signOut();
        myUserState.postValue(false);
    }

    public LiveData<FirebaseUser> getUserLiveData() {
        return myUser;
    }

}
