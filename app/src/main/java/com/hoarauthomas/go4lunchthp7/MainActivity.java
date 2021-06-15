package com.hoarauthomas.go4lunchthp7;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hoarauthomas.go4lunchthp7.adapter.CustomRecyclerViewAdapter;
import com.hoarauthomas.go4lunchthp7.adapter.FragmentsAdapter;
import com.hoarauthomas.go4lunchthp7.api.GitHubService;
import com.hoarauthomas.go4lunchthp7.databinding.ActivityMainBinding;
import com.hoarauthomas.go4lunchthp7.model.User;
import com.hoarauthomas.go4lunchthp7.utils.Authentification;
import com.hoarauthomas.go4lunchthp7.view.WorkFragment;
import com.hoarauthomas.go4lunchthp7.viewmodel.LoginUserViewModel;
import com.hoarauthomas.go4lunchthp7.viewmodel.SystemViewModel;

import java.util.Arrays;
import java.util.List;

import static android.view.View.*;
import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //Added for View Binding
    private ActivityMainBinding binding;

    //Added to use View Model
    private LoginUserViewModel loginUVM;
    private SystemViewModel systemVM;

    //Added for return state
    private static final int RC_SIGN_IN = 123;
    private static final int SIGN_OUT_TASK = 10;
    private static final int DELETE_USER_TASK = 20;

    //Added for manage 3 screens
    FragmentsAdapter myFragmentAdapter;

    //Added for authentification with Firebase UI
    List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.FacebookBuilder().build(),
            new AuthUI.IdpConfig.GoogleBuilder().build());


    @Override
    protected void onResume() {
        super.onResume();
        security();
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

        //  setupViewModel();



        security();

        setupTopAppBar();

        setupNavigationDrawer();

        setupBottomBAr();

        setupViewPager(2);

        setupAdapter();


       // Log.i("[THOMAS","" + GitHubService().toString());

    }


    protected FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    protected Boolean isCurrentUserLogged() {
        return (this.getCurrentUser() != null);
    }

    private void security() {

        systemVM = new ViewModelProvider(this).get(SystemViewModel.class);
        systemVM.getSystemVM().observe(this, this::updateUI);




        getCurrentUser();

        if (!isCurrentUserLogged()) {
            Log.i("[THOMAS]", "Utilisateur non autehntifié !");
            request_login();
        } else {
            Log.i("[THOMAS]", "Utilisateur authentifié");
            request_user_info();
        }
    }

    private void updateUI(Boolean aBoolean) {

        if (aBoolean==false)
        {
           // systemVM.startLogin();
        }





    }









    private void request_user_info() {

        View hv = binding.navigationView.getHeaderView(0);
        TextView name = (TextView) hv.findViewById(R.id.displayName);
        name.setText(this.getCurrentUser().getDisplayName());
        TextView email = (TextView) hv.findViewById(R.id.email);
        email.setText(this.getCurrentUser().getEmail());


        ImageView avatar =(ImageView)hv.findViewById(R.id.avatar);

        Glide.with(avatar)
                .load(this.getCurrentUser().getPhotoUrl())
                .into(avatar);





    }

    private void request_login() {

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        //.setLogo(R.drawable.ic_logo)
                        .setTheme(R.style.LoginTheme)
                        .setIsSmartLockEnabled(false, true)
                        .build(), RC_SIGN_IN
        );

    }


    private void setupViewModel() {

        Log.i("[THOMAS]", "Enter in setupViewModel ...");

    //    loginUVM = new ViewModelProvider(this).get(LoginUserViewModel.class);
     //   loginUVM.getUser().observe(this, this::nextstep);

       // loginUVM.getLogin().observe(this, this::loginform);


//        loginUVM.initAuth();

      /*  if (loginUVM.check_login()) {
            Log.i("[THOMAS]", "connecté  " + loginUVM.check_login());
            request_user_info();

        }else
        {
            Log.i("[THOMAS]", "non connecté " + loginUVM.check_login());
            request_login();
        }


       */


        //Log.i("[THOMAS]","retour checl login" + loginUVM.check_login());
        //  security();

        //loginUVM.getUser().observe(this, this::nextstep);


    }

    private void loginform(String s) {
        switch (s) {
            case "false":
                // request_login();
                Log.i("[THOMAS]", "demande login mainactivity deja fait");
                request_user_info();
                break;
            case "true":
                request_login();
                Log.i("[THOMAS]", "demande login mainactivity");
                break;

        }

        Log.i("[THOMAS]", "changement statut login");
    }

    private void nextstep(User user) {


        Log.i("[THOMAS]", "Enter in nexstep...");
       // loginUVM.updateCurrentUser(this.getCurrentUser().getDisplayName(), this.getCurrentUser().getEmail());
    }

    private void setupAdapter() {


    }

    private void setupNavigationDrawer() {


        binding.navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_drawer_lunch:
                        binding.viewpager.setCurrentItem(1);
                        break;
                    case R.id.navigation_drawer_settings:
                        binding.viewpager.setCurrentItem(4);
                        break;
                    case R.id.navigation_drawer_logout:


                        request_logout();
                       // loginUVM.signOut();
                        Log.i("[THOMAS]", "logout");
                        break;
                }
                binding.drawerLayout.closeDrawer(Gravity.START);
                return true;
            }
        });
    }

    private void request_logout() {


        AuthUI.getInstance()
                .signOut(this);
        //    .addOnSuccessListener(getgetApplicationContext(),this.updateUIAfterRESTRequestCompleted(SIGN_OUT));

    }

    private void setupBottomBAr() {


        binding.bottomNavigationMenu.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.action_map:
                        //add function to pen fragment
                        binding.viewpager.setCurrentItem(1);
                        Log.i("THOMAS", "clic sur carte");
                        break;
                    case R.id.action_list:
                        //add function to pen fragment
                        binding.viewpager.setCurrentItem(2);
                        Log.i("THOMAS", "clic sur liste");
                        break;
                    case R.id.action_work:
                        //add function to pen fragment
                        binding.viewpager.setCurrentItem(3);
                        Log.i("THOMAS", "clic sur collegues");
                        break;
                }


                return true;
            }
        });


    }

    private void setupViewPager(int mode) {

        myFragmentAdapter = new FragmentsAdapter(getSupportFragmentManager());
        binding.viewpager.setAdapter(myFragmentAdapter);
        Log.i("THOMAS", "viewpager item : " + binding.viewpager.getCurrentItem());
        binding.viewpager.setCurrentItem(mode);
        Log.i("THOMAS", "viewpager item : " + binding.viewpager.getCurrentItem());


        //Work fine to show a fragment
      /*  FragmentManager fM = getSupportFragmentManager();
        fM.beginTransaction()
                .replace(R.id.frameLayout, new WorkFragment())
                .addToBackStack(null)
                .commit();

       */

    }

    private void setupTopAppBar() {

        //binding.topAppBar
        //g/etSupportActionBar().setDisplayHomeAsUpEnabled(true);


        binding.topAppBar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("THOMAS", "clic sur menu top bar");


                binding.drawerLayout.openDrawer(Gravity.START);
            }
        });

        binding.topAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Log.i("THOMAS", "Clic sur recherche top bar app");
                return false;
            }
        });


    }

    //TODO: Start Activity for login
    private void startSignInActivity() {

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

             //   loginUVM.updateCurrentUser(this.getCurrentUser().getDisplayName(), this.getCurrentUser().getEmail());


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


    //get item selected on navigation drawer


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {


        return true;
    }



}