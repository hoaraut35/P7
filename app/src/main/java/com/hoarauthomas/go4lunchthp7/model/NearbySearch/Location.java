
package com.hoarauthomas.go4lunchthp7.model.NearbySearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Location {

    @SerializedName("lat")
    @Expose
    private Double lat;
    @SerializedName("lng")
    @Expose
    private Double lng;



    @SerializedName("favorite")
    @Expose
    private Boolean favorite;

    public Boolean getFavorite() { return favorite; }

    public void setFavorite(Boolean favorite) { this.favorite = favorite; }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

}
