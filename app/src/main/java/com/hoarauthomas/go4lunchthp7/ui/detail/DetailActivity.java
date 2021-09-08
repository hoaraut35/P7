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
import com.hoarauthomas.go4lunchthp7.R;
import com.hoarauthomas.go4lunchthp7.databinding.ActivityDetailRestaurantBinding;
import com.hoarauthomas.go4lunchthp7.factory.ViewModelFactory;
import com.hoarauthomas.go4lunchthp7.model.SpecialWorkMates;
import com.hoarauthomas.go4lunchthp7.model.firestore.User;

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

    //update placeId to ViewModel from intent
    private void setupIntent() {
        Intent intent = getIntent();
        if (intent.getStringExtra("TAG_ID") != null) {
            myViewModelDetail.setPlaceId(intent.getStringExtra("TAG_ID"));
            showSnackBar("Place id selected for detail : " + intent.getStringExtra("TAG_ID"));
        }
    }

    private void setupViewModel() {

        myViewModelDetail = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(ViewModelDetail.class);

        myViewModelDetail.getMediatorScreen().observe(this, new Observer<ScreenDetailModel>() {
            @Override
            public void onChanged(ScreenDetailModel screenDetailModel) {

                //get photo
                try {
                    String base = "https://maps.googleapis.com/maps/api/place/photo?";
                    String key = "key=" + BuildConfig.MAPS_API_KEY;
                    String reference = "&photoreference=" + screenDetailModel.getUrlPhoto();
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

                //get title
                binding.restaurantTitre.setText(screenDetailModel.getTitle());

                //get address
                binding.restaurantAddress.setText(screenDetailModel.getAddress());

                //get rating
                binding.ratingbar.setRating(screenDetailModel.getRating());

                //get phone number
                setupButtonPhone(screenDetailModel.getCall());

                //get url
                setupButtonWeb(screenDetailModel.getWebsite());

                setupButtonChoice(screenDetailModel.getFavorite());

                setupButtonLike(screenDetailModel.getLiked());

                //get list of workmates
                setupRecyclerView(screenDetailModel.getListWorkMates());

            }
        });



       /*



                //si aucun placeif alors on prend cleui de l'utilisateur actuel
                if (getIntent().getStringExtra("TAG_ID") == null){
                    myViewModelDetail.setPlaceId(viewStateDetail.myUser.getMyFavoriteRestaurantId());
                }

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

                setupButtonChoice(viewStateDetail.getFavoris());

                setupButtonLike(viewStateDetail.getLike());

            }
        });

        */

    }

    private void setupButtonChoice(Boolean state) {

        if (state) {
            binding.choiceBtn.setImageResource(R.drawable.checkon_foreground);

        } else {
            binding.choiceBtn.setImageResource(R.drawable.checkoff_foreground);
        }

        binding.choiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myViewModelDetail.setFavRestaurant(myViewModelDetail.getCurrentUser().getUid(), myViewModelDetail.getPlaceId());
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
                        showSnackBar(getString(R.string.no_url_string));
                    }
                }
            }
        });
    }

    private void setupButtonLike(Boolean state) {

        if (state) {
            binding.likeBtn.setImageResource(R.drawable.ic_baseline_star_24_green);
        } else {
            binding.likeBtn.setImageResource(R.drawable.ic_baseline_star_24);
        }

        binding.likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSnackBar("Enregistrer le restaurant : " + binding.restaurantTitre.getText());
                //myViewModelDetail.getCurrentUser().
                myViewModelDetail.adLikedRestaurant(myViewModelDetail.getCurrentUser().getUid(), myViewModelDetail.getPlaceId());
            }
        });
    }

    private void setupButtonPhone(@Nullable String number) {

        binding.callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (number != null) {
                    Intent makeCall = new Intent(Intent.ACTION_DIAL);
                    makeCall.setData(Uri.parse("tel:" + number));
                    startActivity(makeCall);
                } else {
                    showSnackBar(getString(R.string.no_phone_string));
                }
            }
        });
    }

    private void showSnackBar(String message) {
        Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_SHORT).show();
    }

}