package com.example.runningapplication;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.runningapplication.chatClient.Client;
import com.example.runningapplication.runningChat.chatActivity;
import com.example.runningapplication.runningMap.mapActivity;

public class MainActivity extends Activity {
    String userId = "123";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
////        创建聊天系统线程
        Thread chatThread = new Thread(() -> Client.connect("192.168.142.1", 9999, userId));
        //用户令牌验证请求
        try {
            chatThread.start();
            Thread.sleep(3000);
        }catch (Exception e){
            e.printStackTrace();
        }

        Intent i = new Intent(MainActivity.this, chatActivity.class);
        startActivity(i);

//        Intent i = new Intent(MainActivity.this, mapActivity.class);
//        startActivity(i);


    }


}