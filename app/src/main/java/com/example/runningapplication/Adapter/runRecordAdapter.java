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
import com.example.runningapplication.entity.runRecordEntity;
import com.example.runningapplication.runningMap.recordMapActivity;
import com.example.runningapplication.utils.Tools;

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

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.run_record_layout, parent, false);

        return new runRecordAdapter.viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull runRecordAdapter.viewHolder holder, int position) {
        runRecordEntity runRecord = runRecordList.get(position);

        holder.ran_distance.setText(String.format("%.2f", Float.parseFloat(runRecord.getRanDistance())));
        holder.target_distance.setText(runRecord.getTargetDistance());
        holder.spend_time.setText(Tools.formatMillisecondsToTimeString(Long.parseLong(runRecord.getSpendTime())));
        holder.speed.setText(String.format("%.2f", Double.parseDouble(runRecord.getSpeed())));
        holder.start_time.setText(Tools.convertTimestampToDateString(Long.parseLong(runRecord.getStartTime())));
        holder.ran_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(mContext, recordMapActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Log.d("runRecord",runRecord.toString());
                intent.putExtra("data", runRecord);
                mContext.startActivity(intent);
            }
        });

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
        private TextView ran_distance;
        private TextView target_distance;
        private TextView spend_time;
        private TextView speed;
        private TextView start_time;
        private LinearLayout ran_record;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            ran_distance = itemView.findViewById(R.id.ran_distance);
            target_distance = itemView.findViewById(R.id.target_distance);
            spend_time = itemView.findViewById(R.id.spend_time);
            speed = itemView.findViewById(R.id.speed);
            start_time = itemView.findViewById(R.id.start_time);
            ran_record = itemView.findViewById(R.id.ran_record);
        }
    }
}
