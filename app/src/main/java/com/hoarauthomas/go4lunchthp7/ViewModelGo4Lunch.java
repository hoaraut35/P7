package com.hoarauthomas.go4lunchthp7;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;
import com.hoarauthomas.go4lunchthp7.model.firestore.User;
import com.hoarauthomas.go4lunchthp7.repository.AuthRepository;
import com.hoarauthomas.go4lunchthp7.repository.WorkMatesRepository;

import java.util.List;

import javax.annotation.Nullable;


public class ViewModelGo4Lunch extends ViewModel {

    //repo
    private final AuthRepository myAuthSource;
    private WorkMatesRepository myWorkMatesSource;

    //data
    private MutableLiveData<FirebaseUser> myUserVM = null;
    private MutableLiveData<Boolean> myUserStateVM;
    private LiveData<List<User>> workMatesLiveData;


    private final MediatorLiveData<MainViewState> myAppMapMediator = new MediatorLiveData<>();


    //constructor to get one instance of each object, called by ViewModelFactory
    public ViewModelGo4Lunch(AuthRepository authRepository, WorkMatesRepository workMatesRepository) {
        this.myAuthSource = authRepository;
        this.myUserVM = myAuthSource.getUserFromRepo();

        this.myUserStateVM = myAuthSource.getUserStateFromRepo();

        this.myWorkMatesSource = workMatesRepository;

        this.workMatesLiveData = myWorkMatesSource.getAllWorkMates();

        myAppMapMediator.addSource(myUserVM, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                Log.i("[NEWLOGIN]", "Updater user system...");
                logicWork(firebaseUser, myUserStateVM.getValue(), workMatesLiveData.getValue());
            }
        });

        myAppMapMediator.addSource(myUserStateVM, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                Log.i("[NEWLOGIN]", "Updater user state login...");
                logicWork(myUserVM.getValue(), aBoolean, workMatesLiveData.getValue());
            }
        });

        myAppMapMediator.addSource(workMatesLiveData, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                logicWork(myUserVM.getValue(), myUserStateVM.getValue(), users);
            }
        });

    }

    //**********************************************************************************************
    // Logic work
    //**********************************************************************************************
    private void logicWork(@Nullable FirebaseUser myUser, @Nullable Boolean userState, @Nullable List<User> user) {

        //on test si l'utiliseur exist dsans firestore
        if (myUser != null) {
            if (!myUser.getUid().isEmpty() ) {
                //  for (int i = 0; i < user.size(); i++) {
                //     if (!myUser.getUid().equals(user.get(i).getUid())) {
                createUser();
                // break;
                //   }
                //}
            }

        }


        if (myUser != null && !myUser.getUid().isEmpty()) {
            myAppMapMediator.setValue(new MainViewState(myUser, true));
        } else {
            if (userState != null) {

                if (!userState) {
                    Log.i("[NEWLOGIN]", "requets login");
                    myAppMapMediator.setValue(new MainViewState(false));
                } else {
                    //      myAppMapMediator.setValue(new MainViewState(true));
                    Log.i("[NEWLOGIN]", " error requets login");

                }


            }


        }


        //myAppMapMediator.setValue();//request login

        /*else
            if (myUser == null && userState != null ){
                myAppMapMediator.setValue(new MainViewState(userState));
            }

         */

    }
    //**********************************************************************************************
    // End of logic work
    //**********************************************************************************************





    //called by mainactivity only when lon gin
    public void updateUSer() {
        //myViewModel.getMyCurrentUser();

        myAuthSource.getUserFromRepo();

    }


    //publish method to activity for
    public MutableLiveData<FirebaseUser> getMyCurrentUser() {
        return myUserVM;
    }

    //publish method to activity... to log out work fine
    public void logOut(Context context) {
        //this.myUserStateVM.setValue(false);
        myAuthSource.signout(context);
        //);AuthSource.logOutFromRepo();
        //    updateUSer();
    }

    //publish method to activity... (logged or not) work fine
    public MutableLiveData<Boolean> getMyUserState() {
        return myUserStateVM;
    }

    //Create user to Firestore
    public void createUser() {
        this.myWorkMatesSource.createUser();
    }


    //to publish mediatorlivedata to mainactivity
    public LiveData<MainViewState> getMediatorLiveData() {
        return myAppMapMediator;
    }

}


