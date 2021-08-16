package com.hoarauthomas.go4lunchthp7;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.hoarauthomas.go4lunchthp7.databinding.ActivityMainBinding;
import com.hoarauthomas.go4lunchthp7.ui.activity.DetailRestaurant;
import com.hoarauthomas.go4lunchthp7.ui.adapter.FragmentsAdapter;
import com.hoarauthomas.go4lunchthp7.viewmodel.MainViewState;
import com.hoarauthomas.go4lunchthp7.viewmodel.ViewModelFactory;
import com.hoarauthomas.go4lunchthp7.viewmodel.ViewModelGo4Lunch;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static android.view.View.OnClickListener;
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
    private static final int SIGN_OUT_TASK = 10;
    private static final int DELETE_USER_TASK = 20;

    //list of auth provider
    private List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.FacebookBuilder().build(),
            new AuthUI.IdpConfig.GoogleBuilder().build());

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

        this.myViewModel.getViewStateLiveData().observe(this, new Observer<MainViewState>() {
            @Override
            public void onChanged(MainViewState mainViewState) {
              //  Log.i("[STATE]","from mainactivity..." + mainViewState.getLocation().getLatitude() + " "  + mainViewState.getLocation().getLongitude());
            }
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
                this.myViewModel.getMyCurrentUser();
                //TODO: write user to firestore
                this.myViewModel.createUser();

            } else {
                //ERRORS

                if (response == null) {
                    showSnackBar(getString(R.string.error_authentification_canceled));

                } else if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    showSnackBar(getString(R.string.error_no_network));

                } else if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    showSnackBar(getString(R.string.error_unknow));
                }
            }
        }
    }

    private void setupNavigationDrawer() {
        binding.navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_drawer_lunch:
                    openMyFavoriteRestaurant();
                    break;
                case R.id.navigation_drawer_settings:
                    binding.viewpager.setCurrentItem(4);
                    break;
                case R.id.navigation_drawer_logout:
                    myViewModel.logOut();
                    break;
            }
            binding.drawerLayout.closeDrawer(START);
            return true;
        });
    }

    private void openMyFavoriteRestaurant() {
        Intent intent = new Intent(this, DetailRestaurant.class);
        startActivity(intent);
    }

    //used when a succes login from firebase ui authentifiction
    private OnSuccessListener<Void> updateUIAfterRESTRequestsCompleted(final int origin) {
        return new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                switch (origin) {
                    case SIGN_OUT_TASK:
                        Log.i("[THOMAS]", "Logout is completed");
                        //  myViewModel.securityUpdate();
                        //  myViewModel.checkSecurity("OnsuccesListener after logout");

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
                        binding.viewpager.setCurrentItem(1);
                        break;
                    case R.id.action_list:
                        binding.viewpager.setCurrentItem(2);
                        break;
                    case R.id.action_work:
                        binding.viewpager.setCurrentItem(3);
                        break;
                }
                return true;
            }
        });
    }

    private void setupViewPager() {
        myFragmentAdapter = new FragmentsAdapter(this);
        binding.viewpager.setAdapter(myFragmentAdapter);
        binding.viewpager.setCurrentItem(1);
        binding.viewpager.setUserInputEnabled(false);
    }

    private void setupTopAppBar() {
        binding.topAppBar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.drawerLayout.openDrawer(START);
            }
        });

        binding.topAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //TODO: add search funciton
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