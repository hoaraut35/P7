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
    private MutableLiveData<FirebaseUser> myUser = new MutableLiveData<>(null);
    private MutableLiveData<Boolean> myUserState = new MutableLiveData<>(false);

    public FirebaseAuthRepository() {

        Log.i("[Auth]","Get an instance of Firebase Auth...");
        this.myAuth = FirebaseAuth.getInstance();
        this.myUser = new MutableLiveData<>();
        this.myUserState = new MutableLiveData<>();

        if (myAuth.getCurrentUser() != null){
            myUser.postValue(myAuth.getCurrentUser());
            myUserState.postValue(false);
        }

        checkUser();

    }

    public void checkUser() {
        Log.i("[Auth]","Check current user ...");
        if (myAuth.getCurrentUser() != null) {
            Log.i("[Auth]","User log in");
            myUser.setValue(myAuth.getCurrentUser());
            myUserState.setValue(true);
        } else {
            Log.i("[Auth]","User not log in");
            myUser.setValue(null);
            myUserState.setValue(false);
        }
    }

    //added
    public MutableLiveData<FirebaseUser> getUserLiveDataNew(){
        return myUser;
    }

    //added
    public MutableLiveData<Boolean> getLoggedOutLiveDataNew(){
        return myUserState;
    }

    //added
    public void logOut(){
        myAuth.signOut();
        myUserState.postValue(true);
    }




    public LiveData<FirebaseUser> getUserLiveData() {
        return myUser;
    }

    public LiveData<Boolean> getUserStateLiveData() {
        return myUserState;
    }





    public Task<Void> signOut(Context context) {

        return AuthUI.getInstance().signOut(context).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                checkUser();
                Log.i("LOGIN","Log out ok...");
            }
        });
    }

}
