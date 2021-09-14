package com.hoarauthomas.go4lunchthp7.ui;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import com.hoarauthomas.go4lunchthp7.PlaceAutocomplete;

import java.util.List;

public class PlaceAdapter extends ArrayAdapter<PlaceAutocomplete> {


    public PlaceAdapter(@NonNull Context context, int resource, @NonNull List<PlaceAutocomplete> objects) {
        super(context, resource, objects);
    }
}
