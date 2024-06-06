package com.example.runningapplication;


import static com.example.runningapplication.DB.SQLStatements.createChatTB;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import com.example.runningapplication.Login.LoginActivity;
import com.example.runningapplication.DB.MyDBOpenHelper;
import com.example.runningapplication.chatClient.Client;
import com.example.runningapplication.config.appConfig;
import com.example.runningapplication.runningMain.runningMainActivity;
import com.example.runningapplication.runningMap.mapActivity;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SQLiteOpenHelper dbHelper = new MyDBOpenHelper(this);
        appConfig.sqLiteDatabase = dbHelper.getWritableDatabase();
////        创建聊天系统线程


        Intent i = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(i);

//        Intent i = new Intent(MainActivity.this, mapActivity.class);
//        startActivity(i);


    }


}