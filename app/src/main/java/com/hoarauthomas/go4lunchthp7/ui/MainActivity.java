package com.hoarauthomas.go4lunchthp7.ui;

import static androidx.core.view.GravityCompat.START;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.hoarauthomas.go4lunchthp7.R;
import com.hoarauthomas.go4lunchthp7.databinding.ActivityMainBinding;
import com.hoarauthomas.go4lunchthp7.factory.ViewModelFactory;
import com.hoarauthomas.go4lunchthp7.ui.detail.DetailActivity;
import com.hoarauthomas.go4lunchthp7.ui.map.ViewModelMap;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    public ActivityMainBinding binding;
    public ViewModelMain myViewModel;
    public ViewModelMap myViewModelMap;


    private final List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.TwitterBuilder().build(),
            new AuthUI.IdpConfig.EmailBuilder().build(),
            new AuthUI.IdpConfig.FacebookBuilder().build(),
            new AuthUI.IdpConfig.GoogleBuilder().build());

    private ActivityResultLauncher<Intent> openFirebaseAuthForResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        setupPermission();

        setupViewModel();
        Authentification();

        setupTopAppBar();
        setupNavigationDrawer();
        setupBottomBAr();
        setupViewPager();

        setupSettings();

        setupWorkManagerListener();



    }



    private void setupWorkManagerListener() {


        /*String uid = "go4lunch";

        WorkManager.getInstance(this).getWorkInfoByIdLiveData(uid."go4lunch").observe(this, new Observer<WorkInfo>() {
            @Override
            public void onChanged(WorkInfo workInfo) {

                if (workInfo.getState() != null && workInfo.getState() == WorkInfo.State.SUCCEEDED){

                    binding.topAppBar.setTitle("success work alarm");
                }
            }
        });



         */
    }

    private void setupSettings() {
        SharedPreferences sp;
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        myViewModel.setNotification(sp.getBoolean("notifications2",true));
        myViewModel.setZoom(sp.getInt("zoom",10));
        myViewModel.setupSP(getApplicationContext());
    }

    private void setupPermission() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.INTERNET}, 0);
    }

    private void setupViewModel() {

      //  this.myViewModelMap = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(ViewModelMap.class);

        this.myViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(ViewModelMain.class);

        this.myViewModel.getAllWorkmatesByPlaceId().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                binding.topAppBar.setTitle("test" + LocalTime.now());
            }
        });



        this.myViewModel.getLoginState().observe(this, aBoolean -> {
            if (aBoolean) {
                request_user_info(myViewModel.getUser());
            } else {
                request_login();
            }
        });




    }





    /**
     * Authentification is load at startup
     */
    private void Authentification() {

        openFirebaseAuthForResult = registerForActivityResult(
                new FirebaseAuthUIActivityResultContract(),
                result -> {

                    if (result.getResultCode() == -1) {
                        myViewModel.createUser();
                        myViewModel.setUser();
                    } else {

                        if (result.getResultCode() == 0) {
                            Log.i("[Auth]", "Login annulé");
                        } else {

                            Log.i("[Auth]", "Erreur login");
                            if (result.getIdpResponse() == null) {
                                MainActivity.this.showSnackBar(MainActivity.this.getString(R.string.error_no_network));
                                MainActivity.this.showSnackBar("Annulée");
                            } else if (result.getIdpResponse().equals(ErrorCodes.NO_NETWORK)) {
                                MainActivity.this.showSnackBar(MainActivity.this.getString(R.string.error_no_network));
                            } else if (result.getIdpResponse().equals(ErrorCodes.UNKNOWN_ERROR)) {
                                MainActivity.this.showSnackBar(MainActivity.this.getString(R.string.error_unknow));
                            }
                        }
                    }
                });
    }

    /**
     * lauch if no user login
     */
    private void request_login() {

        AuthMethodPickerLayout customLayout = new AuthMethodPickerLayout
                .Builder(R.layout.custom_layout_login)
                .setTwitterButtonId(R.id.twitter_button)
                .setEmailButtonId(R.id.email_button)
                .setGoogleButtonId(R.id.google_btn)
                .setFacebookButtonId(R.id.facebook_btn)
                .build();

        openFirebaseAuthForResult.launch(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setAuthMethodPickerLayout(customLayout)
                        .setLogo(R.drawable.go4lunch_sign_in)
                        .setTheme(R.style.LoginTheme)
                        .setIsSmartLockEnabled(false, true)
                        .build()
        );

    }

    private void request_user_info(FirebaseUser myUserResult) {
        myViewModel.setUser();
        if (myUserResult != null) {

            View hv = binding.navigationView.getHeaderView(0);
            TextView name = hv.findViewById(R.id.displayName);

            //name of user profile
            name.setText(Objects.requireNonNull(myUserResult.getDisplayName()));
            //email of user profile
            TextView email = hv.findViewById(R.id.email);
            email.setText(myUserResult.getEmail());

            //try email but don't work
            for (UserInfo profile : myUserResult.getProviderData()) {
                email.setText(profile.getEmail());
            }

            //for photo avatar test
            String avatarSource = "";
            ImageView avatarView = hv.findViewById(R.id.avatar);

            for (UserInfo profile : myUserResult.getProviderData()) {

                if (!profile.getPhotoUrl().equals("")) {
                    avatarSource = profile.getPhotoUrl().toString();
                } else {
                    //construc avatar
                    String nom = myUserResult.getDisplayName();
                    String[] parts = nom.split(" ", 2);
                    String z = "";

                    for (int i = 0; i < parts.length; i++) {
                        z = parts[i].charAt(0) + z;
                    }
                    avatarSource = "https://eu.ui-avatars.com/api/?name=" + z;
                }

            }

            Glide.with(avatarView)
                    .load(avatarSource)
                    .circleCrop()
                    .into(avatarView);


        }
    }

    private void showSnackBar(String message) {
        Snackbar.make(binding.viewpager, message, Snackbar.LENGTH_LONG).show();
    }

    private void setupNavigationDrawer() {

        binding.navigationView.setNavigationItemSelectedListener(item -> {

            int id = item.getItemId();

            if (id == R.id.navigation_drawer_lunch) {
                openMyFavoriteRestaurant();
            } else if (id == R.id.navigation_drawer_settings) {
                binding.viewpager.setCurrentItem(4);
            } else if (id == R.id.navigation_drawer_logout) {
                myViewModel.LogOut();
            }

            binding.drawerLayout.closeDrawer(START);
            return true;
        });

    }

    private void openMyFavoriteRestaurant() {
        if (myViewModel.getMyUserRestaurant().getValue() != null && !myViewModel.getMyUserRestaurant().getValue().isEmpty()) {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra("TAG_ID", myViewModel.getMyUserRestaurant().getValue());
            startActivity(intent);
        } else {
            showSnackBar(getString(R.string.no_fav_msg));
        }

    }

    private void setupBottomBAr() {
        binding.bottomNavigationMenu.setOnItemSelectedListener(item -> {

            int id = item.getItemId();

            if (id == R.id.action_map) {
                binding.viewpager.setCurrentItem(1);
                binding.topAppBar.setTitle("I'm Hungry!");
                binding.topAppBar.findViewById(R.id.searchView).setVisibility(View.VISIBLE);
            } else if (id == R.id.action_list) {
                binding.viewpager.setCurrentItem(2);
                binding.topAppBar.setTitle("I'm Hungry!");
                binding.topAppBar.findViewById(R.id.searchView).setVisibility(View.VISIBLE);
            } else if (id == R.id.action_work) {
                binding.topAppBar.findViewById(R.id.searchView).setVisibility(View.INVISIBLE);
                binding.topAppBar.setTitle("Available workmates");
                binding.viewpager.setCurrentItem(3);
            }
            return true;
        });
    }

    private void setupViewPager() {
        MainFragmentsAdapter myFragmentAdapter = new MainFragmentsAdapter(this);
        binding.viewpager.setAdapter(myFragmentAdapter);
        binding.viewpager.setCurrentItem(1);
        binding.viewpager.setUserInputEnabled(false);
    }

    private void setupTopAppBar() {
        setSupportActionBar(binding.topAppBar);
        binding.topAppBar.setNavigationOnClickListener(v -> binding.drawerLayout.openDrawer(START));
        binding.topAppBar.setOnMenuItemClickListener(item -> false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 123) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                showSnackBar(place.getName() + " id: " + place.getId());

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                showSnackBar("Erreur autocomplete");
            } else if (resultCode == RESULT_CANCELED) {
                showSnackBar("Recherche annulée");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_main_menu, menu);

        MenuItem search = menu.findItem(R.id.searchView);
        SearchView searchView = (SearchView) search.getActionView();
        searchView.setQueryHint("Search restaurant ...");

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                //TODO: get new markers with place
                showSnackBar(getString(R.string.search_abord));
                myViewModel.reloadDataAfterQuery(true);
                return false;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                myViewModel.reloadDataAfterQuery(false);

                if (myViewModel.getMyPosition() != null && query.length() > 3) {
                    Location mypos = myViewModel.getMyPosition();
                    String st = null;
                    List<com.hoarauthomas.go4lunchthp7.PlaceAutocomplete> myListResponse = new ArrayList<>();

                    if (query != null && mypos != null) {
                        // myViewModel.stopPositionListener();
                        myViewModel.getResultAutocomplete(query, mypos);
                    } else {
                        Toast.makeText(getApplicationContext(), "Réessayer plus tard", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                } else {
                    showSnackBar(getString(R.string.query_error_autocomplete));
                    return true;
                }
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}
