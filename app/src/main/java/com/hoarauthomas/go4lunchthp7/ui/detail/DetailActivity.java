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
            //showSnackBar("Place id selected for detail : " + intent.getStringExtra("TAG_ID"));
        }
    }

    private void setupViewModel() {

        myViewModelDetail = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(ViewModelDetail.class);

        myViewModelDetail.getMediatorScreen().observe(this, screenDetailModel -> {


            //si aucun placeif alors on prend cleui de l'utilisateur actuel
            /*if (getIntent().getStringExtra("TAG_ID") == null){
                myViewModelDetail.setPlaceId(viewStateDetail.myUser.getMyFavoriteRestaurantId());
            }
             */

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

            //get choice
            setupButtonChoice(screenDetailModel.getFavorite());

            //TODO: don't work
            setupButtonLike(screenDetailModel.getLiked());

            //get list of workmates
            setupRecyclerView(screenDetailModel.getListWorkMates());

        });
    }



    private void setupRecyclerView(List<User> myWorkmatesList) {

        if (myWorkmatesList == null) {
            Log.i("[MONDETAIL]", "liste user nulle ou vide");
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

        //for view
        if (state) {
            binding.choiceBtn.setImageResource(R.drawable.checked_favorite_restaurant);
        } else {
            binding.choiceBtn.setImageResource(R.drawable.unchecked_favori_restaurant);
        }

        //for action
        binding.choiceBtn.setOnClickListener(v -> {

            if (state){
                //disable
                showSnackBar(getString(R.string.delete_fav_msg));
                myViewModelDetail.deleteFavRestaurant(myViewModelDetail.getCurrentUser().getUid());
            }else
            {
                //enable
                showSnackBar(getString(R.string.add_fav_msg));
                myViewModelDetail.setFavRestaurant(myViewModelDetail.getCurrentUser().getUid(), myViewModelDetail.getPlaceId());
            }

        });

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

            if (state)
            {
                showSnackBar(getString(R.string.like_delete_msg) + myViewModelDetail.getCurrentUser().getUid() + " " + myViewModelDetail.getPlaceId());
                myViewModelDetail.deleteLikedRestaurant(myViewModelDetail.getCurrentUser().getUid(), myViewModelDetail.getPlaceId());

            }else
            {
                showSnackBar(getString(R.string.like_add_msg)  + myViewModelDetail.getCurrentUser().getUid() + " " + myViewModelDetail.getPlaceId());
                myViewModelDetail.adLikedRestaurant(myViewModelDetail.getCurrentUser().getUid(), myViewModelDetail.getPlaceId());
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