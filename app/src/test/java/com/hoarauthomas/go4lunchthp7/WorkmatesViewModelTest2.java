package com.hoarauthomas.go4lunchthp7;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.facebook.internal.Mutable;
import com.google.android.gms.maps.model.LatLng;
import com.hoarauthomas.go4lunchthp7.model.FirestoreUser;
import com.hoarauthomas.go4lunchthp7.model.NearbySearch.RestaurantPojo;
import com.hoarauthomas.go4lunchthp7.repository.FirestoreRepository;
import com.hoarauthomas.go4lunchthp7.repository.PlaceAutocompleteRepository;
import com.hoarauthomas.go4lunchthp7.repository.PositionRepository;
import com.hoarauthomas.go4lunchthp7.repository.RestaurantsRepository;
import com.hoarauthomas.go4lunchthp7.repository.SharedRepository;
import com.hoarauthomas.go4lunchthp7.ui.restaurant.ViewModelRestaurant;
import com.hoarauthomas.go4lunchthp7.ui.workmates.ViewModelWorkMates;
import com.hoarauthomas.go4lunchthp7.ui.workmates.ViewStateWorkMates;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class WorkmatesViewModelTest2 {

    //to run every tasks synchronously
    @Rule
    public final InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

    //repositories
    PositionRepository myPositionRepository = mock(PositionRepository.class);
    RestaurantsRepository myRestaurantRepository = mock(RestaurantsRepository.class);
    FirestoreRepository myFirestoreRepository = mock(FirestoreRepository.class);
    SharedRepository mySharedRepository = mock(SharedRepository.class);
    PlaceAutocompleteRepository myPlaceAutocompleteRepository = mock(PlaceAutocompleteRepository.class);

    //viewmodel
    ViewModelRestaurant myViewModelRestaurants;

    MutableLiveData<Boolean> myBool = new MutableLiveData<>();


    MutableLiveData<LatLng> myLatLng = new MutableLiveData<>();
    MutableLiveData<List<RestaurantPojo>> myListRestaurant = new MutableLiveData<>();
    MutableLiveData<List<FirestoreUser>> myListUser = new MutableLiveData<>();
    MutableLiveData<Boolean> myReload = new MutableLiveData<>();
    MutableLiveData<com.hoarauthomas.go4lunchthp7.PlaceAutocomplete> myPlaces = new MutableLiveData<>();

    @Before
    public void setup() {

        //set position for repo
        myLatLng.setValue(new LatLng(44.0,-1.0));

        //set list of restaurant for repo
        myListRestaurant.setValue(LocalDataForTest.getFakeListFromRestaurantRepositoryForTest());

        //set list of user for trepo
        myListUser.setValue(LocalDataForTest.getFakeListFromFirestoreRepositoryForTest());

        //set reloads
        myReload.setValue(false);

        //set places

        //mock methodf
        Mockito.when(myPositionRepository.getLocationLatLgnLiveData()).thenReturn(myLatLng);
        Mockito.when(myRestaurantRepository.getMyRestaurantsList()).thenReturn(myListRestaurant);
        Mockito.when(myFirestoreRepository.getFirestoreWorkmates()).thenReturn(myListUser);
        Mockito.when(mySharedRepository.getReload()).thenReturn(myReload);

        Mockito.when(myPlaceAutocompleteRepository.getPlaces()).thenReturn(myPlaces);
    }

    @Test
    public void checkPosition(){
        assertEquals(44.0,myPositionRepository.getLocationLatLgnLiveData().getValue().latitude,0.01);
        assertEquals(-1.0,myPositionRepository.getLocationLatLgnLiveData().getValue().longitude,0.01);
    }

    @Test
    public void checkListRestaurants(){
        assertEquals(3, myRestaurantRepository.getMyRestaurantsList().getValue().size());
    }

    @Test
    public void checkListUsers(){
        assertEquals(3, myFirestoreRepository.getFirestoreWorkmates().getValue().size());
    }

    @Test
    public void checkReload(){
        assertFalse(mySharedRepository.getReload().getValue());
    }
}