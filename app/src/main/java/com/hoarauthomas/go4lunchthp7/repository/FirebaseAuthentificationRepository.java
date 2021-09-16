package com.hoarauthomas.go4lunchthp7.repository;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hoarauthomas.go4lunchthp7.model.MyUser;

import org.jetbrains.annotations.NotNull;

//Only to get data from Firestore, store and publish user and user state to ViewModel ...

public class FirebaseAuthentificationRepository {

    private FirebaseAuth myFireBaseAuthInstance;
    private FirebaseUser myFireBaseUser;

    private MutableLiveData<FirebaseUser> myUser = new MutableLiveData<>();
    private MutableLiveData<Boolean> myUserState = new MutableLiveData<>();
    private MutableLiveData<MyUser> myUserDetail = new MutableLiveData<>();

    public FirebaseAuthentificationRepository(FirebaseAuth firebaseAuthInstance) {
        this.myFireBaseAuthInstance = firebaseAuthInstance;
        this.myFireBaseUser = myFireBaseAuthInstance.getCurrentUser();
        checkUser();
    }

    public void checkUser() {

        Log.i("[Auth]","Check current user ...");

        myFireBaseUser = myFireBaseAuthInstance.getCurrentUser();

        if (myFireBaseUser == null) {
            Log.i("[Auth]","User not log in");
            myUser.setValue(null);
            myUserState.setValue(false);
        } else {
            Log.i("[Auth]","User log in" + myFireBaseUser.getDisplayName());
            myUserState.setValue(true);
            myUser.setValue(myFireBaseUser);
        }
    }

    public MutableLiveData<FirebaseUser> getUserLiveDataNew(){
        return myUser;
    }

//    //public LiveData<FirebaseUser> getUserLiveData() {
//        return myUser;
//    }


    public MutableLiveData<Boolean> getLoggedOutLiveDataNew(){
        return myUserState;
    }

    public void logOut(Context context){
        myFireBaseAuthInstance.signOut();
        checkUser();
    }



}
