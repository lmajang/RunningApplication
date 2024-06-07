package com.example.runningapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.runningapplication.R;
import com.example.runningapplication.entity.runRecordEntity;

import java.util.List;

public class runRecordAdapter extends RecyclerView.Adapter<runRecordAdapter.viewHolder>{
    List<runRecordEntity> runRecordList;
    private Context mContext;
    private View view;

    public runRecordAdapter(List<runRecordEntity> runRecordList){
        this.runRecordList = runRecordList;
    }

    @NonNull
    @Override
    public runRecordAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_user_layout, parent, false);

        return new runRecordAdapter.viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull runRecordAdapter.viewHolder holder, int position) {
        runRecordEntity runRecord = runRecordList.get(position);

//        holder.SearchUser.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Intent intent = new Intent();
////                intent.setClass(mContext, chatActivity.class);
////                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////                intent.putExtra("data", friend);
////                mContext.startActivity(intent);
//                Log.d("searchFriend",friend.getUsername());
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return runRecordList.size();
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
