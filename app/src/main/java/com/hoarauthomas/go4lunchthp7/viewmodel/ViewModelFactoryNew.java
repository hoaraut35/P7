package com.hoarauthomas.go4lunchthp7.viewmodel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.hoarauthomas.go4lunchthp7.repository.LocationRepository;
import com.hoarauthomas.go4lunchthp7.repository.RestaurantsRepository;

public class ViewModelFactoryNew implements ViewModelProvider.Factory {

    private static Context context;
    private static ViewModelFactoryNew myFactory;



    private ViewModelFactoryNew(Context context) {
        this.context = context;
    }

    public static ViewModelFactoryNew getInstance() {
        if (myFactory == null) {
            synchronized (ViewModelFactoryNew.class) {
                if (myFactory == null) {
                    myFactory = new ViewModelFactoryNew(context);
                }
            }
        }
        return myFactory;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ViewModelGo4LunchNew.class)) {
            return (T) new ViewModelGo4LunchNew(new RestaurantsRepository(), new LocationRepository(context));
        }
        throw new IllegalArgumentException("Uknow ViewModel class");
    }
}
