package com.hoarauthomas.go4lunchthp7.view.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.hoarauthomas.go4lunchthp7.view.ListFragment;
import com.hoarauthomas.go4lunchthp7.view.MapsFragment;
import com.hoarauthomas.go4lunchthp7.view.SettingsFragment;
import com.hoarauthomas.go4lunchthp7.view.WorkFragment;

public class FragmentsAdapter extends FragmentPagerAdapter {


    public FragmentsAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch(position)
        {
            case 1:
                //TODO : disable for test to prevent use of api map
                return new MapsFragment();

            case 2:
                return ListFragment.newInstance();
            case 3:
                return WorkFragment.newInstance("","");

            case 4:
                return new SettingsFragment();
            default:
                return WorkFragment.newInstance("","");
        }
    }

    @Override
    public int getCount() {
        return 5;
    }
}
