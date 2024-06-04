package com.example.runningapplication.runningMain.ui.runningFriendList;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.runningapplication.Adapter.friendListAdapter;
import com.example.runningapplication.R;
import com.example.runningapplication.entity.friendEntity;

import java.util.ArrayList;
import java.util.List;

public class friendListFragment extends Fragment {

    private List<friendEntity> friendList;
    private RecyclerView recyclerView;
    private friendListAdapter adapter;

    private View view;
    private EditText friendSearch;

    private String TAG = "friendListActivity";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_friend_list,container,false);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        friendSearch = (EditText) view.findViewById(R.id.search_friend);
        Drawable searchDrawable = getResources().getDrawable(R.drawable.icon_search);

        searchDrawable.setBounds(0,0,50,50);
        friendSearch.setCompoundDrawablesRelative(searchDrawable,null,null,null);
        initRecycle();
        friendEntity test = new friendEntity(R.drawable.img,"200","test1", "2222222", false);
        friendEntity test2 = new friendEntity(R.drawable.img,"201","test2", "2222222", false);
        friendList.add(test);
        friendList.add(test2);

        friendSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_UNSPECIFIED || actionId == EditorInfo.IME_ACTION_SEARCH){
                    SearchFriend("test");
                    return true;
                }
                return false;
            }
        });

    }


    private void initRecycle(){
        friendList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.friend_recyclerView);
        LinearLayoutManager LayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(LayoutManager);
        adapter = new friendListAdapter(friendList);
        recyclerView.setAdapter(adapter);
    }

    private void addFriend(friendEntity friend){
        friendList.add(friend);
        adapter.notifyItemInserted(friendList.size() - 1);
        recyclerView.scrollToPosition(friendList.size() - 1);
    }

    private void deleteFriend(friendEntity friend){
        friendList.remove(friend);
        adapter.notifyItemInserted(friendList.size() - 1);
        recyclerView.scrollToPosition(friendList.size() - 1);
    }

    private void SearchFriend(String name){
        Log.d(TAG, name);
    }

}
