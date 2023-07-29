package com.example.english.util;

import android.app.Application;
import android.content.Context;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.tencent.mmkv.MMKV;

public class MyApplication extends Application {

    private static Context context;
    private static MyApplication app;

    public static MyApplication getInstance() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        FormatStrategy format = PrettyFormatStrategy.newBuilder().tag("My custom tag").methodCount(3).methodOffset(2).showThreadInfo(true).build();
        Logger.addLogAdapter(new AndroidLogAdapter(format));

        MMKV.initialize(this);
        //LitePal.initialize(this);
        app = this;
    }

    public static Context getContext() {
        return context;
    }

}