package com.example.runningapplication.entity;

import androidx.annotation.NonNull;

import java.io.Serializable;



public class friendEntity implements Serializable {
    private String id;
    private String friend_hpId;
    private String username;


    public friendEntity(){}

    public friendEntity(String id,String friend_hpId,String username){
        this.id = id;
        this.friend_hpId = friend_hpId;
        this.username = username;
    }
    public String getId() {
        return id;
    }

    public String getFriend_hpId() {
        return friend_hpId;
    }

    public String getUsername() {
        return username;
    }

    public void setFriend_hpId(String friend_hpId) {
        this.friend_hpId = friend_hpId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }
}
