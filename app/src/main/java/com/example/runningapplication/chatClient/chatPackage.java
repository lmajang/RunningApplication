package com.example.runningapplication.chatClient;

import org.json.simple.JSONObject;

public class chatPackage {
    private JSONObject chatJson;

    public chatPackage(String chatString, String UserId, String ToUserId) {
        chatJson = new JSONObject();
        chatJson.put("type", "chat");
        chatJson.put("userId", UserId);
        chatJson.put("TargetUserId", ToUserId);
        chatJson.put("msg", chatString);
    }

    public JSONObject getChatPackage(){
        return chatJson;
    }
}
