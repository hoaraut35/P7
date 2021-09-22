package com.hoarauthomas.go4lunchthp7.ui.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.hoarauthomas.go4lunchthp7.R;
import com.hoarauthomas.go4lunchthp7.factory.ViewModelFactory;
import com.hoarauthomas.go4lunchthp7.repository.SharedRepository;
import com.hoarauthomas.go4lunchthp7.ui.ViewModelMain;

public class SettingsFragment extends PreferenceFragmentCompat {

    public ViewModelMain myViewModel;
    public SharedRepository mySharedRepo;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
        setupViewModel();
    }

    private void setupViewModel() {

        this.myViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(ViewModelMain.class);
        Context context = getActivity();

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());

        boolean cfhk = sp.getBoolean("notification",true);

        if (cfhk){
//            myViewModel.setNotification();
        }else
        {
  //          myViewModel.removeNotification();
        }


    }



}