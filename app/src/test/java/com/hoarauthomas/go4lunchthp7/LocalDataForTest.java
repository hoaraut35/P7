package com.hoarauthomas.go4lunchthp7;

import com.google.android.gms.maps.model.LatLng;
import com.hoarauthomas.go4lunchthp7.model.FirestoreUser;
import com.hoarauthomas.go4lunchthp7.model.NearbySearch.Geometry;
import com.hoarauthomas.go4lunchthp7.model.NearbySearch.Location;
import com.hoarauthomas.go4lunchthp7.model.NearbySearch.RestaurantPojo;

import java.util.ArrayList;
import java.util.List;

public class LocalDataForTest {

    //3 workmates for testing
    public static List<FirestoreUser> getFakeListFromFirestoreRepositoryForTest() {
        List<FirestoreUser> myFakeUserList = new ArrayList<>();
        myFakeUserList.add(new FirestoreUser("ChIJ85WzK9HUDkgRitQb60cwf91", null, "9FYLr2m7csO4fxHtwa99widnQMC1", "https://lh3.googleusercontent.com/a-/AOh14Gj4nMiaSsUjHvyDgYy546bPkf8aopCNdF53F_x4hA=s96-c", "Thomas"));
        myFakeUserList.add(new FirestoreUser("ChIJ85WzK9HUDkgRitQb60cwf92", null, "9FYLr2m7csO4fxHtwa99widnQMC2", "https://lh3.googleusercontent.com/a-/AOh14Gj4nMiaSsUjHvyDgYy546bPkf8aopCNdF53F_x4hA=s96-c", "Samuel"));
        myFakeUserList.add(new FirestoreUser("ChIJ85WzK9HUDkgRitQb60cwf93", null, "9FYLr2m7csO4fxHtwa99widnQMC3", "https://lh3.googleusercontent.com/a-/AOh14Gj4nMiaSsUjHvyDgYy546bPkf8aopCNdF53F_x4hA=s96-c", "JEssica"));
        return myFakeUserList;
    }

    public static List<RestaurantPojo> getFakeListFromRestaurantRepositoryForTest() {

        RestaurantPojo myRestaurant1 = new RestaurantPojo();
        myRestaurant1.setName("Restaurant1");
        myRestaurant1.setPlaceId("ChIJ85WzK9HUDkgRitQb60cwf91");
        myRestaurant1.setGeometry(new Geometry());
        myRestaurant1.getGeometry().setLocation(new Location());
        myRestaurant1.getGeometry().getLocation().setLat(11d);
        myRestaurant1.getGeometry().getLocation().setLng(11d);

        RestaurantPojo myRestaurant2 = new RestaurantPojo();
        myRestaurant2.setName("Restaurant2");
        myRestaurant2.setPlaceId("ChIJ85WzK9HUDkgRitQb60cwf92");
        myRestaurant2.setGeometry(new Geometry());
        myRestaurant2.getGeometry().setLocation(new Location());
        myRestaurant2.getGeometry().getLocation().setLat(22d);
        myRestaurant2.getGeometry().getLocation().setLng(22d);

        RestaurantPojo myRestaurant3 = new RestaurantPojo();
        myRestaurant3.setName("Restaurant3");
        myRestaurant3.setPlaceId("ChIJ85WzK9HUDkgRitQb60cwf93");
        myRestaurant3.setGeometry(new Geometry());
        myRestaurant3.getGeometry().setLocation(new Location());
        myRestaurant3.getGeometry().getLocation().setLat(33d);
        myRestaurant3.getGeometry().getLocation().setLng(33d);

        List<RestaurantPojo> myFakeRestaurantList = new ArrayList<>();
        myFakeRestaurantList.add(myRestaurant1);
        myFakeRestaurantList.add(myRestaurant2);
        myFakeRestaurantList.add(myRestaurant3);

        return myFakeRestaurantList;
    }

    public static com.hoarauthomas.go4lunchthp7.PlaceAutocomplete getPlaceForTest() {
        com.hoarauthomas.go4lunchthp7.PlaceAutocomplete myFakePlace = new com.hoarauthomas.go4lunchthp7.PlaceAutocomplete();
        List<com.hoarauthomas.go4lunchthp7.Prediction> myPredictionList = new ArrayList<>();
        com.hoarauthomas.go4lunchthp7.Prediction myPrediction= new com.hoarauthomas.go4lunchthp7.Prediction();
        myPrediction.setPlaceId("fakePlaceId");
        myPredictionList.add(myPrediction);
        myFakePlace.setPredictions(myPredictionList);
        return myFakePlace;
    }

    public static LatLng getLatLngPosition(){
        return new LatLng(48.0956, -1.3718);
    }

    public static Integer getZoom() {
        return 12;
    }
}
