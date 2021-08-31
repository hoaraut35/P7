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
import com.hoarauthomas.go4lunchthp7.repository.FirebaseAuthRepository;
import com.hoarauthomas.go4lunchthp7.repository.WorkMatesRepository;

import java.util.List;

import javax.annotation.Nullable;


public class ViewModelMain extends ViewModel {

    //repo
    private final FirebaseAuthRepository myFirebaseAuthRepository;
    private final WorkMatesRepository myWorkMatesRepository;

    //mut liv etc
    private final MutableLiveData<FirebaseUser> myUser = null;
    private MutableLiveData<Boolean> myUserState = new MutableLiveData<>(false);

    //for update ViewStateMain data
    private final MediatorLiveData<ViewStateMain> myAppMapMediator = new MediatorLiveData<>();

    public ViewModelMain(FirebaseAuthRepository firebaseAuthRepository, WorkMatesRepository workMatesRepository) {

        this.myFirebaseAuthRepository = firebaseAuthRepository;
//        myFirebaseAuthRepository.checkUser();
        LiveData<FirebaseUser> myUserVM = myFirebaseAuthRepository.getUserLiveData();
        LiveData<Boolean> myUserStateVM = myFirebaseAuthRepository.getUserStateLiveData();

        this.myWorkMatesRepository = workMatesRepository;
        LiveData<List<User>> myWorkMatesVM = this.myWorkMatesRepository.getAllWorkMates();

        myAppMapMediator.addSource(myUserVM, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {

                if (firebaseUser != null) {
                    logicWork(firebaseUser, myUserStateVM.getValue(), myWorkMatesVM.getValue());
                }
            }
        });

        myAppMapMediator.addSource(myUserStateVM, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean != null){
                    logicWork(myUserVM.getValue(), aBoolean, myWorkMatesVM.getValue());
                }

            }
        });

        myAppMapMediator.addSource(myWorkMatesVM, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                if (users != null){
                    if (!users.isEmpty()) {
                        logicWork(myUserVM.getValue(), myUserStateVM.getValue(), users);
                    }
                }

            }
        });

    }

    //**********************************************************************************************
    // Logic work
    //**********************************************************************************************
    private void logicWork(@Nullable FirebaseUser myUser, @Nullable Boolean userState, @Nullable List<User> user) {
     //   Log.i("[Auth]","taille user" + user.size());

        String restaurantUser = null;

        if (userState == null || !userState) {
            myAppMapMediator.setValue(new ViewStateMain(myUser, false));
        } else if (userState) {
            createUser();

            if (user != null){
                if (!user.isEmpty()){

                    for (int i=0 ; i< user.size();i++){

                        if (user.get(i).getUid().equals(myUser.getUid())){
                            Log.i("[Auth]","" + user.get(i).getFavoriteRestaurant());
                            restaurantUser = user.get(i).getFavoriteRestaurant();
                            break;
                        }

                    }

                }
            }

            myAppMapMediator.setValue(new ViewStateMain(myUser, true, restaurantUser));
        }


       /*f (myUser == null) {
            myAppMapMediator.setValue(new ViewStateMain(myUser, false));
        }

        */

   /*     if (myUser != null && userState) {
            myAppMapMediator.setValue(new ViewStateMain(myUser, true));
            createUser();
        }

    */

   /*     if (!userState) {
            Log.i("LOGOUT", "logout");
            myAppMapMediator.setValue(new ViewStateMain(userState.booleanValue()));

        }

    */


    }
    //**********************************************************************************************
    // End of logic work
    //**********************************************************************************************


    //**********************************************************************************************
    // PUBLIC
    //**********************************************************************************************

    public void  checkUserLogin() {
        myFirebaseAuthRepository.checkUser();
        //return myUserState;
    }

    public LiveData<FirebaseUser> getMyCurrentUser() {
        return myUser;
    }


    public void logOut(Context context) {
        //this.myUserStateVM.setValue(false);
        myFirebaseAuthRepository.signOut(context);
        //);AuthSource.logOutFromRepo();
        //    updateUSer();
    }

    //publish method to activity... (logged or not) work fine
 /*   public MutableLiveData<Boolean> getMyUserState() {
        return myUserStateVM;
    }

  */

    //Create user to Firestore
    public void createUser() {
        this.myWorkMatesRepository.createUser();
    }


    //to publish mediatorlivedata to mainactivity
    public LiveData<ViewStateMain> getMediatorLiveData() {
        return myAppMapMediator;
    }
}


