package com.hoarauthomas.go4lunchthp7.ui;

import android.location.Location;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.hoarauthomas.go4lunchthp7.PlaceAutocomplete;
import com.hoarauthomas.go4lunchthp7.Prediction;
import com.hoarauthomas.go4lunchthp7.model.FirestoreUser;
import com.hoarauthomas.go4lunchthp7.repository.AlarmRepository;
import com.hoarauthomas.go4lunchthp7.repository.FirebaseAuthRepository;
import com.hoarauthomas.go4lunchthp7.repository.FirestoreRepository;
import com.hoarauthomas.go4lunchthp7.repository.PlaceAutocompleteRepository;
import com.hoarauthomas.go4lunchthp7.repository.PositionRepository;
import com.hoarauthomas.go4lunchthp7.repository.SharedRepository;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;


public class ViewModelMain extends ViewModel {

    //repository...
    private FirebaseAuthRepository myFirebaseAuthRepoVM;
    private FirestoreRepository myWorkMatesRepoVM;
    private PlaceAutocompleteRepository myPlaceAutocompleteRepoVM;
    private PositionRepository myPositionRepoVM;
    private AlarmRepository myAlarmRepoVM;

    //shared beetween multiple view
    private SharedRepository mySharedRepoVM;

    //livedata...
    private LiveData<FirebaseUser> myUserLiveData;
    private LiveData<Boolean> myUserStateNew;
    private LiveData<List<FirestoreUser>> myWorkMatesListLiveData = new MutableLiveData<>();
    private MutableLiveData<String> myUserRestaurantId = new MutableLiveData<>();
    private MutableLiveData<com.hoarauthomas.go4lunchthp7.PlaceAutocomplete> myPlaceAutocompleteList = new MutableLiveData<>();

    public MutableLiveData<String> getMyUserRestaurantId() {
        return myUserRestaurantId;
    }

    //to update ViewState...
    MediatorLiveData<ViewMainState> myAppMapMediator = new MediatorLiveData<>();

    /**
     * constructor called by viewmodelfactory
     *
     * @param firebaseAuthRepository
     * @param firestoreRepository
     * @param placeAutocompleteRepository
     * @param myPositionRepoVM
     * @param myAlarmRepoVM
     * @param mySharedRepoVM
     */
    public ViewModelMain(
            FirebaseAuthRepository firebaseAuthRepository,
            FirestoreRepository firestoreRepository,
            PlaceAutocompleteRepository placeAutocompleteRepository,
            PositionRepository myPositionRepoVM,
            AlarmRepository myAlarmRepoVM,
            SharedRepository mySharedRepoVM) {

        //get data from Auth repository...
        this.myFirebaseAuthRepoVM = firebaseAuthRepository;
        myUserLiveData = myFirebaseAuthRepoVM.getFirebaseAuthUserFromRepo();
        myUserStateNew = myFirebaseAuthRepoVM.getFirebaseAuthUserStateFromRepo();

        //get data from workmates repository...
        this.myWorkMatesRepoVM = firestoreRepository;
        myWorkMatesListLiveData = myWorkMatesRepoVM.getFirestoreWorkmates();
        LiveData<FirestoreUser> myWorkmate = myWorkMatesRepoVM.getWorkmateFromRepo();

        //get data from place autocomplete repository...
        this.myPlaceAutocompleteRepoVM = placeAutocompleteRepository;
        //  this.myPlaceAutocompleteList = myPlaceAutocompleteRepoVM.getMyPlaceAutocompleteListForVM();

      //  this.myPlaceAutocompleteList = myPlaceAutocompleteRepoVM.getPlaces();

        //get position for autocomplete request
        this.myPositionRepoVM = myPositionRepoVM;

        //get alarm data
        this.myAlarmRepoVM = myAlarmRepoVM;

        this.mySharedRepoVM = mySharedRepoVM;

        //add source on onPlacesAutocomplete
        myAppMapMediator.addSource(myPlaceAutocompleteList, placeAutocomplete -> {
            if (myPlaceAutocompleteList == null) return;
            logicWork(myUserLiveData.getValue(),
                    myWorkMatesListLiveData.getValue(),
                    myUserStateNew.getValue(),
                    myWorkmate.getValue(),
                    placeAutocomplete);
        });


        //add source
        myAppMapMediator.addSource(myUserLiveData, firebaseUser -> {
            if (firebaseUser != null) {
                if (!firebaseUser.getUid().isEmpty()) {
                    logicWork(firebaseUser, myWorkMatesListLiveData.getValue(),
                            myUserStateNew.getValue(),
                            myWorkmate.getValue(),
                            myPlaceAutocompleteList.getValue());
                }
            }
        });

        //add source
        myAppMapMediator.addSource(myWorkMatesListLiveData, users -> {
            if (users == null) return;
            if (myWorkMatesListLiveData != null) {
                if (!myWorkMatesListLiveData.getValue().isEmpty()) {
                    logicWork(myUserLiveData.getValue(),
                            users,
                            myUserStateNew.getValue(),
                            myWorkmate.getValue(),
                            myPlaceAutocompleteList.getValue());
                }
            }
        });

        myAppMapMediator.addSource(myWorkmate, firestoreUser -> {
            if (firestoreUser == null) return;
            logicWork(myUserLiveData.getValue(),
                    myWorkMatesListLiveData.getValue(),
                    myUserStateNew.getValue(),
                    firestoreUser,
                    myPlaceAutocompleteList.getValue());
        });

    }

