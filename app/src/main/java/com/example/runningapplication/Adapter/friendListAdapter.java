package com.example.runningapplication.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.runningapplication.Login.LoginActivity;
import com.example.runningapplication.R;
import com.example.runningapplication.config.appConfig;
import com.example.runningapplication.entity.friendEntity;
import com.example.runningapplication.runningChat.chatActivity;

import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class friendListAdapter extends RecyclerView.Adapter<friendListAdapter.viewHolder> {

    List<friendEntity> friendList;
    private Context mContext;
    private View view;
    private static String userId = LoginActivity.sp.getString("id",null);
    static String avatarnummyself;


    public friendListAdapter(List<friendEntity> friendList){
        this.friendList = friendList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_layout, parent, false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull friendListAdapter.viewHolder holder, int position) {
        friendEntity friend = friendList.get(position);
        String avatarnum=friend.getFriend_hpId();
        if(avatarnum.equals("1")) holder.friend_hp.setImageResource(R.drawable.avatar1);
        if(avatarnum.equals("2")) holder.friend_hp.setImageResource(R.drawable.avatar2);
        if(avatarnum.equals("3")) holder.friend_hp.setImageResource(R.drawable.avatar3);
        if(avatarnum.equals("4")) holder.friend_hp.setImageResource(R.drawable.avatar4);
        if(avatarnum.equals("5")) holder.friend_hp.setImageResource(R.drawable.avatar5);
        holder.username.setText(friend.getUsername());
        holder.firstChat.setText("test1");
        holder.chatBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            FormBody formBody = new FormBody.Builder().add("id", userId).build();
                            OkHttpClient client = new OkHttpClient();
                            Request request = new Request.Builder()
                                    .url(appConfig.ipAddress+"/user6")
                                    .post(formBody)
                                    .build();
                            Response response = client.newCall(request).execute();
                            final String responseData = response.body().string();
                            avatarnummyself=responseData;
                            Intent intent = new Intent();
                            intent.setClass(mContext, chatActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("data", friend);
                            intent.putExtra("avatar",avatarnummyself);
                            mContext.startActivity(intent);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }).start();


            }
        });
    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }

    static class viewHolder extends RecyclerView.ViewHolder{

        private ImageView friend_hp;
        private TextView username;
        private TextView firstChat;

        private TextView chatBtn;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            friend_hp = (ImageView) itemView.findViewById(R.id.friend_hp);
            username = (TextView) itemView.findViewById(R.id.username);
            firstChat = (TextView) itemView.findViewById(R.id.first_chat);
            chatBtn = (TextView) itemView.findViewById(R.id.chat_btn);
        }

    }
}
