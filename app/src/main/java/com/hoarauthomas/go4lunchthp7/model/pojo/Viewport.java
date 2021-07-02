
package com.hoarauthomas.go4lunchthp7.pojo;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Viewport {

    @SerializedName("northeast")
    @Expose
    private com.hoarauthomas.go4lunchthp7.Northeast northeast;
    @SerializedName("southwest")
    @Expose
    private com.hoarauthomas.go4lunchthp7.Southwest southwest;

    public com.hoarauthomas.go4lunchthp7.Northeast getNortheast() {
        return northeast;
    }

    public void setNortheast(com.hoarauthomas.go4lunchthp7.Northeast northeast) {
        this.northeast = northeast;
    }

    public com.hoarauthomas.go4lunchthp7.Southwest getSouthwest() {
        return southwest;
    }

    public void setSouthwest(com.hoarauthomas.go4lunchthp7.Southwest southwest) {
        this.southwest = southwest;
    }

}
