package com.hoarauthomas.go4lunchthp7;

import static org.junit.Assert.assertEquals;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.hoarauthomas.go4lunchthp7.model.FirestoreUser;
import com.hoarauthomas.go4lunchthp7.model.NearbySearch.RestaurantPojo;
import com.hoarauthomas.go4lunchthp7.model.WorkmatesPojoForUI;
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
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class WorkmatesViewModelTest {

    //to run synchronously
    @Rule
    public final InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

    @Mock
    RestaurantsRepository myRestaurantRepository;

    @Mock
    FirestoreRepository myFirestoreRepository;

    private ViewModelWorkMates myViewModel;

    //this line ? to observe
    Observer<ViewStateWorkMates> myViewState;
    //or this line ? to observe
    MutableLiveData<ViewStateWorkMates> myViewStateWorkMatesMediator = new MutableLiveData<>();

    @Before
    public void setup() {

        //init mocks ...
        MockitoAnnotations.initMocks(this);//JUNIT4

        //make fake data here for repository ...
        MutableLiveData<List<FirestoreUser>> myMockUserList = new MutableLiveData<>();
        List<FirestoreUser> myFakeUserList = new ArrayList<>();
        myFakeUserList.add(new FirestoreUser("ChIJ85WzK9HUDkgRitQb60cwf9Y", null, "9FYLr2m7csO4fxHtwa99widnQMC2", "https://lh3.googleusercontent.com/a-/AOh14Gj4nMiaSsUjHvyDgYy546bPkf8aopCNdF53F_x4hA=s96-c", "Thomas"));
        myFakeUserList.add(new FirestoreUser("ChIJ85WzK9HUDkgRitQb60cwf9YB", null, "9FYLr2m7csO4fxHtwa99widnQMCB", "https://lh3.googleusercontent.com/a-/AOh14Gj4nMiaSsUjHvyDgYy546bPkf8aopCNdF53F_x4hA=s96-c", "Samuel"));
        myFakeUserList.add(new FirestoreUser("ChIJ85WzK9HUDkgRitQb60cwf9A", null, "9FYLr2m7csO4fxHtwa99widnQMCC", "https://lh3.googleusercontent.com/a-/AOh14Gj4nMiaSsUjHvyDgYy546bPkf8aopCNdF53F_x4hA=s96-c", "JEssica"));
        myMockUserList.setValue(myFakeUserList);

        //Mock data for restaurant repository...
        MutableLiveData<List<RestaurantPojo>> myMockRestaurantList = new MutableLiveData<>();
        List<RestaurantPojo> myFakeRestaurantList = new ArrayList<>();
        myFakeRestaurantList.add(new RestaurantPojo("ChIJ85WzK9HUDkgRitQb60cwf9Y", "Pizza momo"));
        myFakeRestaurantList.add(new RestaurantPojo("ChIJ85WzK9HUDkgRitQb60cwf9B", "Galette Bretagne"));
        myFakeRestaurantList.add(new RestaurantPojo("ChIJ85WzK9HUDkgRitQb60cwf9C", "Italien"));
        myMockRestaurantList.setValue(myFakeRestaurantList);

        //mock methods for repositories here...
        Mockito.when(myFirestoreRepository.getFirestoreWorkmates()).thenReturn(myMockUserList);
        Mockito.when(myRestaurantRepository.getMyRestaurantsList()).thenReturn(myMockRestaurantList);

        //mock return of mediatorlivedata with doReturn ? do that ? for combine method in viewmodel
        List<WorkmatesPojoForUI> myFakeList = new ArrayList<>();
        myFakeList.add(new WorkmatesPojoForUI("http://111.com", "Thomas", "Restaurant1", "1"));
        myFakeList.add(new WorkmatesPojoForUI("http://222.com", "Samuel", "Restaurant2", "2"));
        myFakeList.add(new WorkmatesPojoForUI("http://333.com", "Jessica", "Restaurant3", "3"));
        myViewStateWorkMatesMediator.setValue(new ViewStateWorkMates(myFakeList));

        //init viewmodel before test
        myViewModel = new ViewModelWorkMates(myRestaurantRepository, myFirestoreRepository);

        //don't work
        //Mockito.when(myViewModel.getMediatorLiveData()).thenReturn(myViewStateWorkMatesMediator);
        //Mockito.doReturn(myViewModel.getMediatorLiveData()).when(myViewStateWorkMatesMediator);

        //observe viewstate ?
       // myViewModel.getMediatorLiveData().observeForever(myViewState);
    }

    @Test
    public void workmateViewModel() throws InterruptedException {

        //when
        ViewStateWorkMates myView = LiveDataTestUtils.getOrAwaitValue(myViewModel.getMediatorLiveData());

        //Then (we must to find 3
        assertEquals(3, myView.getMySpecialWorkMAtes().size());

    }


}



