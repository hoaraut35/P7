
package com.hoarauthomas.go4lunchthp7;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Period {

    @SerializedName("close")
    @Expose
    private com.hoarauthomas.go4lunchthp7.Close close;
    @SerializedName("open")
    @Expose
    private com.hoarauthomas.go4lunchthp7.Open open;

    public com.hoarauthomas.go4lunchthp7.Close getClose() {
        return close;
    }

    public void setClose(com.hoarauthomas.go4lunchthp7.Close close) {
        this.close = close;
    }

    public com.hoarauthomas.go4lunchthp7.Open getOpen() {
        return open;
    }

    public void setOpen(com.hoarauthomas.go4lunchthp7.Open open) {
        this.open = open;
    }

}
