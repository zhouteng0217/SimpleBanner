package com.zt.simplebanner.demo;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created by zhouteng on 2019-10-17
 */
public class DemoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
    }
}
