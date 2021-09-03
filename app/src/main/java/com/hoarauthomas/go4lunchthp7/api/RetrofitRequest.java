package com.hoarauthomas.go4lunchthp7.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitRequest {

    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://maps.googleapis.com/maps/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getGooglePlaceAutocomplete(){
        if (retrofit == null) {

            retrofit = new Retrofit.Builder()
                    .baseUrl("")
                    .addCallAdapterFactory(GsonConverterFactory.create())
                    .build();
        }
    }





}
