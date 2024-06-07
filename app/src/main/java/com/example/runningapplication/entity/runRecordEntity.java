package com.example.runningapplication.entity;

import com.amap.api.maps2d.model.LatLng;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

public class runRecordEntity implements Serializable {
    private String targetDistance;
    private String ranDistance;
    private String spendTime;
    private String startRunTime;
    private List<LatLng> runLine;
    private String speed;
    private String address;
    private String cadence;
    public runRecordEntity(){}
    public runRecordEntity(String targetDistance,
                        String ranDistance,
                        String spendTime,
                        String startRunTime,
                        List<LatLng> runLine,
                        String speed,
                        String address, String cadence){
        this.targetDistance = targetDistance;
        this.address = address;
        this.cadence = cadence;
        this.speed = speed;
        this.startRunTime = startRunTime;
        this.ranDistance = ranDistance;
        this.spendTime = spendTime;
        this.runLine = runLine;
    }

    public List<LatLng> getRunLine() {
        return runLine;
    }

    public String getAddress() {
        return address;
    }

    public String getCadence() {
        return cadence;
    }

    public String getRanDistance() {
        return ranDistance;
    }

    public String getSpeed() {
        return speed;
    }

    public String getSpendTime() {
        return spendTime;
    }

    public String getStartRunTime() {
        return startRunTime;
    }

    public String getTargetDistance() {
        return targetDistance;
    }

    public void setTargetDistance(String targetDistance) {
        this.targetDistance = targetDistance;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCadence(String cadence) {
        this.cadence = cadence;
    }

    public void setRanDistance(String ranDistance) {
        this.ranDistance = ranDistance;
    }

    public void setRunLine(List<LatLng> runLine) {
        this.runLine = runLine;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public void setSpendTime(String spendTime) {
        this.spendTime = spendTime;
    }

    public void setStartRunTime(String startRunTime) {
        this.startRunTime = startRunTime;
    }
}
