package com.hoarauthomas.go4lunchthp7.ui.map;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hoarauthomas.go4lunchthp7.R;
import com.hoarauthomas.go4lunchthp7.ui.detail.DetailActivity;
import com.hoarauthomas.go4lunchthp7.factory.ViewModelFactory;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MapsFragment extends Fragment implements OnRequestPermissionsResultCallback, GoogleMap.OnMarkerClickListener {

    private ViewModelMap myViewModelMap;

    private static final int DEFAULT_ZOOM = 10;

    private GoogleMap myMap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        myViewModelMap = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(ViewModelMap.class);
        return view;
    }

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap map) {

            //myViewModelMap.refresh();
            myViewModelMap.getMediatorLiveData().observe(getViewLifecycleOwner(), new Observer<ViewStateMap>() {
                @Override
                public void onChanged(ViewStateMap viewStateMap) {
                    showMapWithPosition(viewStateMap.getMyLatLng());
                    showRestaurant(viewStateMap.myRestaurantsList);
                }
            });


            //to get an instance
            myMap = map;

            myMap.clear();

            //Setup Google Map
            map.getUiSettings().setZoomControlsEnabled(true);

            map.setMinZoomPreference(DEFAULT_ZOOM);

            map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(@NonNull @NotNull Marker marker) {
                    Intent intent = new Intent(getContext(), DetailActivity.class);
                    String restaurantTag = marker.getTag().toString();
                    intent.putExtra("TAG_ID", restaurantTag);
                    startActivity(intent);
                    return false;
                }
            });

            checkPermissions();

        }
    };

    private void showMapWithPosition(@NonNull LatLng position) {
        if (myMap != null){
            myMap.moveCamera(CameraUpdateFactory.newLatLng(position));
            myMap.animateCamera(CameraUpdateFactory.zoomTo(DEFAULT_ZOOM));
        }

    }

    private void showRestaurant(List<com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo> restaurants) {

        if (restaurants.isEmpty()) {
            Log.i("[MAP]", "liste restauy à zero ds le fragment");
            return;
        }

        if (myMap == null) {
            return;
        }

        myMap.clear();

        LatLng myMarkerPosition;
        MarkerOptions myMarkerOptions;
        Marker myMarker;

        for (int i = 0; i < restaurants.size(); i++) {

            myMarkerPosition = new LatLng(restaurants.get(i).getGeometry().getLocation().getLat(), restaurants.get(i).getGeometry().getLocation().getLng());
            myMarkerOptions = new MarkerOptions();

            myMarkerOptions.position(myMarkerPosition);

            if (restaurants.get(i).getIcon().contains("rouge")) {
                myMarkerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            } else {
                myMarkerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            }

            myMarker = myMap.addMarker(myMarkerOptions);
            myMarker.setTag(restaurants.get(i).getPlaceId());

        }

    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
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