package com.example.testapp;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {
    //Application Context
    @SuppressLint("StaticFieldLeak")
    public static Context mContext;

    public static Context getContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;

    }
}
