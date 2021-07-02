package com.hoarauthomas.go4lunchthp7.repository;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthentificationRepository {

    protected FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    protected Boolean isCurrentUserLogged() {
        return (this.getCurrentUser() != null);
    }

    public AuthentificationRepository() {
    }


    public Boolean getLoginState() {
        if (!isCurrentUserLogged()) {
            //TODO: request login from activity
           // request_login();
            return false;
        } else {
            //TODO: request user info from activity
            //request_user_info();
            return true;
        }
    }


}
