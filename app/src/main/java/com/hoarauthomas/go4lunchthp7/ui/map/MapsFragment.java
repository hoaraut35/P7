package com.hoarauthomas.go4lunchthp7.ui.map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
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

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.hoarauthomas.go4lunchthp7.R;
import com.hoarauthomas.go4lunchthp7.ui.detail.DetailActivity;
import com.hoarauthomas.go4lunchthp7.viewmodel.ViewModelFactory;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MapsFragment extends Fragment implements OnRequestPermissionsResultCallback, GoogleMap.OnMarkerClickListener {

    private ViewModelMap myViewModelMap;

    private static final int DEFAULT_ZOOM = 10;
    private final LatLng defaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean locationPermissionGranted;
    private Location lastKnownLocation, newPosition;

    //TODO: use this to locate the phone
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private GoogleMap myMap;
    private List<Marker> markerMap = new ArrayList<>();
    public LatLng myPosition;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        myViewModelMap = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(ViewModelMap.class);

        myViewModelMap.refresh();
        myViewModelMap.getMediatorLiveData().observe(getViewLifecycleOwner(), new Observer<ViewStateMap>() {
            @Override
            public void onChanged(ViewStateMap viewStateMap) {
                Log.i("[MAP]", "Event ViewStateMap");

                showMapWithPosition(viewStateMap.myPosition);

                showRestaurant2(viewStateMap.myRestaurantsList);
            }
        });

        return view;
    }

    private void showMapWithPosition(@NonNull Location position) {
        if (position == null) {

            return;
        } else {
            Log.i("[MAP]", "Modifier position carte ..." + position.getLongitude() + " " + position.getLatitude());
            LatLng latLng = new LatLng(position.getLatitude(), position.getLongitude());
            myMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            myMap.animateCamera(CameraUpdateFactory.zoomTo(10));//city zoom
        }
    }


    private void showRestaurant2(List<com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo> restaurants) {

        Log.i("[MAP]", "nombre dappel ");

        if (restaurants.isEmpty()) {
            Log.i("[MAP]", "liste restauy à zero ds le fragment");
            return;
        }

        myMap.clear();

        LatLng myMarkerPosition;
        MarkerOptions myMarkerOptions;
        Marker myMarker;

        for (int i = 0; i < restaurants.size(); i++) {

            myMarkerPosition = new LatLng(restaurants.get(i).getGeometry().getLocation().getLat(),restaurants.get(i).getGeometry().getLocation().getLng());
            myMarkerOptions = new MarkerOptions();

            myMarkerOptions.position(myMarkerPosition);

            if (restaurants.get(i).getIcon().toString().contains("rouge")){
                myMarkerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            }else {
                myMarkerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            }

            myMarker = myMap.addMarker(myMarkerOptions);
            myMarker.setTag(restaurants.get(i).getPlaceId());

            Log.i("[MAP]", "nombre dappel " + i);

        }
    }

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap map) {

            //to get an instance
            myMap = map;


            myMap.clear();
            Log.i("[MAP]", "onMapReady");

            //Setup Google Map
            map.getUiSettings().setZoomControlsEnabled(true);

            map.setMinZoomPreference(DEFAULT_ZOOM);

            //to open detail restaurant...
            map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(@NonNull @NotNull Marker marker) {
                    Log.i("[MAP]", "test click" + marker.getTitle() + "tag id : " + marker.getTag());

                    // myViewModelGo4Lunch.setActualRestaurant(marker.getTag().toString());

                    Intent intent = new Intent(getContext(), DetailActivity.class);
                    String restaurantTag = marker.getTag().toString();
                    intent.putExtra("TAG_ID", restaurantTag);
                    startActivity(intent);


                    return false;
                }


            });



            //TODO: update permissions control , add to resume
            //To check permission
            checkPermissions();

        }
    };


    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (myMap != null) {
                myMap.setMyLocationEnabled(true);
            }
        } else {
            // Permission to access the location is missing. Show rationale and request permission
            //   PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
            //         Manifest.permission.ACCESS_FINE_LOCATION, true);
        }
    }


    @SuppressLint("LongLogTag")
    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (locationPermissionGranted) {

                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();

                locationResult.addOnCompleteListener(getActivity(), new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            lastKnownLocation = task.getResult();
                            if (lastKnownLocation != null) {
                                myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(lastKnownLocation.getLatitude(),
                                                lastKnownLocation.getLongitude()), DEFAULT_ZOOM));

                                myPosition = new LatLng(lastKnownLocation.getLongitude(), lastKnownLocation.getLatitude());


                                Log.i("[THOMAS]", "frag map getlast pos : " + lastKnownLocation.getLongitude() + " " + lastKnownLocation.getLatitude());


                                //  LatLng latLng = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                                //  myMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                //  myMap.animateCamera(CameraUpdateFactory.zoomTo(10));//city zoom


                            } else {
                                Log.i("[THOMAS]", "position introuvable ");
                            }
                        } else {
                            //  Log.d("THOMAS", "Current location is null. Using defaults.");
                            //Log.e("THOMAS", "Exception: %s", task.getException());
                            //map.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                            //map.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            } else {
                Log.i("[THOMAS]", "Problème d'autorisation map");
            }
        } catch (SecurityException e) {
            Log.e("Exception de sécurité location : %s", e.getMessage(), e);
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
        Log.i("[MAP]", "clic marqueur");
        return false;
    }
}