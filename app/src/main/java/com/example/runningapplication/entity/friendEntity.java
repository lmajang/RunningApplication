package com.example.runningapplication.entity;

public class friendEntity {
    private int friend_hpId;
    private String username;
    private String firstChat;
    private boolean clickStatus;

    public friendEntity(int friend_hpId, String username, String firstChat, boolean clickStatus){
        this.friend_hpId = friend_hpId;
        this.username = username;
        this.firstChat = firstChat;
        this.clickStatus = clickStatus;
    }

    public String getUsername() {
        return username;
    }

    public boolean isClickStatus() {
        return clickStatus;
    }

    public int getFriend_hpId() {
        return friend_hpId;
    }

    public String getFirstChat() {
        return firstChat;
    }
}
