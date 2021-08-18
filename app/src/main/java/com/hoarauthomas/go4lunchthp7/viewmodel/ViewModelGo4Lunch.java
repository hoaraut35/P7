package com.hoarauthomas.go4lunchthp7.viewmodel;

import android.location.Location;
import android.util.Log;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseUser;
import com.hoarauthomas.go4lunchthp7.model.firestore.User;
import com.hoarauthomas.go4lunchthp7.model.placedetails2.ResultDetailRestaurant;
import com.hoarauthomas.go4lunchthp7.permissions.PermissionChecker;
import com.hoarauthomas.go4lunchthp7.repository.AuthRepository;
import com.hoarauthomas.go4lunchthp7.repository.PositionRepository;
import com.hoarauthomas.go4lunchthp7.repository.RestaurantsRepository;
import com.hoarauthomas.go4lunchthp7.repository.WorkMatesRepository;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;


//This class is for the business logic
//We expose only Livedata
//Do not use reference to a view or lifecycle here !!!!
//https://medium.com/@kashifo/4-steps-to-mvvm-in-android-java-b05fb4148523

public class ViewModelGo4Lunch extends ViewModel {

    //Add repository here...
    private final AuthRepository myAuthSource;
    private final PositionRepository myLocationSource;
    private final RestaurantsRepository myRestaurantsSource;
    private final WorkMatesRepository myWorkMatesSource;
    private final PermissionChecker myPermissionChecker;

    //Add livedata and mutableLiveData mediator here...
    private final MediatorLiveData<MainViewState> myAppMapMediator = new MediatorLiveData<>();
    private final MutableLiveData<FirebaseUser> myUserVM;
    private final MutableLiveData<Boolean> myUserStateVM;
    private final LiveData<Location> myPositionVM;

    private LiveData<List<com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo>> myRestaurantVM;

    private final List<com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo> myRestaurantList = new ArrayList<>();

    private LiveData<List<User>> workMatesLiveData;

    //list of workmates
    private final List<User> myWorkMatesList = new ArrayList<>();

    //others
    private String myActualRestaurant;
    private Double Long, Lat;

