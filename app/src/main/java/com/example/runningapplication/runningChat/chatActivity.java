package com.example.runningapplication.runningChat;

import android.app.Activity;
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

import com.example.runningapplication.Adapter.chatAdapter;
import com.example.runningapplication.R;
import com.example.runningapplication.chatClient.Client;
import com.example.runningapplication.chatClient.chatPackage;
import com.example.runningapplication.chatClient.sendClient;
import com.example.runningapplication.entity.chatEntity;

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

    public static Handler mainHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                receiveMassage((String)msg.obj);
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
//        发送监听器
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = msg.getText().toString();
                if(!messageText.isEmpty()){
                    Log.d("Massage",messageText);
                    chatPackage chatPackage = new chatPackage(messageText, Client.getUserId(), "1234");
                    JSONObject json = chatPackage.getChatPackage();
                    Log.d("json", json.toJSONString());
                    Client.sendclient.sendChatHandler.obtainMessage(1,json).sendToTarget();
                    Client.isSend = true;
                    sendMassage(messageText);
                    msg.setText("");
                }
            }
        });


    }

    private void initRecycle(){
        recyclerView = findViewById(R.id.chat_recyclerView);
        LinearLayoutManager LayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(LayoutManager);
        adapter = new chatAdapter(chatMsgList);
        recyclerView.setAdapter(adapter);
    }

//    发送信息
    private void sendMassage(String msg){
        chatEntity chatMsg = new chatEntity(R.drawable.img, Client.getUserId(), msg, chatEntity.SEND);
        chatMsgList.add(chatMsg);
        adapter.notifyItemInserted(chatMsgList.size() - 1);
        recyclerView.scrollToPosition(chatMsgList.size() - 1);
    }

//    接收信息
    private static void receiveMassage(String msg){
        chatEntity chatMsg = new chatEntity(R.drawable.img, "1234", msg, chatEntity.RECEIVE);
        chatMsgList.add(chatMsg);
        if(adapter == null || recyclerView ==null) return;
        adapter.notifyItemInserted(chatMsgList.size() - 1);
        recyclerView.scrollToPosition(chatMsgList.size() - 1);
    }

}
