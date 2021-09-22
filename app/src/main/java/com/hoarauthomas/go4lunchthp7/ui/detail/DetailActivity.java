package com.hoarauthomas.go4lunchthp7.ui.detail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.hoarauthomas.go4lunchthp7.R;
import com.hoarauthomas.go4lunchthp7.databinding.ActivityDetailRestaurantBinding;
import com.hoarauthomas.go4lunchthp7.factory.ViewModelFactory;
import com.hoarauthomas.go4lunchthp7.repository.FirestoreUser;

import java.util.List;
import java.util.Objects;

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
            myViewModelDetail.setPlaceId(intent.getStringExtra("TAG_ID"));
            showSnackBar("Place id selected for detail : " + intent.getStringExtra("TAG_ID"));
        } else {
            //search favorite restaurant for actual user don't work
            myViewModelDetail.getUserPlaceFavoriteToShow();
        }
    }

    private void setupViewModel() {
        myViewModelDetail = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(ViewModelDetail.class);
        myViewModelDetail.getMediatorScreen().observe(this, screenDetailModel -> {

            Glide.with(binding.backgroundImage)
                    .load(screenDetailModel.getUrlPhoto())
                    .centerCrop()
                    .into(binding.backgroundImage);

            binding.restaurantTitre.setText(screenDetailModel.getTitle());
            binding.restaurantAddress.setText(screenDetailModel.getAddress());
            binding.ratingbar.setRating(screenDetailModel.getRating());

            setupOnClickFavoriteBtn(screenDetailModel.getFavorite(), screenDetailModel.getPlaceId(), screenDetailModel.workmate);
            setupOnClickLikeBtn(screenDetailModel.getLike(), screenDetailModel.getPlaceId(), screenDetailModel.workmate);

            setupOnClickPhoneNumberBtn(screenDetailModel.PhoneNumber());
            setupOnClickWebSiteBtn(screenDetailModel.getWebSite());

            setupDesignFavoriteBtn(screenDetailModel.getFavorite());
            setupDesignLikeBtn(screenDetailModel.getLike());
            setupRecyclerView(screenDetailModel.getListWorkMates());
        });
    }

    private void setupOnClickFavoriteBtn(Boolean favorite, String placeId, String workmate) {
        Objects.requireNonNull(binding.favoriteBtn).setOnClickListener(v -> myViewModelDetail.updateFavoriteRestaurant(favorite, placeId, workmate));
    }

    private void setupOnClickPhoneNumberBtn(@Nullable String number) {
        binding.phoneNumberBtn.setOnClickListener(v -> {
            if (number != null) {
                Intent makeCall = new Intent(Intent.ACTION_DIAL);
                makeCall.setData(Uri.parse("tel:" + number));
                DetailActivity.this.startActivity(makeCall);
            } else {
                DetailActivity.this.showSnackBar(DetailActivity.this.getString(R.string.no_phone_string));
            }
        });
    }

    private void setupOnClickWebSiteBtn(@Nullable String url) {
        binding.webSiteBtn.setOnClickListener(v -> {
            if (url != null) {
                if (!url.isEmpty()) {
                    Log.i("[URL]", "Url site web : " + url);
                    Intent makeURLBrowser = new Intent(Intent.ACTION_VIEW);
                    makeURLBrowser.setData(Uri.parse(url));
                    startActivity(makeURLBrowser);
                } else {
                    showSnackBar(getString(R.string.no_url_string));
                }
            }
        });
    }

    private void setupDesignFavoriteBtn(Boolean state) {
        if (state == null) return;
        if (state) {
            binding.favoriteBtn.setImageResource(R.drawable.checked_favorite_restaurant);
        } else {
            binding.favoriteBtn.setImageResource(R.drawable.unchecked_favori_restaurant);
        }
    }

    private void setupDesignLikeBtn(Boolean state) {
        if (state) {
            binding.likeBtn.setImageResource(R.drawable.ic_baseline_star_24_green);
        } else {
            binding.likeBtn.setImageResource(R.drawable.ic_baseline_star_24);
        }
    }

    public void setupOnClickLikeBtn(Boolean like, String placeId, String workmate) {
        binding.likeBtn.setOnClickListener(v -> myViewModelDetail.updateLikeRestaurant(like, placeId, workmate));
    }

    private void setupRecyclerView(List<FirestoreUser> myWorkmatesList) {
        RecyclerView recyclerView = binding.recyclerView;
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new RecyclerViewAdapterDetail(myWorkmatesList));
    }

    private void showSnackBar(String message) {
        Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_SHORT).show();
    }

}