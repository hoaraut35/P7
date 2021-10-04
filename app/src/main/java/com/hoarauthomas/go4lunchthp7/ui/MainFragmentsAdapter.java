package com.hoarauthomas.go4lunchthp7.ui;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.google.common.collect.Maps;
import com.hoarauthomas.go4lunchthp7.ui.map.MapsFragment;
import com.hoarauthomas.go4lunchthp7.ui.restaurant.ListFragment;
import com.hoarauthomas.go4lunchthp7.ui.settings.SettingsFragment;
import com.hoarauthomas.go4lunchthp7.ui.workmates.WorkmatesFragment;

public class MainFragmentsAdapter extends FragmentStateAdapter {

    public MainFragmentsAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {


        switch (position) {

            case 0:
                return MapsFragment.newInstance();
            case 1:
                return MapsFragment.newInstance();
                //return new MapsFragment();

            case 2:
                return new ListFragment();
            case 3:
                return WorkmatesFragment.newInstance();
            case 4:
                return new SettingsFragment();
            default:
                return MapsFragment.newInstance();
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }

}
