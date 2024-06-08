package com.example.runningapplication.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.runningapplication.R;
import com.example.runningapplication.chatClient.Client;
import com.example.runningapplication.config.appConfig;
import com.example.runningapplication.entity.friendNoticeEntity;
import com.example.runningapplication.entity.runRecordEntity;
import com.example.runningapplication.runningFriend.friendNoticeListActivity;
import com.example.runningapplication.utils.Tools;
import com.example.runningapplication.utils.httpTools;

import org.json.JSONObject;

import java.util.List;


public class noticeListAdapter extends RecyclerView.Adapter<noticeListAdapter.viewHolder>{
    List<friendNoticeEntity> friendNoticeList;
    private Context mContext;
    private View view;

    public noticeListAdapter(List<friendNoticeEntity> friendNoticeList){
        this.friendNoticeList = friendNoticeList;
    }

    @NonNull
    @Override
    public noticeListAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_notice_layout, parent, false);

        return new noticeListAdapter.viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull noticeListAdapter.viewHolder holder, @SuppressLint("RecyclerView") int position) {
        friendNoticeEntity friendNotice = friendNoticeList.get(position);
        holder.friend_notice_hp.setImageResource(Tools.selectHp(friendNotice.getSender_hp_id()));
        holder.notice_username.setText(friendNotice.getSender_username());
        holder.notice_message.setText(friendNotice.getSender_message());
        holder.notice_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("senderId",friendNotice.getSenderId());
                            jsonObject.put("receiverId", Client.getUserId());
                            String getStatus = httpTools.post(appConfig.ipAddress+"/agreeFriendNotice",jsonObject.toString());
                            Log.d("yeeeeees",getStatus);
                            friendNoticeListActivity.noticeList.remove(position);
                            view.invalidate();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
        holder.notice_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("senderId",friendNotice.getSenderId());
                            jsonObject.put("receiverId", Client.getUserId());
                            String getStatus = httpTools.post(appConfig.ipAddress+"/refuseFriendNotice",jsonObject.toString());
                            Log.d("neeeeees",getStatus);

                            friendNoticeListActivity.noticeList.remove(position);
                            view.invalidate();
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
        return friendNoticeList.size();
    }

    static class viewHolder extends RecyclerView.ViewHolder{

        private ImageView friend_notice_hp;
        private TextView notice_username;
        private TextView notice_message;
        private AppCompatButton notice_yes;
        private AppCompatButton notice_no;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            friend_notice_hp = (ImageView) itemView.findViewById(R.id.notice_friend_hp);
            notice_username = (TextView) itemView.findViewById(R.id.notice_username);
            notice_message = (TextView) itemView.findViewById(R.id.notice_message);
            notice_yes = (AppCompatButton) itemView.findViewById(R.id.notice_yes);
            notice_no = (AppCompatButton) itemView.findViewById(R.id.notice_no);
        }
    }

}