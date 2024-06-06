package com.example.runningapplication.entity;


public class singleChatEntity {
    private String senderId;

    private String receiverId;

    private String message;

    private String timestamp;
    public singleChatEntity(){}
    public singleChatEntity(String senderId,String receiverId,String message,String timestamp){
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.message = message;
        this.timestamp = timestamp;
    }
    public String getMessage() {
        return message;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
