package com.example.yuricesar.collective.chat;

import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.example.yuricesar.collective.LoginActivity;
import com.example.yuricesar.collective.R;
import com.example.yuricesar.collective.data.UserInfo;


public class ReceberMsg extends Service {
    public Worker thread;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (thread == null) {
            Bundle extras = intent.getExtras();
            String id = (String) extras.get("id");
            Worker w = new Worker(startId, id, getApplicationContext());
            thread = w;

        }
        thread.start();
        return START_REDELIVER_INTENT;
    }

    class Worker extends Thread{
        public int startId;
        public boolean ativo = true;
        public String idCLiente;
        public Context context;

        public Worker(int startId, String idCLiente, Context context){
            this.startId = startId;
            this.idCLiente = idCLiente;
            this.context = context;
        }

        public void run(){
            while(ativo){
                receberMsg(context, idCLiente);
            }
            stopSelf(startId);
        }

        private void receberMsg(Context context, String id) {
//            try {
//                List<String> result = new CelulaREST().receberMsg(DataBaseHelper.getInstance(context).getUser(id));
//                String msg = result.get(1);
//                while (!msg.equals("")) {
//                    notifyMessages(context);
//                    chat(DataBaseHelper.getInstance(context).getUser(result.get(0)),msg);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

            Log.d("teste", id);
        }

        private void chat (UserInfo friend, String msg) {
            //TODO mostrar as mensagens no chat
        }

        //TODO ajeitar
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        private void notifyMessages(Context context) {
            NotificationCompat.Builder mBuilder =
                    (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.notification_template_icon_bg) //TODO colocar o icon do collective
                            .setContentTitle("Mensagens não lidas")
                            .setContentText("clique aqui");
            // Creates an explicit intent for an Activity in your app
            Intent resultIntent = new Intent(context, LoginActivity.class);

            // The stack builder object will contain an artificial back stack for the
            // started Activity.
            // This ensures that navigating backward from the Activity leads out of
            // your application to the Home screen.
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            // Adds the back stack for the Intent (but not the Intent itself)
            stackBuilder.addParentStack(LoginActivity.class);
            // Adds the Intent that starts the Activity to the top of the stack
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(
                            0,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
            mBuilder.setContentIntent(resultPendingIntent);
            NotificationManager mNotificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            //TODO naum testado, naum sei se precisa de um loop
            int notifyID = 1;
            int numMessages = 0;
            mBuilder.setContentText(numMessages + "mensagens não lidas")
                    .setNumber(++numMessages);
            mNotificationManager.notify(
                    notifyID,
                    mBuilder.build());

            // mId allows you to update the notification later on.
            mNotificationManager.notify(001, mBuilder.build());
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        thread.ativo = false;
    }
}
