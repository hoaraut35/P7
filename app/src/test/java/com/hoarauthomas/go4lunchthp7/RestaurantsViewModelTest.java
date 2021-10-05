package com.hoarauthomas.go4lunchthp7;

import static org.junit.Assert.assertEquals;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.hoarauthomas.go4lunchthp7.repository.FirestoreRepository;
import com.hoarauthomas.go4lunchthp7.repository.PlaceAutocompleteRepository;
import com.hoarauthomas.go4lunchthp7.repository.PositionRepository;
import com.hoarauthomas.go4lunchthp7.repository.RestaurantsRepository;
import com.hoarauthomas.go4lunchthp7.repository.SharedRepository;
import com.hoarauthomas.go4lunchthp7.ui.restaurant.ViewModelRestaurant;
import com.hoarauthomas.go4lunchthp7.ui.restaurant.ViewStateRestaurant;
import com.hoarauthomas.go4lunchthp7.ui.workmates.ViewModelWorkMates;
import com.hoarauthomas.go4lunchthp7.ui.workmates.ViewStateWorkMates;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class RestaurantsViewModelTest {

    //to run every tasks synchronously
    @Rule
    public final InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

    //mocked repositories
    @Mock
    PositionRepository myPositionRepository;
    @Mock
    RestaurantsRepository myRestaurantRepository;
    @Mock
    FirestoreRepository myFirestoreRepository;
    @Mock
    SharedRepository mySharedRepository;
    @Mock
    PlaceAutocompleteRepository placeAutocompleteRepository;

    @Before
    public void setup() {

        //init mocks ...
        MockitoAnnotations.initMocks(this);//JUnit4

        //make fake data here for position here...

        //make fake data here for restaurant ...

        //make fake data here for Firestore ...

        //make fake data here for Shared ...

        //make fake data here for PlaceAutocomplete ...

        //mock methods for repositories here...
     //  Mockito.when(myFirestoreRepository.getFirestoreWorkmates()).thenReturn(myMockUserList);
       // Mockito.when(myRestaurantRepository.getMyRestaurantsList()).thenReturn(myMockRestaurantList);

    }

    @Test
    public void checkIfWorkmateVMGetUserUI() throws InterruptedException {

        //init viewmodel before test
        ViewModelRestaurant myRestaurantViewModel = new ViewModelRestaurant(
                myPositionRepository,
                myRestaurantRepository,
                myFirestoreRepository,
                mySharedRepository,
                placeAutocompleteRepository);

        //when
        ViewStateRestaurant myView = LiveDataTestUtils.getOrAwaitValue(myRestaurantViewModel.getRestaurantsViewUI());

        //Then (we must to find 3 workmates )
        assertEquals(3, myView.getMyRestaurantList().size());
        assertEquals("Pizza momo", myView.getMyRestaurantList().get(0).getPlaceId());

    }

}
