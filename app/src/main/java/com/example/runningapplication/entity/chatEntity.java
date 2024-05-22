package com.example.runningapplication.entity;

public class chatEntity {
    //发送
    public static final int SEND = 0;
    //接收
    public static final int RECEIVE = 1;

    private int imgFaceId;
    private String username;
    private String context;

    private int TYPE;

    public chatEntity(int imgFaceId, String username, String context, int TYPE){
        this.imgFaceId = imgFaceId;
        this.username = username;
        this.context = context;
        this.TYPE = TYPE;
    }

    public int getImgFaceId() {
        return imgFaceId;
    }

    public String getUsername() {
        return username;
    }

    public String getContext() {
        return context;
    }

    public int getTYPE() {
        return TYPE;
    }
}
