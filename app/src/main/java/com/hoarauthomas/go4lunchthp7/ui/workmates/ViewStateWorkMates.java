package com.hoarauthomas.go4lunchthp7.ui.workmates;

import android.util.Log;

import androidx.lifecycle.LiveData;

import com.hoarauthomas.go4lunchthp7.model.firestore.User;

import java.util.List;

public class ViewStateWorkMates {



    List<User> myWorkMatesList;

    public List<User> getMyWorkMatesList() {
        return myWorkMatesList;
    }

    public void setMyWorkMatesList(List<User> myWorkMatesList) {
        this.myWorkMatesList = myWorkMatesList;
    }

    public ViewStateWorkMates(List<User> myWorkMatesList) {
        Log.i("[WORKM]","ViewxStateWorkMAtes constructor " + myWorkMatesList.size());
        this.myWorkMatesList = myWorkMatesList;


    }
}
