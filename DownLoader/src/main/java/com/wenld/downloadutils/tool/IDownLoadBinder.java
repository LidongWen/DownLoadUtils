package com.wenld.downloadutils.tool;

import android.content.Context;

import com.wenld.downloadutils.bean.FileInfo;

/**
 * Created by wenld on 2017/1/10.
 */
public interface IDownLoadBinder {
    <T> void startDownload(Context mContext, FileInfo mFileInfo, T postion);

    <T> void stop(FileInfo mFileInfo, T postion);

    void allStop();
}