    //constructor to get one instance of each object, called by ViewModelFactory
    public ViewModelGo4Lunch(
            AuthRepository authRepository,
            RestaurantsRepository restaurantsRepository,
            PositionRepository positionRepository,
            WorkMatesRepository workMatesRepository,
            PermissionChecker permissionChecker) {

        //for authentification...
        this.myAuthSource = authRepository;
        //user
        this.myUserVM = myAuthSource.getUserFromRepo();
        //connected or not connected...
        this.myUserStateVM = myAuthSource.getUserStateFromRepo();

        //this for permission
        this.myPermissionChecker = permissionChecker;

        //for position...
        this.myLocationSource = positionRepository;

        //publish to mediatorlivedata with googleplace to combine before to show on UI...
        this.myPositionVM = Transformations.map(myLocationSource.getmyPositionFromRepo(), new Function<Location, Location>() {
            @Override
            public Location apply(Location input) {
                if (input == null) {
                    //TODO: return a position by defaut

                    return null;
                } else {
                    return input;
                }
            }
        });

        //for restaurants
        this.myRestaurantsSource = restaurantsRepository;
        this.myRestaurantVM = myRestaurantsSource.getAllRestaurants(Long, Lat);

        //for workmates...
        this.myWorkMatesSource = workMatesRepository;
        this.workMatesLiveData = myWorkMatesSource.getAllWorkMates();

        //update the position in ViewState for UI
        myAppMapMediator.addSource(myPositionVM, new Observer<Location>() {
            @Override
            public void onChanged(Location position) {
                //combine(position,null,null);
                myAppMapMediator.setValue(new MainViewState(position, null, null));
                myRestaurantsSource.UpdateLngLat(position.getLongitude(), position.getLatitude());
            }
        });

        //update the restaurant list in ViewState for UI
        myAppMapMediator.addSource(myRestaurantVM, new Observer<List<com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo>>() {
            @Override
            public void onChanged(List<com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo> restaurants) {
                combine(myPositionVM.getValue(), restaurants, workMatesLiveData.getValue());

                myAppMapMediator.setValue(new MainViewState(myPositionVM.getValue(), restaurants, workMatesLiveData.getValue()));
            }
        });

        //update the workmates list in ViewState for UI
        myAppMapMediator.addSource(workMatesLiveData, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> workMatesList) {
                combine(myPositionVM.getValue(), myRestaurantVM.getValue(), workMatesList);
                myAppMapMediator.setValue((new MainViewState(myPositionVM.getValue(), myRestaurantList, workMatesList)));
            }
        });
    }

    public float distanceBetween(LatLng first, LatLng second) {
        float[] distance = new float[1];
        Location.distanceBetween(first.latitude, first.longitude, second.latitude, second.longitude, distance);
        return distance[0];
    }

    private void combine(@Nullable Location location, @Nullable List<com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo> restau, @Nullable List<User> workUser) {

        if(location != null){
            myAppMapMediator.setValue(new MainViewState(location));
        }

        if (location != null && restau != null && workUser != null) {

            Log.i("[COMB]", "Nombre de restaurant à traiter : " + restau.size() + "avec " + workUser.size() + " colègues"+ " position : "+ location.getLatitude() + " " +location.getLongitude());

            for (int i = 0; i < restau.size(); i++) {


                //Log.i("[COMB]","" + restau.get(i).getPlaceId());

                //TODO:mise à jour distance entre restau et utilisateur dans la classe pojo ne fonctionne pas
                LatLng restauPos = new LatLng(restau.get(i).getGeometry().getLocation().getLng(),restau.get(i).getGeometry().getLocation().getLat());
                LatLng userPos = new LatLng(location.getLongitude(),location.getLatitude());

                int distance = Math.round((distanceBetween(userPos,restauPos)));

                restau.get(i).getGeometry().setMyPosition(distance);

                Log.i("[COMB]","La distance en tre utilisateur et restaurant : " + restau.get(i).getGeometry().getMyPosition());



                for (int z = 0; z < workUser.size(); z++) {

                  //  Log.i("[COMB]", "Equivalent  :" + restau.get(i).getPlaceId() + " " + workUser.get(z).getFavoriteRestaurant());

                    if (restau.get(i).getPlaceId().equals(workUser.get(z).getFavoriteRestaurant())) {
                        Log.i("[COMB]", "Un collegues est renseigné sur le restaurant " + restau.get(i).getName());

                        restau.get(i).getGeometry().getLocation().setFavorite(true);




                    } else {
                        restau.get(i).getGeometry().getLocation().setFavorite(false);
                        //restau.get(i).setMyFavLike(false);
                    }

                    Log.i("[COMB]", "setup fav in viewmodel " + restau.get(i).getMyFavLike());
                }
            }


            myAppMapMediator.setValue(new MainViewState(location, restau, workUser));

        }

    }


    //to publish in UI with not authorized data modification from UI
    public LiveData<MainViewState> getViewStateLiveData() {
        return myAppMapMediator;
    }



    //publish this method to activity for updat eposition...
    public void refreshPosition() {

        if (!myPermissionChecker.hasLocationPermission()) {
            myLocationSource.stopLocationRequest();
        } else {
            myLocationSource.startLocationRequest();
        }
    }

    //update user position
    public void updateLngLat(Double Long, Double Lat) {
        Log.i("[RESTAURANT]", "Update position in restaurant request ..." + Long + " " + Lat);
        this.Long = Long;
        this.Lat = Lat;
        myRestaurantsSource.UpdateLngLat(Long, Lat);
        this.myRestaurantVM = myRestaurantsSource.getAllRestaurants(Long, Lat);
    }

    public LatLng getMyLastPosition() {
        return new LatLng(this.Lat, this.Long);
    }


    //----------------------------------------------------------------------------------------------


    //publish method to activity for
    public MutableLiveData<FirebaseUser> getMyCurrentUser() {
        return myUserVM;
    }

    //publish method to activity... to log out work fine
    public void logOut() {
        myAuthSource.logOutFromRepo();
    }

    //publish method to activity... (logged or not) work fine
    public MutableLiveData<Boolean> getMyUserState() {
        return myUserStateVM;
    }


    //----------------------------------------------------------------------------------------------


    //these methods are published to activity or fragments ...


    //this method must be modified to include color of ùmarker
    public LiveData<List<com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo>> getRestaurants() {
        Log.i("[RESTAURANT]", "getRestaurant in ViewModelm " + this.Long + this.Lat);
        return myRestaurantVM;
    }

    public void setActualRestaurant(String tag) {
        this.myActualRestaurant = tag;
        Log.i("[MAP]", "Restaurant actuel SET: " + tag);

    }

    public String getActualRestaurant() {
        Log.i("[MAP]", "Restaurant actuel gET: " + this.myActualRestaurant);
        return this.myActualRestaurant;

    }


    //----------------------------------------------------------------------------------------------
    //FIRESTORE


    //publish to UI...
    public LiveData<List<User>> getAllWorkMates() {
        Log.i("[WORK]", "in VM get all work mates...");

        return myWorkMatesSource.getAllWorkMates();
    }

    public LiveData<List<User>> getAllWorkMatesByRestaurant() {
        return myWorkMatesSource.getAllWorkMatesByRestaurant("id");
    }


    //Create user to Firestore
    public void createUser() {
        this.myWorkMatesSource.createUser();
    }


    public void addNewRestaurant(String name) {
        this.myWorkMatesSource.addRestaurant(name);
    }


    //called by UI
    public LiveData<ResultDetailRestaurant> getRestaurantDetail2(String restaurant_id) {
        //Log.i("[DETAIL]", "VM DETAIL " + this.myRestaurantsSource.getRestaurantById2(restaurant_id).getFormattedPhoneNumber());

        return this.myRestaurantsSource.getRestaurantById2(restaurant_id);
    }


    //Event from UI when user click on like
    public void onLikeClicked() {
    }

    //Update list of workmates in vm
    public void updateWorkMatesList(List<User> users) {

        this.myWorkMatesList.clear();
        this.myWorkMatesList.addAll(users);
    }

    public User getWorkMAteById(String workmateId) {

        int i = 0;

        for (i = 0; i < this.myWorkMatesList.size(); i++) {
            if (this.myWorkMatesList.get(i).getUid().equals(workmateId)) {
                break;

            }
        }

        return this.myWorkMatesList.get(i);

    }

    //update list of restaurant in vm
    public void updateRestaurantList(List<com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo> restaurants) {
        this.myRestaurantList.clear();
        this.myRestaurantList.addAll(restaurants);
    }

    public List<com.hoarauthomas.go4lunchthp7.pojo.RestaurantPojo> mergeRestauWithFavRestau() {
        for (int i = 0; i < this.myRestaurantList.size(); i++) {
            for (int y = 0; y < myWorkMatesList.size(); y++) {
                if (myRestaurantList.get(i).getPlaceId().equals(myWorkMatesList.get(y).getFavoriteRestaurant())) {
                    //update marker color here .... and retuirn the list to fragment map ?
                    myRestaurantList.get(i).setIcon("green");

                } else {
                    myRestaurantList.get(i).setIcon("red");
                }
            }
        }
        return myRestaurantList;
    }


}


