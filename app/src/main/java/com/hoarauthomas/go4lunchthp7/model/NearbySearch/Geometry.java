
package com.hoarauthomas.go4lunchthp7.model.NearbySearch;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hoarauthomas.go4lunchthp7.model.NearbySearch.Location;
import com.hoarauthomas.go4lunchthp7.model.NearbySearch.Viewport;


public class Geometry {

    @SerializedName("location")
    @Expose
    private Location location;

    @SerializedName("myPosition")
    @Expose
    private int myPosition;


    @SerializedName("viewport")
    @Expose
    private Viewport viewport;

    public int getMyPosition() {     return myPosition;    }
    public void setMyPosition(int myPosition) {        this.myPosition = myPosition;    }

    public Location getLocation() {
        return location;
    }
    public void setLocation(Location location) {
        this.location = location;
    }

    public Viewport getViewport() {
        return viewport;
    }
    public void setViewport(Viewport viewport) {
        this.viewport = viewport;
    }

}
