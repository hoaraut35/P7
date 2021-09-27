package com.hoarauthomas.go4lunchthp7.ui;

import com.hoarauthomas.go4lunchthp7.model.FirestoreUser;

import java.util.List;

public class MyNotification {


    List<FirestoreUser> myWorkmateList;

    MyNotification(List<FirestoreUser> malist) {

    }

    public List<FirestoreUser> getMyWorkmateList() {
        return myWorkmateList;
    }

    public void setMyWorkmateList(List<FirestoreUser> myWorkmateList) {
        this.myWorkmateList = myWorkmateList;
    }
}

