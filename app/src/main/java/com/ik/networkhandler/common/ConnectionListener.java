package com.ik.networkhandler.common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;


import timber.log.Timber;

public class ConnectionListener{

    private Context context;
    private BroadcastReceiver receiver;
    private static Callback mListener;


    public ConnectionListener(Context context){
        this.context = context;
    }

    public static void registerListener(Callback listener){
        mListener = listener;
    }

    public static void removeListener(){
        mListener = null;
    }

    public void register() {
        Timber.i("register");
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");

        if (receiver == null) {
            receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    Timber.i("onReceive");
                    if (mListener != null) {
                        if (Utils.hasNetworkAccess(context)) {
                            mListener.onConnected();
                        } else {
                            mListener.onDisconnected();
                        }
                    }
                }
            };
        }
        context.registerReceiver(receiver, filter);
    }

    protected void unregister() {
        Timber.i("unregister");
        if (receiver != null) {
            context.unregisterReceiver(receiver);
        }
    }

    public interface Callback{
        void onConnected();
        void onDisconnected();
    }
}
