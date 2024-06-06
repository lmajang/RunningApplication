package com.example.runningapplication.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.runningapplication.R;
import com.example.runningapplication.entity.friendEntity;
import com.example.runningapplication.runningChat.chatActivity;

import java.util.List;

public class searchAdapter extends RecyclerView.Adapter<searchAdapter.viewHolder>{
    List<friendEntity> friendList;
    private Context mContext;
    private View view;

    public searchAdapter(List<friendEntity> friendList){
        this.friendList = friendList;
    }

    @NonNull
    @Override
    public searchAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_user_layout, parent, false);

        return new searchAdapter.viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull searchAdapter.viewHolder holder, int position) {
        friendEntity friend = friendList.get(position);
        holder.friend_hp.setImageResource(R.drawable.img);
        holder.username.setText(friend.getUsername());
        holder.SearchUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setClass(mContext, chatActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.putExtra("data", friend);
//                mContext.startActivity(intent);
                Log.d("searchFriend",friend.getUsername());
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
        private LinearLayout SearchUser;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            friend_hp = (ImageView) itemView.findViewById(R.id.search_friend_hp);
            username = (TextView) itemView.findViewById(R.id.search_username);
            SearchUser = (LinearLayout) itemView.findViewById(R.id.search_user);
        }
    }
}
