
package com.hoarauthomas.go4lunchthp7;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class PlaceAutocomplete {

    @SerializedName("predictions")
    @Expose
    private List<com.hoarauthomas.go4lunchthp7.Prediction> predictions = null;
    @SerializedName("status")
    @Expose
    private String status;

    public List<com.hoarauthomas.go4lunchthp7.Prediction> getPredictions() {
        return predictions;
    }

    public void setPredictions(List<com.hoarauthomas.go4lunchthp7.Prediction> predictions) {
        this.predictions = predictions;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
