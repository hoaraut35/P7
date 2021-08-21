package com.hoarauthomas.go4lunchthp7.ui.restaurant;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;
import com.hoarauthomas.go4lunchthp7.R;
import com.hoarauthomas.go4lunchthp7.ui.map.ViewModelMap;
import com.hoarauthomas.go4lunchthp7.viewmodel.MainViewState;
import com.hoarauthomas.go4lunchthp7.viewmodel.ViewModelFactory;
import com.hoarauthomas.go4lunchthp7.viewmodel.ViewModelGo4Lunch;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ListFragment extends Fragment {

    private ViewModelRestaurant myViewModelRestaurant;



    public final ArrayList<com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo> allResult = new ArrayList<>();

    private RecyclerView recyclerView;

    private LatLng myPositionOnMap;
    private View myView;

    public static ListFragment newInstance() {
        return new ListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_fragment, container, false);
        setupViewModel();
        this.myView = view;

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

        // this.myViewModel.getMyPosition().observe(getViewLifecycleOwner(), this::onUpdatePosition);
    }


    private void showRestaurant(List<com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo> restaurants) {

        if (restaurants == null) {

            Log.i("[media]", "Liste restau vide");
            return;
        } else {
            Log.i("[media]", "media size" + restaurants.size());
            allResult.clear();
            allResult.addAll(restaurants);

            setupRecyclerView(myView);
            Objects.requireNonNull(recyclerView.getAdapter()).notifyDataSetChanged();

        }

    }

    private void setupRecyclerView(View view) {
        recyclerView = view.findViewWithTag("recycler_view");
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setAdapter(new RecyclerViewAdapter(allResult));
    }

}