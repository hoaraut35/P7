package com.hoarauthomas.go4lunchthp7.viewmodel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ViewModelSecurity {
    protected FirebaseUser getCurrentUser() {return FirebaseAuth.getInstance().getCurrentUser();}

    protected Boolean isCurrentUserLogged() {
        return (this.getCurrentUser() != null);
    }

    public void security() {
        if (!isCurrentUserLogged()) {
            //if not connected then request login
            //    request_login();
        } else {
            //else request user info to update ui
            //request_user_info();
        }
    }
}
