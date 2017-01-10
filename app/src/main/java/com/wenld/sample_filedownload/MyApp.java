package com.wenld.sample_filedownload;

import android.app.Application;

import com.wenld.downloadutils.DownloadUtils;

/**
 * Created by wenld on 2017/1/10.
 */
public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DownloadUtils.initDataBase(this);
    }
}
