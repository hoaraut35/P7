package com.hoarauthomas.go4lunchthp7;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;
import com.hoarauthomas.go4lunchthp7.model.MyUser;
import com.hoarauthomas.go4lunchthp7.model.firestore.User;
import com.hoarauthomas.go4lunchthp7.repository.AuthentificationRepository;
import com.hoarauthomas.go4lunchthp7.repository.WorkMatesRepository;

import java.util.List;

import javax.annotation.Nullable;


public class ViewModelMain extends ViewModel {

    //repositories
    private final AuthentificationRepository myAuthentificationSource;
    private WorkMatesRepository myWorkMatesSource;

    //data
    private MutableLiveData<FirebaseUser> myUserVM = null;
    private MutableLiveData<Boolean> myUserStateVM;
    private LiveData<List<User>> myWorkMatesVM;

    //for update ViewStateMain data
    private final MediatorLiveData<ViewStateMain> myAppMapMediator = new MediatorLiveData<>();

    public ViewModelMain(AuthentificationRepository authentificationRepository, WorkMatesRepository workMatesRepository) {

        this.myAuthentificationSource = authentificationRepository;
        this.myUserVM = myAuthentificationSource.getUserFromRepo();
        this.myUserStateVM = myAuthentificationSource.getUserStateFromRepo();

        this.myWorkMatesSource = workMatesRepository;
        this.myWorkMatesVM = myWorkMatesSource.getAllWorkMates();

        myAppMapMediator.addSource(myUserVM, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                Log.i("[MAINV]", "VM MainState : changement utilisateur ...");
                logicWork(firebaseUser, myUserStateVM.getValue(), myWorkMatesVM.getValue());
            }
        });

        myAppMapMediator.addSource(myUserStateVM, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                Log.i("[MAINV]", "VM MainState changement état utilisateur ...");
                logicWork(myUserVM.getValue(), aBoolean, myWorkMatesVM.getValue());
            }
        });

        myAppMapMediator.addSource(myWorkMatesVM, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                Log.i("[MAINV]", "VM MainState changement liste collegues ...");
                logicWork(myUserVM.getValue(), myUserStateVM.getValue(), users);
            }
        });
    }

    //**********************************************************************************************
    // Logic work
    //**********************************************************************************************
    private void logicWork(@Nullable FirebaseUser myUser, @Nullable Boolean userState, @Nullable List<User> user) {

        if (myUser != null){
            myAppMapMediator.setValue(new ViewStateMain(myUser));
            createUser();
        }else
        {

            myAppMapMediator.setValue(new ViewStateMain(myUser));
        }


    }
    //**********************************************************************************************
    // End of logic work
    //**********************************************************************************************


    //**********************************************************************************************
    // PUBLIC
    //**********************************************************************************************

    public void checkLogin() {
        //myViewModel.getMyCurrentUser();
        myAuthentificationSource.getUserFromRepo();
    }

    public MutableLiveData<FirebaseUser> getMyCurrentUser() {
        return myUserVM;
    }

    public void logOut(Context context) {
        //this.myUserStateVM.setValue(false);
        myAuthentificationSource.signOut(context);
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
    public LiveData<ViewStateMain> getMediatorLiveData() {
        return myAppMapMediator;
    }
}


