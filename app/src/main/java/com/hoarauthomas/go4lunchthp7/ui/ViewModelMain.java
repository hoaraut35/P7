package com.hoarauthomas.go4lunchthp7.ui;

import android.annotation.SuppressLint;
import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;
import com.hoarauthomas.go4lunchthp7.model.FirestoreUser;
import com.hoarauthomas.go4lunchthp7.permissions.PermissionChecker;
import com.hoarauthomas.go4lunchthp7.repository.AlarmRepository;
import com.hoarauthomas.go4lunchthp7.repository.FirebaseAuthRepository;
import com.hoarauthomas.go4lunchthp7.repository.FirestoreRepository;
import com.hoarauthomas.go4lunchthp7.repository.PlaceAutocompleteRepository;
import com.hoarauthomas.go4lunchthp7.repository.PositionRepository;
import com.hoarauthomas.go4lunchthp7.repository.SharedRepository;

public class ViewModelMain extends ViewModel {

    //repository...
    private final FirebaseAuthRepository myFirebaseAuthRepoVM;
    private final FirestoreRepository myFirestoreRepo;
    private final PlaceAutocompleteRepository myPlaceAutocompleteRepoVM;
    private final PositionRepository myPositionRepoVM;

    private final AlarmRepository myAlarmRepoVM;
    private final PermissionChecker myPermission;

    //shared between multiple view
    private final SharedRepository mySharedRepoVM;

    //livedata...
    private final LiveData<FirebaseUser> myUserLiveData;
    private final LiveData<FirestoreUser> myActualUserFromFirestore;

    public ViewModelMain(
            PermissionChecker myPermission,
            FirebaseAuthRepository firebaseAuthRepository,
            FirestoreRepository firestoreRepository,
            PlaceAutocompleteRepository placeAutocompleteRepository,
            PositionRepository myPositionRepoVM,
            AlarmRepository myAlarmRepoVM,
            SharedRepository mySharedRepoVM
    ) {

        this.myPermission = myPermission;

        //get data from Auth repository...
        this.myFirebaseAuthRepoVM = firebaseAuthRepository;
        myUserLiveData = myFirebaseAuthRepoVM.getFirebaseAuthUserFromRepo();

        //get data from workmates repository...
        this.myFirestoreRepo = firestoreRepository;

        myActualUserFromFirestore = myFirestoreRepo.getPublicUSerFirestore();

        //get data from place autocomplete repository...
        this.myPlaceAutocompleteRepoVM = placeAutocompleteRepository;
        //get position for autocomplete request
        this.myPositionRepoVM = myPositionRepoVM;
        //get alarm data
        this.myAlarmRepoVM = myAlarmRepoVM;
        this.mySharedRepoVM = mySharedRepoVM;

    }

    public LiveData<Boolean> getLoginState() {
        return myFirebaseAuthRepoVM.getFirebaseAuthUserStateFromRepo();
    }

    public void LogOut() {
        myFirebaseAuthRepoVM.logOut();
    }

    //Create user to Firestore
    public void createUser() {
        this.myFirestoreRepo.createUser();
    }

    public void getResultAutocomplete(String query, Location location) {
        myPlaceAutocompleteRepoVM.getAutocompleteDataToPlaceDetailList(query, location);
        //origin
        //myPlaceAutocompleteRepoVM.getPlaceAutocompleteSingle(query, location);
    }

    public Location getMyPosition() {
        return myPositionRepoVM.getLocationLiveData().getValue();
    }

    public void setNotification(String uid, Boolean state) {
        if (state) {
            myAlarmRepoVM.setAlarm(uid);

        } else {
            myAlarmRepoVM.removeAlarm();
        }
    }

    public void setUser() {
        myFirestoreRepo.setupListeners();
    }

    public FirebaseUser getUser() {
        return myUserLiveData.getValue();
    }

    public void setZoom(int myZoom) {
        mySharedRepoVM.setZoom(myZoom);
    }

    public void reloadDataAfterQuery(Boolean bool) {
        mySharedRepoVM.setReloadMap(bool);
    }

    public FirebaseUser getMyUserFromFirestore() {
        return myFirestoreRepo.getCurrentUser();
    }

    public LiveData<FirestoreUser> getActualUserData() {
        return myActualUserFromFirestore;
    }

    @SuppressLint("MissingPermission")
    public void refresh() {
        // No GPS permission
        if (!myPermission.hasLocationPermission()) {
            myPositionRepoVM.stopLocationRequest();
        } else {
            myPositionRepoVM.startLocationRequest();
        }
    }
}