package com.example.runningapplication.entity;

import androidx.annotation.NonNull;

import com.amap.api.maps.model.LatLng;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

public class runRecordEntity implements Serializable {

    private String recordId;
    private String targetDistance;
    private String ranDistance;
    private String spendTime;
    private String startTime;
    private String runLine;
    private String speed;
    private String address;
    private String cadence;

    private String uploadTime;

    private String runnerId;
    public runRecordEntity(){}
    public runRecordEntity(
            String recordId,
                    String targetDistance,
                        String ranDistance,
                        String spendTime,
                        String startTime,
                        String runLine,
                        String speed,
                        String address, String cadence,
                        String uploadTime,String runnerId){
        this.targetDistance = targetDistance;
        this.address = address;
        this.cadence = cadence;
        this.speed = speed;
        this.startTime = startTime;
        this.ranDistance = ranDistance;
        this.spendTime = spendTime;
        this.runLine = runLine;
        this.recordId = recordId;
        this.uploadTime = uploadTime;
        this.runnerId =runnerId;
    }

    public String getRunLine() {
        return runLine;
    }

    public String getRecordId() {
        return recordId;
    }

    public String getRunnerId() {
        return runnerId;
    }

    public String getUploadTime() {
        return uploadTime;
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

    public String getStartTime() {
        return startTime;
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

    public void setRunLine(String runLine) {
        this.runLine = runLine;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public void setSpendTime(String spendTime) {
        this.spendTime = spendTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public void setRunnerId(String runnerId) {
        this.runnerId = runnerId;
    }

    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
    }

    @NonNull
    @Override
    public String toString() {
        return targetDistance+","+
                ranDistance+","+
                 spendTime+","+
                startTime+","+
                 runLine+","+
                 speed+","+
                 address+","+cadence;
    }
}
