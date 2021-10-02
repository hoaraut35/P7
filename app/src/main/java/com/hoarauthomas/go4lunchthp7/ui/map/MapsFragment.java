package com.hoarauthomas.go4lunchthp7.ui.map;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

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
import com.hoarauthomas.go4lunchthp7.ui.detail.DetailActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MapsFragment extends Fragment implements OnRequestPermissionsResultCallback, GoogleMap.OnMarkerClickListener {

    private ViewModelMap myViewModelMap;
    //private SharedRepository mySharedRepository;
    public Marker myMarker;
    private static final int DEFAULT_ZOOM = 12;
    public List<MyMarkerObject> allMarkers = new ArrayList<>();
    private GoogleMap myMap;

    private Integer myZoom;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        setupViewModel();
        return view;
    }

    private void setupViewModel() {
        myViewModelMap = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(ViewModelMap.class);
    }

    private final OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap map) {

            //to get an instance
            myMap = map;
            myMap.clear();

            myMap.animateCamera(CameraUpdateFactory.zoomTo(DEFAULT_ZOOM));

            myViewModelMap.getMyZoom().observe(getViewLifecycleOwner(), integer -> {
                //ok for variable but no zoom in or out
                myZoom = integer;
                myMap.animateCamera(CameraUpdateFactory.zoomTo(integer));
            });

            //myViewModelMap.refresh();
            myViewModelMap.ViewStateForMapUI().observe(getViewLifecycleOwner(), viewStateMap -> {
                showMapWithPosition(viewStateMap.getMyLatLng());
                showRestaurant(viewStateMap.myRestaurantsList);
            });

            myViewModelMap.getPredictionFromRepository().observe(requireActivity(), prediction -> {

                if (prediction == null) return;

                Boolean findPlace = false;

                for (int i = 0; i < allMarkers.size(); i++) {

                    if (prediction.getPlaceId().equals(allMarkers.get(i).getId())) {

                        Toast.makeText(getContext(), "find : " + prediction.getPlaceId(), Toast.LENGTH_SHORT).show();

                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(allMarkers.get(i).getLocation().latitude, allMarkers.get(i).getLocation().longitude), 15));

                        findPlace = true;
                        break;
                    }

                    findPlace = false;

                }

                if (!findPlace) {
                    Intent intent = new Intent(getContext(), DetailActivity.class);
                    String restaurantTag = prediction.getPlaceId();
                    intent.putExtra("TAG_ID", restaurantTag);
                    startActivity(intent);
                }

            });

            //Setup Google Map
            map.getUiSettings().setZoomControlsEnabled(true);

            //  map.setMinZoomPreference(DEFAULT_ZOOM);

            map.setOnMarkerClickListener(marker -> {
                Intent intent = new Intent(getContext(), DetailActivity.class);
                String restaurantTag = Objects.requireNonNull(marker.getTag()).toString();
                intent.putExtra("TAG_ID", restaurantTag);
                startActivity(intent);
                return true;
            });

            checkPermissions();

        }
    };

    private void showMapWithPosition(@NonNull LatLng position) {
        if (myMap != null) {
            myMap.moveCamera(CameraUpdateFactory.newLatLng(position));

        }

    }

    private void showRestaurant(List<com.hoarauthomas.go4lunchthp7.model.NearbySearch.RestaurantPojo> restaurants) {

        if (restaurants.isEmpty()) {
            Log.i("[MAP]", "list restaurant is empty");
            return;
        }

        if (myMap == null) {
            return;
        }

        myMap.clear();

        LatLng myMarkerPosition;
        MarkerOptions myMarkerOptions;

        allMarkers.clear();

        for (int i = 0; i < restaurants.size(); i++) {

            myMarkerPosition = new LatLng(restaurants.get(i).getGeometry().getLocation().getLat(), restaurants.get(i).getGeometry().getLocation().getLng());
            myMarkerOptions = new MarkerOptions();

            myMarkerOptions.position(myMarkerPosition);

            if (restaurants.get(i).getIcon().contains("rouge")) {
                //myMarkerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                myMarkerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.baseline_unreserved_restaurant_24));
            } else {
                //    myMarkerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                myMarkerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.baseline_booked_restaurant_24));
            }


            myMarker = myMap.addMarker(myMarkerOptions);
            assert myMarker != null;
            myMarker.setTag(restaurants.get(i).getPlaceId());


            allMarkers.add(new MyMarkerObject(restaurants.get(i).getPlaceId(), myMarkerPosition));

        }

    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (myMap != null) {
                myMap.setMyLocationEnabled(true);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        checkPermissions();
       myViewModelMap.refresh();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    @Override
    public boolean onMarkerClick(@NonNull @NotNull Marker marker) {
        return false;
    }

}

