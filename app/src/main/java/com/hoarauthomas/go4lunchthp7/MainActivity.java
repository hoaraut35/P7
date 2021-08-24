package com.hoarauthomas.go4lunchthp7;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.hoarauthomas.go4lunchthp7.databinding.ActivityMainBinding;
import com.hoarauthomas.go4lunchthp7.ui.FragmentsAdapter;
import com.hoarauthomas.go4lunchthp7.ui.detail.DetailActivity;
import com.hoarauthomas.go4lunchthp7.viewmodel.ViewModelFactory;
import com.hoarauthomas.go4lunchthp7.viewmodel.ViewModelGo4Lunch;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static androidx.core.view.GravityCompat.START;

//for user interaction with ui only ....
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //View Binding
    public ActivityMainBinding binding;

    //ViewModel
    public ViewModelGo4Lunch myViewModel;


    //Manage fragments map, list and workmates
    FragmentsAdapter myFragmentAdapter;

    //Signal for activity result and callback
    private static final int RC_SIGN_IN = 123;

    //list of auth provider
    private final List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.EmailBuilder().build(),
            new AuthUI.IdpConfig.FacebookBuilder().build(),
            new AuthUI.IdpConfig.GoogleBuilder().build());

    @Override
    protected void onPostResume() {
        super.onPostResume();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        setupPermission();
        setupViewModel();
        setupTopAppBar();
        setupNavigationDrawer();
        setupBottomBAr();
        setupViewPager();
    }

    private void setupPermission() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
    }

    private void setupViewModel() {
        this.myViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(ViewModelGo4Lunch.class);

        this.myViewModel.getMyUserState().observe(this, this::onCheckSecurity);

        this.myViewModel.getViewStateLiveData().observe(this, mainViewState -> {
            //  Log.i("[STATE]","from main activity..." + mainViewState.getLocation().getLatitude() + " "  + mainViewState.getLocation().getLongitude());
        });

    }

    private void onCheckSecurity(Boolean connected) {
        if (!connected) {
            request_login();
        } else {
            request_user_info();
        }
    }

    private void request_login() {
        AuthMethodPickerLayout customLayout = new AuthMethodPickerLayout
                .Builder(R.layout.custom_layout_login)
                .setEmailButtonId(R.id.email_button)
                .setGoogleButtonId(R.id.google_btn)
                .setFacebookButtonId(R.id.facebook_btn)
                .build();

        //TODO: startActivityForResult must be updated


        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setAuthMethodPickerLayout(customLayout)
                        .setLogo(R.drawable.go4lunch_sign_in)
                        .setTheme(R.style.LoginTheme)
                        .setIsSmartLockEnabled(false, true)
                        .build(), RC_SIGN_IN
        );
    }


    //TODO: review binding navigation view ?
    private void request_user_info() {
        View hv = binding.navigationView.getHeaderView(0);

        TextView name = (TextView) hv.findViewById(R.id.displayName);
        name.setText(Objects.requireNonNull(this.myViewModel.getMyCurrentUser().getValue()).getDisplayName());

        TextView email = (TextView) hv.findViewById(R.id.email);
        email.setText(this.myViewModel.getMyCurrentUser().getValue().getEmail());

        ImageView avatar = (ImageView) hv.findViewById(R.id.avatar);
        Glide.with(avatar)
                .load(this.myViewModel.getMyCurrentUser().getValue().getPhotoUrl())
                .circleCrop()
                .into(avatar);

        //TODO:remove this in prod mode
        //showSnackBar(this.myViewModel.getMyCurrentUser().getValue().getUid());
    }

    private void showSnackBar(String message) {
        Snackbar.make(binding.viewpager, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IdpResponse response = IdpResponse.fromResultIntent(data);

        if (requestCode == RC_SIGN_IN) {
            //SUCCESS
            if (resultCode == RESULT_OK) {
                showSnackBar(getString(R.string.connection_succeed));

                this.myViewModel.createUser();

            } else {
                //ERRORS

                if (response == null) {
                    showSnackBar(getString(R.string.error_authentification_canceled));

                } else if (Objects.requireNonNull(response.getError()).getErrorCode() == ErrorCodes.NO_NETWORK) {
                    showSnackBar(getString(R.string.error_no_network));

                } else if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    showSnackBar(getString(R.string.error_unknow));
                }
            }
        }
    }

    private void setupNavigationDrawer() {

        binding.navigationView.setNavigationItemSelectedListener(item -> {

            int id = item.getItemId();

            if (id == R.id.navigation_drawer_lunch) {
                openMyFavoriteRestaurant();
            } else if (id == R.id.navigation_drawer_settings) {
                binding.viewpager.setCurrentItem(4);
            } else if (id == R.id.navigation_drawer_logout) {
                myViewModel.logOut();
            }

            binding.drawerLayout.closeDrawer(START);
            return true;
        });

    }

    private void openMyFavoriteRestaurant() {
        Intent intent = new Intent(this, DetailActivity.class);
        startActivity(intent);
    }

    private void setupBottomBAr() {
        binding.bottomNavigationMenu.setOnItemSelectedListener(item -> {

            int id = item.getItemId();

            if (id == R.id.action_map) {
                binding.viewpager.setCurrentItem(1);
            } else if (id == R.id.action_list) {
                binding.viewpager.setCurrentItem(2);
            } else if (id == R.id.action_work) {
                binding.viewpager.setCurrentItem(3);
            }
            return true;
        });
    }

    private void setupViewPager() {
        myFragmentAdapter = new FragmentsAdapter(this);
        binding.viewpager.setAdapter(myFragmentAdapter);
        binding.viewpager.setCurrentItem(1);
        binding.viewpager.setUserInputEnabled(false);
    }

    private void setupTopAppBar() {
        binding.topAppBar.setNavigationOnClickListener(v -> binding.drawerLayout.openDrawer(START));

        binding.topAppBar.setOnMenuItemClickListener(item -> {
            //TODO: add search function
            Log.i("THOMAS", "Click sur recherche top bar app");
            return false;
        });
    }

    //get item selected on navigation drawer
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return true;
    }

}