package com.example.runningapplication.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.runningapplication.R;
import com.example.runningapplication.entity.chatEntity;

import java.util.List;

public class chatAdapter extends RecyclerView.Adapter<chatAdapter.viewHolder> {
    List<chatEntity> chatEntityList;

    public chatAdapter(List<chatEntity> dataList){
        chatEntityList = dataList;
    }

//    布局加载器
    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.charlist_layout, parent, false);

        return new viewHolder(view);
    }

//    位置绑定
    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
    chatEntity chatMsg = chatEntityList.get(position);
        if(chatMsg.getTYPE() == chatEntity.SEND){
            holder.leftContext.setVisibility(View.GONE);
            holder.rightContext.setVisibility(View.VISIBLE);
            holder.rightChatContext.setText(chatMsg.getContext());
            holder.rightName.setText(chatMsg.getUsername());
            holder.rightHp.setImageResource(R.drawable.img);
        }else{
            holder.rightContext.setVisibility(View.GONE);
            holder.leftContext.setVisibility(View.VISIBLE);
            holder.leftChatContext.setText(chatMsg.getContext());
            holder.leftName.setText(chatMsg.getUsername());
            holder.leftHp.setImageResource(R.drawable.img);
        }
    }

//     获取数据长度
    @Override
    public int getItemCount() {
        return chatEntityList.size();
    }


    static class viewHolder extends RecyclerView.ViewHolder{
        //        左边聊天内容
        ImageView leftHp;
        TextView leftName;
        TextView leftChatContext;
        LinearLayout leftContext;
        //        右边聊天内容
        ImageView rightHp;
        TextView rightName;
        TextView rightChatContext;
        LinearLayout rightContext;


        public viewHolder(@NonNull View itemView) {
            super(itemView);
            leftHp = (ImageView) itemView.findViewById(R.id.left_hp);
            leftName = (TextView) itemView.findViewById(R.id.left_name);
            leftChatContext = (TextView) itemView.findViewById(R.id.left_chat_context);
            leftContext = (LinearLayout) itemView.findViewById(R.id.left_context);

            rightHp = (ImageView) itemView.findViewById(R.id.right_hp);
            rightName = (TextView) itemView.findViewById(R.id.right_name);
            rightChatContext = (TextView) itemView.findViewById(R.id.right_chat_context);
            rightContext = (LinearLayout) itemView.findViewById(R.id.right_context);
        }
    }

}
