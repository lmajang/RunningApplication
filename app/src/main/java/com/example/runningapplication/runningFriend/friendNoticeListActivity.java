package com.example.runningapplication.runningFriend;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.example.runningapplication.Adapter.chatAdapter;
import com.example.runningapplication.Adapter.noticeListAdapter;
import com.example.runningapplication.R;
import com.example.runningapplication.chatClient.Client;
import com.example.runningapplication.config.appConfig;
import com.example.runningapplication.entity.friendNoticeEntity;
import com.example.runningapplication.utils.httpTools;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class friendNoticeListActivity extends Activity {
    private RecyclerView noticeRecyclerView;
    private View view;

    private noticeListAdapter noticeAdapter;

    public static List<friendNoticeEntity> noticeList;
    private static final String TAG = "friendNoticeListActivity";

    private static final int UPDATE_NOTICE = 1;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == UPDATE_NOTICE){
                System.out.println("22222222222222222222222222222222222222222222222222222222222222222222222222222222222");
                System.out.println(msg.obj);
                noticeList.addAll(JSON.parseArray(msg.obj.toString(), friendNoticeEntity.class));
                noticeAdapter.notifyItemInserted(noticeList.size() - 1);
                noticeRecyclerView.scrollToPosition(noticeList.size() - 1);
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_list);
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initNotice();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void init(){
        noticeList = new ArrayList<>();
        noticeRecyclerView = (RecyclerView) findViewById(R.id.notice_list_recyclerView);
        LinearLayoutManager LayoutManager = new LinearLayoutManager(this);
        noticeRecyclerView.setLayoutManager(LayoutManager);
        noticeAdapter = new noticeListAdapter(noticeList);
        noticeRecyclerView.setAdapter(noticeAdapter);
    }

    private void initNotice(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("userId", Client.getUserId());
                    String NoticeList = httpTools.post(appConfig.ipAddress+"/findAllFriendNotice",jsonObject.toString());
                    if(!NoticeList.isEmpty()){
                        handler.obtainMessage(UPDATE_NOTICE,NoticeList).sendToTarget();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
