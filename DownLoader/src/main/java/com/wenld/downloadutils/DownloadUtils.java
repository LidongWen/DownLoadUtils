package com.wenld.downloadutils;

import android.content.Context;

import com.wenld.downloadutils.bean.FileInfo;
import com.wenld.downloadutils.bean.FileInfoDao;
import com.wenld.downloadutils.bean.ThreadInfoDao;
import com.wenld.downloadutils.db.AbstractDatabaseManager;
import com.wenld.downloadutils.db.FileInfoDB;
import com.wenld.downloadutils.tool.DownLoadBinder;

import java.util.List;

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

    /**
     * 获取下载列表
     *
     * @return
     */
    public static List<FileInfo> getAllFileInfos() {
        return getFileDB().loadAll();
    }

    /**
     * 获取未完成文件
     *
     * @return
     */
    public static List<FileInfo> getFileInfosByDownLoading() {
        return getFileDB().getQueryBuilder().where(ThreadInfoDao.Properties.Over.eq(false)).list();
    }

    /**
     * 获取已完成文件
     *
     * @return
     */
    public static List<FileInfo> getFileInfosByFinished() {
        return getFileDB().getQueryBuilder().where(ThreadInfoDao.Properties.Over.eq(true)).list();
    }


    public static <T> void startDownload(Context mContext, FileInfo mFileInfo, T postion) {
        DownLoadBinder.getInstance().startDownload(mContext, mFileInfo, postion);
    }

    public static <T> void stop(FileInfo mFileInfo, T postion) {
        DownLoadBinder.getInstance().stop(mFileInfo, postion);
    }

    public static void allStop() {
//        DownLoadBinder.getInstance().allStop();
    }


    /**
     * 重新下载
     *
     * @param mFileInfo
     * @param postion
     * @param <T>
     */
    public static <T> void ReDownLoad(Context mContext, FileInfo mFileInfo, T postion) {
        DownLoadBinder.getInstance().ReDownLoad(mContext, mFileInfo, postion);
    }

    // TODO: 2017/1/10 删除完以后 通知这块 判断逻辑还需要优化
    public static <T> void delete(FileInfo mFileInfo, T postion) {
        DownLoadBinder.getInstance().delete(mFileInfo, postion);
    }

    public static void deleteAll() {
        DownLoadBinder.getInstance().deleteAll();
    }
}
