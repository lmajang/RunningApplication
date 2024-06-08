package com.example.runningapplication.entity;

import androidx.annotation.NonNull;

public class locationEntity {
    private String latitude;
    private String longitude;

    public locationEntity(){
    }
    public locationEntity(String latitude,String longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    @NonNull
    @Override
    public String toString() {

        return "{"+"latitude"+":"+latitude+","+
                "longitude"+":"+longitude+"}";
    }
}
