package com.example.yuricesar.collective;

import android.app.Activity;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.yuricesar.collective.chat.ChatActivity;
import com.example.yuricesar.collective.data.BD;
import com.example.yuricesar.collective.data.UserInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ygorg_000 on 04/08/2015.
 */
public class FriendsListActivity extends ActionBarActivity {

    private ListView list;
    private FriendArrayAdapter adp;
    public List<UserInfo> users = new ArrayList<UserInfo>();
    private String userId;
    private BD bd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends_list);
        //getIntent();
        userId = (String)getIntent().getExtras().get("userId");

        list = (ListView) findViewById(R.id.listView);

        bd = new BD(this);
        users = bd.selectAllFriends(bd.getUser(userId));

        adp = new FriendArrayAdapter(getApplicationContext(), R.layout.friend_element, users);

        list.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        list.setAdapter(adp);

        adp.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                list.setSelection(adp.getCount() - 1);
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserInfo u = adp.getItem(position);
                Intent it = new Intent();
                it.setClass(FriendsListActivity.this, ChatActivity.class);
                it.putExtra("userId", userId);
                it.putExtra("friendName", u.getName());
                it.putExtra("friendId", u.getId());
                startActivity(it);
            }
        });

    }

    private void chatPage() {
        Log.d("Click", "clicou mulek!");
    }
}
