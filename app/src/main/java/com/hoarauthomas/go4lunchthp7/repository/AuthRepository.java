package com.hoarauthomas.go4lunchthp7.repository;

import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

//Only to get data or store and publish it to ViewModel ...

public class AuthRepository {

    private final FirebaseAuth myAuth;
    private final MutableLiveData<FirebaseUser> myUser = new MutableLiveData<>();
    private final MutableLiveData<Boolean> myUserState = new MutableLiveData<>();

    //constructor called by factory
    public AuthRepository() {
        myAuth = FirebaseAuth.getInstance();//get instance of FirebaseAuth
        CheckUser();
    }

    //internal function
    private void CheckUser() {
        if (myAuth.getCurrentUser() != null) {
            myUser.postValue(myAuth.getCurrentUser());//get actual user object from FirebaseAuth
            myUserState.postValue(true);//set state of login
        } else {
            myUser.postValue(null);//no user logged
            myUserState.postValue(false);//set state of login
        }
    }

    //publish actual user object to VM...
    public MutableLiveData<FirebaseUser> getUserFromRepo() {
        return myUser;
    }

    //publish actual user state to VM...
    public MutableLiveData<Boolean> getUserStateFromRepo() {
        return myUserState;
    }

    //method called from VM to logout...
    public void logOutFromRepo() {
        myAuth.signOut();
        CheckUser();
    }





}
