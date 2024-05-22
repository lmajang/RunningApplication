package com.example.runningapplication.runningFriendList;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.example.runningapplication.R;

public class friendListActivity extends Activity {



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void addFriend(String id){

    }

}
