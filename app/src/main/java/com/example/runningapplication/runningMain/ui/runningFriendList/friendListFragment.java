package com.example.runningapplication.runningMain.ui.runningFriendList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.example.runningapplication.Adapter.friendListAdapter;
import com.example.runningapplication.Adapter.runRecordAdapter;
import com.example.runningapplication.DB.insertDB;
import com.example.runningapplication.Login.LoginActivity;
import com.example.runningapplication.R;
import com.example.runningapplication.config.appConfig;
import com.example.runningapplication.entity.friendEntity;
import com.example.runningapplication.runningFriend.friendActivity;
import com.example.runningapplication.utils.httpTools;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;

public class friendListFragment extends Fragment {
    private OkHttpClient okHttpClient;
    private List<friendEntity> friendList;
    private List<friendEntity> SearchFriendList;

    private List<String> suggestions = new ArrayList<>();
    private RecyclerView recyclerView;

    private RecyclerView searchRecyclerView;
    private friendListAdapter adapter;

    private runRecordAdapter sAdapter;

    private String userId ;
    private View view;
    private AutoCompleteTextView friendSearch;

    private String TAG = "friendListActivity";

    private static final int UPDATE_RE = 1;
    private static final int UPDATE_SRE = 2;

    @SuppressLint("HandlerLeak")
    private Handler handle = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == UPDATE_RE) {
                Log.d(TAG,msg.obj.toString());
                List<friendEntity> List = JSON.parseArray(msg.obj.toString(),friendEntity.class);
                friendList.addAll(List);
                System.out.println(friendList);
                adapter.notifyItemInserted(friendList.size() - 1);
                recyclerView.scrollToPosition(friendList.size() - 1);
            }else if(msg.what == UPDATE_SRE){
                List<friendEntity> List = JSON.parseArray(msg.obj.toString(),friendEntity.class);
//                SearchFriendList.addAll(List);
//                suggestions.clear();
                if(!suggestions.isEmpty()) suggestions.clear();
                for(friendEntity friend:List){
                    System.out.println(friend.getUsername());
                    if(friend.getUsername()==null)continue;
                    suggestions.add(friend.getUsername());
                }
                System.out.println(suggestions.size());
//                Log.d(TAG, String.valueOf(suggestions.size()));
                // 更新AutoCompleteTextView的适配器
                if (!suggestions.isEmpty()){
                ArrayAdapter<String> adapter = new ArrayAdapter<>(view.getContext(),
                        android.R.layout.simple_dropdown_item_1line, suggestions);
                friendSearch.setAdapter(adapter);

//                Log.d(TAG, String.valueOf(SearchFriendList.size()));
//                sAdapter.notifyItemInserted(SearchFriendList.size() - 1);
//                searchRecyclerView.scrollToPosition(SearchFriendList.size() - 1);
            }
            }
        }
    };

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
        friendSearch = (AutoCompleteTextView) view.findViewById(R.id.search_friend);

        userId = LoginActivity.sp.getString("id",null);

        try {
            initRecycle();
            initFriendList();
        } catch (Exception e) {
            e.printStackTrace();
        }

        friendSearch.addTextChangedListener(new TextWatcher() {
            String url = appConfig.ipAddress+"/SearchUser";
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject json = new JSONObject();
                            json.put("username",s.toString());
                            String getFriendListJson = httpTools.post(url,json.toString());
                            handle.obtainMessage(UPDATE_SRE,getFriendListJson).sendToTarget();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }).start();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
//        friendSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if(hasFocus){
//                    searchRecyclerView.setVisibility(View.VISIBLE);
//                }else {
//                    searchRecyclerView.setVisibility(View.GONE);
//                }
//            }
//        });

    }


    private void initRecycle(){
        friendList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.friend_recyclerView);
        LinearLayoutManager LayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(LayoutManager);
        adapter = new friendListAdapter(friendList);
        recyclerView.setAdapter(adapter);

//        SearchFriendList = new ArrayList<>();
//        searchRecyclerView = view.findViewById(R.id.search_recyclerView);
//        LinearLayoutManager searchLayoutManger = new LinearLayoutManager(view.getContext());
//        searchRecyclerView.setLayoutManager(searchLayoutManger);
//        sAdapter = new searchAdapter(SearchFriendList);
//        searchRecyclerView.setAdapter(sAdapter);
        friendSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    String searchMessage = v.getText().toString();
                    Log.d(TAG, searchMessage);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("username",searchMessage);
                                String res = httpTools.post(appConfig.ipAddress+"/user4",jsonObject.toString());
                                Log.d(TAG, res);
                                jsonObject=new JSONObject(res);
                                String id=jsonObject.getString("id");
                                if(res!=null){
                                    Intent intent = new Intent();
                                    intent.setClass(view.getContext(), friendActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.putExtra("receiverid", id);
                                    view.getContext().startActivity(intent);
                                }else{
                                    Toast.makeText(view.getContext(),"没有搜索到该用户!",Toast.LENGTH_SHORT).show();
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }).start();

                    return true;
                }
                return false;
            }

        });
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

    private void initFriendList() throws Exception {
        okHttpClient = new OkHttpClient();
        String url = appConfig.ipAddress+"/getAllFriend";
        JSONObject json = new JSONObject();
        json.put("userId", userId);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String getFriendListJson = httpTools.post(url,json.toString());
                    if(!getFriendListJson.isEmpty()){
                        handle.obtainMessage(UPDATE_RE,getFriendListJson).sendToTarget();
                        List<friendEntity> fList = JSON.parseArray(getFriendListJson, friendEntity.class);
                        for(friendEntity friend:fList){
                            insertDB.insertFriendEntity(friend);
                        }
                    }else {
                        Cursor cursor = appConfig.sqLiteDatabase.query("friendlist",null,null,null,null,null,null,null);
                        if(cursor.getCount()>0){
                            cursor.moveToFirst();
                            while (cursor.moveToNext()){
                                friendList.add(new friendEntity(cursor.getString(cursor.getColumnIndexOrThrow("friend_id")),
                                        cursor.getString(cursor.getColumnIndexOrThrow("avatar")),
                                        cursor.getString(cursor.getColumnIndexOrThrow("username"))));
                            }
                        }
                        // 完成后关闭cursor
                        cursor.close();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }).start();

    }


}
