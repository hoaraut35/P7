package com.hoarauthomas.go4lunchthp7.model;



import android.media.Image;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

//This is a Model Class for API Google Places
public class Place {


    @SerializedName("next_page_token")
    @Expose
    private String nextPageToken;
    @SerializedName("status")
    @Expose
    private String status;

    /**
     * @return The htmlAttributions
     */


    /**
     * @param htmlAttributions The html_attributions
     */
    public void setHtmlAttributions(List<Object> htmlAttributions) {
        //this.htmlAttributions = htmlAttributions;
    }

    /**
     * @return The nextPageToken
     */
    public String getNextPageToken() {
        return nextPageToken;
    }

    /**
     * @param nextPageToken The next_page_token
     */
    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }

    /**
     * @return The results
     */

    /**
     * @param results The results
     */


    /**
     * @return The status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status The status
     */
    public void setStatus(String status) {
        this.status = status;
    }
}