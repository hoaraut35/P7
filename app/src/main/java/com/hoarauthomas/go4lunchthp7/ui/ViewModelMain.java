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
    //private MutableLiveData<FirestoreUser> myWorkmate = new MutableLiveData<>();
    //private final LiveData<Boolean> myUserStateNew;
    //private final LiveData<List<FirestoreUser>> myWorkMatesListLiveData;
    //private final MutableLiveData<String> myUserRestaurantId = new MutableLiveData<>();

    // private final MutableLiveData<com.hoarauthomas.go4lunchthp7.PlaceAutocomplete> myPlaceAutocompleteList = new MutableLiveData<>();

    private final LiveData<FirestoreUser> myActualUserFromFirestore;

    //to update ViewState...
    //MediatorLiveData<ViewMainState> myAppMapMediator = new MediatorLiveData<>();

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
        //myUserStateNew = myFirebaseAuthRepoVM.getFirebaseAuthUserStateFromRepo();

        //get data from workmates repository...
        this.myFirestoreRepo = firestoreRepository;
        //myWorkMatesListLiveData = myFirestoreRepo.getFirestoreWorkmates();
        //myWorkmate = myFirestoreRepo.getWorkmateFromRepo();

        myActualUserFromFirestore = myFirestoreRepo.getPublicUSerFirestore();

        //get data from place autocomplete repository...
        this.myPlaceAutocompleteRepoVM = placeAutocompleteRepository;
        //get position for autocomplete request
        this.myPositionRepoVM = myPositionRepoVM;
        //get alarm data
        this.myAlarmRepoVM = myAlarmRepoVM;
        this.mySharedRepoVM = mySharedRepoVM;

        //add source on onPlacesAutocomplete
       /* myAppMapMediator.addSource(myPlaceAutocompleteList, placeAutocomplete -> {
            if (myPlaceAutocompleteList == null) return;
            logicWork(myUserLiveData.getValue(),
                    myWorkMatesListLiveData.getValue(),
                    myUserStateNew.getValue(),
                    myWorkmate.getValue(),
                    placeAutocomplete);
        });

        */


        //add source
     /*   myAppMapMediator.addSource(myUserLiveData, firebaseUser -> {
            if (firebaseUser != null) {
                if (!firebaseUser.getUid().isEmpty()) {
                    logicWork(firebaseUser, myWorkMatesListLiveData.getValue(),
                            myUserStateNew.getValue(),
                            myWorkmate.getValue(),
                            myPlaceAutocompleteList.getValue());
                }
            }
        });

      */

        //add source
        /*myAppMapMediator.addSource(myWorkMatesListLiveData, users -> {
            if (users == null) return;
            if (myWorkMatesListLiveData != null) {
                if (!Objects.requireNonNull(myWorkMatesListLiveData.getValue()).isEmpty()) {
                    logicWork(myUserLiveData.getValue(),
                            users,
                            myUserStateNew.getValue(),
                            myWorkmate.getValue(),
                            myPlaceAutocompleteList.getValue());
                }
            }
        });

         */

        /*myAppMapMediator.addSource(myWorkmate, firestoreUser -> {
            if (firestoreUser == null) return;
            logicWork(myUserLiveData.getValue(),
                    myWorkMatesListLiveData.getValue(),
                    myUserStateNew.getValue(),
                    firestoreUser,
                    myPlaceAutocompleteList.getValue());
        });

         */

    }

    // Logic work
    /*private void logicWork(
            @Nullable FirebaseUser myUser,
            @Nullable List<FirestoreUser> workmates,
            @Nullable Boolean bool,
            FirestoreUser myFirestoreUserData,
            PlaceAutocomplete myPlacesAuto) {

        createUser();

        if (myUser != null && workmates != null && myFirestoreUserData != null) {




            //get data here for prepare alarm
            if (myFirestoreUserData.getFavoriteRestaurant() != null) {
                Log.i("[ALARM]", "User name " + myFirestoreUserData.getUsername());
            }


            Log.i("[NOTIFICATION]", "My user favorite restaurant : " + myFirestoreUserData.getFavoriteRestaurant());


            if (myPlacesAuto != null) {


                List<String> myPlacesIdList = new ArrayList<>();

                for (int z = 0; z < myPlacesAuto.getPredictions().size(); z++) {
                    myPlacesIdList.add(myPlacesAuto.getPredictions().get(z).getPlaceId());

                    //     Log.i("[SEARCH]", "prediction" + myPlacesAuto.getPredictions().get(z).getDescription().toString() + myPlacesAuto.getPredictions().get(z).getPlaceId());
                }

                mySharedRepoVM.setMyRestaurantList(myPlacesIdList);

            } else {

                if (!myUser.getUid().isEmpty()) {

                    if (!myFirestoreUserData.getFavoriteRestaurant().isEmpty()) {
                        myUserRestaurantId.setValue(myFirestoreUserData.getFavoriteRestaurant());
                    } else {
                        myUserRestaurantId.setValue(null);
                    }

                } else {
                    myAppMapMediator.setValue(new ViewMainState(true, "pas de restaurant", myUser));
                }
                myAppMapMediator.setValue(new ViewMainState(true, "list  non chargée", myUser));

            }
        }
    }

     */

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


//  public LiveData<ViewMainState> getMediatorLiveData() {
//        return myAppMapMediator;
//    }


    public void getResultAutocomplete(String query, Location location) {
        myPlaceAutocompleteRepoVM.getPlaceAutocompleteSingle(query, location);
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

    public void stopPositionListener() {
        myPositionRepoVM.stopLocationRequest();
    }

    public void startPositionListener() {
        myPositionRepoVM.startLocationRequest();
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