package com.hoarauthomas.go4lunchthp7.ui;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.common.collect.Maps;
import com.hoarauthomas.go4lunchthp7.ui.map.MapsFragment;
import com.hoarauthomas.go4lunchthp7.ui.restaurant.ListFragment;
import com.hoarauthomas.go4lunchthp7.ui.settings.SettingsFragment;
import com.hoarauthomas.go4lunchthp7.ui.workmates.WorkmatesFragment;

public class MainFragmentsAdapter extends FragmentStateAdapter {

    private static final int NUM_PAGES = 4;
    private ViewPager2 viewPager;

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
              //  return MapsFragment.newInstance();
                //return new MapsFragment();
                return ListFragment.newInstance();
            case 2:
                //return MapsFragment.newInstance();
                //return ListFragment.newInstance();
               // return new ListFragment();
                return WorkmatesFragment.newInstance();
            case 3:
                //return MapsFragment.newInstance();
return new SettingsFragment();
                //return WorkmatesFragment.newInstance();
            case 4:
                return MapsFragment.newInstance();
                //return new SettingsFragment();
            default:
                return MapsFragment.newInstance();
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }

}
