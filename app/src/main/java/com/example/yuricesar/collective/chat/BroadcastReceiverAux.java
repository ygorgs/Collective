package com.example.yuricesar.collective.chat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by franklin on 07/08/15.
 */
public class BroadcastReceiverAux extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        String id = (String) extras.get("id");
        Intent i = new Intent("SERVICE_MSG");
        i.putExtra("id", id);
        context.startService(i);
    }
}