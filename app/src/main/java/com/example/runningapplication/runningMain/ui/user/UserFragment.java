package com.example.runningapplication.runningMain.ui.user;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.runningapplication.Login.ChangePwdActivity;
import com.example.runningapplication.Login.LoginActivity;
import com.example.runningapplication.R;
import com.example.runningapplication.View.Avatar;
import com.example.runningapplication.chatClient.Client;
import com.example.runningapplication.config.appConfig;
import com.example.runningapplication.runningFriend.friendNoticeListActivity;
import com.example.runningapplication.runningMain.runningMainActivity;
import com.example.runningapplication.utils.httpTools;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UserFragment extends Fragment {
    TextView textView1,textView2,textView3;
    String avatarnum=null,message=null,username=null,target=null;
    private View view;
    Avatar avatar;
    private ProgressBar progressBar;
    Button btn1,btn2,btn3,btn4;
    ImageButton NoticeButton;
    SharedPreferences sp;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user,container,false);
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        avatar=view.findViewById(R.id.Avatar);
        progressBar=view.findViewById(R.id.progressBar);
        btn1=view.findViewById(R.id.button1);
        btn2=view.findViewById(R.id.button2);
        btn3=view.findViewById(R.id.button3);
        btn4=view.findViewById(R.id.button4);
        NoticeButton = view.findViewById(R.id.btnImage);
        textView1=view.findViewById(R.id.text1);
        textView2=view.findViewById(R.id.text2);
        textView3=view.findViewById(R.id.text3);

        sp = this.getActivity().getSharedPreferences("user", this.getActivity().MODE_PRIVATE);
        String id=sp.getString("id",null);
        String name=sp.getString("username",null);
        progressBar.setProgress(75);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    FormBody formBody = new FormBody.Builder().add("id", id).build();
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
                    username=jsonObject.getString("name");
                    target=jsonObject.getString("target");
                    avatarchange(avatarnum);
                    textView1.setText(name);
                    textView2.setText("个性签名:\n"+message);
                    textView3.setText(target);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        avatar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                        if(avatarnum.equals("5")) avatarnum="1";
                        else avatarnum=String.valueOf(Integer.parseInt(avatarnum)+1);
                        avatarchange(avatarnum);
                        return true;
                }
                return false;
            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            FormBody formBody = new FormBody.Builder().add("id", id).add("avatar", avatarnum).build();
                            OkHttpClient client = new OkHttpClient();
                            Request request = new Request.Builder()
                                    .url(appConfig.ipAddress+"/user1")
                                    .post(formBody)
                                    .build();
                            Response response = client.newCall(request).execute();
                            final String responseData = response.body().string();
                            if(responseData.equals("true"))
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getContext(),"修改成功！",Toast.LENGTH_SHORT).show();
                                    }
                                });
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
                String target1=textView3.getText().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            FormBody formBody = new FormBody.Builder().add("id", id).add("target", target1).build();
                            OkHttpClient client = new OkHttpClient();
                            Request request = new Request.Builder()
                                    .url(appConfig.ipAddress+"/user3")
                                    .post(formBody)
                                    .build();
                            Response response = client.newCall(request).execute();
                            final String responseData = response.body().string();
                            if(responseData.equals("true"))
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getContext(),"修改成功！",Toast.LENGTH_SHORT).show();
                                    }
                                });
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(view.getContext(), ChangePwdActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                view.getContext().startActivity(intent);
                runningMainActivity.instance.finish();
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject json = new JSONObject();
                try {
                    json.put("userId", Client.getUserId());
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String str = httpTools.post(appConfig.ipAddress+"/closeMySocket",json.toString());
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    Intent intent = new Intent();
                    intent.setClass(view.getContext(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    Toast.makeText(getContext(),"退出登录！",Toast.LENGTH_SHORT).show();
                    view.getContext().startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        NoticeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), friendNoticeListActivity.class);
                startActivity(intent);
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
