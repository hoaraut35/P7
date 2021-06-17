package com.hoarauthomas.go4lunchthp7;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hoarauthomas.go4lunchthp7.data.UserHelper;
import com.hoarauthomas.go4lunchthp7.injection.Injection;
import com.hoarauthomas.go4lunchthp7.injection.ViewModelFactory;
import com.hoarauthomas.go4lunchthp7.model.pojo.Result;
import com.hoarauthomas.go4lunchthp7.utils.Authentification;
import com.hoarauthomas.go4lunchthp7.viewmodel.viewModelGo4Lunch;
import com.hoarauthomas.go4lunchthp7.ui.adapter.FragmentsAdapter;
import com.hoarauthomas.go4lunchthp7.databinding.ActivityMainBinding;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.view.View.*;


//this is for user interaction only ....

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //Added for View Binding
    private ActivityMainBinding binding;

    private Authentification myAuth;

    //Added for ViewModel
    private viewModelGo4Lunch myViewModel;

    RecyclerView recyclerView;

    //Added for security authentification
    private static final int RC_SIGN_IN = 123;
    private static final int SIGN_OUT_TASK = 10;
    private static final int DELETE_USER_TASK = 20;

    //list for restaurants
    public final ArrayList<Result> allResult = new ArrayList<>();


    List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.FacebookBuilder().build(),
            new AuthUI.IdpConfig.GoogleBuilder().build());

    //Added for manage 3 screens (map, list, workmates and preferences)
    FragmentsAdapter myFragmentAdapter;

    //Added to check security after resume
    @Override
    protected void onResume() {
        super.onResume();
        security();
    }

    //Added for Firebase UI Authentification
    protected FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    protected Boolean isCurrentUserLogged() {
        return (this.getCurrentUser() != null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        security();

        //initialise viewmodel
        setupViewModel();

        //TODO: regroup to a ui file or baseactivitry file
        setupTopAppBar();
        setupNavigationDrawer();
        setupBottomBAr();
        setupViewPager(1);
    }

    //function to check login statut
    public void security() {
        //to get actual singleton of firebase user
        getCurrentUser();
        //to check is user is connected or not
        if (!isCurrentUserLogged()) {
            //if not connected then request login
            request_login();
        } else {
            //else request user info to update ui
            request_user_info();
        }
    }

    public List<Result> myData(){
        return allResult;
    }

    //update navigation drawer data user
    private void request_user_info() {
        Log.i("[THOMAS]", "request user infos...");

        View hv = binding.navigationView.getHeaderView(0);
        TextView name = (TextView) hv.findViewById(R.id.displayName);
        name.setText(this.getCurrentUser().getDisplayName());
        TextView email = (TextView) hv.findViewById(R.id.email);
        email.setText(this.getCurrentUser().getEmail());

        ImageView avatar = (ImageView) hv.findViewById(R.id.avatar);

        Glide.with(avatar)
                .load(this.getCurrentUser().getPhotoUrl())
                .into(avatar);
    }

    //TODO: move to viewmodel. ..
    //to load firebase ui auth activity
    private void request_login() {
        Log.i("[THOMAS", "request login...");

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

    //TODO: move to viewmodel file...
    private void request_logout() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnSuccessListener(this, this.updateUIAfterRESTRequestsCompleted(SIGN_OUT_TASK));
    }


    //**********************************************************************************************
    private void setupViewModel() {
        ViewModelFactory myViewModelFactory = Injection.provideViewModelFactory(this);
        this.myViewModel = new ViewModelProvider(this, myViewModelFactory).get(viewModelGo4Lunch.class);
        this.myViewModel.getSecurity().observe(this, this::onUpdateSecurity);
        this.myViewModel.getRestaurants().observe(this, this::onUpdateRestaurants);
        this.myViewModel.getWorkMates().observe(this, this::onUpdateWorkMates);
    }

    private void onUpdateSecurity(List<Result> results) {
        Log.i("[THOMAS]", "ViewModel Security Event");
    }

    private void onUpdateRestaurants(List<Result> places) {
        Log.i("[THOMAS]", "ViewModel Restaurants Event" + places.size());
        allResult.clear();
        allResult.addAll(places);

    }

    private void onUpdateWorkMates(List<Result> results) {
        Log.i("[THOMAS]", "ViewModel WorkMates Event" + results.size());
    }
    //**********************************************************************************************

    private void setupNavigationDrawer() {
        binding.navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_drawer_lunch:

                      //  Intent i = new Intent(this, DetailRestaurant.class);
                      //  startActivity(i);
                           myViewModel.createuser();

                        break;
                    case R.id.navigation_drawer_settings:
                        binding.viewpager.setCurrentItem(4);

//                        UserHelper.createUser(this.getCurrentUser().getUid(),this.getCurrentUser().getDisplayName());

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

               // this.myViewModel.createuser();


                UserHelper.createUser(this.getCurrentUser().getUid(),this.getCurrentUser().getDisplayName());

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

    private OnSuccessListener<Void> updateUIAfterRESTRequestsCompleted(final int origin) {
        return new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                switch (origin) {
                    case SIGN_OUT_TASK:
                        security();
                        // finish();
                        break;
                    case DELETE_USER_TASK:
                        // finish();
                        break;
                    default:
                        break;
                }
            }
        };
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
                        binding.topAppBar.setTitle(R.string.topAppBar_title);
                        break;
                }
                return true;
            }
        });
    }

    private void setupViewPager(int default_view) {

        myFragmentAdapter = new FragmentsAdapter(this);//FragmentsAdapter(getSuppgetSupportFragmentManager());
        binding.viewpager.setAdapter(myFragmentAdapter);
        //to setup default fragment to view
        binding.viewpager.setCurrentItem(default_view);
    }

    private void setupTopAppBar() {
        binding.topAppBar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("THOMAS", "clic sur menu top bar");
                binding.drawerLayout.openDrawer(GravityCompat.START);
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


    //get item selected on navigation drawer
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return true;
    }

}