package com.hoarauthomas.go4lunchthp7.ui.map;

import com.google.android.gms.maps.model.LatLng;

/**
 * Class for marker object, used for create a list from googgle map with tag id on marker
 */
public class MyMarkerObject {
    String id;
    LatLng location;

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public MyMarkerObject(String id, LatLng location) {
        this.id = id;
        this.location = location;
    }
}
