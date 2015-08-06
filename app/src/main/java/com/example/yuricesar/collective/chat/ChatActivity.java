package com.example.yuricesar.collective.chat;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.yuricesar.collective.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ygorg_000 on 06/08/2015.
 */
public class ChatActivity  extends ActionBarActivity {

    private ChatArrayAdapter adp;
    private ListView list;
    private EditText chatText;
    private TextView nameText;
    private Button send;
    Intent it;
    private boolean side = false;
    public List<ChatMessage> messages = new ArrayList<ChatMessage>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);
        Intent i = getIntent();
        Bundle extras = i.getExtras();
        String user = (String)extras.get("userId");
        String friendId = (String)extras.get("friendId");
        String friendName = (String)extras.get("friendName");


        send = (Button) findViewById(R.id.btn);
        list = (ListView) findViewById(R.id.listView);

        chatText = (EditText) findViewById(R.id.chat);
        chatText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)){
                    return sendChatMessager();
                }
                return false;
            }
        });
        nameText = (TextView) findViewById(R.id.name);
        nameText.setText(friendName);

        adp = new ChatArrayAdapter(getApplicationContext(), R.layout.messager, messages);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendChatMessager();
            }
        });

        list.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        list.setAdapter(adp);

        adp.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                list.setSelection(adp.getCount() - 1);
            }
        });

    }

    private boolean sendChatMessager() {
        adp.add(new ChatMessage(side, chatText.getText().toString()));
        chatText.setText("");
        side = !side;
        return true;
    }


}