    /**
     * logic work here
     *
     * @param myUser
     * @param workmates
     * @param bool
     */
    // Logic work
    private void logicWork(
            @Nullable FirebaseUser myUser,
            @Nullable List<FirestoreUser> workmates,
            Boolean bool, FirestoreUser myFirestoreUserData,
            PlaceAutocomplete myPlacesAuto) {

        createUser();

        if (myUser != null && workmates != null && myFirestoreUserData != null) {

            //placeautocomplete à traduire en placedetails

            if (myPlacesAuto != null) {


                List<String> myPlacesIdList = new ArrayList<>();

                for (int z = 0; z < myPlacesAuto.getPredictions().size(); z++) {
                    myPlacesIdList.add(myPlacesAuto.getPredictions().get(z).getPlaceId());

               //     Log.i("[SEARCH]", "predic" + myPlacesAuto.getPredictions().get(z).getDescription().toString() + myPlacesAuto.getPredictions().get(z).getPlaceId());
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
                    myAppMapMediator.setValue(new ViewMainState(true, "pas de restau", myUser));
                }
                myAppMapMediator.setValue(new ViewMainState(true, "liste restaur non chargée", myUser));


            }


        }
    }

    public void setMyUserRestaurantId(String myUserRestaurantId) {
        this.myUserRestaurantId.setValue(myUserRestaurantId);
    }

    public LiveData<Boolean> getLoginState() {
        return myFirebaseAuthRepoVM.getFirebaseAuthUserStateFromRepo();
    }


    public LiveData<Boolean> getMyLogin() {
        return myUserStateNew;
    }

    public void LogOut() {
        myFirebaseAuthRepoVM.logOut();
    }

    public LiveData<String> getMyUserRestaurant() {
        return myUserRestaurantId;
    }

    //Create user to Firestore
    public void createUser() {
        this.myWorkMatesRepoVM.createUser();
    }

    //to publish mediatorlivedata to mainactivity
    public LiveData<ViewMainState> getMediatorLiveData() {
        return myAppMapMediator;
    }

    public void getResultAutocomplete(String query, Location location) {
        // myPlaceAutocompleteRepoVM.getPlaceAutocomplete(query,location);
        myPlaceAutocompleteRepoVM.getPlaceAutocompleteSingle(query, location);
        //return myPlaceAutocompleteList;
    }

    public Location getMyPosition() {
        return myPositionRepoVM.getLocationLiveData().getValue();
    }

    //not used
    public void setNotification() {
        myAlarmRepoVM.setAlarm();
    }

    /**
     * To enable or disable notification
     *
     * @param state
     */
    public void setNotification(Boolean state) {
        myAlarmRepoVM.setNotification(state);
    }

    /**
     * Update the place prediction in repository shared
     *
     * @param place
     */
/*   public void setPredictionFromUIWithPlaceId(Prediction place) {
        mySharedRepoVM.setMyPlaceIdFromAutocomplete(place);
    }

 */

    public void updataApp() {
        myWorkMatesRepoVM.getFirestoreWorkmates();
        //WorkMatesListFromRepo();
    }

 /*   public Task<DocumentSnapshot> requestUserForestoreFromVM(String uid) {
        return myWorkMatesRepoVM.getUserFirestoreFromRepo(uid);
    }

  */

    public void updateUserSystem() {
        //    myFirebaseAuthRepoVM.updateUser();
    }

    public void setUser() {
        myWorkMatesRepoVM.setupListeners();
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

    public void reloadDataAfterQuery() {

    }
}