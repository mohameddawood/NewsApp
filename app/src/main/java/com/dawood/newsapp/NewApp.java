package com.dawood.newsapp;

import android.app.Application;

import com.dawood.newsapp.network.ConnectivityReceiver;


/**
 * Created by mohamed on 2/18/18.
 */

public class NewApp extends Application {
    public static NewApp mInstance;


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized NewApp getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.sConnectivityReceiverListener = listener;
    }

}
