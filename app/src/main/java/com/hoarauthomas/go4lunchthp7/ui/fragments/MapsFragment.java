package com.hoarauthomas.go4lunchthp7.ui.fragments;

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
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.hoarauthomas.go4lunchthp7.R;
import com.hoarauthomas.go4lunchthp7.RestaurantsDetails;
import com.hoarauthomas.go4lunchthp7.ui.activity.DetailRestaurant;
import com.hoarauthomas.go4lunchthp7.viewmodel.MainViewState;
import com.hoarauthomas.go4lunchthp7.viewmodel.ViewModelFactory;
import com.hoarauthomas.go4lunchthp7.viewmodel.ViewModelGo4Lunch;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MapsFragment extends Fragment implements OnRequestPermissionsResultCallback, GoogleMap.OnMarkerClickListener {

    private ViewModelGo4Lunch myViewModelGo4Lunch;


    private static final int DEFAULT_ZOOM = 10;
    private final LatLng defaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean locationPermissionGranted;
    private Location lastKnownLocation, newPosition;

    //TODO: use this to locate the phone
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private GoogleMap myMap;
    public LatLng myPosition;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myViewModelGo4Lunch = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(ViewModelGo4Lunch.class);
        myViewModelGo4Lunch.getViewStateLiveData().observe(getViewLifecycleOwner(), new Observer<MainViewState>() {
            @Override
            public void onChanged(MainViewState mainViewState) {
                Log.i("[MAP]","Changement dans le ViewState ... Mise à jour");
                showMapWithPosition(mainViewState.getLocation());
                showRestaurant(mainViewState.getMyRestaurantsList());

            }
        });

        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    private void showMapWithPosition(Location position) {
        if (position != null) {
            LatLng latLng = new LatLng(position.getLatitude(), position.getLongitude());
            myMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            myMap.animateCamera(CameraUpdateFactory.zoomTo(10));//city zoom
        }
    }

    private void showRestaurant(List <com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo> restaurants) {

        myMap.clear();



        if (restaurants == null)
        {
            Log.i("[MAP]","Liste restau vide");
            return;
        }else
        {
            Log.i("[MAP]","" + restaurants.size() );
        }

        for(int i =0 ; i< restaurants.size(); i++){
            Double lat = restaurants.get(i).getGeometry().getLocation().getLat();
            Double lng = restaurants.get(i).getGeometry().getLocation().getLng();

            //Options
            MarkerOptions markerOptions = new MarkerOptions();
                LatLng latLng = new LatLng(lat, lng);
                markerOptions.position(latLng);
                markerOptions.title(restaurants.get(i).getName());

            //view
            Marker m = myMap.addMarker(markerOptions);
            m.setTag(restaurants.get(i).getPlaceId());
        }

    }

    private void onUpdateRestaurants(List<com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo> restaurants) {

        Log.i("[RESTAURANT]", "Frag map, onUpdateRestaurants : total => " + restaurants.size());


        this.myViewModelGo4Lunch.updateRestaurantList(restaurants);

        myMap.clear();

        for (int i = 0; i < restaurants.size(); i++) {

            Double lat = restaurants.get(i).getGeometry().getLocation().getLat();
            Double lng = restaurants.get(i).getGeometry().getLocation().getLng();

            //String placeName = response.body().getResults().get(i).getName();

            MarkerOptions markerOptions = new MarkerOptions();
            LatLng latLng = new LatLng(lat, lng);
            markerOptions.position(latLng);
            markerOptions.title(restaurants.get(i).getName());
            //  markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.green_restaurant_marker));

            Marker m = myMap.addMarker(markerOptions);
            m.setTag(restaurants.get(i).getPlaceId());


        }

    }


    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap map) {

            //to get an instance
            myMap = map;

            //we must use location object with fused location provider
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
            myViewModelGo4Lunch.refreshPosition();

            //Setup Google Map
            map.getUiSettings().setZoomControlsEnabled(true);
            map.setMinZoomPreference(1);


            //to open detail restaurant...
            map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(@NonNull @NotNull Marker marker) {
                    Log.i("[MAP]", "test click" + marker.getTitle() + "tag id : " + marker.getTag());

                    // myViewModelGo4Lunch.setActualRestaurant(marker.getTag().toString());

                    Intent intent = new Intent(getContext(), DetailRestaurant.class);
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