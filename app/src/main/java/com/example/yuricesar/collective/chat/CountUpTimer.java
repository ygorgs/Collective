package com.example.yuricesar.collective.chat;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;

public abstract class CountUpTimer {

    private static final int MSG = 1;
    private final long mCountupInterval;

    private long mMillisStart;
    private long mMillisLast;
    private boolean mCancelled = false;

    public CountUpTimer(long countupInterval) {
        mCountupInterval = countupInterval;
    }

    public synchronized final void cancel() {
        mCancelled = true;
        mHandler.removeMessages(MSG);
    }

    public synchronized final CountUpTimer start() {
        mCancelled = false;
        mMillisStart = mMillisLast = SystemClock.elapsedRealtime();
        mHandler.sendMessage(mHandler.obtainMessage(MSG));
        return this;
    }

    public abstract void onTick(long millisElapsed);

    // handler
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            synchronized (CountUpTimer.this) {
                final long elapsedRealtime = SystemClock.elapsedRealtime();
                if (mCancelled) {
                    return;
                }

                final long millisElapsed = elapsedRealtime - mMillisStart;
                final long millisLastInterval = elapsedRealtime - mMillisLast;
                if (millisLastInterval < mCountupInterval) {
                    sendMessageDelayed(obtainMessage(MSG), mCountupInterval
                            - millisLastInterval);
                } else {
                    mMillisLast = elapsedRealtime;

                    onTick(millisElapsed);

                    // calcula o tempo para executar o onTick
                    long millisDelay = mMillisLast + mCountupInterval
                            - SystemClock.elapsedRealtime();

                    // ajusta para o próximo intervalo (se onTick demorou mais
                    // que mCountupInterval)
                    while (millisDelay < 0) {
                        millisDelay += mCountupInterval;
                    }

                    sendMessageDelayed(obtainMessage(MSG), mCountupInterval
                            - millisDelay);
                }
            }
        }
    };
}
