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
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class WorkmatesViewModelTest {

    //to run every tasks synchronously
    @Rule
    public final InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

    //mock repositories here...
    FirestoreRepository myFirestoreRepository = mock(FirestoreRepository.class);
    RestaurantsRepository myRestaurantRepository = mock(RestaurantsRepository.class);

    //viewModel here...
    ViewModelWorkMates myWorkmatesViewModel;

    //viewState
    ViewStateWorkMates myView;

    @Before
    public void setup() throws InterruptedException {
        //mock methods for repositories here...
        Mockito.when(myFirestoreRepository.getFirestoreWorkmates()).thenReturn(new MutableLiveData<>(LocalDataForTest.getFakeListFromFirestoreRepositoryForTest()));
        Mockito.when(myRestaurantRepository.getMyRestaurantsList()).thenReturn(new MutableLiveData<>(LocalDataForTest.getFakeListFromRestaurantRepositoryForTest()));

        //init viewModel before test
        ViewModelWorkMates myWorkmatesViewModel = new ViewModelWorkMates(myRestaurantRepository, myFirestoreRepository);

        //when
      myView = LiveDataTestUtils.getOrAwaitValue(myWorkmatesViewModel.getMediatorLiveData());

    }

    @Test
    public void checkSizeOfWorkmates() throws InterruptedException {
        //Then (we must to find 3 workmates )
        assertEquals(3, myView.getMySpecialWorkMAtes().size());
    }

    @Test
    public void checkUserRestaurant1(){
        assertEquals("Restaurant1", myView.getMySpecialWorkMAtes().get(0).getNameOfRestaurant());
    }

    @Test
    public void checkUserRestaurant2(){
        assertEquals("Restaurant2", myView.getMySpecialWorkMAtes().get(1).getNameOfRestaurant());
    }

    @Test
    public void checkUserRestaurant3(){
        assertEquals("Restaurant3", myView.getMySpecialWorkMAtes().get(2).getNameOfRestaurant());
    }

}