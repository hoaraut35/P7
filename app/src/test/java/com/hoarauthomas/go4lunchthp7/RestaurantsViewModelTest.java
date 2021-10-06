package com.hoarauthomas.go4lunchthp7;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.maps.model.LatLng;
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
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class RestaurantsViewModelTest {

    //to run every tasks synchronously
    @Rule
    public final InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

    //mocked repositories
    PositionRepository myPositionRepository = mock(PositionRepository.class);

    //RestaurantsRepository myRestaurantRepository;
    //= mock(RestaurantsRepository.class);
    RestaurantsRepository myRestaurantRepository = mock(RestaurantsRepository.class);

    FirestoreRepository myFirestoreRepository = mock(FirestoreRepository.class);
    @Mock
    SharedRepository mySharedRepository = new SharedRepository();

    PlaceAutocompleteRepository placeAutocompleteRepository = mock(PlaceAutocompleteRepository.class);


    LatLng myFakePositionLatLng;

    @Before
    public void setup() {

        //ok
        myFakePositionLatLng = new LatLng(48.0956, -1.3718);

        //mock method for viewmodel
        //ok
        Mockito.when(myPositionRepository.getLocationLatLgnLiveData()).thenReturn(new MutableLiveData<>(myFakePositionLatLng));
        Mockito.when(myRestaurantRepository.getMyRestaurantsList()).thenReturn(new MutableLiveData<>(LocalDataForTest.getFakeListFromRestaurantRepositoryForTest()));
        Mockito.when(myFirestoreRepository.getFirestoreWorkmates()).thenReturn(new MutableLiveData<>(LocalDataForTest.getFakeListFromFirestoreRepositoryForTest()));
//        Mockito.when(mySharedRepository.getReload()).thenReturn(LocalDataForTest.getReloadMapForTest());
        Mockito.when(placeAutocompleteRepository.getPlaces()).thenReturn(LocalDataForTest.getPlacesForTest());


        //Mockito.doReturn(new MutableLiveData<>(LocalDataForTest.getFakeListFromRestaurantRepositoryForTest())).when(myRestaurantRepository.getMyRestaurantsList());


        //set combine


    }

    @Test
    public void checkFakePosition() throws InterruptedException {
        //check if fake position is good
        assertEquals(48.0956, myFakePositionLatLng.latitude, 0.1);
        assertEquals(-1.3718, myFakePositionLatLng.longitude, 0.1);
    }

    @Test
    public void checkFakeRestaurantsList() {
        assertEquals(3, myRestaurantRepository.getMyRestaurantsList().getValue().size());
    }

    @Test
    public void checkFakeUserList() {
        assertEquals(3, myFirestoreRepository.getFirestoreWorkmates().getValue().size());
    }

    @Test
    public void checkFakeRelaodMode() {
        assertFalse(mySharedRepository.getReload().getValue());
    }

    @Test
    public void checkViewState() throws InterruptedException {

        ViewModelRestaurant myRestaurantViewModel = new ViewModelRestaurant(
                myPositionRepository,
                myRestaurantRepository,
                myFirestoreRepository,
                mySharedRepository,
                placeAutocompleteRepository);

        ViewStateRestaurant myView = LiveDataTestUtils.getOrAwaitValue(myRestaurantViewModel.getRestaurantsViewUI());

        assertEquals("testname", myView.getMyRestaurantList().get(0).getName());

    }
}
