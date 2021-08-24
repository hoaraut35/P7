package com.hoarauthomas.go4lunchthp7.ui.detail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.hoarauthomas.go4lunchthp7.BuildConfig;
import com.hoarauthomas.go4lunchthp7.api.GooglePlaceApi;
import com.hoarauthomas.go4lunchthp7.api.UserHelper;
import com.hoarauthomas.go4lunchthp7.databinding.ActivityDetailRestaurantBinding;
import com.hoarauthomas.go4lunchthp7.model.firestore.User;
import com.hoarauthomas.go4lunchthp7.viewmodel.ViewModelFactory;

import java.util.List;

import javax.annotation.Nullable;

public class DetailActivity extends AppCompatActivity {

    private ActivityDetailRestaurantBinding binding;

    private ViewModelDetail myViewModelDetail;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDetailRestaurantBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        setupViewModel();
        setupIntent();
    }

    private void setupIntent() {

        Intent intent = getIntent();

        if (intent.getStringExtra("TAG_ID") != null) {
            //Log.i("[MONDETAIL]", "TAG MODE" + restaurant_id);
            myViewModelDetail.setPlaceId(intent.getStringExtra("TAG_ID"));
            showSnackBar(intent.getStringExtra("TAG_ID"));
        } else {
            //restaurant_id = "";
            // Log.i("[MONDETAIL]", "Pas de restaurant sélectionné : [" + restaurant_id + "]");
        }
    }

    private void setupViewModel() {

        myViewModelDetail = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(ViewModelDetail.class);
        myViewModelDetail.getMediatorLiveData().observe(this, new Observer<ViewStateDetail>() {
            @Override
            public void onChanged(ViewStateDetail viewStateDetail) {
                Log.i("[MONDETAIL]", "detail demandé activity");

                //show image of restaurant if exist
                try {
                    String base = "https://maps.googleapis.com/maps/api/place/photo?";
                    String key = "key=" + BuildConfig.MAPS_API_KEY;
                    String reference = "&photoreference=" + viewStateDetail.myRestaurantObject.getPhotos().get(0).getPhotoReference();
                    String maxH = "&maxheight=157";
                    String maxW = "&maxwidth=157";
                    String query = base + key + reference + maxH + maxW;

                    Glide.with(binding.backgroundImage)
                            .load(query)
                            .centerCrop()
                            .into(binding.backgroundImage);

                } catch (Exception e) {
                    Log.i("[IMAGE]", "Exception : " + e.getMessage());
                }

                //titre retaurant
                binding.restaurantTitre.setText(viewStateDetail.myRestaurantObject.getName());

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

                //TODO:or null ?
                if (viewStateDetail.myRestaurantDetailObject.getUrl().isEmpty()) {
                    showSnackBar("PAs de lien internet");
                    return;
                } else {
                    setupButtonWeb(viewStateDetail.getMyRestaurantDetailObject().getUrl());

                }

                if (viewStateDetail.getMyRestaurantDetailObject().getFormattedPhoneNumber() != null) {
                    setupButtonPhone(viewStateDetail.getMyRestaurantDetailObject().getFormattedPhoneNumber());
                }

                setupRecyclerView(viewStateDetail.getMyWorkMatesTag());


                setupButtonChoice();
                setupButtonLike();

            }
        });


    }

    private void setupButtonChoice() {

        binding.choiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i("[CLIC]", "Clic sur choix restaurant ..." + myViewModelDetail.getPlaceId());
                myViewModelDetail.setFavRestaurant("X94e0eWaE1hxG9AFu1MSLxqExnp1", myViewModelDetail.getPlaceId());
            }
        });

    }


    //to map a range to another range ... from arduino library
    public double map(double value, double in_min, double in_max, double out_min, double out_max) {
        return (value - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }

    private void setupRecyclerView(List<User> myWorkmatesList) {
        if (myWorkmatesList == null) {
            Log.i("[MONDETAIL]", "liste user nulle ou vide");
            return;
        } else if (!myWorkmatesList.isEmpty()) {
            Log.i("[MONDETAIL]", "liste user ok");
            RecyclerView recyclerView = binding.recyclerView;
            //     RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
            recyclerView.setHasFixedSize(false);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            //   recyclerView.addItemDecoration(itemDecoration);
            recyclerView.setAdapter(new RecyclerViewAdapterDetail(myWorkmatesList));
        } else {
            Log.i("[MONDETAIL]", "liste user vide");
        }
    }


    //TODO: set the web site from viewmodel ?
    private void setupButtonWeb(@Nullable String url) {

        binding.website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (url != null) {

                    if (!url.isEmpty()) {
                        Intent makeURLBrowser = new Intent(Intent.ACTION_VIEW);
                        makeURLBrowser.setData(Uri.parse(url));
                        startActivity(makeURLBrowser);
                    } else {
                        showSnackBar("Pas de site renseigné!");
                    }

                }
            }

        });
    }

    private void setupButtonLike() {
        binding.likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSnackBar("Enregistrer le restaurant : " + binding.restaurantTitre.getText());

                    myViewModelDetail.adLikedRestaurant("X94e0eWaE1hxG9AFu1MSLxqExnp1", "test");
            }
        });
    }

    //TODO: set the phone number to dial
    private void setupButtonPhone(@Nullable String number) {
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