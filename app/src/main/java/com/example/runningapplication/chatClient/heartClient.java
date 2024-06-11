package com.example.runningapplication.chatClient;

import android.util.Log;

import org.json.simple.JSONObject;

import java.io.ObjectOutputStream;
import java.net.Socket;

public class heartClient implements Runnable{
    private Socket socket;
    private ObjectOutputStream oos;

    public heartClient(Socket socket, ObjectOutputStream oos){
        this.socket = socket;
        this.oos = oos;
    }

    @Override
    public void run(){
        try {
            while(!Thread.currentThread().isInterrupted()){
                long startTime = System.currentTimeMillis();
                long endTime = startTime + 10 * 1000; // 10 seconds later

                while (System.currentTimeMillis() < endTime){}
//                Thread.sleep(10*1000);
                Log.d("statustest",String.valueOf(Thread.currentThread().isInterrupted()));
                if(Thread.currentThread().isInterrupted()) {
                    break;
                }
                JSONObject json = new JSONObject();
                json.put("type", "heart");
                json.put("msg", "123 checking connecting status");
                oos.writeObject(json);
                oos.flush();
            }
        }catch (Exception e){
            e.printStackTrace();
            try {
                socket.close();
                Client.connectionState = false;
                Client.reconnect();
            }catch (Exception ee){
                ee.printStackTrace();
            }
        }
    }
}
