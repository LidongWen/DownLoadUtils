package com.wenld.downloadutils;

import com.wenld.downloadutils.constant.DownloadConfig;
import com.wenld.downloadutils.tool.HttpUtils;
import com.wenld.downloadutils.tool.SSLParams;

import java.io.File;
import java.io.InputStream;

/**
 *  下载配置
 * <p/>
 * Author: 温利东 on 2017/2/6 14:21.
 * blog: http://blog.csdn.net/sinat_15877283
 * github: https://github.com/LidongWen
 */

public class DownLoadSetting {

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

}
