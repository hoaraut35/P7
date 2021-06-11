package com.hoarauthomas.go4lunchthp7.view;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.hoarauthomas.go4lunchthp7.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
    }
}