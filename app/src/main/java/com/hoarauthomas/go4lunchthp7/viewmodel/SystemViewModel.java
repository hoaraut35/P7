package com.hoarauthomas.go4lunchthp7.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hoarauthomas.go4lunchthp7.model.System;

import java.util.Arrays;
import java.util.List;

public class SystemViewModel extends ViewModel {

    //declare here provider to login to the app...
    List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.FacebookBuilder().build(),
            new AuthUI.IdpConfig.GoogleBuilder().build());

    //to get current user....
    protected FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    //to check is current user is logged...
    protected Boolean isCurrentUserLogged() {
        return (this.getCurrentUser() != null);
    }

    private MutableLiveData<Boolean> loginState = new MutableLiveData<>();

    public void updateStateLogin(boolean bool) {
        loginState.postValue(bool);
    }

    public LiveData<Boolean> getSystemVM() {
        return loginState;
    }



}
