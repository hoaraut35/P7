package com.hoarauthomas.go4lunchthp7.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.hoarauthomas.go4lunchthp7.view.ListFragment;
import com.hoarauthomas.go4lunchthp7.view.MapsFragment;
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
            case 0:
                return new MapsFragment();
            case 1:
                return ListFragment.newInstance();
            case 2:
                return WorkFragment.newInstance("","");
            default:
                return WorkFragment.newInstance("","");
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
