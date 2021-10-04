package com.hoarauthomas.go4lunchthp7.ui.restaurant;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hoarauthomas.go4lunchthp7.R;
import com.hoarauthomas.go4lunchthp7.factory.ViewModelFactory;
import com.hoarauthomas.go4lunchthp7.model.NearbySearch.RestaurantPojo;
import com.hoarauthomas.go4lunchthp7.ui.detail.DetailActivity;
import com.hoarauthomas.go4lunchthp7.ui.map.MapsFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ListFragment extends Fragment implements RecyclerViewAdapter.RestaurantListener {

    private final ArrayList<RestaurantPojo> allResult = new ArrayList<>();
    private RecyclerView recyclerView;

    @Override
    public void onClickDetailRestaurant(String restaurantId) {
        Intent intent = new Intent(getContext(), DetailActivity.class);
        intent.putExtra("TAG_ID", restaurantId);
        startActivity(intent);
    }

    @Override
    public void popupSnack(String message) {
        Toast toast = Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_fragment, container, false);
        setupRecyclerView(view);
        setupViewModel();
        return view;
    }


    public static ListFragment newInstance() {
        ListFragment listFragment = new ListFragment();
        return listFragment;
    }

    private void setupViewModel() {
        ViewModelRestaurant myViewModelRestaurant = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(ViewModelRestaurant.class);
        myViewModelRestaurant.getRestaurantsViewUI().observe(getViewLifecycleOwner(), viewStateRestaurant -> showRestaurant(viewStateRestaurant.getMyRestaurantList()));
    }

    private void showRestaurant(List<RestaurantPojo> restaurants) {
        if (restaurants != null) {
            allResult.clear();
            allResult.addAll(restaurants);
            setupRecyclerViewData();
            Objects.requireNonNull(recyclerView.getAdapter()).notifyDataSetChanged();
        }
    }

    private void setupRecyclerViewData() {
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