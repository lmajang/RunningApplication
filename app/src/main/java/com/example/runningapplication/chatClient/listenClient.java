package com.example.runningapplication.chatClient;

import android.util.Log;

import org.json.JSONObject;

import java.io.ObjectInputStream;
import java.net.Socket;

public class listenClient implements Runnable{
    private Socket socket;
    private ObjectInputStream ois;
    public listenClient(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run(){
        try {
            ois = new ObjectInputStream(socket.getInputStream());
            while (true){
                JSONObject json = (JSONObject)ois.readObject();
                Log.d("receiveMsg", json.toString());
                if(json.get("type").equals("chat")){
                    String msg = (String) json.get("msg");
                    Log.d("receiveMsg", msg);

                }
            }
        }catch (Exception e){
            System.out.println("listenException");
            e.printStackTrace();
        }

    }
}
