package com.hoarauthomas.go4lunchthp7.ui.workmates;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.hoarauthomas.go4lunchthp7.model.firestore.User;
import com.hoarauthomas.go4lunchthp7.repository.WorkMatesRepository;
import com.hoarauthomas.go4lunchthp7.ui.map.ViewStateMap;


import java.util.List;

public class ViewModelWorkMates extends ViewModel implements WorkMatesAdapter.ClickListener {


    private WorkMatesRepository myWorkMatesRepository;


    private final MediatorLiveData<ViewStateWorkMates> myViewStateWorkMatesMediator = new MediatorLiveData<>();


    public ViewModelWorkMates(WorkMatesRepository myWorkMatesRepository) {
        this.myWorkMatesRepository = myWorkMatesRepository;

        LiveData<List<User>> myWorkMatesList = this.myWorkMatesRepository.getAllWorkMates();


        myViewStateWorkMatesMediator.addSource(myWorkMatesList, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                Log.i("[WORKM]", "Event workmates list...");
                if (users != null) {
                    Log.i("[WORKM]", "Liste workmates" + users.size());
                    logicWork( users);
                }

            }
        });
    }


    private void logicWork(List<User> myList) {


        if (myList != null){
            Log.i("[WORKM]","mylist" + myList.size());
            myViewStateWorkMatesMediator.setValue(new ViewStateWorkMates(myList));
        }


    }


    //for publish to state class
    public LiveData<ViewStateWorkMates> getMediatorLiveData() {
        return myViewStateWorkMatesMediator;
    }

    @Override
    public void onClickDetailWorkMate(String restaurantId) {

    }

    @Override
    public void popupSnack(String message) {

    }
}
