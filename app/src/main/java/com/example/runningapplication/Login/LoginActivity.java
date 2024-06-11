package com.example.runningapplication.Login;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.runningapplication.R;
import com.example.runningapplication.config.appConfig;
import com.example.runningapplication.runningMain.runningMainActivity;
import com.example.runningapplication.service.chatSocketService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends Activity {
    EditText name, pwd;
    TextView forget;
    Button btnlogin, btnreg;

    View view;
    static public SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        name = this.findViewById(R.id.Username);            //用户名输入框
        pwd = this.findViewById(R.id.Password);              //密码输入框
        forget = this.findViewById(R.id.Forgetpassword);    //忘记密码按钮
        btnlogin = this.findViewById(R.id.Sign_in);         //登录按钮
        btnreg = this.findViewById(R.id.Sign_up);               //注册按钮

        sp = this.getSharedPreferences("user", this.MODE_PRIVATE);
        Intent serviceSocketIntent = new Intent(LoginActivity.this, chatSocketService.class);
        name.setText(sp.getString("mail", null));
        if(sp.getString("id",null)!=null){
            startService(serviceSocketIntent);
            Intent intent = new Intent();
            intent.setClass(LoginActivity.this, runningMainActivity.class);            //设置页面跳转
            startActivity(intent);
            finish();
        }
        btnlogin.setOnClickListener(new View.OnClickListener() {                //登录事件
            @Override
            public void onClick(View v) {
                String username = name.getText().toString();
                String password = pwd.getText().toString();                 //获取用户输入的用户名和密码
                if (!Pattern.matches("^(\\w+([-.][A-Za-z0-9]+)*){3,18}@\\w+([-.][A-Za-z0-9]+)*\\.\\w+([-.][A-Za-z0-9]+)*$", username)) {
                    Toast.makeText(LoginActivity.this, "邮箱格式错误！", Toast.LENGTH_LONG).show();
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                FormBody formBody=new FormBody.Builder().add("mail",username).add("pwd",password).build();
                                OkHttpClient client = new OkHttpClient();
                                Request request = new Request.Builder()
                                        .url(appConfig.ipAddress+"/login")
                                        .post(formBody)
                                        .build();
                                Response response = client.newCall(request).execute();
                                final String responseData = response.body().string();
                                JSONObject jsonObject = new JSONObject(responseData);
                                if(!responseData.equals("0")){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent();
                                            intent.setClass(LoginActivity.this, runningMainActivity.class);            //设置页面跳转
                                            SharedPreferences.Editor editor = sp.edit();
                                            String id = "",name="",target;
                                            try {
                                                id = jsonObject.getString("id");
                                                name = jsonObject.getString("name");
                                                target = jsonObject.getString("target");
                                            } catch (JSONException e) {
                                                throw new RuntimeException(e);
                                            }
                                            editor.putString("mail", username);
                                            editor.putString("id", id);
                                            editor.putString("username", name);
                                            editor.putString("target",target);
                                            editor.commit();                                        //将用户名存到SharedPreferences中
                                            //cursor.moveToFirst();                                   //将光标移动到position为0的位置，默认位置为-1
                                            //String loginname = cursor.getString(0);
                                            startService(serviceSocketIntent);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                                }
                                else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(LoginActivity.this, "邮箱或密码错误！", Toast.LENGTH_LONG).show();
                                        }
                                        });
                                }
                            }catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        }).start();
                }

            }
        });

        btnreg.setOnClickListener(new View.OnClickListener() {                  //注册事件
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, RegisterActivity.class);          //跳转到注册页面
                Toast.makeText(LoginActivity.this, "前往注册！", Toast.LENGTH_SHORT).show();
                startActivity(intent);

            }
        });
        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, ForgetActivity.class);
                Toast.makeText(LoginActivity.this, "前往忘记密码!", Toast.LENGTH_SHORT).show();
                startActivity(intent);

            }
        });
    }
}

