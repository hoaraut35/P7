package com.hoarauthomas.go4lunchthp7;

import static org.junit.Assert.assertEquals;

import com.hoarauthomas.go4lunchthp7.permissions.PermissionChecker;
import com.hoarauthomas.go4lunchthp7.repository.AlarmRepository;
import com.hoarauthomas.go4lunchthp7.repository.FirebaseAuthRepository;
import com.hoarauthomas.go4lunchthp7.repository.FirestoreRepository;
import com.hoarauthomas.go4lunchthp7.repository.PlaceAutocompleteRepository;
import com.hoarauthomas.go4lunchthp7.repository.PositionRepository;
import com.hoarauthomas.go4lunchthp7.repository.RestaurantsRepository;
import com.hoarauthomas.go4lunchthp7.repository.SharedRepository;
import com.hoarauthomas.go4lunchthp7.ui.map.ViewModelMap;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */



@RunWith(MockitoJUnitRunner.class)
public class MapViewModelTest {

    private ViewModelMap myViewModelMap;

    @Mock
    private PermissionChecker myPermission;
    @Mock
    private PositionRepository myPositionRepository;
    @Mock
    private RestaurantsRepository myRestaurantRepository;
    @Mock
    private FirestoreRepository myFirestoreRepository;
    @Mock
    private SharedRepository mySharedRepository;
    @Mock
    private PlaceAutocompleteRepository myPlaceAutocompleteRepository;

    @Before
    public void setup()
    {
      //  myViewModelMap = new ViewModelMap(myPermission, myPositionRepository,myRestaurantRepository,myFirestoreRepository,mySharedRepository,myPlaceAutocompleteRepository);
    }

    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }


}