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

import com.hoarauthomas.go4lunchthp7.R;
import com.hoarauthomas.go4lunchthp7.factory.ViewModelFactory;
import com.hoarauthomas.go4lunchthp7.model.NearbySearch.RestaurantPojo;
import com.hoarauthomas.go4lunchthp7.model.PlaceDetails.ResultPlaceDetail;
import com.hoarauthomas.go4lunchthp7.ui.detail.DetailActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

//public class ListFragment extends Fragment implements RecyclerViewAdapter.RestaurantListener{
public class ListFragment extends Fragment implements ClickListener{

    private final ArrayList<RestaurantPojo> allResult = new ArrayList<>();
    private final List<ResultPlaceDetail> AutocompleteResult = new ArrayList<>();

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

        myViewModelRestaurant.getRestaurantsViewUI().observe(getViewLifecycleOwner(), new Observer<ViewStateRestaurant>() {
            @Override
            public void onChanged(ViewStateRestaurant viewStateRestaurant) {

                List<ResultPlaceDetail> myTest = new ArrayList<>();
                if (viewStateRestaurant.getMyAutocompleteList() != null){
                    myTest.clear();
                    myTest.addAll(viewStateRestaurant.getMyAutocompleteList());
                }

                List<RestaurantPojo> myRestaurantPojoList = new ArrayList<>();
                if (viewStateRestaurant.getMyRestaurantList() != null){
                    myRestaurantPojoList.clear();
                    myRestaurantPojoList.addAll(viewStateRestaurant.getMyRestaurantList());
                }

                ListFragment.this.showRestaurant(myRestaurantPojoList, myTest);

            }
        });
    }

    private void showRestaurant(List<RestaurantPojo> restaurants, List<ResultPlaceDetail> myAutocompleteResult) {

       if (myAutocompleteResult != null && myAutocompleteResult.size()>0){
           Log.i("[AUTOCOMPLETE]", "view autocomplete" + myAutocompleteResult.size() );
          // myAutocompleteResult.clear();
         //  myAutocompleteResult.addAll(myAutocompleteResult);
           recyclerView.setAdapter(new RecyclerViewAdapterAutocomplete(myAutocompleteResult, this));
           Objects.requireNonNull(recyclerView.getAdapter()).notifyDataSetChanged();

       }else
        if (restaurants != null) {
            Log.i("[AUTOCOMPLETE]", "view restaurant" + restaurants.size());
            allResult.clear();
            allResult.addAll(restaurants);
           // recyclerView.setAdapter(new RecyclerViewAdapter(allResult, this));
            recyclerView.setAdapter(new RecyclerViewAdapter(allResult, this));
            Objects.requireNonNull(recyclerView.getAdapter()).notifyDataSetChanged();
        }

    }

    private void setupRecyclerView(View view) {
        recyclerView = view.findViewWithTag("recycler_view");
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.addItemDecoration(itemDecoration);

    }

}