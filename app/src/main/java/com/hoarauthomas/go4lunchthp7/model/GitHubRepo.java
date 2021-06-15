package com.hoarauthomas.go4lunchthp7.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GitHubRepo {



    @SerializedName("name")
    @Expose
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
