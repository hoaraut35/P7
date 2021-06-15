package com.hoarauthomas.go4lunchthp7;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

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
import com.hoarauthomas.go4lunchthp7.adapter.FragmentsAdapter;
import com.hoarauthomas.go4lunchthp7.model.Place;
import com.hoarauthomas.go4lunchthp7.retrofit.ApiRetrofitService;
import com.hoarauthomas.go4lunchthp7.databinding.ActivityMainBinding;
import com.hoarauthomas.go4lunchthp7.model.GitHubRepo;
import com.hoarauthomas.go4lunchthp7.model.User;
import com.hoarauthomas.go4lunchthp7.retrofit.GooglePlacesInterface;
import com.hoarauthomas.go4lunchthp7.viewmodel.ListPlacesViewModel;
import com.hoarauthomas.go4lunchthp7.viewmodel.LoginUserViewModel;
import com.hoarauthomas.go4lunchthp7.viewmodel.SystemViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.view.View.*;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //Added for View Binding
    private ActivityMainBinding binding;

    //Added to use View Model
    private LoginUserViewModel loginUVM;
    private SystemViewModel systemVM;
    private ListPlacesViewModel myPlacesViewModel;


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

    //Added to check security
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

         setupRetrofit();
        setupMyPlaces();

        setupTopAppBar();

        setupNavigationDrawer();

        //    setupBottomBAr();

        setupViewPager(2);

        //    setupAdapter();


        //   setupPlaces();


        // Log.i("[THOMAS","" + GitHubService().toString());

    }

    private void setupMyPlaces() {

        //    loginUVM = new ViewModelProvider(this).get(LoginUserViewModel.class);
        //   loginUVM.getUser().observe(this, this::nextstep);

        // myPlacesViewModel = new ViewModelProvider(this).get(ListPlacesViewModel.class);

        //myPlacesViewModel.getMyPlaces().observe(this,this::newfunc);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/maps/api/place/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Create a REST
        GooglePlacesInterface service = retrofit.create(GooglePlacesInterface.class);

        //Fetch a list of the Github repositories
        Call<Place> call = service.getNearbyPlaces("AIzaSyDzUUJlN7hmetd7MtQR5s5TTzWiO4dwpCA",10);


        call.enqueue(new Callback<Place>() {
            @Override
            public void onResponse(Call<Place> call, Response<Place> response) {
                Log.i("[THOMAS]","dssdfdsf" + response.body().getStatus());


            }

            @Override
            public void onFailure(Call<Place> call, Throwable t) {

            }
        });


    }

    private void newfunc(List<Place> places) {
        Log.i("[THOMAS]", "ddddddddddddd");
    }


    //Create a simple REST
    private void setupRetrofit() {


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Create a REST
        ApiRetrofitService service = retrofit.create(ApiRetrofitService.class);

        //Fetch a list of the Github repositories
        Call<List<GitHubRepo>> call = service.reposForUser("hoaraut35");

        //Execute th e call asynchronolously
        call.enqueue(new Callback<List<GitHubRepo>>() {
            @Override
            public void onResponse(Call<List<GitHubRepo>> call, Response<List<GitHubRepo>> response) {


                List<GitHubRepo> newlist = new ArrayList<>();

                newlist.addAll(response.body());

                Log.i("[THOMAS]", "retour retrofit => " + response.body().size());


                for (int i = 0; i < newlist.size(); i++) {
                    Log.i("[THOMAS]", "" + newlist.get(i).getName());
                }

                //use repository to viewx data


            }

            @Override
            public void onFailure(Call<List<GitHubRepo>> call, Throwable t) {

            }
        });


    }

    private void setupPlaces() {

        //  GitHubService service = RetrofitService.

    }


    protected FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    protected Boolean isCurrentUserLogged() {
        return (this.getCurrentUser() != null);
    }

    private void security() {

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

    private void updateUI(Boolean aBoolean) {

        if (aBoolean == false) {
            Log.i("[THOMAS]", "non logué");

        } else {
            Log.i("[THOMAS]", "logué");
        }


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
                .signOut(this)
                .addOnSuccessListener(this, this.updateUIAfterRESTRequestsCompleted(SIGN_OUT_TASK));

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