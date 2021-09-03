package com.hoarauthomas.go4lunchthp7;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.facebook.internal.Mutable;
import com.google.firebase.auth.FirebaseUser;
import com.hoarauthomas.go4lunchthp7.model.firestore.User;
import com.hoarauthomas.go4lunchthp7.repository.FirebaseAuthRepository;
import com.hoarauthomas.go4lunchthp7.repository.PlaceAutocompleteRepository;
import com.hoarauthomas.go4lunchthp7.repository.WorkMatesRepository;

import java.util.List;

import javax.annotation.Nullable;


public class ViewModelMain extends ViewModel {

    //repository
    private FirebaseAuthRepository myFirebaseAuthRepoVM;
    private WorkMatesRepository myWorkMatesRepoVM;
    private PlaceAutocompleteRepository myPlaceAutocompleteRepoVM;

    //livedata 1
    private MutableLiveData<FirebaseUser> myUserLiveData;
    private MutableLiveData<Boolean> myUserStateNew;

    //livedata2
    private MutableLiveData<List<User>> myWorkMatesListLiveData;

    //others
    private MutableLiveData<String> myUserRestaurantId = new MutableLiveData<>();


    private MutableLiveData<com.hoarauthomas.go4lunchthp7.PlaceAutocomplete> myPlaceAutocompleteList = new MutableLiveData<>();


    //to merge data
    MediatorLiveData<ViewMainState2> myAppMapMediator = new MediatorLiveData<>();

    //constructor
    public ViewModelMain(FirebaseAuthRepository firebaseAuthRepository, WorkMatesRepository workMatesRepository, PlaceAutocompleteRepository placeAutocompleteRepository) {

        //get data from Auth repository...
        this.myFirebaseAuthRepoVM = firebaseAuthRepository;
        myUserLiveData = myFirebaseAuthRepoVM.getUserLiveDataNew();
        myUserStateNew = myFirebaseAuthRepoVM.getLoggedOutLiveDataNew();

        //get data from workmates repository...
        this.myWorkMatesRepoVM = workMatesRepository;
        myWorkMatesListLiveData = myWorkMatesRepoVM.getAllWorkMatesList();

        //get data from place autocomplete repository...
        this.myPlaceAutocompleteRepoVM = placeAutocompleteRepository;

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
                Log.i("MEDIA", "new event on user");


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

                            myAppMapMediator.setValue(new ViewMainState2(true, myUserRestaurantId.getValue()));

                        } else {
                            myAppMapMediator.setValue(new ViewMainState2(true, "pas de restau"));

                        }







            }else
            {
                if (myUser != null && workmates == null){
                    createUser();

                }
            }

            myAppMapMediator.setValue(new ViewMainState2(true, "liste restaur non chargée"));


        } else {
            myAppMapMediator.setValue(new ViewMainState2(false, "echec login"));
            //myUserRestaurantId.setValue("text");
        }


    }
    //**********************************************************************************************
    // End of logic work
    //**********************************************************************************************


    //**********************************************************************************************
    // PUBLIC
    //**********************************************************************************************


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
    public LiveData<ViewMainState2> getMediatorLiveData() {
        return myAppMapMediator;
    }



    public MutableLiveData<com.hoarauthomas.go4lunchthp7.PlaceAutocomplete> getResultAutocomplete(){
        myPlaceAutocompleteList = getDataFromAutoCompleteRepo();
        return myPlaceAutocompleteList;

    }


    private MutableLiveData<com.hoarauthomas.go4lunchthp7.PlaceAutocomplete> getDataFromAutoCompleteRepo(){
        return myPlaceAutocompleteRepoVM.getPlaceAutocomplete("cantine de fran");
    }









}


