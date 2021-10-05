package com.hoarauthomas.go4lunchthp7;

import static org.mockito.Mockito.mock;

import android.location.Location;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.hoarauthomas.go4lunchthp7.repository.FirestoreRepository;
import com.hoarauthomas.go4lunchthp7.repository.PlaceAutocompleteRepository;
import com.hoarauthomas.go4lunchthp7.repository.PositionRepository;
import com.hoarauthomas.go4lunchthp7.repository.RestaurantsRepository;
import com.hoarauthomas.go4lunchthp7.repository.SharedRepository;
import com.hoarauthomas.go4lunchthp7.ui.restaurant.ViewModelRestaurant;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class RestaurantsViewModelTest {

    //to run every tasks synchronously
    @Rule
    public final InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

    //mocked repositories
    PositionRepository myPositionRepository = mock(PositionRepository.class);
    RestaurantsRepository myRestaurantRepository = mock(RestaurantsRepository.class);
    FirestoreRepository myFirestoreRepository = mock(FirestoreRepository.class);
    SharedRepository mySharedRepository = mock(SharedRepository.class);
    PlaceAutocompleteRepository placeAutocompleteRepository = mock(PlaceAutocompleteRepository.class);

    Location myLocationClass = mock(Location.class);

    MutableLiveData<Location> myMutLoc = new MutableLiveData<>();

    @Before
    public void setup() {

        Location myDummyLocation = new Location("");
        myDummyLocation.setLongitude(4545d);
        myDummyLocation.setLatitude(55757d);

        Mockito.doCallRealMethod().when(this.myLocationClass).getLatitude();
        Mockito.doCallRealMethod().when(this.myLocationClass).getLongitude();

        this.myLocationClass.setLatitude(LocalDataForTest.getLatitudeFromTest());
        this.myLocationClass.setLongitude(LocalDataForTest.getLongitudeFromTest());


        //location = new Location("");
        //    location.setLongitude(45454);
        // location.setLatitude(454);
        //  myMutLoc.setValue(location);

        Mockito.when(myPositionRepository.getLocationLiveData()).thenReturn(myMutLoc);

        //ok
        Mockito.when(myRestaurantRepository.getMyRestaurantsList()).thenReturn(LocalDataForTest.getFakeListFromRestaurantRepositoryForTest());
        //ok
        Mockito.when(myFirestoreRepository.getFirestoreWorkmates()).thenReturn(LocalDataForTest.getFakeListFromFirestoreRepositoryForTest());
        //ok
        Mockito.when(mySharedRepository.getReload()).thenReturn(LocalDataForTest.getReloadMapForTest());
        //non ok
        Mockito.when(placeAutocompleteRepository.getPlaces()).thenReturn(LocalDataForTest.getPlacesForTest());

        /*//get data from repositories


        LiveData<Boolean> reloadMap = mySharedRepository.getReload();

         */
    }

    @Test
    public void checkIfRestaurantsUI() throws InterruptedException {


        //when
        ViewModelRestaurant myRestaurantViewModel = new ViewModelRestaurant(myPositionRepository, myRestaurantRepository, myFirestoreRepository, mySharedRepository, placeAutocompleteRepository);
        // ViewStateRestaurant myView = LiveDataTestUtils.getOrAwaitValue(myRestaurantViewModel.getRestaurantsViewUI());

        //Then (we must to find 3 workmates )
        // assertEquals(3, myView.getMyRestaurantList().size());
        //assertEquals("Pizza momo", myView.getMyRestaurantList().get(0).getPlaceId());

    }

}
