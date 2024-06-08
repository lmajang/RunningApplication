package com.example.runningapplication.entity;

import androidx.annotation.NonNull;

import java.sql.Timestamp;

public class friendNoticeEntity {
    String noticeId;
    String senderId;
    String sender_hp_id;
    String sender_username;
    String sender_message;
    public friendNoticeEntity(){}

    public friendNoticeEntity(
            String noticeId,
            String senderId,
            String sender_hp_id,
            String sender_username,
            String sender_message
    ){
        this.noticeId = noticeId;
        this.sender_message = sender_message;
        this.sender_username = sender_username;
        this.senderId = senderId;
        this.sender_hp_id = sender_hp_id;
    }
    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public void setNoticeId(String noticeId) {
        this.noticeId = noticeId;
    }

    public void setSender_hp_id(String sender_hp_id) {
        this.sender_hp_id = sender_hp_id;
    }

    public void setSender_message(String sender_message) {
        this.sender_message = sender_message;
    }

    public void setSender_username(String sender_username) {
        this.sender_username = sender_username;
    }


    public String getSenderId() {
        return senderId;
    }

    public String getNoticeId() {
        return noticeId;
    }

    public String getSender_hp_id() {
        return sender_hp_id;
    }

    public String getSender_message() {
        return sender_message;
    }

    public String getSender_username() {
        return sender_username;
    }

    @NonNull
    @Override
    public String toString() {
        return noticeId+","+senderId
        +","+sender_hp_id
        +","+sender_username
        +","+sender_message;
    }
}
