package com.example.runningapplication.runningChat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
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
    private static final String TAG = "chatActivity";
    @SuppressLint("HandlerLeak")
    public static Handler mainHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                receiveMassage((String)msg.obj,true);
            }else if (msg.what == INIT_CHAT){
                if(!chatMsgList.isEmpty()){
                    chatMsgList.clear();
                }
                List<singleChatEntity> sChatList = JSON.parseArray(msg.obj.toString(),singleChatEntity.class);
                if(!sChatList.isEmpty()){
                    for(singleChatEntity singleChat:sChatList){
                        if(singleChat.getSenderId().equals(Client.getUserId())){
                            chatMsgList.add(new chatEntity(Client.getUserId(),singleChat.getMessage(),chatEntity.SEND));
                        }else if(singleChat.getReceiverId().equals(Client.getUserId())){
                            chatMsgList.add(new chatEntity(friend.getUsername(),singleChat.getMessage(),chatEntity.RECEIVE));
                        }
                    }
                }
                adapter.notifyDataSetChanged();
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
        initChatPage();
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
        chatEntity chatMsg = new chatEntity(Client.getUserId(), msg, chatEntity.SEND);
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
                    json.put("sender_id",Client.getUserId());
                    json.put("receiver_id",friend.getId());
                    String getChatListJson = httpTools.post(appConfig.ipAddress+"/FindFriendChat",json.toJSONString());
                    Log.d(TAG,getChatListJson);
                    if(getChatListJson!=null){
                        mainHandler.obtainMessage(INIT_CHAT,getChatListJson).sendToTarget();
                    }else {
//                        本地记录
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
