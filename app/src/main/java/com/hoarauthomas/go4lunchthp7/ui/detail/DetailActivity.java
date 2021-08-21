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
import com.hoarauthomas.go4lunchthp7.ui.restaurant.RecyclerViewAdapter;
import com.hoarauthomas.go4lunchthp7.viewmodel.ViewModelFactory;

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

        //    setupButtonLike();


    }


    //TODO: get the restaurant ...
    private void setupViewModel() {

        myViewModelDetail = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(ViewModelDetail.class);

        myViewModelDetail.setPlaceId(restaurant_id);

        myViewModelDetail.getMediatorLiveData().observe(this, new Observer<ViewStateDetail>() {
            @Override
            public void onChanged(ViewStateDetail viewStateDetail) {
                Log.i("[MONDETAIL]", "detail demandé activity");

                //titre retaurant
                binding.restaurantTitre.setText(viewStateDetail.getMyTitle());

                //adresse restaurant
                binding.restaurantAddress.setText(viewStateDetail.myRestaurantObject.getVicinity());

                //show star rating
                try {
                    double ratingDbl = map(viewStateDetail.myRestaurantObject.getRating(), 1.0, 5.0, 1.0, 3.0);

                    int ratingInt = (int) Math.round(ratingDbl);

                    //Log.i("[RATING]", "Convert [1.0 ... 5.0] : " + result.getRating() + " to [1.00 ... 3.0] : " + map(result.getRating(), 1.0, 5.0, 1.0, 3.0) + "to int :" + ratingInt);

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

                //setupButtonWeb(viewStateDetail.getMyRestaurantDetailObject().getUrl());

                //setupButtonPhone(viewStateDetail.getMyRestaurantDetailObject().getFormattedPhoneNumber());

                setupRecyclerView(viewStateDetail.getMyWorkMatesTag());
            }
        });


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

    private void setupRecyclerView(List<User> myWorkmatesList) {
        if (myWorkmatesList == null)
        {
            Log.i("[MONDETAIL]","liste user nulle ou vide");
            return;
        }else
            if (!myWorkmatesList.isEmpty()){
                Log.i("[MONDETAIL]","liste user ok");
                recyclerView = binding.recyclerView;
                RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
                recyclerView.setHasFixedSize(false);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                recyclerView.addItemDecoration(itemDecoration);
                recyclerView.setAdapter(new RecyclerViewAdapterDetail(myWorkmatesList));
            }else
            {
                Log.i("[MONDETAIL]","liste user vide");
            }
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