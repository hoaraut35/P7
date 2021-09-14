package com.hoarauthomas.go4lunchthp7.ui;

import static androidx.core.view.GravityCompat.START;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
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
import com.google.firebase.auth.UserInfo;
import com.hoarauthomas.go4lunchthp7.BuildConfig;
import com.hoarauthomas.go4lunchthp7.PlaceAutocomplete;
import com.hoarauthomas.go4lunchthp7.R;
import com.hoarauthomas.go4lunchthp7.databinding.ActivityMainBinding;
import com.hoarauthomas.go4lunchthp7.factory.ViewModelFactory;
import com.hoarauthomas.go4lunchthp7.ui.detail.DetailActivity;
import com.hoarauthomas.go4lunchthp7.ui.map.ViewModelMap;
import com.hoarauthomas.go4lunchthp7.workmanager.AlarmManager;
import com.hoarauthomas.go4lunchthp7.workmanager.WorkManagerTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    /**
     * for binding views
     */
    public ActivityMainBinding binding;
    String myChoice = null;
    /**
     * for viewModel
     */
    public ViewModelMain myViewModel;
    public ViewModelMap myViewModelMap;

    /**
     * List of providers for authentification
     */
    private final List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.TwitterBuilder().build(),
            new AuthUI.IdpConfig.EmailBuilder().build(),
            new AuthUI.IdpConfig.FacebookBuilder().build(),
            new AuthUI.IdpConfig.GoogleBuilder().build());

    private ActivityResultLauncher<Intent> openFirebaseAuthForResult;

    /**
     * Main methof for initialise application
     *
     * @param savedInstanceState
     */

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


        setupSettings();

        //    loadWork();//alarm

        //   loadtest();
        setupAutocomplete();

        //     testDialog();

    }

    private void testDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "annuler", Toast.LENGTH_LONG).show();
                dialog.cancel();
                //finish();
            }
        });

        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "ok", Toast.LENGTH_LONG).show();
                dialog.cancel();
            }
        });

        String[] test = {"choiix", " choix 2", "choix3"};

        builder.setTitle("Résultat recherche")

                .setItems(test, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //                    Toast.makeText(MainActivity.this,"coiol",Toast.LENGTH_LONG).show();
                    }


                });


        builder.setCancelable(true);

        AlertDialog dialoog = builder.create();
        dialoog.show();


    }


    private void loadtest() {

        AlarmManager newAlarm = new AlarmManager();
        newAlarm.getAlarmManager(this);
        newAlarm.setAlarm();

    }

    /**
     * Get notification state from settings fragments and send it to viewmodel
     */
    private void setupSettings() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean myBool = sharedPref.getBoolean("notifications2", false);
        myViewModel.setNotification(myBool);
        //for debug only
        showSnackBar("Notification is : " + Boolean.toString(myBool));
    }

    /**
     * setup autocomplete api
     */
    private void setupAutocomplete() {
        //initialize
        Places.initialize(getApplicationContext(), BuildConfig.MAPS_API_KEY);
        // create
        PlacesClient placesClient = Places.createClient(this);
    }

    // @RequiresApi(api = Build.VERSION_CODES.O)
    private void loadWork() {


        //get actual date
        Calendar currentDate = Calendar.getInstance();
        Log.i("[JOB]", "Calendar actual " + currentDate.getTime().toString());

        //set target date
        Calendar targetDate = Calendar.getInstance();
        //targetDate.set(Calendar.HOUR_OF_DAY,12);
        // targetDate.set(Calendar.MINUTE,0);
        // targetDate.set(Calendar.SECOND,0);
        Log.i("[JOB]", "Calendar target " + targetDate.getTime().toString());

        //to check
        targetDate.set(Calendar.SECOND, 30);

        //add one day if target before current date
        if (targetDate.before(currentDate)) {
            targetDate.add(Calendar.HOUR_OF_DAY, 24);
            Log.i("[JOB]", "Calendar comparaison, one day added to the target " + targetDate.getTime());
        }

        //delay 60000 ms / minute
        long delayTime = targetDate.getTimeInMillis() - currentDate.getTimeInMillis();
        Log.i("[JOB]", "Calendar delay : " + Long.toString(delayTime));

        //build request periodic

        WorkRequest newLoadPeriodicWork = new OneTimeWorkRequest.Builder(WorkManagerTest.class)
                .setInitialDelay(delayTime, TimeUnit.MILLISECONDS)
                .addTag("popup12h00")// //constrains
                .build();

        WorkManager.getInstance(this)
                .enqueue(newLoadPeriodicWork);


//
//        //Determine the format to work
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
//
//        //Determine actual datetime
//        LocalDateTime dateactu = LocalDateTime.now();
//        Log.i("[JOB]","Actual date and time : " + dateactu.format(formatter));
//
//        //Determine the target date and time to execute request
//        LocalDate dateToStart = LocalDate.now();
//        LocalTime timeToStart = LocalTime.parse("12:00:00");
//        LocalDateTime fullDateTimeToStart = LocalDateTime.of(dateToStart,timeToStart);
//        Log.i("[JOB]","Target date and time : " + fullDateTimeToStart.format(formatter).toString());
//
//        //extract initial delay to construct work request after
//        long minutes = ChronoUnit.MINUTES.between(fullDateTimeToStart,dateactu);
//        Log.i("[JOB]","Extraction initial delay for work request : " + Long.toString(minutes) + " min");
//
//        //extract position
//
//        //on peut creer l'alrme
//        if (minutes < 0 ){
//
//
//            //first we cancel all job with tag popup12h00
//          //  androidx.work.WorkManager.getInstance(this).cancelAllWorkByTag("popup12h00");
//
//
//            // .setInitialDelay(Math.abs((int)minutes), TimeUnit.MINUTES)
//            //second create a new work
//            WorkRequest newLoadWork = new OneTimeWorkRequest.Builder(WorkManagerTest.class)
//                  // .setInitialDelay(15,TimeUnit.MINUTES)
//                  //  .addTag("popup12h00")
//                    .build();
//
//            androidx.work.WorkManager.getInstance(this).enqueue(newLoadWork);
//
//            //periodic mode
//            // PeriodicWorkRequest newLoadPeriodicWork = new PeriodicWorkRequest.Builder(WorkManagerTest.class,
//            //        15, TimeUnit.MINUTES)
//            // //constrains
//            //.build();
//        }
//        //le temps est dépassé
//        else
//        {
//            //first we cancel all job with tag popup12h00
//            androidx.work.WorkManager.getInstance(this).cancelAllWorkByTag("popup12h00");
//
//            //second create a new work
//            WorkRequest newLoadWork = new OneTimeWorkRequest.Builder(WorkManagerTest.class)
//                    //.setInitialDelay(Math.abs((int)minutes) + 1440, TimeUnit.MINUTES)
//                //    .setInitialDelay(15,TimeUnit.MINUTES)
//               //     .addTag("popup12h00")
//                    .build();
//
//            androidx.work.WorkManager.getInstance(this).enqueue(newLoadWork);
//
//            //nothing to do
//            //j+1 ?
//        }

    }

    private void setupPermission() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.INTERNET}, 0);
    }

    private void setupViewModel() {

        this.myViewModelMap = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(ViewModelMap.class);
        this.myViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(ViewModelMain.class);

        this.myViewModel.myAppMapMediator.observe(this, new Observer<ViewMainState>() {
            @Override
            public void onChanged(ViewMainState viewStateMain) {
                if (viewStateMain.LoginState) {
                    request_user_info(viewStateMain.getMyUser());
                    //testNotification(viewStateMain.myRestaurant.toString());
                } else {
                    request_login();
                }
            }
        });

        this.myViewModel.getMyPlaceListForUISingle().observe(this, new Observer<PlaceAutocomplete>() {
            @Override
            public void onChanged(PlaceAutocomplete placeAutocomplete) {

              //  Toast.makeText(MainActivity.this, "test single" + placeAutocomplete.getPredictions().get(0).getDescription(), Toast.LENGTH_LONG).show();

                //get the list
                List<String> placeAutoCompleteList = new ArrayList();
                for (int i = 0; i < placeAutocomplete.getPredictions().size(); i++) {
                    placeAutoCompleteList.add(placeAutocomplete.getPredictions().get(i).getDescription());
                }

                //transform list to array
                String[] ArrayListForDialog = new String[placeAutoCompleteList.size()];
                ArrayListForDialog = placeAutoCompleteList.toArray(ArrayListForDialog);




                //make dialog list
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                     //   Toast.makeText(MainActivity.this, "annuler", Toast.LENGTH_LONG).show();
                        dialog.cancel();
                        //finish();
                    }
                });

                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       // Toast.makeText(MainActivity.this, "ok", Toast.LENGTH_LONG).show();
                       // myChoice = placeAutocomplete.getPredictions().get(which).getDescription();
                        dialog.cancel();
                    }
                });

                builder.setTitle("Résultat recherche")

                        //.setItems(ArrayListForDialog, new DialogInterface.OnClickListener() {
                        .setItems(ArrayListForDialog, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                              //  Toast.makeText(MainActivity.this, "coiol", Toast.LENGTH_LONG).show();
                          //      dialog.dismiss();
                        //        dialog.cancel();
                                //    myViewModelMap.setPositionWithPlaceId(placeAutocomplete.getPredictions().get(which).getPlaceId());
                            }


                        });


                //Toast.makeText(MainActivity.this, myChoice, Toast.LENGTH_LONG).show();

                builder.setCancelable(true);

                AlertDialog dialoog = builder.create();
                dialoog.show();



            }
        });

        //TODO: bug alertdialog
        this.myViewModelMap.getMyPositionFromAutoSingleMode().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(MainActivity.this, s, Toast.LENGTH_LONG).show();
            }
        });


     /*   this.myViewModel.getMyPlaceListForUI().observe(this, new Observer<PlaceAutocomplete>() {
            @Override
            public void onChanged(PlaceAutocomplete placeAutocomplete) {

                //get the list
                List<String> placeAutoCompleteList = new ArrayList();
                for (int i = 0; i < placeAutocomplete.getPredictions().size(); i++) {
                    placeAutoCompleteList.add(placeAutocomplete.getPredictions().get(i).getDescription());
                }

                //transform list to array
                String[] ArrayListForDialog = new String[placeAutoCompleteList.size()];
                ArrayListForDialog = placeAutoCompleteList.toArray(ArrayListForDialog);

                String[] test = {"un","deux","trois"};

                //make dialog list
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "annuler", Toast.LENGTH_LONG).show();
                        dialog.cancel();
                        //finish();
                    }
                });

                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "ok", Toast.LENGTH_LONG).show();
                        dialog.cancel();
                    }
                });

                builder.setTitle("Résultat recherche")

                        //.setItems(ArrayListForDialog, new DialogInterface.OnClickListener() {
                        .setItems(test, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Toast.makeText(MainActivity.this, "coiol", Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                                dialog.cancel();
                                //    myViewModelMap.setPositionWithPlaceId(placeAutocomplete.getPredictions().get(which).getPlaceId());
                            }


                        });


                builder.setCancelable(true);

                AlertDialog dialoog = builder.create();
                dialoog.show();


            }
        });


      */

    }

    private void Authentification() {

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


            ImageView avatar = hv.findViewById(R.id.avatar);


            //avatar variable
            String avatar2 = "";

            //specific provider
            for (UserInfo profile : myUserResult.getProviderData()) {


                String photoProfile = Objects.requireNonNull(profile.getPhotoUrl()).toString();
                Log.i("[PROFILE]", "Profile photo url : " + profile.getPhotoUrl());


                email.setText(profile.getEmail());

                if (photoProfile == null) {

                    String nom = this.myViewModel.getUser().getDisplayName();
                    String[] parts = nom.split(" ", 2);
                    String z = "";

                    for (int i = 0; i < parts.length; i++) {
                        z = parts[i].charAt(0) + z;
                    }

                    avatar2 = "https://eu.ui-avatars.com/api/?name=" + z;
                } else {
                    avatar2 = photoProfile;
                }


            }


            //if (this.myViewModel.getUser().getPhotoUrl() == null) {
//            if (myUserResult.getPhotoUrl() == null) {
//
//                String nom = this.myViewModel.getUser().getDisplayName();
//                String[] parts = nom.split(" ", 2);
//                String z = "";
//
//                for (int i = 0; i < parts.length; i++) {
//                    z = parts[i].charAt(0) + z;
//                }
//
//                avatar2 = "https://eu.ui-avatars.com/api/?name=" + z;
//            } else {
//
//
//                           //     myUserResult.getProviderId();
//                             //   if (myUserResult.getProviderId().equals(FacebookAuthProvider.PROVIDER_ID)){
//                                    avatar2 = myUserResult.getPhotoUrl().toString() ;//+ "?access_token=<"+ AccessToken.getCurrentAccessToken()+">" ;
//                                    //+ "?access_token=<facebook_access_token>"
//                               // }else
//                               // {
//                                 //   avatar2 = myUserResult.getPhotoUrl().toString();
//
//                               // }//
//                //avatar2 = this.myViewModel.getUser().getPhotoUrl().toString();
//
//                Log.i("[AVATAR]", "" + avatar2.toString());
//
//
//            }

            Glide.with(avatar)
                    .load(avatar2)
                    .circleCrop()
                    .into(avatar);

//            if (myViewModel.getUser().getPhotoUrl() == null) {
//                Log.i("[LOGIN]", "Pas d'avatar");
//            } else {
//
//                Glide.with(avatar)
//                        .load(this.myViewModel.getUser().getPhotoUrl())
//                        .circleCrop()
//                        .into(avatar);
//            }
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


                if (myViewModel.getMyPosition() != null && query.length() > 3) {

                    Location mypos = myViewModel.getMyPosition();

                    String st = null;


                    List<com.hoarauthomas.go4lunchthp7.PlaceAutocomplete> myListResponse = new ArrayList<>();


                    myViewModel.getResultAutocomplete(query, mypos);


                    //envoyer la requete au viewmodel


//                    List<PlaceAutocomplete> myAutocompleteList = (List<PlaceAutocomplete>) myViewModel.getResultAutocomplete(query, mypos).getValue();
//
//
//                    if (myViewModel.getResultAutocomplete(query, mypos).getValue() != null) {
//
//
//                        for (int i = 0; i < myViewModel.getResultAutocomplete(query, mypos).getValue().getPredictions().size(); i++) {
//
//                            myListResponse.add(myViewModel.getResultAutocomplete(query, mypos).getValue());
//                            st = st + myViewModel.getResultAutocomplete(query, mypos).getValue().getPredictions().get(i).getDescription();
//
//
//                            Log.i("[AUTOCOMPLETE]", "" + myListResponse.size() + myListResponse.get(i).getPredictions().get(i).getDescription());
//                        }
//
//
//                    }
//
//
//                    if (myListResponse.size() > 0) {
//                        showSnackBar("Resultat autocomplete :" + myListResponse.size());
//
//                        Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
//                        intent.putExtra("TAG_ID", myListResponse.get(0).getPredictions().get(0).getPlaceId());
//                        startActivity(intent);
//
//
////                       if (myListResponse.size()>0) {
////                            Intent intent = new Intent(this, DetailActivity.class);
////                            intent.putExtra("TAG_ID", myListResponse.get(0).getPredictions().get(0).getPlaceId());
////                            startActivity(intent);
////                        } else {
////                            showSnackBar("Vous n'avez pas de favoris");
////                        }
//
//
//                    } else {
//                        showSnackBar("Resultat vide autocomplete :");
//                    }
                    // return true;

                } else {
                    showSnackBar("requete impossible actuellemtn");
                    //    return true;
                }

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {


                return false;
            }
        });


        return super.onCreateOptionsMenu(menu);

    }
}