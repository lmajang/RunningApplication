package com.example.runningapplication.chatClient;

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
                Thread.sleep(10*1000);
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
