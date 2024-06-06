package com.example.runningapplication.Login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.runningapplication.EamilUtil;
import com.example.runningapplication.R;
import com.example.runningapplication.config.appConfig;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ForgetActivity extends Activity {
    EditText name,pwd,verify;
    Button btnsend,btnchange;
    String verify1="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
        name = this.findViewById(R.id.Username);            //用户名输入框
        pwd = this.findViewById(R.id.Password);              //密码输入框
        verify = this.findViewById(R.id.verify);             //验证码输入框
        btnsend = this.findViewById(R.id.send);               //发送验证码按钮
        btnchange = this.findViewById(R.id.change);               //修改密码按钮
        btnchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail=name.getText().toString();
                String password=pwd.getText().toString();
                String verify2=verify.getText().toString();
                if(verify1.equals("")){
                    Toast.makeText(ForgetActivity.this, "您还未获取验证码！", Toast.LENGTH_LONG).show();
                }
                else if(!verify1.equals(verify2)){
                    Toast.makeText(ForgetActivity.this, "验证码错误！", Toast.LENGTH_LONG).show();
                }else if(password.equals("")) {
                    Toast.makeText(ForgetActivity.this, "密码不能为空！", Toast.LENGTH_LONG).show();
                }else {
                    new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            FormBody formBody=new FormBody.Builder().add("mail",mail).add("pwd",password).build();
                            OkHttpClient client = new OkHttpClient();
                            Request request = new Request.Builder()
                                    .url(appConfig.ipAddress + "/forget")
                                    .post(formBody)
                                    .build();
                            Response response = client.newCall(request).execute();
                            final String responseData = response.body().string();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(responseData.equals("false")){
                                        Toast.makeText(ForgetActivity.this, "新密码与原密码相同！", Toast.LENGTH_SHORT).show();
                                    }else {
                                        Intent intent = new Intent();
                                        intent.setClass(ForgetActivity.this, LoginActivity.class);
                                        Toast.makeText(ForgetActivity.this, "修改密码成功！请回到登录界面重新登陆！", Toast.LENGTH_SHORT).show();
                                        startActivity(intent);
                                    }
                                }
                            });
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }).start();

                }
            }
        });
        btnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Pattern.matches("^(\\w+([-.][A-Za-z0-9]+)*){3,18}@\\w+([-.][A-Za-z0-9]+)*\\.\\w+([-.][A-Za-z0-9]+)*$", name.getText().toString())) {
                    Toast.makeText(ForgetActivity.this, "邮箱格式错误！", Toast.LENGTH_LONG).show();
                } else {
                    // 随机验证码
                    String[] beforeShuffle = new String[]{"2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F",
                            "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "a",
                            "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v",
                            "w", "x", "y", "z"};
                    List list = Arrays.asList(beforeShuffle);//将数组转换为集合
                    Collections.shuffle(list);  //打乱集合顺序
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < list.size(); i++) {
                        sb.append(list.get(i)); //将集合转化为字符串
                    }
                    verify1 = sb.toString().substring(3, 8);  //截取字符串第4到8
                    //发送邮件代码

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            EamilUtil.sendMail(String.valueOf(name.getText()), verify1);
                        }
                    }).start();
                    Toast.makeText(ForgetActivity.this, "验证码已发送！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
