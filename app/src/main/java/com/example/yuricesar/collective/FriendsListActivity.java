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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends_list);
        //getIntent();
        userId = (String)getIntent().getExtras().get("userId");

        list = (ListView) findViewById(R.id.listView);

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

        addUsers();

    }

    private void chatPage() {
        Log.d("Click", "clicou mulek!");
    }


    private void addUsers() {

        adp.add(new UserInfo("01", "Stuart", "minions@live.com", "https://fbcdn-profile-a.akamaihd.net/hprofile-ak-xpt1/v/t1.0-1/p160x160/11692716_379744582235944_4185090178840721327_n.png?oh=9ea571cb4ff866fc93cab75fbabc2c07&oe=56586549&__gda__=1447607348_2a8ee0e84ce65ba260cfce80df92ef30"));
        adp.add(new UserInfo("02", "Son Goku", "goku@gmail.com ", "https://fbcdn-profile-a.akamaihd.net/hprofile-ak-xfa1/v/t1.0-1/p160x160/1794709_610527669070597_7693514918791629833_n.jpg?oh=2876ddb28d7b613f9e0a6e7b596b6591&oe=5659A89B&__gda__=1447274781_164afcecba2f9e169c3de433f1d037da"));
        adp.add(new UserInfo("03", "Hermione Granger", "hermione@gmail.com ", "https://fbcdn-profile-a.akamaihd.net/hprofile-ak-xaf1/v/t1.0-1/p160x160/24303_283229481824504_229579283_n.png?oh=24d29ee34275f850ba233e94c13f0a3e&oe=561E1CD0&__gda__=1443981766_12287de6f25f335d36a4967d5eb6148e"));
        adp.add(new UserInfo("04", "Simone Simons", "simone@gmail.com ", "https://fbcdn-profile-a.akamaihd.net/hprofile-ak-xfp1/v/t1.0-1/c0.0.160.160/p160x160/11659561_866056010147484_4387585850039672658_n.png?oh=1bd44043b758bd007686a040200952fa&oe=565A48CD&__gda__=1447408605_ec002f62997507e92610a57d66389876"));
    }
}
