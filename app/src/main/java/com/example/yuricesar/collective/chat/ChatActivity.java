package com.example.yuricesar.collective.chat;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.yuricesar.collective.R;
import com.example.yuricesar.collective.data.CelulaREST;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
    public List<ChatMessage> messages = new ArrayList<ChatMessage>();
    private String friendId;
    private String user;
    private CountUpTimer countupTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);
        Intent i = getIntent();

        Bundle extras = i.getExtras();
        user = (String)extras.get("userId");
        friendId = (String)extras.get("friendId");
        String friendName = (String)extras.get("friendName");


        send = (Button) findViewById(R.id.btn);
        list = (ListView) findViewById(R.id.listView);

        chatText = (EditText) findViewById(R.id.chat);
        chatText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)){
                    return sendChatMessager(user, friendId);
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
                sendChatMessager(user, friendId);
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
        countupTimer = new CountUpTimer(500) {
            @Override
            public void onTick(long millisElapsed) {
                receveChatMessager(user + "_" + friendId);
            }
        };

        countupTimer.start();
    }

    @Override
    public void onResume() {
        super.onResume();
        countupTimer = new CountUpTimer(500) {
            @Override
            public void onTick(long millisElapsed) {
                receveChatMessager(user + "_" + friendId);
            }
        };

        countupTimer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        countupTimer.cancel();
    }

    private boolean sendChatMessager(String user, String friendId) {
        try {
            new CelulaREST().enviarMsg(user, friendId, chatText.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //escreve no arquivo
        escreveArquivo(user + "_" + friendId, user + ":" + chatText.getText().toString());
        chatText.setText("");
        return true;
    }

    //TODO bug: todas as mensagens em um unico lado
    public boolean receveChatMessager(String arq) {
        //ler do arquivo
        String texto = lerAquivo(user + "_" + friendId);
        if (!texto.equals("")) {
            String[] result = texto.split("\n");
            for (int i = 0; i < result.length; i++) {
                if (result[i].substring(0, result[i].indexOf(":")).equals(user)) {
                    ChatMessage msg = new ChatMessage(true, result[i].substring(result[i].indexOf(":") + 1));
                    if (!messages.contains(msg)) {
                        adp.add(msg);
                        messages.add(msg);
                    }
                } else {
                    ChatMessage msg = new ChatMessage(false, result[i].substring(result[i].indexOf(":") + 1));
                    if (!messages.contains(msg)) {
                        Log.d("teste", "ei");
                        adp.add(msg);
                        messages.add(msg);
                    }
                }
            }
        }
        return true;
    }

    private void escreveArquivo (String arquivo, String msg) {
        try{
            FileOutputStream arquivoGravar = openFileOutput(arquivo + ".txt", MODE_APPEND);
            arquivoGravar.write(msg.getBytes());
            arquivoGravar.write("\n".getBytes());
            arquivoGravar.close();
            Log.d("arquivo", "Arquivo "+ arquivo + " gravado com sucesso");
        } catch (FileNotFoundException e) {
            Log.e("arquivo", "Arquivo nao encontrado");
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("arquivo", "Erro de entrada e saida");
            e.printStackTrace();
        }
    }

    private String lerAquivo(String arquivo) {
        String result = "";
        try {
            File file = this.getFilesDir();
            File textfile = new File(file + "/" + arquivo + ".txt");
            FileInputStream input = this.openFileInput(arquivo + ".txt");
            byte[] buffer = new byte[(int) textfile.length()];
            input.read(buffer);
            Log.d("arquivo", "Arquivo lido com sucesso");
            result = new String(buffer);
        } catch (FileNotFoundException e) {
            Log.e("arquivo", "Arquivo nao encontrado");
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("arquivo", "Erro de entrada e saida");
            e.printStackTrace();
        }
        return result;
    }
}
