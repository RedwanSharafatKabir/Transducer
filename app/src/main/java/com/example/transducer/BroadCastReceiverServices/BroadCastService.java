package com.example.transducer.BroadCastReceiverServices;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;

public class BroadCastService extends Service {

    public static final String COUNTDOWN_BR = "com.example.transducer.BroadCastReceiverServices";
    Intent intent = new Intent(COUNTDOWN_BR);
    CountDownTimer countDownTimer = null;

    @Override
    public void onCreate() {
        super.onCreate();
        countDownTimer = new CountDownTimer(300000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                intent.putExtra("countdown", millisUntilFinished);
                sendBroadcast(intent);
            }

            @Override
            public void onFinish() {}
        }.start();
    }

    @Override
    public void onDestroy() {
        countDownTimer.cancel();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
}
