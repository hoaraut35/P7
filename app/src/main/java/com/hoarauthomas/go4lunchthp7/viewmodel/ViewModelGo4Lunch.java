package com.hoarauthomas.go4lunchthp7.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;
import com.hoarauthomas.go4lunchthp7.model.firestore.User;
import com.hoarauthomas.go4lunchthp7.repository.AuthRepository;
import com.hoarauthomas.go4lunchthp7.repository.WorkMatesRepository;

import java.util.List;


public class ViewModelGo4Lunch extends ViewModel {

    private final AuthRepository myAuthSource;
    private final WorkMatesRepository myWorkMatesSource;
    private final MediatorLiveData<MainViewState> myAppMapMediator = new MediatorLiveData<>();
    private final MutableLiveData<FirebaseUser> myUserVM;
    private final MutableLiveData<Boolean> myUserStateVM;
    private LiveData<List<User>> workMatesLiveData;

    //constructor to get one instance of each object, called by ViewModelFactory
    public ViewModelGo4Lunch(AuthRepository authRepository, WorkMatesRepository workMatesRepository) {


        this.myAuthSource = authRepository;
        this.myUserVM = myAuthSource.getUserFromRepo();

        this.myUserStateVM = myAuthSource.getUserStateFromRepo();
        this.myWorkMatesSource = workMatesRepository;
        this.workMatesLiveData = myWorkMatesSource.getAllWorkMates();

    }

    //publish method to activity for
    public MutableLiveData<FirebaseUser> getMyCurrentUser() {
        return myUserVM;
    }

    //publish method to activity... to log out work fine
    public void logOut() {
        myAuthSource.logOutFromRepo();
    }

    //publish method to activity... (logged or not) work fine
    public MutableLiveData<Boolean> getMyUserState() {
        return myUserStateVM;
    }

    //Create user to Firestore
    public void createUser() {
        this.myWorkMatesSource.createUser();
    }

}


