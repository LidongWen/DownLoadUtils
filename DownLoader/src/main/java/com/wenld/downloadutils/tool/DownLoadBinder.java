package com.wenld.downloadutils.tool;

import android.content.Context;
import android.os.Binder;

import com.wenld.downloadutils.bean.FileInfo;
import com.wenld.downloadutils.bean.FileInfoDao;
import com.wenld.downloadutils.constant.DownloadConfig;
import com.wenld.downloadutils.db.FileInfoDB;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wenld on 2017/1/10.
 */
public class DownLoadBinder extends Binder implements IDownLoadBinder {
    private static DownLoadBinder mBinder = new DownLoadBinder();
    /**
     * 文件下载线程任务的集合
     */
    private Map<String, DownloadTask> mTask = new LinkedHashMap<String, DownloadTask>();

    public static DownLoadBinder getInstance() {
        return mBinder;
    }

    public <T> void startDownload(Context mContext, FileInfo mFileInfo, T postion) {
        List<FileInfo> list = new FileInfoDB().getQueryBuilder().where(FileInfoDao.Properties.Id.eq(mFileInfo.getId())).list();
        if (list != null && list.size() > 0) {
            mFileInfo = list.get(0);
            if (mFileInfo.getOver() != null && mFileInfo.getOver()) {
                return;
            }
            DownloadTask tast = mTask.get(mFileInfo.getId()); // 从集合中取出下载任务
            if (tast != null) {
                if (!tast.isPause)
                    return;
                else
                    mTask.remove(tast);
            }
            openTaskDownLoad(mContext, mFileInfo, postion);
        } else {
            netWork(mContext, mFileInfo, postion);
        }
    }

    private <T> void netWork(final Context mContext, final FileInfo mFileInfo, final T postion) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpURLConnection conn = null;
                    conn = HttpUtils.getInstance().createConnection(mFileInfo.getUrl());
                    if (conn.getResponseCode() == 200) {
                        // 获取文件长度
                        long length = conn.getContentLength();
                        if (length <= 0) {
                            return;
                        }
                        mFileInfo.setLength((int) length);

                        // 启动下载任务
                        openTaskDownLoad(mContext, mFileInfo, postion);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    public <T> void stop(FileInfo mFileInfo, T postion) {
        DownloadTask tast = mTask.get(mFileInfo.getId()); // 从集合中取出下载任务
        if (tast != null) {
            tast.isPause = true;
            mTask.remove(tast);
        }
    }

    public void allStop() {

    }


    private <T> void openTaskDownLoad(Context mContext, FileInfo mFileInfo, T postion) {
        try {
            File dir = new File(DownloadConfig.getFileDir());
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, mFileInfo.getFileName());
            if (file.exists()) {

            }
            RandomAccessFile raf = new RandomAccessFile(file, "rwd");
            // 设置文件长度
            raf.setLength(mFileInfo.getLength());
            //关闭连接
            if (raf != null) {
                raf.close();
            }
        } catch (Exception e) {

        }

        // 启动下载任务
        DownloadTask task = new DownloadTask(mContext,
                mFileInfo, DownloadConfig.getFileThreadNum(), postion);
        task.download();
        mTask.put(mFileInfo.getId(), task);
    }

}
