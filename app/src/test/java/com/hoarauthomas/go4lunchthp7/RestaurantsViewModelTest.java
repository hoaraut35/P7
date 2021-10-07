package com.hoarauthomas.go4lunchthp7;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.maps.model.LatLng;
import com.hoarauthomas.go4lunchthp7.model.FirestoreUser;
import com.hoarauthomas.go4lunchthp7.model.NearbySearch.Geometry;
import com.hoarauthomas.go4lunchthp7.model.NearbySearch.Location;
import com.hoarauthomas.go4lunchthp7.model.NearbySearch.RestaurantPojo;
import com.hoarauthomas.go4lunchthp7.repository.FirestoreRepository;
import com.hoarauthomas.go4lunchthp7.repository.PlaceAutocompleteRepository;
import com.hoarauthomas.go4lunchthp7.repository.PositionRepository;
import com.hoarauthomas.go4lunchthp7.repository.RestaurantsRepository;
import com.hoarauthomas.go4lunchthp7.repository.SharedRepository;
import com.hoarauthomas.go4lunchthp7.ui.restaurant.ViewModelRestaurant;
import com.hoarauthomas.go4lunchthp7.ui.restaurant.ViewStateRestaurant;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class RestaurantsViewModelTest {

    //to run every tasks synchronously
    @Rule
    public final InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

    //repositories
    PositionRepository myPositionRepository = mock(PositionRepository.class);
    RestaurantsRepository myRestaurantRepository = mock(RestaurantsRepository.class);
    FirestoreRepository myFirestoreRepository = mock(FirestoreRepository.class);
    SharedRepository mySharedRepository = mock(SharedRepository.class);
    PlaceAutocompleteRepository myPlaceAutocompleteRepository = mock(PlaceAutocompleteRepository.class);


    RestaurantPojo myRestaurantPojo = mock(RestaurantPojo.class);
    Geometry myGeometry = mock(Geometry.class);
    Location myLocation = mock(Location.class);

    //viewmodel here...
    ViewModelRestaurant myRestaurantViewModel;
    //viewState here...
    ViewStateRestaurant myView;

    //fake live data for mediator
    MutableLiveData<LatLng> myLatLng = new MutableLiveData<>();
    MutableLiveData<List<RestaurantPojo>> myListRestaurant = new MutableLiveData<>();
    MutableLiveData<List<FirestoreUser>> myListUser = new MutableLiveData<>();
    MutableLiveData<Boolean> myReload = new MutableLiveData<>();
    MutableLiveData<com.hoarauthomas.go4lunchthp7.PlaceAutocomplete> myPlace = new MutableLiveData<>();

    @Before
    public void setup() throws  InterruptedException{

        //set position for repo
        myLatLng.setValue(LocalDataForTest.getLatLngPosition());

        //set list of restaurant for repo
        myListRestaurant.setValue(LocalDataForTest.getFakeListFromRestaurantRepositoryForTest());

        //set list of user for trepo
        myListUser.setValue(LocalDataForTest.getFakeListFromFirestoreRepositoryForTest());

        //set reloads to true for mode without autocomplete
        myReload.setValue(true);

        //set places
        myPlace.setValue(LocalDataForTest.getPlaceForTest());

        //mock methodf
        Mockito.when(myPositionRepository.getLocationLatLgnLiveData()).thenReturn(myLatLng);
        Mockito.when(myRestaurantRepository.getMyRestaurantsList()).thenReturn(myListRestaurant);
        Mockito.when(myFirestoreRepository.getFirestoreWorkmates()).thenReturn(myListUser);
        Mockito.when(mySharedRepository.getReload()).thenReturn(myReload);
        Mockito.when(myPlaceAutocompleteRepository.getPlaces()).thenReturn(myPlace);

        //init my viewxmodel
        myRestaurantViewModel = new ViewModelRestaurant(
                myPositionRepository,
                myRestaurantRepository,
                myFirestoreRepository,
                mySharedRepository,
                myPlaceAutocompleteRepository);

        //init viewstate
        myView = LiveDataTestUtils.getOrAwaitValue(myRestaurantViewModel.getRestaurantsViewUI());


    }

    @Test
    public void checkPosition() {
        assertEquals(48.0956, myPositionRepository.getLocationLatLgnLiveData().getValue().latitude, 0.01);
        assertEquals(-1.3718, myPositionRepository.getLocationLatLgnLiveData().getValue().longitude, 0.01);
    }

    @Test
    public void checkListRestaurants() {
        assertEquals(3, myRestaurantRepository.getMyRestaurantsList().getValue().size());
    }

    @Test
    public void checkListUsers() {
        assertEquals(3, myFirestoreRepository.getFirestoreWorkmates().getValue().size());
    }

    @Test
    public void checkReload() {
        assertTrue(mySharedRepository.getReload().getValue());
    }

    @Test
    public void checkPlace() {
        assertEquals("fakePlaceId", myPlaceAutocompleteRepository.getPlaces().getValue().getPredictions().get(0).getPlaceId());
    }


    @Test
    public void checkSizeOfRestaurantToShow_3() {
        //start check
        assertEquals(3, myView.getMyRestaurantList().size());
    }

    @Test
    public void checkIfTheFirstHaveAWorkmate() {
        assertEquals("1",myView.getMyRestaurantList().get(0).myNumberOfWorkmates);
    }

}