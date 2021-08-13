
package com.hoarauthomas.go4lunchthp7;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RestaurantDetailPojo {

    @SerializedName("formatted_phone_number")
    @Expose
    private String formattedPhoneNumber;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("opening_hours")
    @Expose
    private com.hoarauthomas.go4lunchthp7.OpeningHours2 openingHours;
    @SerializedName("rating")
    @Expose
    private Double rating;

    public String getFormattedPhoneNumber() {
        return formattedPhoneNumber;
    }

    public void setFormattedPhoneNumber(String formattedPhoneNumber) {
        this.formattedPhoneNumber = formattedPhoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public com.hoarauthomas.go4lunchthp7.OpeningHours2 getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(com.hoarauthomas.go4lunchthp7.OpeningHours2 openingHours) {
        this.openingHours = openingHours;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

}
