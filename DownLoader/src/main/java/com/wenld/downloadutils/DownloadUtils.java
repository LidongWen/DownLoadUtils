package com.wenld.downloadutils;

import android.content.Context;

import com.wenld.downloadutils.bean.FileInfo;
import com.wenld.downloadutils.bean.FileInfoDao;
import com.wenld.downloadutils.db.AbstractDatabaseManager;
import com.wenld.downloadutils.db.FileInfoDB;
import com.wenld.downloadutils.tool.DownLoadBinder;

/**
 * Created by wenld on 2017/1/9.
 */
public class DownloadUtils {
    /**
     * 初始化数据库
     *
     * @param mContext
     */
    public static void initDataBase(Context mContext) {
        AbstractDatabaseManager.initOpenHelper(mContext);//初始化数据库
        AbstractDatabaseManager.getDaoSession().getDatabase().execSQL("update " + FileInfoDao.TABLENAME + " set " + FileInfoDao.Properties.IsDownload.columnName + "=0");

    }

    /**
     * 获取DB 操作数据库
     *
     * @return
     */
    public static FileInfoDB getFileDB() {
        return new FileInfoDB();
    }

    public static <T> void startDownload(Context mContext, FileInfo mFileInfo, T postion) {
        DownLoadBinder.getInstance().startDownload(mContext, mFileInfo, postion);
    }

    public static  <T> void stop(FileInfo mFileInfo, T postion) {
        DownLoadBinder.getInstance().stop(mFileInfo, postion);
    }

    public static  void allStop() {
        DownLoadBinder.getInstance().allStop();
    }
}
