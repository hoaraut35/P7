package com.hoarauthomas.go4lunchthp7.ui.fragments;

import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.hoarauthomas.go4lunchthp7.MainActivity;
import com.hoarauthomas.go4lunchthp7.R;
//import com.hoarauthomas.go4lunchthp7.model.pojo.Result;

import com.hoarauthomas.go4lunchthp7.pojo.Result;
import com.hoarauthomas.go4lunchthp7.ui.adapter.RecyclerViewAdapter;
import com.hoarauthomas.go4lunchthp7.viewmodel.ViewModelFactory;
import com.hoarauthomas.go4lunchthp7.viewmodel.ViewModelGo4Lunch;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class ListFragment extends Fragment {

    private ViewModelGo4Lunch myViewModel;

    public final ArrayList<Result> allResult = new ArrayList<>();

    private RecyclerView recyclerView;

    private LatLng myPositionOnMap;

    public static ListFragment newInstance() {
        return new ListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_fragment, container, false);
        setupViewModel();
        setupRecyclerView(view);
        return view;
    }

    private void setupViewModel() {
        this.myViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(ViewModelGo4Lunch.class);
        this.myViewModel.getMyPosition().observe(getViewLifecycleOwner(), this::onUpdatePosition);
        this.myViewModel.getRestaurants().observe(getViewLifecycleOwner(), this::onUpdateRestaurants);
    }

    private void onUpdatePosition(Location location) {
        //Position de l'utilisateir ici...
        myPositionOnMap = new LatLng(location.getLatitude(), location.getLongitude());

        this.myViewModel.UpdateLngLat(location.getLongitude(), location.getLatitude());

        this.myViewModel.getRestaurants().observe(getViewLifecycleOwner(), this::onUpdateRestaurants);
    }

    private void onUpdateRestaurants(List<Result> results) {
        Log.i("[RESTAURANT]", "Fragment liste, onUpdateRestaurants Event " + results.size());
        allResult.clear();
        //Liste des restaurants autour de l'utilisateur...
        allResult.addAll(results);
        Objects.requireNonNull(recyclerView.getAdapter()).notifyDataSetChanged();
    }

    private void setupRecyclerView(View view) {
        recyclerView = view.findViewWithTag("recycler_view");
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.setHasFixedSize(false);

        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.addItemDecoration(itemDecoration);



        recyclerView.setAdapter(new RecyclerViewAdapter(0, allResult));
    }

}