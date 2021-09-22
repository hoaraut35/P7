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
import com.hoarauthomas.go4lunchthp7.Prediction;
import com.hoarauthomas.go4lunchthp7.R;
import com.hoarauthomas.go4lunchthp7.factory.ViewModelFactory;
import com.hoarauthomas.go4lunchthp7.ui.detail.DetailActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MapsFragment extends Fragment implements OnRequestPermissionsResultCallback, GoogleMap.OnMarkerClickListener {

    private ViewModelMap myViewModelMap;
    public Marker myMarker;
    private static final int DEFAULT_ZOOM = 12;
    public List<MyMarkerObject> allMarkers = new ArrayList<MyMarkerObject>();
    private GoogleMap myMap;

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

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap map) {

            //to get an instance
            myMap = map;
            myMap.clear();

            //myViewModelMap.refresh();
            myViewModelMap.ViewStateForMapUI().observe(getViewLifecycleOwner(), new Observer<ViewStateMap>() {
                @Override
                public void onChanged(ViewStateMap viewStateMap) {
                    showMapWithPosition(viewStateMap.getMyLatLng());
                    showRestaurant(viewStateMap.myRestaurantsList);
                }
            });

            myViewModelMap.getPredictionFromRepository().observe(getActivity(), new Observer<Prediction>() {
                @Override
                public void onChanged(Prediction prediction) {

                    if (prediction == null) return;

                    //Toast.makeText(getContext(), prediction.getDescription() + "placeid : " + prediction.getPlaceId(), Toast.LENGTH_SHORT).show();

                    Boolean findPlace = false;

                    for (int i = 0; i < allMarkers.size(); i++) {


                        if (prediction.getPlaceId().equals(allMarkers.get(i).getId())) {

                            Toast.makeText(getContext(), "trouvé: " + prediction.getPlaceId(), Toast.LENGTH_SHORT).show();

//

                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(allMarkers.get(i).getLocation().latitude, allMarkers.get(i).getLocation().longitude), 15));
                            //                        CameraUpdateFactory.zoomTo(20);
                            //   myMap.moveCamera(CameraUpdateFactory.newLatLng(allMarkers.get(i).getLocation()));
                            findPlace = true;
                            break;
                        }

                        findPlace = false;

                    }

                    if (!findPlace) {

                        //  Toast.makeText(getContext(), "non trouvé: " + prediction.getPlaceId(), Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(getContext(), DetailActivity.class);
                        String restaurantTag = prediction.getPlaceId();
                        intent.putExtra("TAG_ID", restaurantTag);
                        startActivity(intent);

                    }





                   /* Iterator<Marker> iterator = allMarkers.iterator();

                    while(iterator.hasNext()){
                        Marker marker = iterator.next();

                        if (marker.getTag().equals(prediction.getPlaceId())){
                            Toast.makeText(getContext(), "trouvé", Toast.LENGTH_SHORT).show();
                        }else
                        {
                            Toast.makeText(getContext(), "non trouvé", Toast.LENGTH_SHORT).show();
                        }
                    }

                    */


                    /*for (Marker item: allMarkers){
                        if (item.getTag().equals(prediction.getPlaceId())){
                            Toast.makeText(getContext(), "trouvé", Toast.LENGTH_SHORT).show();
                        }else
                        {
                            Toast.makeText(getContext(), "non trouvé", Toast.LENGTH_SHORT).show();
                        }
                    }

                     */


          /*          if (allMarkers.contains(prediction.getPlaceId())) {
                        Toast.makeText(getContext(), "trouvé", Toast.LENGTH_SHORT).show();

//
                    }else
                    {
                        Toast.makeText(getContext(), " non trouvé", Toast.LENGTH_SHORT).show();
                    }

           */


                    /* //for the list of marker we search
                    for (int i = 0; i < allMarkers.size();i++){

                        if (allMarkers.get(i).getTag().equals(prediction.getPlaceId())){
                            Toast.makeText(getContext(), "trouvé", Toast.LENGTH_SHORT).show();


                           // myMap.moveCamera(CameraUpdateFactory.newLatLngBounds(australiaBounds, 0));
                          // myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(allMarkers.get(i).getPosition(),15));

                            //  myMap.
                          ///  Toast.makeText(getContext(), "position ajustée sur le restaurant", Toast.LENGTH_SHORT).show();

                            break;

                        }

                        else
                        {



                        }

                    }


                     */


                }
            });

