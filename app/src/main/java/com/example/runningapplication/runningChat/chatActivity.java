package com.example.runningapplication.runningChat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.example.runningapplication.Adapter.chatAdapter;
import com.example.runningapplication.DB.insertDB;
import com.example.runningapplication.Login.LoginActivity;
import com.example.runningapplication.R;
import com.example.runningapplication.chatClient.Client;
import com.example.runningapplication.chatClient.chatPackage;
import com.example.runningapplication.chatClient.sendClient;
import com.example.runningapplication.config.appConfig;
import com.example.runningapplication.entity.chatEntity;
import com.example.runningapplication.entity.friendEntity;
import com.example.runningapplication.entity.singleChatEntity;
import com.example.runningapplication.utils.httpTools;

import org.json.simple.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class chatActivity extends Activity {
    private static List<chatEntity> chatMsgList = new ArrayList<chatEntity>();
    private static RecyclerView recyclerView;
    private static chatAdapter adapter;
    private EditText msg;
    private TextView sendBtn;
    static private final int INIT_CHAT = 2;
    static friendEntity friend;

    private static String userId = LoginActivity.sp.getString("id",null);
    private static String username = LoginActivity.sp.getString("username",null);
    private static final String TAG = "chatActivity";
    @SuppressLint("HandlerLeak")
    public static Handler mainHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                receiveMassage((String)msg.obj,true);
            }else if (msg.what == INIT_CHAT){
                List<singleChatEntity> sChatList = JSON.parseArray(msg.obj.toString(),singleChatEntity.class);
                if(!sChatList.isEmpty()){
                    for(singleChatEntity singleChat:sChatList){
                        insertDB.insertChatEntity(friend.getId(),userId,singleChat.getMessage());
                        if(singleChat.getSenderId().equals(Client.getUserId())){
                            chatMsgList.add(new chatEntity(username,singleChat.getMessage(),chatEntity.SEND));
                        }else if(singleChat.getReceiverId().equals(Client.getUserId())){
                            chatMsgList.add(new chatEntity(friend.getUsername(),singleChat.getMessage(),chatEntity.RECEIVE));
                        }
                    }
                }
                adapter = new chatAdapter(chatMsgList);
                recyclerView.setAdapter(adapter);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initRecycle();
        msg = findViewById(R.id.send_msg);
        sendBtn = findViewById(R.id.send_msg_btn);

        Intent intent = getIntent();
        friend= (friendEntity) intent.getSerializableExtra("data");
        Log.d(TAG, friend.getUsername());
//        发送监听器
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = msg.getText().toString();
                if(!messageText.isEmpty()){
                    Log.d("Massage",messageText);
                    chatPackage chatPackage = new chatPackage(messageText, Client.getUserId(), friend.getId());
                    JSONObject json = chatPackage.getChatPackage();
                    Log.d("json", json.toJSONString());
                    Client.sendclient.sendChatHandler.obtainMessage(1,json).sendToTarget();
                    Client.isSend = true;
                    sendMassage(messageText,true);
                    msg.setText("");
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!chatMsgList.isEmpty()){
            chatMsgList.clear();
        }
        initChatPage();
        initChatPageLocal();
    }

    private void initRecycle(){
        recyclerView = findViewById(R.id.chat_recyclerView);
        LinearLayoutManager LayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(LayoutManager);
        adapter = new chatAdapter(chatMsgList);
        recyclerView.setAdapter(adapter);
    }

//    发送信息
    private void sendMassage(String msg,boolean newChat){
        chatEntity chatMsg = new chatEntity(username, msg, chatEntity.SEND);
        if(newChat) chatMsgList.add(chatMsg);
        insertDB.insertChatEntity(userId,friend.getId(),msg);
        adapter.notifyItemInserted(chatMsgList.size() - 1);
        recyclerView.scrollToPosition(chatMsgList.size() - 1);
    }

//    接收信息
    private static void receiveMassage(String msg,boolean newChat){
        chatEntity chatMsg = new chatEntity(friend.getUsername(), msg, chatEntity.RECEIVE);
        if(newChat) chatMsgList.add(chatMsg);
        insertDB.insertChatEntity(friend.getId(),userId,msg);
        if(adapter == null || recyclerView ==null) return;
        adapter.notifyItemInserted(chatMsgList.size() - 1);
        recyclerView.scrollToPosition(chatMsgList.size() - 1);
    }

    private void initChatPage(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject json = new JSONObject();
                    json.put("userId",Client.getUserId());
                    json.put("sendId",friend.getId());
                    String getChatListJson = httpTools.post(appConfig.ipAddress+"/FindNoReceiveChat",json.toJSONString());
                    Log.d(TAG,getChatListJson);
                    if(getChatListJson!=null){
                        mainHandler.obtainMessage(INIT_CHAT,getChatListJson).sendToTarget();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private void initChatPageLocal(){

        String[] columns = {"sender_id","receiver_id","message"};

        String whereClause = "(sender_id = ? AND receiver_id = ?) OR (sender_id = ? AND receiver_id = ?)";

        String[] whereArgs = {friend.getId(), Client.getUserId(), Client.getUserId(), friend.getId()};
//                        本地记录
        Cursor cursor = appConfig.sqLiteDatabase.query("chat",columns,whereClause,whereArgs,null,null,"timestamp ASC",null);


        if(cursor.getCount()>0){
            cursor.moveToFirst();
            Log.d(TAG,String.valueOf(cursor.getCount()));
            while (cursor.moveToNext()){
                System.out.println(cursor.getString(cursor.getColumnIndexOrThrow("message"))+":"+cursor.getString(cursor.getColumnIndexOrThrow("receiver_id")));
                if(Client.getUserId().equals(cursor.getString(cursor.getColumnIndexOrThrow("sender_id")))){

                    chatMsgList.add(new chatEntity(username,cursor.getString(cursor.getColumnIndexOrThrow("message")),chatEntity.SEND));
                }else if(friend.getId().equals(cursor.getString(cursor.getColumnIndexOrThrow("sender_id")))){

                    chatMsgList.add(new chatEntity(friend.getUsername(),cursor.getString(cursor.getColumnIndexOrThrow("message")),chatEntity.RECEIVE));
                }
            }
            adapter.notifyItemInserted(chatMsgList.size() - 1);
            recyclerView.scrollToPosition(chatMsgList.size() - 1);
        }
        // 完成后关闭cursor
        cursor.close();
    }
}
