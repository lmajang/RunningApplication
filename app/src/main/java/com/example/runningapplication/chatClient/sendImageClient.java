package com.example.runningapplication.chatClient;

import org.json.simple.JSONObject;

import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class sendImageClient {
    private ObjectOutputStream oos;

    private FileInputStream fis;

    public sendImageClient(ObjectOutputStream oos){
        this.oos = oos;
    }

    public void sendImage(String imgPath){

        byte[] bytes = new byte[1024];
        int len = 0;
        try {
            fis = new FileInputStream(imgPath);
            //发送图片信息
            JSONObject json = new JSONObject();
            json.put("type", "image");

            json.put("userId", "123");
            json.put("TargetUserId", "1234");
            oos.writeObject(json);
            oos.flush();

            while((len = fis.read(bytes)) != -1){
                oos.write(bytes, 0, len);
            }

            oos.flush();
            System.out.println("发送图片完成");
            fis.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
