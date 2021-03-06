package com.hoarauthomas.go4lunchthp7.ui.map;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hoarauthomas.go4lunchthp7.R;
import com.hoarauthomas.go4lunchthp7.factory.ViewModelFactory;
import com.hoarauthomas.go4lunchthp7.model.NearbySearch.RestaurantPojo;
import com.hoarauthomas.go4lunchthp7.model.PlaceDetails.ResultPlaceDetail;
import com.hoarauthomas.go4lunchthp7.ui.detail.DetailActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MapsFragment extends Fragment implements OnRequestPermissionsResultCallback, GoogleMap.OnMarkerClickListener {

    private ViewModelMap myViewModelMap;
    public Marker myMarker;

    public List<MyMarkerObject> allMarkers = new ArrayList<>();
    private GoogleMap myMap;
    private SupportMapFragment mapFragment;
    private Integer myZoom;
    private SharedPreferences mySharedPreferences;

    public static MapsFragment newInstance() {
        MapsFragment mapsFragment = new MapsFragment();
        return mapsFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        setupViewModel();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        //get preferences
        mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireActivity());
    }

    private void setupViewModel() {

        //init viewModel
        myViewModelMap = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(ViewModelMap.class);

        //display a map
        myViewModelMap.getViewStateForMapUI().observe(getViewLifecycleOwner(), new Observer<ViewStateMap>() {
            @Override
            public void onChanged(ViewStateMap viewStateMap) {
                mapFragment.getMapAsync(googleMap -> {

                    myMap = googleMap;
                    googleMap.clear();

                    //update position locator on map
                    MapsFragment.this.checkPermissionsForZoom();

                    //move to user position
                    myMap.moveCamera(CameraUpdateFactory.newLatLng(viewStateMap.getMyPosition()));

                    googleMap.animateCamera(CameraUpdateFactory.zoomTo(mySharedPreferences.getInt("zoom", 12)));

                    MapsFragment.this.DisplayMarker(viewStateMap.getMyRestaurantsList(), viewStateMap.getMyAutocompleteList());

                    //zoom in out button
                    googleMap.getUiSettings().setZoomControlsEnabled(true);

                    //set listener on marker
                    googleMap.setOnMarkerClickListener(marker -> {
                        Intent intent = new Intent(MapsFragment.this.getContext(), DetailActivity.class);
                        String restaurantTag = Objects.requireNonNull(marker.getTag()).toString();
                        intent.putExtra("TAG_ID", restaurantTag);
                        MapsFragment.this.startActivity(intent);
                        return true;
                    });

                });
            }
        });

        //setup zoom on map
        myViewModelMap.getMyZoom().observe(getViewLifecycleOwner(), integer -> {

            if (mapFragment != null){
                mapFragment.getMapAsync((OnMapReadyCallback) googleMap -> {
                    if (integer != null) {
                        setupZoom(integer);
                    }
                });
            }

        });
    }

    private void setupZoom(Integer integer) {

        mapFragment.getMapAsync(googleMap -> {

            myZoom = integer;
            if (mapFragment!=null && googleMap!=null){
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(integer));
            }

        });
    }

    private void DisplayMarker(List<com.hoarauthomas.go4lunchthp7.model.NearbySearch.RestaurantPojo> myMarkersList, List<ResultPlaceDetail> myAutocomplete) {

        mapFragment.getMapAsync(googleMap -> {

            MarkerOptions myMarkerOptions;


            if (myAutocomplete != null && !myAutocomplete.isEmpty()){
                for (ResultPlaceDetail myAutocompleteResult : myAutocomplete){
                    LatLng myMarkerPosition = new LatLng(myAutocompleteResult.getGeometry().getLocation().getLat(), myAutocompleteResult.getGeometry().getLocation().getLng());
                    myMarkerOptions = new MarkerOptions();
                    myMarkerOptions.position(myMarkerPosition);

                    if (myAutocompleteResult.getIcon().contains("rouge")) {
                        myMarkerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.baseline_unreserved_restaurant_24));
                    } else {
                        myMarkerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.baseline_booked_restaurant_24));
                    }

                    myMarker = googleMap.addMarker(myMarkerOptions);

                    assert myMarker != null;
                    myMarker.setTag(myAutocompleteResult.getPlaceId());
                    allMarkers.add(new MyMarkerObject(myAutocompleteResult.getPlaceId(), myMarkerPosition));


                }

            }else
            {
                if (myMarkersList!= null && !myMarkersList.isEmpty())
                {
                    //loop to iterate restaurants
                    for (RestaurantPojo myRestaurantItem : myMarkersList) {
                        LatLng myMarkerPosition = new LatLng(myRestaurantItem.getGeometry().getLocation().getLat(), myRestaurantItem.getGeometry().getLocation().getLng());

                        myMarkerOptions = new MarkerOptions();

                        myMarkerOptions.position(myMarkerPosition);

                        if (myRestaurantItem.getIcon().contains("rouge")) {
                            myMarkerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.baseline_unreserved_restaurant_24));
                        } else {
                            myMarkerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.baseline_booked_restaurant_24));
                        }

                        myMarker = googleMap.addMarker(myMarkerOptions);
                        assert myMarker != null;
                        myMarker.setTag(myRestaurantItem.getPlaceId());
                        allMarkers.add(new MyMarkerObject(myRestaurantItem.getPlaceId(), myMarkerPosition));

                    }
                }

            }

        });

    }

    private void checkPermissionsForZoom() {
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (myMap != null) {
                myMap.setMyLocationEnabled(true);
            }
        }
    }

    @Override
    public boolean onMarkerClick(@NonNull @NotNull Marker marker) {
        return false;
    }

}