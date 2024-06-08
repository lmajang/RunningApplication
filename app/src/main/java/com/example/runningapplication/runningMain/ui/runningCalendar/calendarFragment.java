package com.example.runningapplication.runningMain.ui.runningCalendar;

import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.example.runningapplication.Adapter.friendListAdapter;
import com.example.runningapplication.Adapter.runRecordAdapter;
import com.example.runningapplication.R;
import com.example.runningapplication.chatClient.Client;
import com.example.runningapplication.config.appConfig;
import com.example.runningapplication.entity.calendarRecordEntity;
import com.example.runningapplication.entity.runRecordEntity;
import com.example.runningapplication.utils.httpTools;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;

import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class calendarFragment extends Fragment {

    private CalendarView calendarView;

    runRecordAdapter adapter;
    private Button signIn;

    private RecyclerView recyclerView;
    private RecyclerView runningHistoryReView;
    private Map<String,Calendar> cList;

    private List<runRecordEntity> runRecordList;
    private static final String TAG = "calendarFragment";

    private static final int GET_ALL_RUN_RECORD = 1001;
    private static final int GET_ALL_MARKET_RECORD = 1002;

    private static final int GET_SIGN_IN_STATUS = 1003;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == GET_ALL_RUN_RECORD){
                if(!runRecordList.isEmpty()) runRecordList.clear();

                List<runRecordEntity> list = JSON.parseArray(msg.obj.toString(),runRecordEntity.class);
                Log.d(TAG,String.valueOf(list.size()));
                runRecordList.addAll(list);
                if (!runRecordList.isEmpty()){
                    adapter.notifyItemInserted(runRecordList.size() - 1);
                    recyclerView.scrollToPosition(runRecordList.size() - 1);
                }
            }else if (msg.what == GET_ALL_MARKET_RECORD){
                if(!cList.isEmpty()) cList.clear();
                Log.d(TAG,msg.obj.toString());
                List<calendarRecordEntity> list = JSON.parseArray(msg.obj.toString(),calendarRecordEntity.class);
                for(calendarRecordEntity calendarRecord: list){
                    Calendar calendar = marketDate(Integer.parseInt(calendarRecord.getRecordYear()),
                            Integer.parseInt(calendarRecord.getRecordMonth()),
                            Integer.parseInt(calendarRecord.getRecordDay()));
                    cList.put(calendar.toString(),calendar);
                }
                marketAllDate(cList);
            }else if(msg.what == GET_SIGN_IN_STATUS){
                if(msg.obj.toString().equals("1")){
                    signIn.setText("今日已签到");
                    signIn.setTextColor(Color.WHITE);
                    signIn.setEnabled(false);
                }
            }
        }
    };
    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_calendar,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initRecyclerView();
        initOnListener();
        initInformationRecord();
    }

    private void marketAllDate(Map<String,Calendar> CList){
        calendarView.setSchemeDate(CList);
    }

    private void initView(){
        signIn = (Button) view.findViewById(R.id.sign_in);
        calendarView = (CalendarView) view.findViewById(R.id.calendarView);
        runningHistoryReView = (RecyclerView) view.findViewById(R.id.running_history);
        cList = new HashMap<>();
        runRecordList = new ArrayList<>();
    }

    private void initOnListener(){
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("userId",Client.getUserId());
                            jsonObject.put("year",calendarView.getCurYear());
                            jsonObject.put("month",calendarView.getCurMonth());
                            jsonObject.put("day",calendarView.getCurDay());
                            String isSuccess = httpTools.post(appConfig.ipAddress+"/setCalendarRecord",jsonObject.toString());
                            if(isSuccess.equals("0")){
                                Toast.makeText(getContext(),"签到失败",Toast.LENGTH_SHORT).show();
                            }else {
                                cList.put(marketDate().toString(),marketDate());
                                marketAllDate(cList);
                                signIn.setText("今日已签到");
                                signIn.setTextColor(Color.WHITE);
                                signIn.setEnabled(false);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }

    private void initRecyclerView(){
        recyclerView = view.findViewById(R.id.running_history);
        LinearLayoutManager LayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(LayoutManager);
        adapter = new runRecordAdapter(runRecordList);
        recyclerView.setAdapter(adapter);

    }

    private void initInformationRecord(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("userId", Client.getUserId());
                    String recordList = httpTools.post(appConfig.ipAddress+"/findAllRecord",jsonObject.toJSONString());
                    if(!recordList.isEmpty()){
                        handler.obtainMessage(GET_ALL_RUN_RECORD,recordList).sendToTarget();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("userId", Client.getUserId());
                    String recordList = httpTools.post(appConfig.ipAddress+"/findAllCalendarRecord",jsonObject.toJSONString());
                    if(!recordList.isEmpty()){
                        handler.obtainMessage(GET_ALL_MARKET_RECORD,recordList).sendToTarget();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("userId",Client.getUserId());
                    jsonObject.put("year",calendarView.getCurYear());
                    jsonObject.put("month",calendarView.getCurMonth());
                    jsonObject.put("day",calendarView.getCurDay());
                    String now_SignInStatus = httpTools.post(appConfig.ipAddress+"/isCalendarsRecord",jsonObject.toJSONString());
                    handler.obtainMessage(GET_SIGN_IN_STATUS,now_SignInStatus).sendToTarget();
                }catch (Exception e){
                    e.printStackTrace();
                }


            }
        }).start();

    }
    private Calendar marketDate(){
        Calendar calendar = new Calendar();
        calendar.setYear(calendarView.getCurYear());
        calendar.setMonth(calendarView.getCurMonth());
        calendar.setDay(calendarView.getCurDay());
        calendar.setSchemeColor(Color.GREEN);//如果单独标记颜色、则会使用这个颜色
        calendar.setScheme("跑");
        return calendar;
    }
    private Calendar marketDate(int year, int month, int day){
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(Color.GREEN);//如果单独标记颜色、则会使用这个颜色
        calendar.setScheme("跑");
        return calendar;
    }
}
