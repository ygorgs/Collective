package com.example.yuricesar.collective.chat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by franklin on 07/08/15.
 */
public class BroadcastReceiverAux extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        intent = new Intent("SERVICE_MSG");
        context.startService(intent);
    }
}