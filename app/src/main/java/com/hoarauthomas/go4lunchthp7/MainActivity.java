package com.hoarauthomas.go4lunchthp7;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.hoarauthomas.go4lunchthp7.databinding.ActivityMainBinding;
import com.hoarauthomas.go4lunchthp7.ui.activity.DetailRestaurant;
import com.hoarauthomas.go4lunchthp7.ui.adapter.FragmentsAdapter;
import com.hoarauthomas.go4lunchthp7.viewmodel.ViewModelFactory;
import com.hoarauthomas.go4lunchthp7.viewmodel.ViewModelGo4Lunch;

import java.util.Arrays;
import java.util.List;

import static android.view.View.OnClickListener;


//for user interaction with ui only ....
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //for View Binding... check if binding work from another class by sample
    public ActivityMainBinding binding;

    //to add viewmodel
    public ViewModelGo4Lunch myViewModel;

    //for viewpager
    FragmentsAdapter myFragmentAdapter;

    //signal for activity result and callback
    private static final int RC_SIGN_IN = 123;
    private static final int SIGN_OUT_TASK = 10;
    private static final int DELETE_USER_TASK = 20;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    //list of auth provider, move to viewmodel ?
    List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.FacebookBuilder().build(),
            new AuthUI.IdpConfig.GoogleBuilder().build());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        setupViewModel();
        setupTopAppBar();
        setupNavigationDrawer();
        setupBottomBAr();
        setupViewPager(1);
    }

    private void setupViewModel() {
        this.myViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(ViewModelGo4Lunch.class);
        //this.myViewModel.checkSecurity("setupViewmModel").observe(this, this::onCheckSecurity);
        this.myViewModel.getMyUserState().observe(this,this::onCheckSecurity);
    }

    private void onCheckSecurity(Boolean aBoolean) {


        if (!aBoolean) {
            Log.i("[THOMAS]", "Security event : request login");
            request_login();

        } else {
            Log.i("[THOMAS]", "Security event : request user data");
            request_user_info();
        }


    }


    //called when the user is not logged ...
    private void request_login() {
        Log.i("[THOMAS]", "Security login form show ...");

        //TODO: update this part of code to remove error
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

    private void request_user_info() {
        Log.i("[THOMAS]", "info user request ... update ui");

        View hv = binding.navigationView.getHeaderView(0);
        TextView name = (TextView) hv.findViewById(R.id.displayName);
        //name.setText(myViewModel.getCurrentUser().getDisplayName());
        TextView email = (TextView) hv.findViewById(R.id.email);
        //email.setText(myViewModel.getCurrentUser().getEmail());
        ImageView avatar = (ImageView) hv.findViewById(R.id.avatar);
      /*  Glide.with(avatar)
                .load(myViewModel.getCurrentUser().getPhotoUrl())
                .circleCrop()
                .into(avatar);

       */
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i("[THOMAS]","onActivityResult load ...");
        IdpResponse response = IdpResponse.fromResultIntent(data);

        if (requestCode == RC_SIGN_IN) {

            Log.i("[THOMAS]","RC_SIGN_IN...");

            if (resultCode == RESULT_OK) {
                Log.i("[THOMAS]","RESULT_OK SDIGN IN ...");

               // this.myViewModel.securityUpdate();
             //   this.myViewModel.checkSecurity("onActivityResult result login ok");
                //this.myViewModel.createuser();



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


    private void request_logout() {
        Log.i("[THOMAS]", "demand logout to FirebaseUI");
        AuthUI.getInstance()
                .signOut(this)
                .addOnSuccessListener(this, this.updateUIAfterRESTRequestsCompleted(SIGN_OUT_TASK));
    }

    //**********************************************************************************************
    // End of Security UI Management
    //**********************************************************************************************


    //**********************************************************************************************
    // Section for UI Tools
    //**********************************************************************************************
    private void setupNavigationDrawer() {
        binding.navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_drawer_lunch:
                        openMyFavoriteRestaurant();
                        break;
                    case R.id.navigation_drawer_settings:
                        binding.viewpager.setCurrentItem(4);
                        break;
                    case R.id.navigation_drawer_logout:

                        request_logout();
                       // myViewModel.checkSecurity("setupNavigationDrawer")
                        break;
                }
                binding.drawerLayout.closeDrawer(Gravity.START);
                return true;
            }
        });
    }

    //**********************************************************************************************
    // End of Section for UI Tools
    //**********************************************************************************************


    private void openMyFavoriteRestaurant() {
        Log.i("[THOMAS]", "Open favorite restaurant acitvity....");
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
        binding.viewpager.setUserInputEnabled(false);


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

    @Override
    protected void onPostResume() {
        super.onPostResume();

       // this.myViewModel.checkSecurity("onPostResume");
    }
}