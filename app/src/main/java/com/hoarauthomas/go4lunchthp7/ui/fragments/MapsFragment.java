package com.hoarauthomas.go4lunchthp7.ui.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

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
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.hoarauthomas.go4lunchthp7.BuildConfig;
import com.hoarauthomas.go4lunchthp7.R;
import com.hoarauthomas.go4lunchthp7.api.GooglePlacesInterface;
import com.hoarauthomas.go4lunchthp7.model.pojo.Place;
import com.hoarauthomas.go4lunchthp7.viewmodel.ViewModelFactory;
import com.hoarauthomas.go4lunchthp7.viewmodel.ViewModelGo4Lunch;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapsFragment extends Fragment implements OnRequestPermissionsResultCallback {

    private ViewModelGo4Lunch viewModelGo4Lunch;

    private LocationManager lm;
    private static final int DEFAULT_ZOOM = 15;
    private final LatLng defaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean locationPermissionGranted;
    private Location lastKnownLocation, newPosition;

    //TODO: use this to locate the phone
    private FusedLocationProviderClient fusedLocationProviderClient;

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private GoogleMap map;


    public LatLng myPosition;


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

            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());




            map = googleMap;


            map.getUiSettings().setZoomControlsEnabled(true);



        //    map.setOnMyLocationButtonClickListener();
         //   map.setOnMyLocationClickListener(this);

            enableMyLocation();

            getDeviceLocation();


           /* map.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                @Override
                public boolean onMyLocationButtonClick() {
                    return false;
                }
            });

            */





          //  getLocationPermission();

         //   updateLocationUI();


            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://maps.googleapis.com/maps/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            //Create a REST
            GooglePlacesInterface service = retrofit.create(GooglePlacesInterface.class);

            //TODO: add long lat
            //Fetch a list of the Github repositories
            Call<Place> call = service.getNearbyPlaces(BuildConfig.MAPS_API_KEY,1000);

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
                            //m.setTag();


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
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (map != null) {
                map.setMyLocationEnabled(true);
            }
        } else {
            // Permission to access the location is missing. Show rationale and request permission
         //   PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
           //         Manifest.permission.ACCESS_FINE_LOCATION, true);
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

                               myPosition = new LatLng(lastKnownLocation.getLongitude(),lastKnownLocation.getLatitude());


                                Log.i("[THOMAS]","" + lastKnownLocation.getLongitude() + lastKnownLocation.getLatitude());
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
        viewModelGo4Lunch = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(ViewModelGo4Lunch.class);
        viewModelGo4Lunch.getMyPosition().observe(this,this::onUpdatePosition);


        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    private void onUpdatePosition(Location location) {
        this.newPosition.set(location);

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