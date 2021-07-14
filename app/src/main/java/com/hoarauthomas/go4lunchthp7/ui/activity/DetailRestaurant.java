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

import com.google.android.gms.maps.model.LatLng;
import com.hoarauthomas.go4lunchthp7.R;
import com.hoarauthomas.go4lunchthp7.databinding.ActivityDetailRestaurantBinding;
import com.hoarauthomas.go4lunchthp7.ui.adapter.RecyclerViewAdapter;
import com.hoarauthomas.go4lunchthp7.viewmodel.ViewModelFactory;
import com.hoarauthomas.go4lunchthp7.viewmodel.ViewModelGo4Lunch;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DetailRestaurant extends AppCompatActivity {

    private ActivityDetailRestaurantBinding binding;
    private ViewModelGo4Lunch myViewModel;
    //th elist of restaurants
    public final ArrayList<com.hoarauthomas.go4lunchthp7.pojo.Result> allResult = new ArrayList<>();
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDetailRestaurantBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        setupRecyclerView();
        setupViewModel();
        setupButtonPhone();
        setupButtonLike();
        setupButtonWeb();
    }

    private void setupViewModel() {

        this.myViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(ViewModelGo4Lunch.class);
        this.myViewModel.getMyPosition().observe(this, this::onUpdatePosition);
    }


    private void setupRecyclerView() {
        recyclerView = binding.recyclerView;
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setAdapter(new RecyclerViewAdapter(0, allResult));
    }


    private void onUpdatePosition(Location location) {

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        this.myViewModel.UpdateLngLat(location.getLongitude(), location.getLatitude());

        this.myViewModel.getRestaurants().observe(this, this::onUpdateRestaurants);
    }

    private void onUpdateRestaurants(List<com.hoarauthomas.go4lunchthp7.pojo.Result> results) {


        allResult.clear();
        allResult.addAll(results);
        Objects.requireNonNull(recyclerView.getAdapter()).notifyDataSetChanged();
    }

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
    }

    private void setupButtonPhone() {
        binding.callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("[DETAIL]","clic sur phone");

                Intent makeCall = new Intent(Intent.ACTION_DIAL);//not action_call...
                makeCall.setData(Uri.parse("tel:" + "0781804664"));
                startActivity(makeCall);
            }
        });

    }


}