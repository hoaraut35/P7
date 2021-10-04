package com.hoarauthomas.go4lunchthp7.ui.settings;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

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
        Log.i("[SETTINGS]","onPause");

        myViewModel.setZoom(sp.getInt("zoom", 12));
        myViewModel.setNotification(myViewModel.getMyUserFromFirestore().getUid(), sp.getBoolean("notifications2", true));
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
        sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        setupViewModel();
    }

    private void setupViewModel() {

        this.myViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(ViewModelMain.class);

/*        sp.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                Log.i("[SETTINGS]","test");
            }
        });


 */
   //     SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());



     /*   sp.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                Log.i("[SETTINGS]", "changed");



                myViewModel.setZoom(sp.getInt("zoom", 10));

                if (myViewModel.getMyUserFromFirestore() != null) {

                }

            }
        });

      */

    }
}