package com.example.runningapplication.chatClient;

import android.util.Log;

import com.example.runningapplication.runningChat.chatActivity;

import org.json.simple.JSONObject;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class Client {

    private static String ipAddress;

    public static boolean isSend = false;
    private static int port;

    private static String userId;
    private static Socket socket;
    private static ObjectOutputStream oos;

    private static ObjectInputStream ois;

    private static Thread listenClientThread;
    private static Thread sendClientThread;
    private static Thread heartClientThread;

    public static sendClient sendclient;
    public static boolean connectionState = false;

    public static void connect(String ipAd, int Iport, String id){
        try {
            ipAddress = ipAd;
            port = Iport;
            userId = id;
            socket = new Socket(ipAddress,port);
            Log.d("connectState:", String.valueOf(socket.isConnected()));
            oos = new ObjectOutputStream(socket.getOutputStream());
            //发送id
            JSONObject json = new JSONObject();
            json.put("type", "identity");
            json.put("userId", userId);
            oos.writeObject(json);
            oos.flush();

            sendclient = new sendClient(socket,oos);
            sendClientThread = new Thread(sendclient);
            heartClientThread = new Thread(new heartClient(socket,oos));
            sendClientThread.start();
            heartClientThread.start();

            ois = new ObjectInputStream(socket.getInputStream());
            connectionState = true;
//
//            listenClientThread = new Thread(new listenClient(socket,ois));
//
//            listenClientThread.start();
//            保持连接
            while (true){
                    json = (JSONObject)ois.readObject();
                    if(json.get("type").equals("chat")){
                        String msg = (String) json.get("msg");
                        chatActivity.mainHandler.obtainMessage(1,msg).sendToTarget();
                        Log.d("receiveMsg", msg);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            connectionState = false;
        }
    }

    public static void reconnect(){
        while (!connectionState){
            System.out.println("reconnecting...");
            connect(ipAddress, port, userId);
            try {
                Thread.sleep(5*1000);
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    public static String getUserId() {
        return userId;
    }
}