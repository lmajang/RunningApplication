package com.example.runningapplication.DB;

public class SQLStatements {
    public static String createChatTB = "create table chat(" +
            "chat_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "sender_id TEXT,"+
            "receiver_id TEXT,"+
            "message TEXT NOT NULL,"+
            "timestamp TIMESTAMP NOT NULL"+
            ")";

    public static String createFriendListTB = "create table friendlist(" +
            "friend_id TEXT PRIMARY KEY NOT NULL," +
            "avatar TEXT ,"+
            "username TEXT NOT NULL"+
            ")";


}
