package com.zhour.zhoursecurity.Utils;

import android.app.Application;
import android.content.Context;

/**
 * Created by madhu on 17-Dec-17.
 */

public class MyApplication extends Application {

    private static Context mContext;

    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getAppContext() {
        return mContext;
    }
}
