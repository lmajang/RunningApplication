package com.example.runningapplication;


import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import com.example.runningapplication.Login.LoginActivity;
import com.example.runningapplication.DB.MyDBOpenHelper;
import com.example.runningapplication.config.appConfig;
import com.example.runningapplication.runningMap.mapActivity;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SQLiteOpenHelper dbHelper = new MyDBOpenHelper(this);
        appConfig.sqLiteDatabase = dbHelper.getWritableDatabase();


        Intent i = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(i);
        finish();



//        Intent i = new Intent(MainActivity.this, mapActivity.class);
//        startActivity(i);


    }


}