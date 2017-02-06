package com.wenld.downloadutils;

import com.wenld.downloadutils.bean.FileInfo;

/**
 *
 * <p/>
 * Author: 温利东 on 2017/2/6 13:54.
 * blog: http://blog.csdn.net/sinat_15877283
 * github: https://github.com/LidongWen
 */

public class DownLoadStutasUtil {
    public final static int DownLoad_Status_waitStart = 1;//等待开始
    public final static int DownLoad_Status_loading = 2;//下载中
    public final static int DownLoad_Status_pauseLoad = 3;//暂停
    public final static int DownLoad_Status_finished = 4;//完成下载

    /**
     * 获取下载文件的状态
     * @param fileInfo
     * @return
     */
    public static int getDownLoadStatus(FileInfo fileInfo) {
        // 完成
        if (fileInfo.getOver() != null && fileInfo.getOver()) {
            return DownLoad_Status_finished;
        } else {
            if (fileInfo.getIsDownload()) {
                return DownLoad_Status_loading;
            } else {
                if (fileInfo.getFinished() != null && fileInfo.getFinished() > 0) {
                    return DownLoad_Status_pauseLoad;
                } else {
                    return DownLoad_Status_waitStart;
                }
            }
        }
    }
}
