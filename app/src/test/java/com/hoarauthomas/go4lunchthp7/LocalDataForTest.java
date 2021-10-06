package com.hoarauthomas.go4lunchthp7;

import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hoarauthomas.go4lunchthp7.model.FirestoreUser;
import com.hoarauthomas.go4lunchthp7.model.NearbySearch.RestaurantPojo;

import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

public class LocalDataForTest {

    //general data
    private final static MutableLiveData<List<FirestoreUser>> myMockUserList = new MutableLiveData<>();
    private final static MutableLiveData<List<RestaurantPojo>> myMockRestaurantList = new MutableLiveData<>();

    //for restaurant ui
    private final static MutableLiveData<Boolean> myReloadMap = new MutableLiveData<>();
    private final static MutableLiveData<Location> myLocation = new MutableLiveData<>(null);
    private final static MutableLiveData<com.hoarauthomas.go4lunchthp7.PlaceAutocomplete> myPlaceAutoCompleteListSingle = new MutableLiveData<>();

    //for map ui

    public static List<FirestoreUser> getFakeListFromFirestoreRepositoryForTest() {
        List<FirestoreUser> myFakeUserList = new ArrayList<>();
        myFakeUserList.add(new FirestoreUser("ChIJ85WzK9HUDkgRitQb60cwf9Y", null, "9FYLr2m7csO4fxHtwa99widnQMC2", "https://lh3.googleusercontent.com/a-/AOh14Gj4nMiaSsUjHvyDgYy546bPkf8aopCNdF53F_x4hA=s96-c", "Thomas"));
        myFakeUserList.add(new FirestoreUser("ChIJ85WzK9HUDkgRitQb60cwf9YB", null, "9FYLr2m7csO4fxHtwa99widnQMCB", "https://lh3.googleusercontent.com/a-/AOh14Gj4nMiaSsUjHvyDgYy546bPkf8aopCNdF53F_x4hA=s96-c", "Samuel"));
        myFakeUserList.add(new FirestoreUser("ChIJ85WzK9HUDkgRitQb60cwf9A", null, "9FYLr2m7csO4fxHtwa99widnQMCC", "https://lh3.googleusercontent.com/a-/AOh14Gj4nMiaSsUjHvyDgYy546bPkf8aopCNdF53F_x4hA=s96-c", "JEssica"));
        return myFakeUserList;
    }

    public static List<RestaurantPojo> getFakeListFromRestaurantRepositoryForTest() {
        List<RestaurantPojo> myFakeRestaurantList = new ArrayList<>();
        myFakeRestaurantList.add(new RestaurantPojo("ChIJ85WzK9HUDkgRitQb60cwf9Y", "Pizza momo"));
        myFakeRestaurantList.add(new RestaurantPojo("ChIJ85WzK9HUDkgRitQb60cwf9B", "Galette Bretagne"));
        myFakeRestaurantList.add(new RestaurantPojo("ChIJ85WzK9HUDkgRitQb60cwf9C", "Italien"));
        myMockRestaurantList.setValue(myFakeRestaurantList);
        return myFakeRestaurantList;
    }



    public static double getLatitudeFromTest() {
        return 48.0949d;
    }

    public static double getLongitudeFromTest() {
        return -1.3712d;
    }


    public static LiveData<Location> getPositionForTest() {

      //  Location newLocation = new Location("");

       // Mockito.doCallRealMethod().when(newLocation).setLatitude(48.0949d);
        //Mockito.doCallRealMethod().when(newLocation).setLongitude(-1.3712d);

//        newLocation.setLongitude(-1.3712d);
  //      newLocation.setLatitude(48.0949d);
       // newLocation.setAccuracy(3.0f);
    //    myLocation.setValue(newLocation);
        return null;
    }

    public static MutableLiveData<com.hoarauthomas.go4lunchthp7.PlaceAutocomplete> getPlacesForTest() {


        return null;
    }


    public static Boolean getReloadMapForTest() {
        boolean test = false;
        return false;
    }
}
