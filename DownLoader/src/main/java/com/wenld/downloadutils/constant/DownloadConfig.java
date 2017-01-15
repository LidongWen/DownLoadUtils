package com.wenld.downloadutils.constant;

import android.os.Environment;


public class DownloadConfig {

    /**
     * 每个文件的分段下载线程数
     */
    public static int DONWNLOAD_THREAD_NUM = 1;
    /**
     * 最大下载文件数
     */
    public static int DOWNLOAD_MAX_NUM = 4;
    /**
     * 文件下载后在本地的保存路径
     */
    public static String destFileDir = Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_DOWNLOADS + "/多线程断点续传下载";//下载文件目录


    private final static String URL = "http://192.168.254.158:8080/";
    /**
     * web上的工程名
     */
    private final static String PROJECT_NAME = "webService";
    /**
     * web上的请求内容
     */
    private final static String URL_TARGET = URL + PROJECT_NAME + "/index.jsp?content=";
    /**
     * 文件web上的下载地址
     */
    public final static String URL_DOWNLOAD_PATH = URL + PROJECT_NAME + "/temp/";

    /**
     * 获取URL路径
     */
    public static String getURL() {
        return URL;
    }

    /**
     * 文件下载的目录
     *
     * @return
     */
    public static String getFileDir() {
        return destFileDir;
    }


    public static void setDestFileDir(String destFileDir) {
        DownloadConfig.destFileDir = destFileDir;
    }

    public static String getDestFileDir() {
        return DownloadConfig.destFileDir;
    }


    /**
     * 每个文件的 下载线程数
     */
    public static int getFileThreadNum() {
        return DONWNLOAD_THREAD_NUM;
    }

    public static int getFileMaxNum() {
        return DOWNLOAD_MAX_NUM;
    }
    public static void setFileMaxNum(int maxNum){
        DOWNLOAD_MAX_NUM=maxNum;
    }
}
