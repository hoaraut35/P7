package com.hoarauthomas.go4lunchthp7.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.facebook.internal.Mutable;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

//Only to get data and publish it to ViewModel ...
public class AuthRepository {

    private FirebaseAuth myAuth;
    private MutableLiveData<FirebaseUser> myUser;
    private MutableLiveData<Boolean> myUserState;

    //constructor
    public AuthRepository() {
        myAuth = FirebaseAuth.getInstance();
        myUser = new MutableLiveData<>();
        myUserState = new MutableLiveData<>();

        if (myAuth.getCurrentUser() != null){
            Log.i("[THOMAS]", "- Appel Repository Authentification [user already logged]" );
            myUser.postValue(myAuth.getCurrentUser());
            myUserState.postValue(true);
        }else
        {
            Log.i("[THOMAS]", "- Appel Repository Authentification [user not logged]" );
            myUserState.postValue(false);
        }
    }

    //livedata for viewmodel
    public MutableLiveData<FirebaseUser> getUserLiveData(){
        return myUser;
    }
    public MutableLiveData<Boolean> getMyUserState(){
        return myUserState;
    }

    //method for viewmodel
    public void logOut(){
        myAuth.signOut();
        myUserState.postValue(false);
    }
}
