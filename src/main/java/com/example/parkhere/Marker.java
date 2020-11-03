package com.example.parkhere;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;
import java.util.HashMap;

public class Marker {

    private String creatorId;
    private Long creationTime;
    private LatLng location;

    public Marker(LatLng location, User creator) {
        this.location = location;
        this.creatorId = creator.getFirebaseId();
        Date timeNow = new Date();
        this.creationTime = timeNow.getTime();
    }

    public Marker(HashMap<String, Object> markerValue) {
        this.creatorId = (String) markerValue.get("creatorId");
        this.creationTime = (Long) markerValue.get("creationTime");
        double latitude = (double) markerValue.get("latitude");
        double longitude = (double) markerValue.get("longtitude");
        this.location = new LatLng(latitude, longitude);
    }

    public HashMap<String, Object> getMarkerValues (){
        HashMap <String, Object> markerValue = new HashMap<>();
        markerValue.put("creatorId", creatorId);
        markerValue.put("creationTime", creationTime);
        markerValue.put("latitude", location.latitude);
        markerValue.put("longtitude", location.longitude);
        return markerValue;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreator(String creatorId) {
        this.creatorId = creatorId;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }


}
