package com.hoarauthomas.go4lunchthp7.ui;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.facebook.internal.Mutable;
import com.google.firebase.auth.FirebaseUser;
import com.hoarauthomas.go4lunchthp7.PlaceAutocomplete;
import com.hoarauthomas.go4lunchthp7.SingleLiveEvent;
import com.hoarauthomas.go4lunchthp7.model.firestore.User;
import com.hoarauthomas.go4lunchthp7.repository.AlarmRepository;
import com.hoarauthomas.go4lunchthp7.repository.FirebaseAuthRepository;
import com.hoarauthomas.go4lunchthp7.repository.PlaceAutocompleteRepository;
import com.hoarauthomas.go4lunchthp7.repository.PositionRepository;
import com.hoarauthomas.go4lunchthp7.repository.SharedRepository;
import com.hoarauthomas.go4lunchthp7.repository.WorkMatesRepository;

import java.util.List;

import javax.annotation.Nullable;


public class ViewModelMain extends ViewModel {

    //repository...
    private FirebaseAuthRepository myFirebaseAuthRepoVM;
    private WorkMatesRepository myWorkMatesRepoVM;
    private PlaceAutocompleteRepository myPlaceAutocompleteRepoVM;
    private PositionRepository myPositionRepoVM;
    private AlarmRepository myAlarmRepoVM;
    private SharedRepository mySharedRepoVM;

    //livedata...
    private MutableLiveData<FirebaseUser> myUserLiveData;
    private MutableLiveData<Boolean> myUserStateNew;
    private MutableLiveData<List<User>> myWorkMatesListLiveData;
    private MutableLiveData<String> myUserRestaurantId = new MutableLiveData<>();
    private MutableLiveData<com.hoarauthomas.go4lunchthp7.PlaceAutocomplete> myPlaceAutocompleteList = new MutableLiveData<>();


    private SingleLiveEvent<PlaceAutocomplete> myPlaceAutoCompleteListSingleEvent = new SingleLiveEvent<>();

    //to update ViewState...
    MediatorLiveData<ViewMainState> myAppMapMediator = new MediatorLiveData<>();

    //constructor...
    public ViewModelMain(FirebaseAuthRepository firebaseAuthRepository, WorkMatesRepository workMatesRepository, PlaceAutocompleteRepository placeAutocompleteRepository, PositionRepository myPositionRepoVM, AlarmRepository myAlarmRepoVM) {

        //get data from Auth repository...
        this.myFirebaseAuthRepoVM = firebaseAuthRepository;
        myUserLiveData = myFirebaseAuthRepoVM.getUserLiveDataNew();
        myUserStateNew = myFirebaseAuthRepoVM.getLoggedOutLiveDataNew();

        //get data from workmates repository...
        this.myWorkMatesRepoVM = workMatesRepository;
        myWorkMatesListLiveData = myWorkMatesRepoVM.getAllWorkMatesList();

        //get data from place autocomplete repository...
        this.myPlaceAutocompleteRepoVM = placeAutocompleteRepository;
      //  this.myPlaceAutocompleteList = myPlaceAutocompleteRepoVM.getMyPlaceAutocompleteListForVM();

        this.myPlaceAutoCompleteListSingleEvent = myPlaceAutocompleteRepoVM.getMyPlaceAutocompleteListForVMSingle();

        //get position for autocomplete request
        this.myPositionRepoVM = myPositionRepoVM;

        //get alarm data
        this.myAlarmRepoVM = myAlarmRepoVM;

        //add source
        myAppMapMediator.addSource(myUserStateNew, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                Log.i("MEDIA", "new event on state");
                logicWork(myUserLiveData.getValue(), myWorkMatesListLiveData.getValue(), aBoolean);
            }
        });

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
        myAppMapMediator.addSource(myWorkMatesListLiveData, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {

                Log.i("MEDIA", "new event on list workmates");


                if (myWorkMatesListLiveData != null) {
                    if (!myWorkMatesListLiveData.getValue().isEmpty()) {
                        logicWork(myUserLiveData.getValue(), users, myUserStateNew.getValue());

                    }

                }
            }
        });


    }

    // Logic work
    private void logicWork(@Nullable FirebaseUser myUser, @Nullable List<User> workmates, Boolean bool) {

        Log.i("MEDIA", "Logic work ...");


      //  if (myUser == null) return;
     //   if (workmates == null) return;







        if (bool) {

            createUser();


            if (myUser != null && workmates != null) {


                        if (!myUser.getUid().isEmpty()) {

                            for (int i = 0; i < workmates.size(); i++) {

                                if (workmates.get(i).getUid().equals(myUser.getUid())) {
                                    myUserRestaurantId.setValue(workmates.get(i).getFavoriteRestaurant());

                                    break;
                                }
                            }

                            //getUser();
                            myAppMapMediator.setValue(new ViewMainState(true, myUserRestaurantId.getValue(),getUser()));

                        } else {
                            //getUser();
                            myAppMapMediator.setValue(new ViewMainState(true, "pas de restau",getUser()));

                        }

            }else
            {
                if (myUser != null && workmates == null){
                    createUser();

                }
            }

            myAppMapMediator.setValue(new ViewMainState(true, "liste restaur non chargée",getUser()));


        } else {
            myAppMapMediator.setValue(new ViewMainState(false, "echec login",getUser()));
            //myUserRestaurantId.setValue("text");
        }


    }
    //**********************************************************************************************
    // End of logic work
    //**********************************************************************************************


    //**********************************************************************************************
    // PUBLIC
    //**********************************************************************************************


    public MutableLiveData<PlaceAutocomplete> getMyPlaceListForUI(){
        return myPlaceAutocompleteList;
    }

    public SingleLiveEvent<PlaceAutocomplete> getMyPlaceListForUISingle(){

        return myPlaceAutoCompleteListSingleEvent;
    }



    //addedd
    public LiveData<Boolean> getMyLogin() {
        return myUserStateNew;
    }

    //added
    public void LogOut(Context context) {
        myFirebaseAuthRepoVM.logOut(context);
    }

    public FirebaseUser getUser(){
        return myUserLiveData.getValue();
    }


    public LiveData<String> getMyUserRestaurant() {
        return myUserRestaurantId;
    }


    public void checkUserLogin() {
        myFirebaseAuthRepoVM.checkUser();
        //return myUserState;
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
    public void getResultAutocomplete(String query, Location location){
     // myPlaceAutocompleteRepoVM.getPlaceAutocomplete(query,location);
       myPlaceAutocompleteRepoVM.getPlaceAutocompleteSingle(query,location);
        //return myPlaceAutocompleteList;

    }


    public Location getMyPosition(){
        return myPositionRepoVM.getLocationLiveData().getValue();
    }


    //not used
    public void setNotification() {
        myAlarmRepoVM.setAlarm();
    }


    /**
     * To enable or disable notification
     * @param state
     */
    public void setNotification(Boolean state){
        myAlarmRepoVM.setNotification(state);
    }

    public void setPositionWithPlaceId(String placeId) {


    }
}


