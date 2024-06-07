package com.example.runningapplication.runningMain.ui.runningCalendar;

import androidx.fragment.app.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.runningapplication.R;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class calendarFragment extends Fragment {

    private CalendarView calendarView;
    private Button signIn;

    private RecyclerView runningHistoryReView;
    private Map<String,Calendar> cList;
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
        initOnListener();
    }

    private void marketAllDate(Map<String,Calendar> CList){
        calendarView.setSchemeDate(CList);
    }

    private void initView(){
        signIn = (Button) view.findViewById(R.id.sign_in);
        calendarView = (CalendarView) view.findViewById(R.id.calendarView);
        runningHistoryReView = (RecyclerView) view.findViewById(R.id.running_history);
        cList = new HashMap<>();
    }

    private void initOnListener(){
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cList.put(marketDate().toString(),marketDate());
                marketAllDate(cList);
                signIn.setText("已签到");
                signIn.setTextColor(Color.WHITE);
                signIn.setEnabled(false);
            }
        });
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
