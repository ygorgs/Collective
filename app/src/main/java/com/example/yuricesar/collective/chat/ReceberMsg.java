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
import com.example.yuricesar.collective.data.CelulaREST;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;


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
        public String idCliente;
        public Context context;

        public Worker(int startId, String idCliente, Context context){
            this.startId = startId;
            this.idCliente = idCliente;
            this.context = context;
        }

        public void run(){
            receberMsg(context);
            stopSelf(startId);
        }

        private void receberMsg(Context context) {
            try {
                List<String> result = new CelulaREST().receberMsg(idCliente);
                String idAmigo = result.get(0);
                String msg = result.get(1);
                if (!msg.equals("")) {
                    notifyMessages(context);
                    escreveArquivo(idCliente + "_" + idAmigo, idAmigo + ":" + msg);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void escreveArquivo (String arquivo, String msg) {
            try{
                FileOutputStream arquivoGravar = openFileOutput(arquivo + ".txt", MODE_APPEND);
                arquivoGravar.write(msg.getBytes());
                arquivoGravar.write("\n".getBytes());
                arquivoGravar.close();
                Log.d("arquivo", "Arquivo " + arquivo + " gravado com sucesso");
            } catch (FileNotFoundException e) {
                Log.e("arquivo", "Arquivo nao encontrado");
                e.printStackTrace();
            } catch (IOException e) {
                Log.e("arquivo", "Erro de entrada e saida");
                e.printStackTrace();
            }
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
        }
    }
}
