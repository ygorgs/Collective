package com.example.yuricesar.collective;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by Felipe Araujo on 09/08/2015.
 */
public class ProfileActivity extends ActionBarActivity {

    private ProgressBar mProgress;
    private int mProgressStatus = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_details);

        Bundle extras = getIntent().getExtras();
        String nomeUsuario = (String) extras.get("nameUser");
        Integer afinidade = (Integer) extras.get("affinityLevel");

        EditText editText = (EditText) findViewById(R.id.nameUserProfile);
        editText.setText(nomeUsuario);
        showProgressAffinity(afinidade);

    }

    private void showProgressAffinity(final int affinityLevel) {

        mProgress = (ProgressBar) findViewById(R.id.progressBar1);
        final Handler mHandler = new Handler();

        final TextView textView = (TextView) findViewById(R.id.afinidade);

        // Start lengthy operation in a background thread
        new Thread(new Runnable() {
            public void run() {
                while (mProgressStatus < affinityLevel) {
                    mProgressStatus++;
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // Update the progress bar

                    mHandler.post(new Runnable() {
                        public void run() {
                            textView.setText("Nivel de afinidade : "+mProgressStatus+"%");
                            mProgress.setProgress(mProgressStatus);
                        }
                    });
                }
            }
        }).start();
    }
}