package com.hoarauthomas.go4lunchthp7;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hoarauthomas.go4lunchthp7.model.FirestoreUser;
import com.hoarauthomas.go4lunchthp7.model.NearbySearch.RestaurantPojo;
import com.hoarauthomas.go4lunchthp7.model.PlaceDetails.ResultPlaceDetail;
import com.hoarauthomas.go4lunchthp7.model.WorkmatesPojoForUI;

import java.util.ArrayList;
import java.util.List;

public class LocalDataForTest {

    private static MutableLiveData<List<FirestoreUser>> myMockUserList = new MutableLiveData<>();
    private static MutableLiveData<List<RestaurantPojo>> myMockRestaurantList = new MutableLiveData<>();

    public static MutableLiveData<List<FirestoreUser>> getFakeListFromFirestoreRepositoryForTest() {
        List<FirestoreUser> myFakeUserList = new ArrayList<>();
        myFakeUserList.add(new FirestoreUser("ChIJ85WzK9HUDkgRitQb60cwf9Y", null, "9FYLr2m7csO4fxHtwa99widnQMC2", "https://lh3.googleusercontent.com/a-/AOh14Gj4nMiaSsUjHvyDgYy546bPkf8aopCNdF53F_x4hA=s96-c", "Thomas"));
        myFakeUserList.add(new FirestoreUser("ChIJ85WzK9HUDkgRitQb60cwf9YB", null, "9FYLr2m7csO4fxHtwa99widnQMCB", "https://lh3.googleusercontent.com/a-/AOh14Gj4nMiaSsUjHvyDgYy546bPkf8aopCNdF53F_x4hA=s96-c", "Samuel"));
        myFakeUserList.add(new FirestoreUser("ChIJ85WzK9HUDkgRitQb60cwf9A", null, "9FYLr2m7csO4fxHtwa99widnQMCC", "https://lh3.googleusercontent.com/a-/AOh14Gj4nMiaSsUjHvyDgYy546bPkf8aopCNdF53F_x4hA=s96-c", "JEssica"));
        myMockUserList.setValue(myFakeUserList);
        return myMockUserList;
    }

    public static MutableLiveData<List<RestaurantPojo>> getFakeListFromRestaurantRepositoryForTest() {
        List<RestaurantPojo> myFakeRestaurantList = new ArrayList<>();
        myFakeRestaurantList.add(new RestaurantPojo("ChIJ85WzK9HUDkgRitQb60cwf9Y", "Pizza momo"));
        myFakeRestaurantList.add(new RestaurantPojo("ChIJ85WzK9HUDkgRitQb60cwf9B", "Galette Bretagne"));
        myFakeRestaurantList.add(new RestaurantPojo("ChIJ85WzK9HUDkgRitQb60cwf9C", "Italien"));
        myMockRestaurantList.setValue(myFakeRestaurantList);
        return myMockRestaurantList;
    }
}
