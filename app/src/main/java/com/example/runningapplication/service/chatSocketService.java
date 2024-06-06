package com.example.runningapplication.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.runningapplication.Login.LoginActivity;
import com.example.runningapplication.chatClient.Client;
import com.example.runningapplication.config.appConfig;

public class chatSocketService extends Service {
    private String TAG = "chatSocketService";

    @Override
    public void onCreate() {
        super.onCreate();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG,LoginActivity.sp.getString("id",null));
                Client.connect(appConfig.socketAddress, 9999, LoginActivity.sp.getString("id",null));
            }
        }).start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
