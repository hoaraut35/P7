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
import com.hoarauthomas.go4lunchthp7.BuildConfig;
import com.hoarauthomas.go4lunchthp7.R;
import com.hoarauthomas.go4lunchthp7.databinding.ActivityDetailRestaurantBinding;
import com.hoarauthomas.go4lunchthp7.factory.ViewModelFactory;
import com.hoarauthomas.go4lunchthp7.repository.FirestoreUser;

import java.util.List;

import javax.annotation.Nullable;

public class DetailActivity extends AppCompatActivity {

    private ActivityDetailRestaurantBinding binding;
    private ViewModelDetail myViewModelDetail;

    private Boolean mFavorite;
    private String mPlaceId;
    private String mWorkmate;
    private RecyclerView myRecyclerView;
    private RecyclerViewAdapterDetail myRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailRestaurantBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        setupViewModel();
        setupIntent();

        setupOnClicFavoriteBtn();

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

        myViewModelDetail.getMediatorScreen().observe(this, screenDetailModel -> {

            //debug
            mFavorite = screenDetailModel.getFavorite();
            mPlaceId = screenDetailModel.getPlaceId();
            mWorkmate = screenDetailModel.getWorkmate();

            //get photo

            // binding.backgroundImage.setImageResource(screenDetailModel.);
            Glide.with(binding.backgroundImage)
                    .load(screenDetailModel.getUrlPhoto())
                    .centerCrop()
                    .into(binding.backgroundImage);

            binding.restaurantTitre.setText(screenDetailModel.getTitle());
            binding.restaurantAddress.setText(screenDetailModel.getAddress());
            binding.ratingbar.setRating(screenDetailModel.getRating());

            //get phone number
            setupButtonPhone(screenDetailModel.getCall());

            //get url
            setupButtonWeb(screenDetailModel.getWebsite());

            //get choice
            setupButtonChoice(screenDetailModel.getFavorite());

            //       setupButtonLike(screenDetailModel.getLiked());

            //get list of workmates
            setupRecyclerView(screenDetailModel.getListWorkMates());
            //dd

            setupDataRC(screenDetailModel.getListWorkMates());


        });
    }


    private void setupOnClicFavoriteBtn() {
        binding.choiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myViewModelDetail.updateFavRestaurant(mFavorite, mPlaceId, mWorkmate);
            }
        });
    }


    private void setupDataRC(List<FirestoreUser> listWorkMates) {
        //     myRecyclerViewAdapter.notifyDataSetChanged();


    }


    private void setupRecyclerView(List<FirestoreUser> myWorkmatesList) {
//        private void setupRecyclerView() {

        //if (myWorkmatesList == null) {
        //  Log.i("[MONDETAIL]", "liste user nulle ou vide");
        //} else if (!myWorkmatesList.isEmpty()) {

        RecyclerView recyclerView = binding.recyclerView;
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new RecyclerViewAdapterDetail(myWorkmatesList));
        //recyclerView.setAdapter(new RecyclerViewAdapterDetail());

        // } else {
        //    Log.i("[MONDETAIL]", "liste user vide");
        // }

    }

    private void setupButtonWeb(@Nullable String url) {

        binding.website.setOnClickListener(v -> {
            if (url != null) {
                if (!url.isEmpty()) {
                    Intent makeURLBrowser = new Intent(Intent.ACTION_VIEW);
                    makeURLBrowser.setData(Uri.parse(url));
                    startActivity(makeURLBrowser);
                } else {
                    showSnackBar(getString(R.string.no_url_string));
                }
            }
        });
    }

    private void setupButtonChoice(Boolean state) {

        if (state == null) return;

        //for view
        if (state) {
            binding.choiceBtn.setImageResource(R.drawable.checked_favorite_restaurant);
        } else {
            binding.choiceBtn.setImageResource(R.drawable.unchecked_favori_restaurant);
        }


    }

    private void setupButtonLike(Boolean state) {

        //for view
        if (state) {
            binding.likeBtn.setImageResource(R.drawable.ic_baseline_star_24_green);
        } else {
            binding.likeBtn.setImageResource(R.drawable.ic_baseline_star_24);
        }

        //for action
        binding.likeBtn.setOnClickListener(v -> {

            if (state) {
                //showSnackBar(getString(R.string.like_delete_msg) + myViewModelDetail.getCurrentUser().getUid() + " " + myViewModelDetail.getPlaceId());
                try {
                    myViewModelDetail.deleteLikedRestaurant(myViewModelDetail.getCurrentUser().getUid(), myViewModelDetail.getPlaceId());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


//
            } else {
                // showSnackBar(getString(R.string.like_add_msg)  + myViewModelDetail.getCurrentUser().getUid() + " " + myViewModelDetail.getPlaceId());
                try {
                    myViewModelDetail.addLikedRestaurant(myViewModelDetail.getCurrentUser().getUid(), myViewModelDetail.getPlaceId());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        });
    }

    private void setupButtonPhone(@Nullable String number) {

        binding.callBtn.setOnClickListener(v -> {
            if (number != null) {
                Intent makeCall = new Intent(Intent.ACTION_DIAL);
                makeCall.setData(Uri.parse("tel:" + number));
                startActivity(makeCall);
            } else {
                showSnackBar(getString(R.string.no_phone_string));
            }
        });
    }

    private void showSnackBar(String message) {
        Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_SHORT).show();
    }

}