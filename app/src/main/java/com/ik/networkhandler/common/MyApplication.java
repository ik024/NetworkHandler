package com.ik.networkhandler.common;

import android.app.Application;

import timber.log.Timber;


public class MyApplication extends Application {

    private ConnectionListener connectionListener;

    @Override
    public void onCreate() {
        super.onCreate();

        Timber.plant(new Timber.DebugTree());

        connectionListener = new ConnectionListener(this);
        connectionListener.register();

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        connectionListener.unregister();
    }

}
