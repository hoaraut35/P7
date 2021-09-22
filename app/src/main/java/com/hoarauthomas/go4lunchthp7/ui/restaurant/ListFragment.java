package com.hoarauthomas.go4lunchthp7.ui.restaurant;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;
import com.hoarauthomas.go4lunchthp7.Prediction;
import com.hoarauthomas.go4lunchthp7.R;
import com.hoarauthomas.go4lunchthp7.factory.ViewModelFactory;
import com.hoarauthomas.go4lunchthp7.ui.detail.DetailActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ListFragment extends Fragment implements RecyclerViewAdapter.RestaurantListener {

    private ViewModelRestaurant myViewModelRestaurant;
    public ArrayList<com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo> allResult = new ArrayList<>();
    private RecyclerView recyclerView;
    private LatLng myPositionOnMap;
    private View myView;

    @Override
    public void onClickDetailRestaurant(String restaurantId) {
        Log.i("[WORK]", "Item cliqué : " + restaurantId);
        Intent intent = new Intent(getContext(), DetailActivity.class);
        intent.putExtra("TAG_ID", restaurantId);
        startActivity(intent);
    }

    @Override
    public void popupSnack(String message) {
        Toast toast = Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }

    public static ListFragment newInstance() {
        return new ListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_fragment, container, false);
        setupRecyclerView(view);
        setupViewModel();
        return view;
    }

    private void setupViewModel() {
        myViewModelRestaurant = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(ViewModelRestaurant.class);
        myViewModelRestaurant.getMediatorLiveData().observe(getViewLifecycleOwner(), new Observer<ViewStateRestaurant>() {
            @Override
            public void onChanged(ViewStateRestaurant viewStateRestaurant) {
                showRestaurant(viewStateRestaurant.getMyRestaurantList());
            }
        });
    }

    private void showRestaurant(List<com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo> restaurants) {
        if (restaurants == null) {
            Log.i("[media]", "Liste restau vide");
            return;
        } else {
            Log.i("[media]", "media size" + restaurants.size());
            allResult.clear();
            allResult.addAll(restaurants);
            setupRecyclerViewData(myView);
            Objects.requireNonNull(recyclerView.getAdapter()).notifyDataSetChanged();
        }
    }

    private void setupRecyclerViewData(View myView) {
        recyclerView.setAdapter(new RecyclerViewAdapter(allResult, this));
    }

    private void setupRecyclerView(View view) {
        recyclerView = view.findViewWithTag("recycler_view");
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.addItemDecoration(itemDecoration);

    }

}