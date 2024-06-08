package com.example.runningapplication.runningFriend;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.runningapplication.R;
import com.example.runningapplication.View.Avatar;
import com.example.runningapplication.View.CircularStatView;
import com.example.runningapplication.config.appConfig;
import com.example.runningapplication.entity.friendEntity;
import com.example.runningapplication.runningChat.chatActivity;

import org.json.JSONObject;

import java.util.Calendar;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class friendActivity extends Activity {
    TextView textView1,textView2,textView3;
    String run="",avatarnum=null,message=null,target="",receivername="";
    Avatar avatar;
    private ProgressBar progressBar;
    Button btn1,btn2;
    SharedPreferences sp;
    CircularStatView circularStatView;
    private static final String TAG = "friendActivity";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_freind);
        circularStatView=this.findViewById(R.id.circularStatView);
        avatar=this.findViewById(R.id.Avatar);
        progressBar=this.findViewById(R.id.progressBar);
        btn1=this.findViewById(R.id.button1);
        btn2=this.findViewById(R.id.button2);
        textView1=this.findViewById(R.id.text1);
        textView2=this.findViewById(R.id.text2);
        textView3=this.findViewById(R.id.text3);
        Intent intent=getIntent();
        String receiverid=intent.getStringExtra("receiverid");
        Log.d(TAG,receiverid);
        sp = this.getSharedPreferences("user", this.MODE_PRIVATE);
        String senderid=sp.getString("id",null);
        progressBar.setProgress(75);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    FormBody formBody = new FormBody.Builder().add("friendId", receiverid).add("userId",senderid).build();
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(appConfig.ipAddress+"/isFriend")
                            .post(formBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    final String responseData = response.body().string();
                    Log.d(TAG,responseData);
                    if(responseData.equals("1")){
                        btn1.setVisibility(View.INVISIBLE);
                        btn2.setVisibility(View.VISIBLE);
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    FormBody formBody = new FormBody.Builder().add("id", receiverid).build();
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(appConfig.ipAddress+"/user2")
                            .post(formBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    final String responseData = response.body().string();
                    JSONObject jsonObject = new JSONObject(responseData);
                    avatarnum=jsonObject.getString("avatar");
                    message=jsonObject.getString("message");
                    receivername=jsonObject.getString("name");
                    target=jsonObject.getString("target");
                    avatarchange(avatarnum);
                    textView1.setText(receivername);
                    textView2.setText("个性签名:\n"+message);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Calendar cd = Calendar.getInstance();
                    String year= String.valueOf(cd.get(Calendar.YEAR));
                    String month= String.valueOf(cd.get(Calendar.MONTH)+1);
                    String day= String.valueOf(cd.get(Calendar.DATE));
                    FormBody formBody = new FormBody.Builder().add("id", receiverid).add("date",year+"/"+month+"/"+day+"").build();
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(appConfig.ipAddress+"/home2")
                            .post(formBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    final String responseData = response.body().string();
                    run=responseData;
                    while(target.equals("")){}
                    textView3.setText("今日已跑:\n"+run+"公里/"+target+"公里");
                    // 设置圆环统计图的两个部分的百分比
                    circularStatView.setPercentage((float) (Double.parseDouble(run)/Double.parseDouble(target))*100);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                FormBody formBody = new FormBody.Builder().add("senderid", senderid).add("receiverid",receiverid).build();
                                OkHttpClient client = new OkHttpClient();
                                Request request = new Request.Builder()
                                        .url(appConfig.ipAddress+"/user5")
                                        .post(formBody)
                                        .build();
                                Response response = client.newCall(request).execute();
                                final String responseData = response.body().string();
                                if(responseData.equals("true")){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(friendActivity.this, "发送请求成功!", Toast.LENGTH_SHORT).show();
                                        }
                                        });
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }).start();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            FormBody formBody = new FormBody.Builder().add("id", senderid).build();
                            OkHttpClient client = new OkHttpClient();
                            Request request = new Request.Builder()
                                    .url(appConfig.ipAddress+"/user6")
                                    .post(formBody)
                                    .build();
                            Response response = client.newCall(request).execute();
                            final String responseData = response.body().string();
                            Intent intent = new Intent();
                            intent.setClass(friendActivity.this, chatActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            friendEntity friend=new friendEntity(receiverid,avatarnum,receivername);
                            intent.putExtra("data", friend);
                            intent.putExtra("avatar",responseData);
                            startActivity(intent);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }
    public void avatarchange(String avatarnum){
        if(avatarnum.equals("1")) avatar.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.avatar1));
        if(avatarnum.equals("2")) avatar.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.avatar2));
        if(avatarnum.equals("3")) avatar.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.avatar3));
        if(avatarnum.equals("4")) avatar.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.avatar4));
        if(avatarnum.equals("5")) avatar.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.avatar5));
    }

}
