package com.hoarauthomas.go4lunchthp7.ui;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.hoarauthomas.go4lunchthp7.ui.restaurant.ListFragment;
import com.hoarauthomas.go4lunchthp7.ui.map.MapsFragment;
import com.hoarauthomas.go4lunchthp7.ui.settings.SettingsFragment;
import com.hoarauthomas.go4lunchthp7.ui.workmates.WorkFragment;

public class FragmentsAdapter extends FragmentStateAdapter {

    public FragmentsAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {


        switch (position) {
            case 1:
                //TODO : disable for test to prevent use of api map
                return new MapsFragment();

            case 2:

                return new ListFragment();//.newInstance();
            case 3:

                return WorkFragment.newInstance("", "");

            case 4:
                return new SettingsFragment();
            default:
                return ListFragment.newInstance();
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }

}