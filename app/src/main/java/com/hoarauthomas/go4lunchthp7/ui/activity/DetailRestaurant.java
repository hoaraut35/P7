package com.hoarauthomas.go4lunchthp7.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.hoarauthomas.go4lunchthp7.databinding.ActivityDetailRestaurantBinding;
import com.hoarauthomas.go4lunchthp7.model.firestore.User;
import com.hoarauthomas.go4lunchthp7.ui.adapter.RecyclerViewAdapter;
import com.hoarauthomas.go4lunchthp7.viewmodel.ViewModelFactory;
import com.hoarauthomas.go4lunchthp7.viewmodel.ViewModelGo4Lunch;

import java.util.ArrayList;
import java.util.List;

public class DetailRestaurant extends AppCompatActivity {

    private ActivityDetailRestaurantBinding binding;
    private ViewModelGo4Lunch myViewModel;
    //th elist of restaurants
    public final ArrayList<com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo> allResult = new ArrayList<>();
    private RecyclerView recyclerView;


    private String restaurant_id,workmate_id;

    private com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDetailRestaurantBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Intent intent = getIntent();
        restaurant_id = intent.getStringExtra("TAG_ID");
        Log.i("[DETAIL]","id restaurant " + restaurant_id);
        workmate_id = intent.getStringExtra("WORKMATEID");
        //showSnackBar(restaurant_id);

        setupRecyclerView();
     //   setupViewModel();
        setupButtonPhone();
        setupButtonLike();
        setupButtonWeb();




    }


    //TODO: get the restaurant ...
    private void setupViewModel() {
        this.myViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(ViewModelGo4Lunch.class);

        this.myViewModel.getRestaurantDetail(restaurant_id);

       // this.myViewModel.getMyPosition().observe(this, this::onUpdatePosition);
        //this.myViewModel.getAllWorkMatesByRestaurant().observe(this, this::onUpdateWorkMates);

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
        recyclerView.setAdapter(new RecyclerViewAdapter( allResult));
    }


    //TODO: set the web site from viewmodel ?
    private void setupButtonWeb() {
        binding.website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = "http://www.google.fr";
                Intent makeURLBrowser = new Intent(Intent.ACTION_VIEW);
                makeURLBrowser.setData(Uri.parse(url));
                startActivity(makeURLBrowser);

            }
        });
    }

    private void setupButtonLike() {
        binding.likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("[DETAIL]", "clic sur like bouton");
                myViewModel.onLikeClicked();
            }
        });
    }

    //TODO: set the phone number to dial
    private void setupButtonPhone() {
        binding.callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("[DETAIL]", "clic sur phone");

                Intent makeCall = new Intent(Intent.ACTION_DIAL);
                makeCall.setData(Uri.parse("tel:" + "0781804664"));
                startActivity(makeCall);
            }
        });

    }

    private void showSnackBar(String message) {
        Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_SHORT).show();
    }


}