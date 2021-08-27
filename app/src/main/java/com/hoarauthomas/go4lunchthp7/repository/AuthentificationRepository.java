package com.hoarauthomas.go4lunchthp7.repository;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

//Only to get data or store and publish it to ViewModel ...

public class AuthentificationRepository {

    //
    private final FirebaseAuth myAuth;

    private MutableLiveData<FirebaseUser> myUser = new MutableLiveData<>();
    private MutableLiveData<Boolean> myUserState = new MutableLiveData<>();

    //constructor called by factory
    public AuthentificationRepository() {
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
        CheckUser();
        return myUser;
    }

    //publish actual user state to VM...
    public MutableLiveData<Boolean> getUserStateFromRepo() {
      //  CheckUser();
        return myUserState;
    }

    //method called from VM to logout...
    public void logOutFromRepo() {


        myAuth.signOut();
        //myAuth.getCurrentUser().ssignOut();
        //     CheckUser();
        //getUserStateFromRepo();

        //CheckUser();
    }

    public Task<Void> signout(Context context)
    {
        return AuthUI.getInstance().signOut(context).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                CheckUser();
            }
        });

    }

}
