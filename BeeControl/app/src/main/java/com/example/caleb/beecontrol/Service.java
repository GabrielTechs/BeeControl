package com.example.caleb.beecontrol;

import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class Service  extends android.app.Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // do your jobs here
        return super.onStartCommand(intent, flags, startId);
    }
}
