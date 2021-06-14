package com.hoarauthomas.go4lunchthp7.viewmodel;

import android.util.Log;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.firebase.ui.auth.AuthUI;
import com.hoarauthomas.go4lunchthp7.R;
import com.hoarauthomas.go4lunchthp7.model.User;

import java.util.Arrays;
import java.util.List;

public class LoginUserViewModel extends ViewModel {

    private static final int RC_SIGN_IN = 123;

    List<AuthUI.IdpConfig> providers = Arrays.asList(
            // new AuthUI.IdpConfig.EmailBuilder().build(),//TODO: remove it at the end
            new AuthUI.IdpConfig.FacebookBuilder().build(),

            new AuthUI.IdpConfig.GoogleBuilder().build());


    private MutableLiveData<User> currentUserMLD;

    public LiveData<User> getUser() {
        if (currentUserMLD == null) {
            currentUserMLD = new MutableLiveData<User>();
            signIn();
        }
        return currentUserMLD;
    }

    //load if no user are login
    public void signIn() {
      /*  startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        //.setLogo(R.drawable.ic_logo)
                        .setTheme(R.style.LoginTheme)
                        .setIsSmartLockEnabled(false, true)
                        .build(), RC_SIGN_IN
        );
        
       */
    }


}
