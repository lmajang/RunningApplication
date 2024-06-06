package com.example.runningapplication.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.runningapplication.R;
import com.example.runningapplication.entity.friendEntity;
import com.example.runningapplication.runningChat.chatActivity;

import java.util.List;

public class friendListAdapter extends RecyclerView.Adapter<friendListAdapter.viewHolder> {

    List<friendEntity> friendList;
    private Context mContext;
    private View view;

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
        holder.friend_hp.setImageResource(R.drawable.img);
        holder.username.setText(friend.getUsername());
        holder.firstChat.setText("test1");
        holder.chatBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(mContext, chatActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("data", friend);
                mContext.startActivity(intent);
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
