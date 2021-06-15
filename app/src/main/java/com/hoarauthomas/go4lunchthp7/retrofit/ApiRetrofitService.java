package com.hoarauthomas.go4lunchthp7.retrofit;

import com.hoarauthomas.go4lunchthp7.model.GitHubRepo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiRetrofitService {

    //set the endpoint of API
    @GET("/users/{user}/repos")

    Call<List<GitHubRepo>> reposForUser(@Path("user") String user);



    //@GET("/json?key={KEY}?")

}
