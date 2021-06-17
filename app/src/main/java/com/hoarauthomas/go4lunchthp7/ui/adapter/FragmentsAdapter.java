package com.hoarauthomas.go4lunchthp7.ui.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.hoarauthomas.go4lunchthp7.ui.fragments.ListFragment;
import com.hoarauthomas.go4lunchthp7.ui.fragments.MapsFragment;
import com.hoarauthomas.go4lunchthp7.ui.fragments.SettingsFragment;
import com.hoarauthomas.go4lunchthp7.ui.fragments.WorkFragment;

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
                return ListFragment.newInstance();
            case 3:
                return WorkFragment.newInstance("", "");

            case 4:
                return new SettingsFragment();
            default:
                return WorkFragment.newInstance("", "");
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }


   /* public FragmentsAdapter(@NonNull FragmentActivity fa) {
        super(fa);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 1:
                //TODO : disable for test to prevent use of api map
                return new MapsFragment();

            case 2:
                return ListFragment.newInstance();
            case 3:
                return WorkFragment.newInstance("", "");

            case 4:
                return new SettingsFragment();
            default:
                return WorkFragment.newInstance("", "");
        }
    }

    @Override
    public int getCount() {
        return 5;
    }




    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return null;
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    */
}
