package com.hoarauthomas.go4lunchthp7;

import androidx.lifecycle.LiveData;

import com.hoarauthomas.go4lunchthp7.model.FirestoreUser;
import com.hoarauthomas.go4lunchthp7.model.NearbySearch.RestaurantPojo;
import com.hoarauthomas.go4lunchthp7.model.PlaceDetails.ResultPlaceDetail;
import com.hoarauthomas.go4lunchthp7.model.WorkmatesPojoForUI;

import java.util.ArrayList;
import java.util.List;

public class LocalDataForTest {


    public static List<WorkmatesPojoForUI> getWorkmatesForTest() {

        List<WorkmatesPojoForUI> favoriteRestaurantList = new ArrayList<>();
        favoriteRestaurantList.add(new WorkmatesPojoForUI("avatar","THOMAS H","FRANCAIS","151515"));
        favoriteRestaurantList.add(new WorkmatesPojoForUI("avatar2","MELISSA H","INDIEN","5454"));
        favoriteRestaurantList.add(new WorkmatesPojoForUI("avatar3","JESSICA H","CHINOIS","454545"));
        favoriteRestaurantList.add(new WorkmatesPojoForUI("avatar4","CAMILLE H","CHINOIS","454545"));
        favoriteRestaurantList.add(new WorkmatesPojoForUI("avatar5","SAMUEL H","ANGLAIS","4545454"));

        return favoriteRestaurantList;



    }

    public List<RestaurantPojo> getMyRestaurantList() {

        List<RestaurantPojo> myFakeList = new ArrayList<>();

        return myFakeList;
    }
}
