package com.wenld.downloadutils;

import android.content.Context;

import com.wenld.downloadutils.bean.FileInfo;
import com.wenld.downloadutils.bean.FileInfoDao;
import com.wenld.downloadutils.bean.ThreadInfoDao;
import com.wenld.downloadutils.constant.DownloadConfig;
import com.wenld.downloadutils.db.AbstractDatabaseManager;
import com.wenld.downloadutils.db.FileInfoDB;
import com.wenld.downloadutils.tool.DownLoadBinder;
import com.wenld.downloadutils.tool.HttpUtils;
import com.wenld.downloadutils.tool.SSLParams;

import java.io.File;
import java.io.InputStream;
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

    public static void changeFileDir(String fileDir) {
        if (fileDir == null)
            throw new NullPointerException("无效的地址");
        if (new File(fileDir).exists()) {
            DownloadConfig.setDestFileDir(fileDir);
        } else {
            throw new NullPointerException("无效的地址");
        }
    }

    public static String getFileDir() {
        return DownloadConfig.getDestFileDir();
    }

    public static void setFileMaxNum(int maxNum) {
        if (maxNum < 1)
            throw new NullPointerException("the num not less than 1");
        DownloadConfig.setFileMaxNum(maxNum);
    }

    /**
     * https 验证
     *
     * @param certificates
     * @param bksFile
     * @param password
     */
    public static void setSsl(InputStream[] certificates, InputStream bksFile, String password) {
        HttpUtils.getInstance().setSslParams(SSLParams.getSslSocketFactory(certificates, bksFile, password));
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

    public static <T> void startDownload(Context mContext, String id, String url, String fileName, T postion) {
        FileInfo mFileInfo = new FileInfo();
        mFileInfo.setUrl(url);
        mFileInfo.setId(id);
        mFileInfo.setFileName(fileName);
        mFileInfo.setMd5(url);
        startDownload(mContext, mFileInfo, postion);
    }

    public static <T> void startDownload(Context mContext, String url, String fileName, T postion) {
        FileInfo mFileInfo = new FileInfo();
        mFileInfo.setUrl(url);
        mFileInfo.setId(url);
        mFileInfo.setFileName(fileName);
        mFileInfo.setMd5(url);
        startDownload(mContext, mFileInfo, postion);
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

    public static <T> void ReDownLoadById(Context mContext, String id, T postion) {
        FileInfo mFileInfo = new FileInfo();
        mFileInfo.setId(id);
        ReDownLoad(mContext, mFileInfo, postion);
    }

    public static <T> void ReDownLoad(Context mContext, String url, T postion) {
        FileInfo mFileInfo = new FileInfo();
        mFileInfo.setId(url);
        ReDownLoad(mContext, mFileInfo, postion);
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
