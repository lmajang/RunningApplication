package com.example.runningapplication.Adapter;

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

import java.util.List;

public class friendListAdapter extends RecyclerView.Adapter<friendListAdapter.viewHolder> {

    List<friendEntity> friendList;

    public friendListAdapter(List<friendEntity> friendList){
        this.friendList = friendList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_layout, parent, false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull friendListAdapter.viewHolder holder, int position) {
        friendEntity friend = friendList.get(position);
        holder.friend_hp.setImageResource(friend.getFriend_hpId());
        holder.username.setText(friend.getUsername());
        holder.firstChat.setText(friend.getFirstChat());
    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }

    static class viewHolder extends RecyclerView.ViewHolder{

        private ImageView friend_hp;
        private TextView username;
        private TextView firstChat;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            friend_hp = (ImageView) itemView.findViewById(R.id.friend_hp);
            username = (TextView) itemView.findViewById(R.id.username);
            firstChat = (TextView) itemView.findViewById(R.id.first_chat);

        }
    }
}
