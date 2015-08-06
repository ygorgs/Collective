package com.example.yuricesar.collective.chat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.yuricesar.collective.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ygorg_000 on 29/07/2015.
 */
public class ChatArrayAdapter extends ArrayAdapter<ChatMessage>{

    private TextView chatText;
    private List<ChatMessage> messageList = new ArrayList<ChatMessage>();
    private LinearLayout layout;

    public ChatArrayAdapter(Context context, int resource, List<ChatMessage> objects) {
        super(context, resource, objects);
    }

    public void add(ChatMessage chatMessage) {
        messageList.add(chatMessage);
        super.add(chatMessage);
    }

    public int getCount(){
        return this.messageList.size();
    }

    public ChatMessage getItem(int index){
        return this.messageList.get(index);
    }

    public View getView(int position, View convertView, ViewGroup parent){
        View v = convertView;
        if(v==null){
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.messager, parent, false);
        }
        layout = (LinearLayout)v.findViewById(R.id.Message1);
        ChatMessage messageObj = getItem(position);
        chatText = (TextView)v.findViewById(R.id.SingleMessage);
        chatText.setText(messageObj.message);
        chatText.setBackgroundResource(R.drawable.bubble_green);
        layout.setGravity(Gravity.RIGHT);
        return v;
    }

    public Bitmap decodeToBitmap(byte[] decodeByte){
        return BitmapFactory.decodeByteArray(decodeByte,0,decodeByte.length);
    }




}
