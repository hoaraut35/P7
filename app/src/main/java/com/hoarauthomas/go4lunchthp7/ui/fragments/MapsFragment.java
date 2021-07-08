package com.hoarauthomas.go4lunchthp7.ui.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
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
import com.hoarauthomas.go4lunchthp7.BuildConfig;
import com.hoarauthomas.go4lunchthp7.R;

import com.hoarauthomas.go4lunchthp7.api.GooglePlaceApi;
import com.hoarauthomas.go4lunchthp7.model.pojo.Place;
//import com.hoarauthomas.go4lunchthp7.model.pojo.Result;
import com.hoarauthomas.go4lunchthp7.pojo.Result;
import com.hoarauthomas.go4lunchthp7.viewmodel.ViewModelFactory;
import com.hoarauthomas.go4lunchthp7.viewmodel.ViewModelGo4Lunch;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapsFragment extends Fragment implements OnRequestPermissionsResultCallback {

    private ViewModelGo4Lunch viewModelGo4Lunch;


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
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewModelGo4Lunch = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(ViewModelGo4Lunch.class);
        // viewModelGo4Lunch.refreshPosition();
        viewModelGo4Lunch.getMyPosition().observe(getViewLifecycleOwner(), this::onUpdatePosition);

        viewModelGo4Lunch.getRestaurants().observe(getViewLifecycleOwner(), this::onUpdateRestaurants);


        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    private void onUpdatePosition(Location location) {

        Log.i("[THOMAS]", "onUpdatePosition MapsFragment ... " + location.getLatitude() + " " + location.getLongitude());

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        if (Double.isNaN(location.getLongitude()) && Double.isNaN(location.getLatitude())) {

        }

        if (location != null)
            //check if position is empty
            myMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        myMap.animateCamera(CameraUpdateFactory.zoomTo(10));//city zoom

        if (location != null) {
            this.newPosition = new Location(location);
        }
    }

    private void onUpdateRestaurants(List<Result> results) {

        Log.i("[THOMAS]", "Frag map, onUpdateRestaurants : total => " + results.size());

        myMap.clear();

        for (int i = 0; i < results.size(); i++) {
            Double lat = results.get(i).getGeometry().getLocation().getLat();
            Double lng = results.get(i).getGeometry().getLocation().getLng();
            //String placeName = response.body().getResults().get(i).getName();
            MarkerOptions markerOptions = new MarkerOptions();
            LatLng latLng = new LatLng(lat, lng);
            markerOptions.position(latLng);
            markerOptions.title(results.get(i).getName());
          //  markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_baseline_restaurant_24));
            Marker m = myMap.addMarker(markerOptions);
        }

    }


    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap map) {

            //to get an instance
            myMap = map;

            //we must use location object with fused location provider
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

            //Setup Google Map
            map.getUiSettings().setZoomControlsEnabled(true);
            map.setMinZoomPreference(1);

            //TODO: update permissions control , add to resume
            //To check permission
            checkPermissions();

            //used by fusedLocationProviderClient to get the last position
            //  getDeviceLocation();

            //  getLocationPermission();
            //   updateLocationUI();


        }
    };


/*    private void getMyPosition(){
        try {
            Log.i("[THOMAS]","Longitude " + newPosition.getLongitude());
            Log.i("[THOMAS]","Latitude " + newPosition.getLatitude());

            MarkerOptions markerOptions2 = new MarkerOptions();

            LatLng latLng = new LatLng(newPosition.getLatitude(), newPosition.getLongitude());
            // LatLng latLng = new LatLng(48.0785146,-0.7669906);

            markerOptions2.position(latLng);
            markerOptions2.title("Ma position");
            markerOptions2.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
            markerOptions2.snippet("kotchi").draggable(true);

            Marker z = map.addMarker(markerOptions2);

            map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            map.animateCamera(CameraUpdateFactory.zoomTo(14));

        } catch (Exception e) {
            Log.i("[THOMAS]","Exception getMyPosition() : " + e.getMessage());
        }
    }

 */

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


/*    private void updateLocationUI() {


        if (map == null) {
            return;
        }
        try {
            if (locationPermissionGranted) {
                map.setMyLocationEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                map.setMyLocationEnabled(false);
                map.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;
                //    getLocationPermission();
            }
        } catch (SecurityException e) {
            //Log.e("Exception: %s", e.getMessage());
        }
    }

 */


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
}