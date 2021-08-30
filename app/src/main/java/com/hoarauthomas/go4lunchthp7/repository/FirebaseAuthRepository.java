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

    private final FirebaseAuth myAuth;
    private final MutableLiveData<FirebaseUser> myUser = new MutableLiveData<>(null);
    private final MutableLiveData<Boolean> myUserState = new MutableLiveData<>(false);

    public FirebaseAuthRepository() {
        this.myAuth = FirebaseAuth.getInstance();
        checkUser();
    }

    public void checkUser() {
        if (myAuth.getCurrentUser() != null) {
            myUser.postValue(myAuth.getCurrentUser());
            myUserState.postValue(true);
        } else {
            myUser.postValue(null);
            myUserState.postValue(false);
        }
    }



    public LiveData<FirebaseUser> getUserLiveData() {
        return this.myUser;
    }

    public LiveData<Boolean> getUserStateLiveData() {
        return this.myUserState;
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
