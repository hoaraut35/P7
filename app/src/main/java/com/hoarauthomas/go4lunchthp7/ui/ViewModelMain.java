package com.hoarauthomas.go4lunchthp7.ui;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.hoarauthomas.go4lunchthp7.PlaceAutocomplete;
import com.hoarauthomas.go4lunchthp7.Prediction;
import com.hoarauthomas.go4lunchthp7.SingleLiveEvent;
import com.hoarauthomas.go4lunchthp7.repository.AlarmRepository;
import com.hoarauthomas.go4lunchthp7.repository.FirebaseAuthentificationRepository;
import com.hoarauthomas.go4lunchthp7.repository.FirestoreRepository;
import com.hoarauthomas.go4lunchthp7.repository.FirestoreUser;
import com.hoarauthomas.go4lunchthp7.repository.PlaceAutocompleteRepository;
import com.hoarauthomas.go4lunchthp7.repository.PositionRepository;
import com.hoarauthomas.go4lunchthp7.repository.SharedRepository;

import java.util.List;

import javax.annotation.Nullable;


public class ViewModelMain extends ViewModel {

    //repository...
    private FirebaseAuthentificationRepository myFirebaseAuthRepoVM;
    private FirestoreRepository myWorkMatesRepoVM;
    private PlaceAutocompleteRepository myPlaceAutocompleteRepoVM;
    private PositionRepository myPositionRepoVM;
    private AlarmRepository myAlarmRepoVM;



    //shared beetween multiple view
    private SharedRepository mySharedRepoVM;

    //livedata...
    private LiveData<FirebaseUser> myUserLiveData;
    private MutableLiveData<Boolean> myUserStateNew;
    private LiveData<List<FirestoreUser>> myWorkMatesListLiveData = new MutableLiveData<>();


    private MutableLiveData<String> myUserRestaurantId = new MutableLiveData<>();


    private MutableLiveData<com.hoarauthomas.go4lunchthp7.PlaceAutocomplete> myPlaceAutocompleteList = new MutableLiveData<>();


    private SingleLiveEvent<PlaceAutocomplete> myPlaceAutoCompleteListSingleEvent = new SingleLiveEvent<>();

    //to update ViewState...
    MediatorLiveData<ViewMainState> myAppMapMediator = new MediatorLiveData<>();

    //constructor...
    public ViewModelMain(FirebaseAuthentificationRepository firebaseAuthentificationRepository, FirestoreRepository firestoreRepository, PlaceAutocompleteRepository placeAutocompleteRepository, PositionRepository myPositionRepoVM, AlarmRepository myAlarmRepoVM, SharedRepository mySharedRepoVM) {

        //get data from Auth repository...
        this.myFirebaseAuthRepoVM = firebaseAuthentificationRepository;
        myUserLiveData = myFirebaseAuthRepoVM.getUserLiveDataNew();
        myUserStateNew = myFirebaseAuthRepoVM.getLoggedOutLiveDataNew();

        //get data from workmates repository...
        this.myWorkMatesRepoVM = firestoreRepository;
        myWorkMatesListLiveData = myWorkMatesRepoVM.getFirestoreWorkmates();

        //get data from place autocomplete repository...
        this.myPlaceAutocompleteRepoVM = placeAutocompleteRepository;
        //  this.myPlaceAutocompleteList = myPlaceAutocompleteRepoVM.getMyPlaceAutocompleteListForVM();


        //get position for autocomplete request
        this.myPositionRepoVM = myPositionRepoVM;

        //get alarm data
        this.myAlarmRepoVM = myAlarmRepoVM;


        this.mySharedRepoVM = mySharedRepoVM;


        //add source
        myAppMapMediator.addSource(myUserLiveData, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if (firebaseUser != null) {
                    if (!firebaseUser.getUid().isEmpty()) {
                        logicWork(firebaseUser, myWorkMatesListLiveData.getValue(), myUserStateNew.getValue());
                    }
                }
            }
        });

        //add source
        myAppMapMediator.addSource(myWorkMatesListLiveData, new Observer<List<FirestoreUser>>() {
            @Override
            public void onChanged(List<FirestoreUser> users) {

                if (users == null) return;

                if (myWorkMatesListLiveData != null) {
                    if (!myWorkMatesListLiveData.getValue().isEmpty()) {
                        logicWork(myUserLiveData.getValue(), users, myUserStateNew.getValue());

                    }

                }
            }
        });


    }

    public MutableLiveData<String> getMyUserRestaurantId() {
        return myUserRestaurantId;
    }

    public void setMyUserRestaurantId(String myUserRestaurantId) {
        this.myUserRestaurantId.setValue(myUserRestaurantId);
    }

    // Logic work
    private void logicWork(@Nullable FirebaseUser myUser, @Nullable List<FirestoreUser> workmates, Boolean bool) {

        createUser();


        if (myUser != null && workmates != null) {


            if (!myUser.getUid().isEmpty()) {

                /*for (int i = 0; i < workmates.size(); i++) {

                    if (workmates.get(i).getMyUID().equals(myUser.getUid())) {
                   //     myUserRestaurantId.setValue(workmates.get(i).getFavoriteRestaurant());
                        Log.i("", "");
                        myUserLiveData = myFirebaseAuthRepoVM.getUserLiveDataNew();
                        break;
                    }
                }

                 */

                //getUser();
                //myAppMapMediator.setValue(new ViewMainState(true, myUserRestaurantId.getValue(), getUser()));
                // myAppMapMediator.setValue(new ViewMainState(true, "pas de restau", getUser()));
            } else {
                //getUser();
                myAppMapMediator.setValue(new ViewMainState(true, "pas de restau", myUser));

            }


            myAppMapMediator.setValue(new ViewMainState(true, "liste restaur non chargée", myUser));
            Log.i("","");

        }
    }


    //**********************************************************************************************
    // PUBLIC
    //**********************************************************************************************

    public LiveData<Boolean> getLoginState() {

        return myFirebaseAuthRepoVM.getLoginState();
    }

    public MutableLiveData<PlaceAutocomplete> getMyPlaceListForUI() {
        return myPlaceAutocompleteRepoVM.getPlaces();
    }

  /*  public SingleLiveEvent<PlaceAutocomplete> getMyPlaceListForUISingle(){

        return myPlaceAutoCompleteListSingleEvent;
    }

   */


    //addedd
    public LiveData<Boolean> getMyLogin() {
        return myUserStateNew;
    }

    //added
    public void LogOut(Context context) {
        myFirebaseAuthRepoVM.logOut(context);


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


    //
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
    public void setPredictionFromUIWithPlaceId(Prediction place) {
        mySharedRepoVM.setMyPlaceIdFromAutocomplete(place);
    }


    public void updataApp() {
        myWorkMatesRepoVM.getFirestoreWorkmates();
    //WorkMatesListFromRepo();
    }



    public Task<DocumentSnapshot> requestUserForestoreFromVM(String uid) {
        return myWorkMatesRepoVM.getUserFirestoreFromRepo(uid);
    }

    public void updateUserSystem() {
        myFirebaseAuthRepoVM.updateUser();
    }

    public void setUser() {
        myWorkMatesRepoVM.setupListenerds();
    }
}


