package com.hoarauthomas.go4lunchthp7;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hoarauthomas.go4lunchthp7.databinding.ActivityMainBinding;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //Added for view binding
    private ActivityMainBinding binding;

    //Added for return state
    private static final int RC_SIGN_IN = 123;
    private static final int SIGN_OUT_TASK = 10;
    private static final int DELETE_USER_TASK = 20;

    //Added for login mode
    List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.EmailBuilder().build(),
            new AuthUI.IdpConfig.GoogleBuilder().build(),
            new AuthUI.IdpConfig.FacebookBuilder().build());


    @Override
    protected void onResume() {
        super.onResume();
        this.updateUIWhenResuming();

    }

    private void updateUIWhenResuming() {


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //call Firebase Sign In Activity
        // startSignInActivity();

        setupTopBar();
        setupBottomBAr();
        setupNavigationDrawer();

        setupFragments();



    }

    private void setupNavigationDrawer() {
    }

    private void setupBottomBAr() {




    }

    private void setupFragments() {


        FragmentManager fragmentManager = getSupportFragmentManager();



    }

    private void setupTopBar() {



    }

    //open Firebase UI SignIn Activity
    //TODO: add a theme for login form, create lmogo
    private void startSignInActivity() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setLogo(R.drawable.ic_logo)
                        .setTheme(R.style.LoginTheme)
                        .setIsSmartLockEnabled(false, true)
                        .build(), RC_SIGN_IN
        );
    }


    //get the result after login fail or not
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i("THOMAS", "Retour authentification ...");

        IdpResponse response = IdpResponse.fromResultIntent(data);

        if (requestCode == RC_SIGN_IN) {

            //success
            if (resultCode == RESULT_OK) {
                Log.i("THOMAS", "authentification réussi");
            } else {//error

                if (response == null) {
                    Log.i("THOMAS", "authentification annulée");
                } else if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Log.i("THOMAS", "authentification impossible sans réseau");

                } else if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    Log.i("THOMAS", "authentification impossible erreur inconnu");
                }


            }

        }
    }

    //get a singleton for current user
    protected FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    //check if currentuser is connected or not
    protected Boolean isCurrentUserLogged() {
        return this.getCurrentUser() != null;
    }

}