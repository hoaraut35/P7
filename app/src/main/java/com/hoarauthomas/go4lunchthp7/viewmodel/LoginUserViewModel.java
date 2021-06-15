package com.hoarauthomas.go4lunchthp7.viewmodel;

import android.util.Log;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hoarauthomas.go4lunchthp7.R;
import com.hoarauthomas.go4lunchthp7.model.User;

import java.util.Arrays;
import java.util.List;

import static androidx.core.app.ActivityCompat.startActivityForResult;
import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class LoginUserViewModel extends ViewModel {

    //Added to declare signal login
    private static final int RC_SIGN_IN = 123;
    private static final int SIGN_OUT = 10;




    private MutableLiveData<User> currentUserMLD;

    private MutableLiveData<String> loginState = new MutableLiveData<>();

    public void setFalse(String test)
    {
        loginState.postValue(test);
    }

    public LiveData<String> getLogin(){
        return loginState;
    }


    public void setFalse(){
        loginState.postValue("false");
    }


    public LiveData<User> getUser() {

        if (currentUserMLD == null) {
            currentUserMLD = new MutableLiveData<User>();
            signIn();
        } else {
            if (!isCurrentUserLogged()) {
                Log.i("[THOMAS]", "Utilisateur non autehntifié !");
                //request_login();

                setFalse("false");
            } else {
                Log.i("[THOMAS]", "Utilisateur authentifié");

                request_user_info();


            }

            return currentUserMLD;
        }

        return currentUserMLD;
    }


    private void request_user_info() {
        currentUserMLD.postValue(new User("test","test"));
    }




    //load if no user are login
    public void signIn() {
        setFalse("true");
    }

    public void signOut() {
        AuthUI.getInstance()
                .signOut(getApplicationContext());
            //    .addOnSuccessListener(getgetApplicationContext(),this.updateUIAfterRESTRequestCompleted(SIGN_OUT));


    }


    /*private OnSuccessListener<Void> updateUIAfterRESTRequestCompleted(final int orgin){
        return new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                switch (origin){
                    case SIGN_OUT_TASK:
                        finish();
                        break;
                    case DELETE_USER_TASK:
                        finish();
                        break;
                    default:
                        break;
                }
            }
        };
    }

     */





    public Boolean check_login() {

        if (!isCurrentUserLogged()) {

            return false;
        } else {
            return true;


        }
    }

    public void updateCurrentUser(String name, String email) {
        User user = new User(name, email);
        //  currentUserMLD.setValue(user);
    }


    //Tools
    protected FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    protected Boolean isCurrentUserLogged() {
        return (this.getCurrentUser() != null);
    }


}
