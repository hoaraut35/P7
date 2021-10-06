package com.hoarauthomas.go4lunchthp7;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.hoarauthomas.go4lunchthp7.repository.FirestoreRepository;
import com.hoarauthomas.go4lunchthp7.repository.RestaurantsRepository;
import com.hoarauthomas.go4lunchthp7.ui.workmates.ViewModelWorkMates;
import com.hoarauthomas.go4lunchthp7.ui.workmates.ViewStateWorkMates;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class WorkmatesViewModelTestGood {

    //to run every tasks synchronously
    @Rule
    public final InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

    @Mock
    FirestoreRepository myFirestoreRepository;

    RestaurantsRepository myRestaurantRepository = mock(RestaurantsRepository.class);

    @Before
    public void setup() {

        //mock methods for repositories here...
        Mockito.when(myFirestoreRepository.getFirestoreWorkmates()).thenReturn(new MutableLiveData<>(LocalDataForTest.getFakeListFromFirestoreRepositoryForTest()));
        Mockito.when(myRestaurantRepository.getMyRestaurantsList()).thenReturn(new MutableLiveData<>(LocalDataForTest.getFakeListFromRestaurantRepositoryForTest()));

    }

    @Test
    public void checkIfWorkmateVMGetUserUI() throws InterruptedException {

        //init viewmodel before test
        ViewModelWorkMates myWorkmatesViewModel = new ViewModelWorkMates(myRestaurantRepository, myFirestoreRepository);

        //when
        ViewStateWorkMates myView = LiveDataTestUtils.getOrAwaitValue(myWorkmatesViewModel.getMediatorLiveData());

        //Then (we must to find 3 workmates )
        assertEquals(3, myView.getMySpecialWorkMAtes().size());
        assertEquals("Pizza momo",  myView.getMySpecialWorkMAtes().get(0).getNameOfRestaurant());

    }

}