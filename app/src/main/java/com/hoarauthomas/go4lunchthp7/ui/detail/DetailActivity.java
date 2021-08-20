package com.hoarauthomas.go4lunchthp7.ui.detail;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.hoarauthomas.go4lunchthp7.databinding.ActivityDetailRestaurantBinding;
import com.hoarauthomas.go4lunchthp7.model.firestore.User;
import com.hoarauthomas.go4lunchthp7.model.placedetails2.MyDetailRestaurant;
import com.hoarauthomas.go4lunchthp7.model.placedetails2.ResultDetailRestaurant;
import com.hoarauthomas.go4lunchthp7.ui.map.ViewModelMap;
import com.hoarauthomas.go4lunchthp7.ui.map.ViewStateMap;
import com.hoarauthomas.go4lunchthp7.ui.restaurant.RecyclerViewAdapter;
import com.hoarauthomas.go4lunchthp7.viewmodel.MainViewState;
import com.hoarauthomas.go4lunchthp7.viewmodel.ViewModelFactory;
import com.hoarauthomas.go4lunchthp7.viewmodel.ViewModelGo4Lunch;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private ActivityDetailRestaurantBinding binding;

    private ViewModelDetail myViewModelDetail;

    //th elist of restaurants
    public final ArrayList<com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo> allResult = new ArrayList<>();
    private RecyclerView recyclerView;


    private String restaurant_id, workmate_id, my_restaurant_id;
    private ResultDetailRestaurant monRestauDetail;

    private String phone, url;
    private com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDetailRestaurantBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Intent intent = getIntent();

        restaurant_id = intent.getStringExtra("TAG_ID");
       // Log.i("[DETAIL]", "id restaurant " + restaurant_id);
     //   workmate_id = intent.getStringExtra("WORKMATEID");

       // my_restaurant_id = intent.getStringExtra("MYRESTAURANT");


        if (intent.getStringExtra("TAG_ID") != null && intent.getStringExtra("WORKMATEID_ID") != null && intent.getStringExtra("MYRESTAURANT_ID") != null) {
            Log.i("[DETAIL]", "TAG MODE");
        } else {


        }



        setupViewModel();
        //showSnackBar(restaurant_id);

  //      setupRecyclerView();
        //setupButtonPhone();
    //    setupButtonLike();
        // setupButtonWeb();


    }


    //TODO: get the restaurant ...
    private void setupViewModel() {

        myViewModelDetail = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(ViewModelDetail.class);

        myViewModelDetail.setPlaceId(restaurant_id);

        myViewModelDetail.getMediatorLiveData().observe(this, new Observer<ViewStateDetail>() {
            @Override
            public void onChanged(ViewStateDetail viewStateDetail) {
                Log.i("[MONDETAIL]","detail demandé activity");
                binding.restaurantTitre.setText(viewStateDetail.getMyTitle());
            }
        });

        /*myViewModelDetail.getMediatorLiveData().observe(this, new Observer<ViewStateDetail>() {
            @Override
            public void onChanged(ViewStateDetail viewStateDetail) {
                Log.i("[DETAIL]", "Event ViewDetail Event");

            }
        } );

         */
        /*new Observer<ViewStateMap>() {
            @Override
            public void onChanged(ViewStateMap viewStateMap) {


                showMapWithPosition(viewStateMap.myPosition);

                showRestaurant2(viewStateMap.myRestaurantsList);
            }
        });

         */


     /*   this.myViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(ViewModelGo4Lunch.class);


        this.myViewModel.getViewStateLiveData().observe(this, new Observer<MainViewState>() {
            @Override
            public void onChanged(MainViewState mainViewState) {


                if (mainViewState.getMyRestaurantsList() != null) {
                    showSnackBar("Restaurants : " + mainViewState.getMyRestaurantsList().size() + " " + restaurant_id);


                    for (int i = 0; i < mainViewState.getMyRestaurantsList().size(); i++) {

                        if (mainViewState.getMyRestaurantsList().get(i).getPlaceId().equals(restaurant_id)) {
                            binding.restaurantTitre.setText(mainViewState.getMyRestaurantsList().get(i).getName());
                            binding.restaurantAddress.setText(mainViewState.getMyRestaurantsList().get(i).getVicinity());
                        }

                    }

                }

            }
        });

      */


        //this.myViewModel.getRestaurantDetail2(restaurant_id).observe(this, this::onUpdateDetail);

//        binding.restaurantTitre.setText(monRestauDetail.getFormattedPhoneNumber());

        //  monRestauDetail = this.myViewModel.getRestaurantDetail2(restaurant_id);
        //test = this.myViewModel.getRestaurantDetail(restaurant_id);


        //   binding.restaurantTitre.setText(results.get(i).getName());
        // binding.restaurantAddress.setText(results.get(i).getVicinity());
        // this.myViewModel.getMyPosition().observe(this, this::onUpdatePosition);
        //this.myViewModel.getAllWorkMatesByRestaurant().observe(this, this::onUpdateWorkMates);

    }

    private void onUpdateDetail2(MyDetailRestaurant myDetailRestaurant) {
        Log.i("[DETAIL]", "detail sur activity" + myDetailRestaurant.getResult().getUrl());

        this.phone = myDetailRestaurant.getResult().getFormattedPhoneNumber();
        this.url = myDetailRestaurant.getResult().getUrl();

    }

    private void onUpdateDetail(ResultDetailRestaurant resultDetailRestaurant) {

        //     for (int i =0; i< )


        setupButtonPhone(resultDetailRestaurant.getFormattedPhoneNumber());
        setupButtonWeb(resultDetailRestaurant.getUrl());

        Log.i("[DETAIL]", "detail sur activity" + resultDetailRestaurant.getFormattedPhoneNumber() + resultDetailRestaurant.getUrl());
    }

    private void onUpdatePosition(Location location) {
        //  LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        //  this.myViewModel.updateLngLat(location.getLongitude(), location.getLatitude());
        //replace by th elist of workmates...
        //  Log.i("[FIND]", "onupdateposition");
//
    }

    private void onUpdateWorkMates(List<User> users) {

        Log.i("[WORK]", "Workmates on detail activity" + users.size());
        allResult.clear();
        for (int i = 0; i < users.size(); i++) {
            User myUser = users.get(i);


        }

    }

    private void onUpdateRestaurants(List<com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo> results) {


        //recuperer le restauirant ici ?

        for (int i = 0; i < results.size(); i++) {

            result = results.get(i);

            Log.i("[FIND]", "Nb restaurant à scanner : " + results.size() + " " + results.get(i).getPlaceId().toString() + " " + restaurant_id);


            if (results.get(i).getPlaceId().toString().equals(restaurant_id)) {
                Log.i("[FIND]", "Restaurant trouvé");

                binding.restaurantTitre.setText(results.get(i).getName());
                binding.restaurantAddress.setText(results.get(i).getVicinity());

                //setupButtonPhone(result..);binding.

                //show star rating
                try {
                    double ratingDbl = map(result.getRating(), 1.0, 5.0, 1.0, 3.0);
                    int ratingInt = (int) Math.round(ratingDbl);
                    Log.i("[RATING]", "Convert [1.0 ... 5.0] : " + result.getRating() + " to [1.00 ... 3.0] : " + map(result.getRating(), 1.0, 5.0, 1.0, 3.0) + "to int :" + ratingInt);

                    if (ratingInt == 1) {
                        binding.ratingbar.setRating(1);
                    } else if (ratingInt == 2) {
                        binding.ratingbar.setRating(2);
                    } else if (ratingInt == 3) {
                        binding.ratingbar.setRating(3);
                    }

                } catch (Exception e) {
                    Log.i("[RATING]", "Pas de notation pour ce restaurant !");
                    binding.ratingbar.setRating(0);
                }


            }

        }

        allResult.clear();
        allResult.addAll(results);
        // Objects.requireNonNull(recyclerView.getAdapter()).notifyDataSetChanged();
    }

    //to map a range to another range ... from arduino library
    public double map(double value, double in_min, double in_max, double out_min, double out_max) {
        return (value - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }

    private void setupRecyclerView() {
        recyclerView = binding.recyclerView;
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setAdapter(new RecyclerViewAdapter(allResult));
    }


    //TODO: set the web site from viewmodel ?
    private void setupButtonWeb(String url) {
        binding.website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (url != null) {
                    Intent makeURLBrowser = new Intent(Intent.ACTION_VIEW);
                    makeURLBrowser.setData(Uri.parse(url));
                    startActivity(makeURLBrowser);
                } else {
                    showSnackBar("Pas de site renseigné!");
                }

            }
        });
    }

    private void setupButtonLike() {
        binding.likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showSnackBar("Enregistrer le restaurant : " + binding.restaurantTitre.getText());
           //     myViewModel.onLikeClicked();
            }
        });
    }

    //TODO: set the phone number to dial
    private void setupButtonPhone(String number) {
        binding.callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (number != null) {
                    Intent makeCall = new Intent(Intent.ACTION_DIAL);
                    makeCall.setData(Uri.parse("tel:" + number));
                    startActivity(makeCall);
                } else {
                    showSnackBar("Pas de téléphone renseigné!");
                }
            }
        });

    }

    private void showSnackBar(String message) {
        Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_SHORT).show();
    }


}