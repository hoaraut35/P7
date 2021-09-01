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
import com.hoarauthomas.go4lunchthp7.repository.WorkMatesRepository;

import java.util.List;

import javax.annotation.Nullable;


public class ViewModelMain extends ViewModel {

    //for Auth
    private FirebaseAuthRepository myFirebaseAuthRepoVM;
    private MutableLiveData<FirebaseUser> myUserLiveData;
    private MutableLiveData<Boolean> myUserStateNew;

    //for Workmates
    private WorkMatesRepository myWorkMatesRepoVM;
    private MutableLiveData<List<User>> myWorkMatesListLiveData;

    //
    private MutableLiveData<String> myUserRestaurantId = new MutableLiveData<>();

    //to merge data
    private final MediatorLiveData<ViewStateMain> myAppMapMediator = new MediatorLiveData<>();

    //constructor
    public ViewModelMain(FirebaseAuthRepository firebaseAuthRepository, WorkMatesRepository workMatesRepository) {

        //get data from Auth repository...
        myFirebaseAuthRepoVM = firebaseAuthRepository;
        myUserLiveData = myFirebaseAuthRepoVM.getUserLiveDataNew();
        myUserStateNew = myFirebaseAuthRepoVM.getLoggedOutLiveDataNew();
        //get data from workmates repository...
        myWorkMatesRepoVM = workMatesRepository;
        myWorkMatesListLiveData = myWorkMatesRepoVM.getAllWorkMatesList();

        //fisrt init...
   //     logicWork(myUserLiveData.getValue(),myWorkMatesListLiveData.getValue());

        //we observe these data... to merge for extract
        myAppMapMediator.addSource(myUserLiveData, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                logicWork(firebaseUser, myWorkMatesListLiveData.getValue());
            }
        });

        myAppMapMediator.addSource(myWorkMatesListLiveData, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {

                logicWork(myUserLiveData.getValue(), users);
            }
        });

    }

    // Logic work
    private void logicWork(@Nullable FirebaseUser myUser, @Nullable List<User> workmates) {

        if (myUser != null && workmates != null){

            for (int i =0; i<workmates.size(); i++){

                if (myUser.getUid().equals(workmates.get(i).getUid())){

                    myUserRestaurantId.setValue(workmates.get(i).getFavoriteRestaurant());

                }

            }

        }else
        {
            myUserRestaurantId.setValue("text");
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
    public void LogOut() {
        myFirebaseAuthRepoVM.logOut();
    }

    public LiveData<FirebaseUser> getMyUser(){
        return myUserLiveData;
    }

    public LiveData<String> getMyUserRestaurant(){
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
    public LiveData<ViewStateMain> getMediatorLiveData() {
        return myAppMapMediator;
    }
}