/*            myViewModelMap.getMyPositionFromAutoSingleMode().observe(getViewLifecycleOwner(), new Observer<Event<Prediction>>() {
                @Override
                public void onChanged(Event<Prediction> predictionEvent) {

                    if (predictionEvent != null){

                         Toast.makeText(getContext(),"Move to this restaurant : " + predictionEvent.getContentIfNotHandled().getDescription(), Toast.LENGTH_SHORT).show();

                      //  Toast.makeText(getContext(), "" + allMarkers.size(), Toast.LENGTH_SHORT).show();


                        for (int i=0; i< allMarkers.size();i++){

                            if (allMarkers.get(i).getTag().equals(predictionEvent.getContentIfNotHandled().getPlaceId())){

                                Toast.makeText(getContext(), "Déplacement sur la carte" , Toast.LENGTH_SHORT).show();



                                //myMap.animateCamera(CameraUpdateFactory.newLatLng(allMarkers.get(i).getPosition()));

                               // myMap.moveCamera(CameraUpdateFactory.newLatLng(allMarkers.get(i).getPosition()));
                               // myMap.animateCamera(CameraUpdateFactory.zoomTo(15));


                                break;
                            }else
                            {
                                Toast.makeText(getContext(), "ouverture du détail"  , Toast.LENGTH_SHORT).show();

                             //   Intent intent = new Intent(getContext(), DetailActivity.class);
                               // String restaurantTag = predictionEvent.getContentIfNotHandled().getPlaceId();
                               // intent.putExtra("TAG_ID", restaurantTag);
                               // startActivity(intent);



                            }


                        }

                    }


                }
            });

 */



            /*new Observer<Prediction>() {
                @Override
                public void onChanged(Prediction prediction) {
                    //showMapWithPosition(prediction.);
                    //Toast.makeText(getContext(), "test", Toast.LENGTH_SHORT).show();
                    if (prediction != null){
                      //  Toast.makeText(getContext(),"Move to this restaurant : " + prediction.getDescription(), Toast.LENGTH_SHORT).show();

                        //TODO: don't work
                       // myMap.animateCamera(CameraUpdateFactory.newLatLng(myMarker.getPosition()));

                        //Toast.makeText(getContext(), ""+ allMarkers.get(prediction.getDescription()), Toast.LENGTH_SHORT).show();

                        Toast.makeText(getContext(), "" + allMarkers.size(), Toast.LENGTH_SHORT).show();


                        for (int i=0; i< allMarkers.size();i++){

                            if (allMarkers.get(i).getTag().equals(prediction.getPlaceId())){

                                Toast.makeText(getContext(), "Déplacement sur la carte" , Toast.LENGTH_SHORT).show();



                                //myMap.animateCamera(CameraUpdateFactory.newLatLng(allMarkers.get(i).getPosition()));

                                myMap.moveCamera(CameraUpdateFactory.newLatLng(allMarkers.get(i).getPosition()));
                                myMap.animateCamera(CameraUpdateFactory.zoomTo(15));


                                break;
                            }else
                            {
                                Toast.makeText(getContext(), "ouverture du détail" + prediction.getDescription() , Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(getContext(), DetailActivity.class);
                                String restaurantTag = prediction.getPlaceId();
                                intent.putExtra("TAG_ID", restaurantTag);
                                startActivity(intent);



                            }


                        }

                    }

                }
            });

             */

          /*  myViewModelMap.getPositionFromAutoComplete().observe(getViewLifecycleOwner(), new Observer<String>() {
                @Override
                public void onChanged(String s) {
                    Log.i("[COMPLETE]","reour sur frag map" + s);
                }
            });

           */


            //Setup Google Map
            map.getUiSettings().setZoomControlsEnabled(true);

            //  map.setMinZoomPreference(DEFAULT_ZOOM);

            map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(@NonNull @NotNull Marker marker) {
                    Intent intent = new Intent(getContext(), DetailActivity.class);
                    String restaurantTag = marker.getTag().toString();
                    intent.putExtra("TAG_ID", restaurantTag);
                    startActivity(intent);
                    return true;
                }
            });

            checkPermissions();

        }
    };

    private void showMapWithPosition(@NonNull LatLng position) {
        if (myMap != null) {
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
            myMarker.setTag(restaurants.get(i).getPlaceId());


            allMarkers.add(new MyMarkerObject(restaurants.get(i).getPlaceId(), myMarkerPosition));

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

