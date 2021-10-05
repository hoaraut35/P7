package com.hoarauthomas.go4lunchthp7;

import static org.junit.Assert.assertEquals;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.hoarauthomas.go4lunchthp7.model.FirestoreUser;
import com.hoarauthomas.go4lunchthp7.model.NearbySearch.RestaurantPojo;
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
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class WorkmatesViewModelTest {

    //to run every tasks synchronously
    @Rule
    public final InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

    @Mock
    RestaurantsRepository myRestaurantRepository;
    @Mock
    FirestoreRepository myFirestoreRepository;

    List<FirestoreUser> myFakeUserList = new ArrayList<>();
    List<RestaurantPojo> myFakeRestaurantList = new ArrayList<>();
    MutableLiveData<List<FirestoreUser>> myMockUserList = new MutableLiveData<>();
    MutableLiveData<List<RestaurantPojo>> myMockRestaurantList = new MutableLiveData<>();

    @Before
    public void setup() {

        //init mocks ...
        MockitoAnnotations.initMocks(this);//JUNIT4

        //mock methods for repositories here...
        Mockito.when(myFirestoreRepository.getFirestoreWorkmates()).thenReturn(LocalDataForTest.getFakeListFromFirestoreRepositoryForTest());
        Mockito.when(myRestaurantRepository.getMyRestaurantsList()).thenReturn(LocalDataForTest.getFakeListFromRestaurantRepositoryForTest());

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



