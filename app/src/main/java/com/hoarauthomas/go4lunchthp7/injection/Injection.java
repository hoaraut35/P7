package com.hoarauthomas.go4lunchthp7.injection;

import android.content.Context;

import com.hoarauthomas.go4lunchthp7.repositories.RestaurantsRepository;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Injection {

    //add repository for places
    public static RestaurantsRepository providePlaceDataSource(Context context) {
        return new RestaurantsRepository();
    }

    //add executor to load a separated thread
    public static Executor provideExecutor() {
        return Executors.newSingleThreadExecutor();
    }

    //public constructor to provide repository in MainActivity ..
    public static ViewModelFactory provideViewModelFactory(Context context) {
        RestaurantsRepository dataSourcePlace = providePlaceDataSource(context);
        Executor executor = provideExecutor();
        return new ViewModelFactory(dataSourcePlace, executor);
    }

}
