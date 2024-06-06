package com.example.runningapplication.DB;

import android.content.ContentValues;

import com.example.runningapplication.config.appConfig;
import com.example.runningapplication.entity.chatEntity;
import com.example.runningapplication.entity.friendEntity;

import java.sql.Timestamp;

public class insertDB {
    static public void insertFriendEntity(friendEntity friend){
        ContentValues values = new ContentValues();
        values.put("friend_id", friend.getId());
        values.put("avatar",friend.getFriend_hpId());
        values.put("username",friend.getUsername());
        long newRowId = appConfig.sqLiteDatabase.insert("friendlist",null,values);
    }

    static public void insertChatEntity(String sender_id, String receiver_id, String message){
        ContentValues values = new ContentValues();
        values.put("sender_id", sender_id);
        values.put("receiver_id",receiver_id);
        values.put("message",message);
        values.put("timestamp", String.valueOf(new Timestamp(System.currentTimeMillis())));
        long newRowId = appConfig.sqLiteDatabase.insert("chat",null,values);
    }
}
