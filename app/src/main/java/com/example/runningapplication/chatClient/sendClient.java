package com.example.runningapplication.chatClient;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import org.json.simple.JSONObject;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class sendClient implements Runnable{
    private Socket socket;
    private ObjectOutputStream oos;

    private static JSONObject chatJson;

    public Handler sendChatHandler;

    public sendClient(Socket socket, ObjectOutputStream oos){
        this.socket = socket;
        this.oos = oos;
    }

    public void sendMsg(JSONObject msgJson){
        try{
            oos.writeObject(msgJson);
            oos.flush();
//            sendImageClient sic = new sendImageClient(oos);
//            sic.sendImage("src/main/java/org/example/Client/assets/testImage.png");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        Looper.prepare();
        sendChatHandler = new Handler(){
            public void handleMessage(Message msg){
                switch (msg.what){
                    case 1:
                        sendMsg((JSONObject) msg.obj);
                }
            }
        };
        Looper.loop();

//        while(true){
//            if(Client.isSend && chatJson!=null){
//
//            }
//        }
    }
}
