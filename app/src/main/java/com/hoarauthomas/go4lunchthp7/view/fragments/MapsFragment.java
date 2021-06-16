package com.hoarauthomas.go4lunchthp7.view.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.FusedLocationProviderClient;
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
import com.hoarauthomas.go4lunchthp7.api.GooglePlacesInterface;
import com.hoarauthomas.go4lunchthp7.model.pojo.Place;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static androidx.constraintlayout.motion.widget.Debug.getLocation;
import static com.facebook.internal.FeatureManager.Feature.Places;

public class MapsFragment extends Fragment implements ActivityCompat.OnRequestPermissionsResultCallback {


    private LocationManager lm;
    private static final int DEFAULT_ZOOM = 15;
    private final LatLng defaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean locationPermissionGranted;
    private Location lastKnownLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private GoogleMap map;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {

            map = googleMap;

            //setup
            map.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                @Override
                public boolean onMyLocationButtonClick() {
                    return false;
                }
            });



            enableMyLocation();

          //  getLocationPermission();

         //   updateLocationUI();


            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://maps.googleapis.com/maps/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            //Create a REST
            GooglePlacesInterface service = retrofit.create(GooglePlacesInterface.class);

            //Fetch a list of the Github repositories
            Call<Place> call = service.getNearbyPlaces("AIzaSyDzUUJlN7hmetd7MtQR5s5TTzWiO4dwpCA",1000);

            //send asynchronous task
            call.enqueue(new Callback<Place>() {
                @Override
                public void onResponse(Call<Place> call, Response<Place> response) {





                    try{
                        map.clear();


                        Log.i("[THOMAS]","Nombre de restaurant(s) trouvé(s) : " + response.body().getResults().size());


                        //loop to add marker on map for everybody result
                        for (int i = 0; i < response.body().getResults().size(); i++){
                            Double lat = response.body().getResults().get(i).getGeometry().getLocation().getLat();
                            Double lng = response.body().getResults().get(i).getGeometry().getLocation().getLng();

                            //Log.i("[THOMAS]","coordonne["+ i + "] " + lat + " " + lng );

                            String placeName = response.body().getResults().get(i).getName();

                            MarkerOptions markerOptions = new MarkerOptions();
                            LatLng latLng = new LatLng(lat,lng);
                            markerOptions.position(latLng);
                            markerOptions.title(placeName);
                            Marker m = map.addMarker(markerOptions);


                            //move camera to the latest position
                            map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                            map.animateCamera(CameraUpdateFactory.zoomTo(14));


                        }



                    }catch (Exception e)
                    {
                        Log.i("[THOMAS]","erreur map");
                    }



                }

                @Override
                public void onFailure(Call<Place> call, Throwable t) {

                }
            });


        }
    };



    private void enableMyLocation() {



        if (ContextCompat.checkSelfPermission(getContext(),Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            if (map !=null)
            {
                map.setMyLocationEnabled(true);
            }
            else
            {
            }
        }
    }


    private void updateLocationUI() {


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
                                map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(lastKnownLocation.getLatitude(),
                                                lastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                            }
                        } else {
                          //  Log.d("THOMAS", "Current location is null. Using defaults.");
                            //Log.e("THOMAS", "Exception: %s", task.getException());
                            map.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                            map.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
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