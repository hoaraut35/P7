package com.hoarauthomas.go4lunchthp7.model.PlaceDetails;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class PlaceDetailsFinal {

    @SerializedName("html_attributions")
    @Expose
    private List<Object> htmlAttributions = null;
    @SerializedName("result")
    @Expose
    private ResultPlaceDetail resultPlaceDetail;
    @SerializedName("status")
    @Expose
    private String status;

    public List<Object> getHtmlAttributions() {
        return htmlAttributions;
    }

    public void setHtmlAttributions(List<Object> htmlAttributions) {
        this.htmlAttributions = htmlAttributions;
    }

    public ResultPlaceDetail getResult() {
        return resultPlaceDetail;
    }

    public void setResult(ResultPlaceDetail resultPlaceDetail) {
        this.resultPlaceDetail = resultPlaceDetail;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
