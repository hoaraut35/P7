package com.hoarauthomas.go4lunchthp7.ui;

import static androidx.core.view.GravityCompat.START;

import android.Manifest;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkRequest;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseUser;
import com.hoarauthomas.go4lunchthp7.BuildConfig;
import com.hoarauthomas.go4lunchthp7.PlaceAutocomplete;
import com.hoarauthomas.go4lunchthp7.R;
import com.hoarauthomas.go4lunchthp7.databinding.ActivityMainBinding;
import com.hoarauthomas.go4lunchthp7.factory.ViewModelFactory;
import com.hoarauthomas.go4lunchthp7.ui.detail.DetailActivity;
import com.hoarauthomas.go4lunchthp7.workmanager.WorkManagerTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    public ActivityMainBinding binding;

    public ViewModelMain myViewModel;

    private final List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.TwitterBuilder().build(),
            new AuthUI.IdpConfig.EmailBuilder().build(),
            new AuthUI.IdpConfig.FacebookBuilder().build(),
            new AuthUI.IdpConfig.GoogleBuilder().build());

    private ActivityResultLauncher<Intent> openFirebaseAuthForResult;

    @RequiresApi(api = Build.VERSION_CODES.O)
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
        loadWork();
        setupAutocomplete();

    }

    private void setupAutocomplete() {
        //initialize
        Places.initialize(getApplicationContext(), BuildConfig.MAPS_API_KEY);
        // create
        PlacesClient placesClient = Places.createClient(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void loadWork() {

        //Determine the format to work
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        //Determine actual datetime
        LocalDateTime dateactu = LocalDateTime.now();
        Log.i("[JOB]","Actual date and time : " + dateactu.format(formatter));

        //Determine the target date and time to execute request
        LocalDate dateToStart = LocalDate.now();
        LocalTime timeToStart = LocalTime.parse("12:00:00");
        LocalDateTime fullDateTimeToStart = LocalDateTime.of(dateToStart,timeToStart);
        Log.i("[JOB]","Target date and time : " + fullDateTimeToStart.format(formatter).toString());

        //extract initial delay to construct work request after
        long minutes = ChronoUnit.MINUTES.between(fullDateTimeToStart,dateactu);
        Log.i("[JOB]","Extraction initial delay for work request : " + Long.toString(minutes) + " min");

        //extract position

        //on peut creer l'alrme
        if (minutes < 0 ){


            //first we cancel all job with tag popup12h00
            androidx.work.WorkManager.getInstance(this).cancelAllWorkByTag("popup12h00");

            //second create a new work
            WorkRequest newLoadWork = new OneTimeWorkRequest.Builder(WorkManagerTest.class)
                    .setInitialDelay(Math.abs((int)minutes), TimeUnit.MINUTES)
                    .addTag("popup12h00")
                    .build();

            androidx.work.WorkManager.getInstance(this).enqueue(newLoadWork);

            //periodic mode
            // PeriodicWorkRequest newLoadPeriodicWork = new PeriodicWorkRequest.Builder(WorkManagerTest.class,
            //        15, TimeUnit.MINUTES)
            // //constrains
            //.build();
        }
        //le temps est dépassé
        else
        {
            //first we cancel all job with tag popup12h00
            androidx.work.WorkManager.getInstance(this).cancelAllWorkByTag("popup12h00");

            //second create a new work
            WorkRequest newLoadWork = new OneTimeWorkRequest.Builder(WorkManagerTest.class)
                    .setInitialDelay(Math.abs((int)minutes) + 1440, TimeUnit.MINUTES)
                    .addTag("popup12h00")
                    .build();

            androidx.work.WorkManager.getInstance(this).enqueue(newLoadWork);

            //nothing to do
            //j+1 ?
        }

    }

    private void setupPermission() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.INTERNET}, 0);
    }

    private void setupViewModel() {

        this.myViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(ViewModelMain.class);
        this.myViewModel.myAppMapMediator.observe(this, new Observer<ViewMainState>() {
            @Override
            public void onChanged(ViewMainState viewStateMain) {

                if (viewStateMain.LoginState) {

                   // myViewModel.getUser();


                    request_user_info(viewStateMain.getMyUser());
                    Log.i("MEDIA", "connecté recup infoMEDIA");
                } else {
                    request_login();
                    Log.i("MEDIA", "non connecté attente");
                }

            }
        });
    }

    private void Authentification() {

        //0 = canceled
        //-1 = OK

        openFirebaseAuthForResult = registerForActivityResult(
                new FirebaseAuthUIActivityResultContract(),
                new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                    @Override
                    public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {

                        showSnackBar(result.getIdpResponse().toString());

                        myViewModel.checkUserLogin();
                        if (result.getResultCode() == -1) {
                            Log.i("[Auth]", "login ok " + result.getResultCode());

                            myViewModel.checkUserLogin();


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
                    }
                });
    }

    private void request_login() {

        //setup layout
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

    //TODO: review binding navigation view ?
    private void request_user_info(FirebaseUser myUserResult) {


       // showSnackBar(myUserResult.getDisplayName());

        //if (mymyViewModel.getUser() != null) {
        if (myUserResult != null) {



            //showSnackBar(myViewModel.getUser().getDisplayName());

            View hv = binding.navigationView.getHeaderView(0);

            TextView name = hv.findViewById(R.id.displayName);

            //name.setText(Objects.requireNonNull(this.myViewModel.getUser().getDisplayName()));
            name.setText(Objects.requireNonNull(myUserResult.getDisplayName()));

            TextView email = hv.findViewById(R.id.email);

            //email.setText(this.myViewModel.getUser().getEmail());
            email.setText(myUserResult.getEmail());

            ImageView avatar = hv.findViewById(R.id.avatar);


            //avatar
            String avatar2 = "";

            //if (this.myViewModel.getUser().getPhotoUrl() == null) {
            if (myUserResult.getPhotoUrl() == null) {

                String nom = this.myViewModel.getUser().getDisplayName();
                String[] parts = nom.split(" ", 2);
                String z = "";

                for (int i = 0; i < parts.length; i++) {
                    z = parts[i].charAt(0) + z;
                }

                avatar2 = "https://eu.ui-avatars.com/api/?name=" + z;
            } else {


                           //     myUserResult.getProviderId();
                             //   if (myUserResult.getProviderId().equals(FacebookAuthProvider.PROVIDER_ID)){
                                    avatar2 = myUserResult.getPhotoUrl().toString() ;//+ "?access_token=<"+ AccessToken.getCurrentAccessToken()+">" ;
                                    //+ "?access_token=<facebook_access_token>"
                               // }else
                               // {
                                 //   avatar2 = myUserResult.getPhotoUrl().toString();

                               // }//
                //avatar2 = this.myViewModel.getUser().getPhotoUrl().toString();

                Log.i("[AVATAR]", "" + avatar2.toString());


            }

            Glide.with(avatar)
                    .load(avatar2)
                    .circleCrop()
                    .into(avatar);

            if (myViewModel.getUser().getPhotoUrl() == null) {
                Log.i("[LOGIN]", "Pas d'avatar");
            } else {

                Glide.with(avatar)
                        .load(this.myViewModel.getUser().getPhotoUrl())
                        .circleCrop()
                        .into(avatar);
            }
        }
        else
        {
            showSnackBar("profil introuvable");
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
                //myViewModel.logOut(this);

                myViewModel.LogOut(this);
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
            } else if (id == R.id.action_list) {
                binding.viewpager.setCurrentItem(2);
            } else if (id == R.id.action_work) {
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

        //work fine
        binding.topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.drawerLayout.openDrawer(START);
            }
        });


        binding.topAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                return false;
            }
        });
    }


    public void onSearchCalled() {


        // Set the fields to specify which types of place data to return.
        /*  List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);
        // Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields).setCountry("FR") //NIGERIA
                .build(this);


       */
        //  startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);

    }


    //work fine but sdk with can't specify position
    public void startAutocompleteActivity(MenuItem item) {

        /*    String st = null;

        if (myViewModel.getResultAutocomplete().getValue() != null) {
            for (int i = 0; i < myViewModel.getResultAutocomplete().getValue().getPredictions().size(); i++) {
                st = st + myViewModel.getResultAutocomplete().getValue().getPredictions().get(i).getPlaceId();
            }
        }
     */

 /*       if (st != null) {
            showSnackBar("Resultat autocomplete :" + st);
        } else {
            showSnackBar("Resultat vide autocomplete :");
        }

  */


  /*      Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.OVERLAY,

                Arrays.asList(Place.Field.ID, Place.Field.NAME)    )
                .setTypeFilter(TypeFilter.ESTABLISHMENT)
                .setCountries(Arrays.asList("FR"))
                .build(this);

        startActivityForResult(intent, 123);


*/
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 123) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                showSnackBar(place.getName() + " id: " + place.getId());




              /*  if (myListResponse.size()>0) {
                    Intent intent = new Intent(this, DetailActivity.class);
                    intent.putExtra("TAG_ID", myListResponse.get(0).getPredictions().get(0).getPlaceId());
                    startActivity(intent);
                } else {
                    showSnackBar("Vous n'avez pas de favoris");
                }

               */


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
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if (query.length() > 3) {

                    Location mypos = myViewModel.getMyPosition();

                    showSnackBar("test" + mypos.getLatitude() + " " + mypos.getLatitude());

                    String st = null;


                    List<com.hoarauthomas.go4lunchthp7.PlaceAutocomplete> myListResponse = new ArrayList<>();


                    List<PlaceAutocomplete> myAutocompleteList = (List<PlaceAutocomplete>) myViewModel.getResultAutocomplete(query, mypos).getValue();


                    if (myViewModel.getResultAutocomplete(query, mypos).getValue() != null) {


                        for (int i = 0; i < myViewModel.getResultAutocomplete(query, mypos).getValue().getPredictions().size(); i++) {

                            myListResponse.add(myViewModel.getResultAutocomplete(query, mypos).getValue());
                            st = st + myViewModel.getResultAutocomplete(query, mypos).getValue().getPredictions().get(i).getDescription();


                            Log.i("[AUTOCOMPLETE]", "" + myListResponse.size() + myListResponse.get(i).getPredictions().get(i).getDescription());
                        }


                    }


                    if (myListResponse.size() > 0) {
                        showSnackBar("Resultat autocomplete :" + myListResponse.size());

                        Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                        intent.putExtra("TAG_ID", myListResponse.get(0).getPredictions().get(0).getPlaceId());
                        startActivity(intent);


//                       if (myListResponse.size()>0) {
//                            Intent intent = new Intent(this, DetailActivity.class);
//                            intent.putExtra("TAG_ID", myListResponse.get(0).getPredictions().get(0).getPlaceId());
//                            startActivity(intent);
//                        } else {
//                            showSnackBar("Vous n'avez pas de favoris");
//                        }


                    } else {
                        showSnackBar("Resultat vide autocomplete :");
                    }


                } else {
                    showSnackBar("critere trop faible");
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {


                return false;
            }
        });


        return super.onCreateOptionsMenu(menu);

    }
}
