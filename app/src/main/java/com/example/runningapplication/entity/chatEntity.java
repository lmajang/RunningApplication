package com.example.runningapplication.entity;


public class chatEntity {
    //发送
    public static final int SEND = 0;
    //接收
    public static final int RECEIVE = 1;

    private String username;
    private String context;
    private int TYPE;
    public chatEntity(){}
    public chatEntity(String username, String context, int TYPE){
        this.username = username;
        this.context = context;
        this.TYPE = TYPE;
    }
    public String getUsername() {
        return username;
    }

    public int getTYPE() {
        return TYPE;
    }

    public String getContext() {
        return context;
    }


    public void setUsername(String username) {
        this.username = username;
    }

    public void setContext(String context) {
        this.context = context;
    }


    public void setTYPE(int TYPE) {
        this.TYPE = TYPE;
    }
}
