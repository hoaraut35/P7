package com.hoarauthomas.go4lunchthp7.api;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GooglePlaceApiHolder {

    public static GooglePlaceApi getInstance(){

        //instance of new interceptor
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        //set level ?
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        return new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/maps/api/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(GooglePlaceApi.class);
    }
}
