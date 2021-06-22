package com.hoarauthomas.go4lunchthp7.injection;

import android.content.Context;

import com.hoarauthomas.go4lunchthp7.repositories.LocationRepository;
import com.hoarauthomas.go4lunchthp7.repositories.RestaurantsRepository;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Injection {

    //add repository here...
    public static RestaurantsRepository providePlaceDataSource() {
        return new RestaurantsRepository();
    }

    public static LocationRepository provideLocationDataSource(Context context) {
        return new LocationRepository(context);
    }

    //to load in another thread...
    public static Executor provideExecutor() {
        return Executors.newSingleThreadExecutor();
    }

    //to construct ViewModelFactory
    public static ViewModelFactory provideViewModelFactory(Context context) {
        RestaurantsRepository dataSourcePlace = providePlaceDataSource();
        LocationRepository dataSourceLocation = provideLocationDataSource(context);
        Executor executor = provideExecutor();
        return new ViewModelFactory(dataSourcePlace, dataSourceLocation, executor);
    }

}
