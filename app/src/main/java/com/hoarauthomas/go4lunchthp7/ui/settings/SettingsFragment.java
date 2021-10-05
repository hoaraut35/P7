package com.hoarauthomas.go4lunchthp7.ui.settings;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.hoarauthomas.go4lunchthp7.R;
import com.hoarauthomas.go4lunchthp7.factory.ViewModelFactory;
import com.hoarauthomas.go4lunchthp7.ui.ViewModelMain;

public class SettingsFragment extends PreferenceFragmentCompat {


    public ViewModelMain myViewModel;
    SharedPreferences sp;

    @Override
    public void onPause() {
        super.onPause();
        myViewModel.setZoom(sp.getInt("zoom", 12));
        myViewModel.setNotification(myViewModel.getMyUserFromFirestore().getUid(), sp.getBoolean("notifications2", true));
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
        sp = PreferenceManager.getDefaultSharedPreferences(requireContext());
        setupViewModel();
    }

    private void setupViewModel() {
        this.myViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(ViewModelMain.class);
    }
}