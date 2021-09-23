
package com.hoarauthomas.go4lunchthp7.model.NearbySearch;

import java.util.List;

import com.hoarauthomas.go4lunchthp7.model.NearbySearch.RestaurantPojo;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Place {

    @SerializedName("html_attributions")
    @Expose
    private List<Object> htmlAttributions = null;
    @SerializedName("next_page_token")
    @Expose
    private String nextPageToken;
    @SerializedName("results")
    @Expose
    private List<RestaurantPojo> results = null;
    @SerializedName("status")
    @Expose
    private String status;

    public List<Object> getHtmlAttributions() {
        return htmlAttributions;
    }

    public void setHtmlAttributions(List<Object> htmlAttributions) {
        this.htmlAttributions = htmlAttributions;
    }

    public String getNextPageToken() {
        return nextPageToken;
    }

    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }

    public List<RestaurantPojo> getResults() {
        return results;
    }

    public void setResults(List<RestaurantPojo> results) {
        this.results = results;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
