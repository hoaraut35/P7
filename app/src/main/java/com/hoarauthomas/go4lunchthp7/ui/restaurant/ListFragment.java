package com.hoarauthomas.go4lunchthp7.ui.restaurant;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;
import com.hoarauthomas.go4lunchthp7.R;
import com.hoarauthomas.go4lunchthp7.viewmodel.MainViewState;
import com.hoarauthomas.go4lunchthp7.viewmodel.ViewModelFactory;
import com.hoarauthomas.go4lunchthp7.viewmodel.ViewModelGo4Lunch;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ListFragment extends Fragment {

    private ViewModelGo4Lunch myViewModelGo4Lunch;

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
        myViewModelGo4Lunch = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(ViewModelGo4Lunch.class);

        myViewModelGo4Lunch.getViewStateLiveData().observe(getViewLifecycleOwner(), new androidx.lifecycle.Observer<MainViewState>() {
            @Override
            public void onChanged(MainViewState mainViewState) {
                Log.i("[media]", "list restau frag changed.... update");

                showRestaurant(mainViewState.getMyRestaurantsList());

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