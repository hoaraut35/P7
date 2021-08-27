package com.hoarauthomas.go4lunchthp7.repository;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

//Only to get data from Firestore, store and publish user and userstate to ViewModel ...

public class AuthentificationRepository {

    private final FirebaseAuth myAuth;

    private MutableLiveData<FirebaseUser> myUser = new MutableLiveData<>();
    private MutableLiveData<Boolean> myUserState = new MutableLiveData<>();

    public AuthentificationRepository() {
        this.myAuth = FirebaseAuth.getInstance();
        checkUser();
    }

    //**********************************************************************************************
    // PRIVATE
    //**********************************************************************************************
    private void checkUser() {
        if (myAuth.getCurrentUser() != null) {
            myUser.postValue(myAuth.getCurrentUser());
            myUserState.postValue(true);
        } else {
            myUser.postValue(null);
            myUserState.postValue(false);
        }
    }

    //**********************************************************************************************
    // PUBLIC
    //**********************************************************************************************

    public MutableLiveData<FirebaseUser> getUserFromRepo() {
        checkUser();
        return this.myUser;
    }

    public MutableLiveData<Boolean> getUserStateFromRepo() {
        checkUser();
        return myUserState;
    }

    public Task<Void> signOut(Context context) {
        return AuthUI.getInstance().signOut(context).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                checkUser();
            }
        });
    }

    public Task<Void> deleteUser(Context context){
        return AuthUI.getInstance().delete(context);
    }

}
