package com.wenld.downloadutils.tool;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.wenld.downloadutils.bean.FileInfo;
import com.wenld.downloadutils.bean.FileInfoDao;
import com.wenld.downloadutils.bean.ThreadInfo;
import com.wenld.downloadutils.bean.ThreadInfoDao;
import com.wenld.downloadutils.constant.DownloadConfig;
import com.wenld.downloadutils.constant.IntentAction;
import com.wenld.downloadutils.constant.KeyName;
import com.wenld.downloadutils.db.FileInfoDB;
import com.wenld.downloadutils.db.ThreadInfoDB;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.wenld.downloadutils.constant.DownloadConfig.getFileMaxNum;


/**
 * 下载任务类
 **/
public class DownloadTask<T> {
    /**
     * 上下文
     */
    private Context mContext = null;
    /**
     * 下载的文件的所有属性信息
     */
    private FileInfo mFileInfo = null;
    /**
     * 已下载字节长度
     */
    private int mFinishedLen = 0;
    /**
     * 下载暂停标志
     */
    public Boolean isPause = false;


    //------------------------
    /**
     * 默认的下载线程数
     */
    private int mThreadNum = 1;
    /**
     * 下载线程集合
     */
    private List<DownloadThread> mThreadList = null;
    /**
     * 带缓存线程池，s开头表示用到static关键字
     */
    public static ExecutorService sExecutorService = Executors.newFixedThreadPool(getFileMaxNum());


    public T postion;

    /**
     * 文件下载的线程任务类
     *
     * @param mContext  上下文
     * @param mFileInfo 下载的文件的所有属性信息
     * @param threadNum 文件分段下载线程数
     */
    public DownloadTask(Context mContext, FileInfo mFileInfo, int threadNum, T postion) {
        this.mContext = mContext;
        this.mFileInfo = mFileInfo;
        this.mThreadNum = threadNum;
        this.postion = postion;
    }


    /**
     * 开始下载文件
     */
    public void download() {
        /**
         * 通知 我在等待开始下载了
         */
        List<FileInfo> list = new FileInfoDB().getQueryBuilder().where(FileInfoDao.Properties.Id.eq(mFileInfo.getId())).list();
        if (list != null && list.size() > 0) {
            /**
             *  发送到多线程下载
             */
            mFileInfo = list.get(0);
            this.mFileInfo.setIsDownload(true);
            new FileInfoDB().update(mFileInfo);
        } else {
            this.mFileInfo.setIsDownload(true);
            new FileInfoDB().insert(mFileInfo);
        }

        mFileInfo.setOver(false);
        Intent intent = new Intent(IntentAction.ACTION_WAIT_DownLoad);
        intent.putExtra(KeyName.FILEINFO_TAG, mFileInfo);
        intent.putExtra(KeyName.OTHER_MESSAGE, ((Serializable) postion));
        mContext.sendBroadcast(intent);

        List<ThreadInfo> threadInfoList = null;
        {
            // 2016/12/29  先从数据库中获取线程
            threadInfoList = new ThreadInfoDB().getQueryBuilder().where(ThreadInfoDao.Properties.FileId.eq(mFileInfo.getId()), ThreadInfoDao.Properties.Over.eq(false)).list();
        }
        if (threadInfoList == null || threadInfoList.size() == 0) {
            threadInfoList = new ArrayList<>();
            int length = mFileInfo.getLength() / mThreadNum; // 获取单个线程下载长度
            for (int i = 0; i < mThreadNum; i++) {            // 创建线程信息
                ThreadInfo threadInfo = new ThreadInfo();
                threadInfo.setId(mFileInfo.getId() + "_" + i);
                threadInfo.setUrl(mFileInfo.getUrl());
                threadInfo.setStart(length * i);
                threadInfo.setEnd(((i + 1) * length - 1));
                threadInfo.setFinished(0);
                threadInfo.setFileId(mFileInfo.getId());
                threadInfo.setMd5(mFileInfo.getMd5());
                threadInfo.setOver(false);
                threadInfo.setOvertime("none");

                if (i == mThreadNum - 1) {
                    threadInfo.setEnd(mFileInfo.getLength());// 设置最后一个线程的下载长度
                }
                threadInfoList.add(threadInfo);    // 添加线程信息到集合

            }
            {
                // TODO: 2016/12/29 将线程情况 插入数据库
                new ThreadInfoDB().insertList(threadInfoList);
            }
        }
        // 启动多个线程进行下载
        mThreadList = new ArrayList<>();
        for (ThreadInfo info : threadInfoList) {
            DownloadThread downloadThread = new DownloadThread(info);
            if ((info.getFinished() < (info.getEnd() - info.getStart()) && info.getId().equals(mFileInfo.getId() + "_0")) ||
                    (info.getFinished() < (info.getEnd() - info.getStart() + 1) && !info.getId().equals(mFileInfo.getId() + "_0")))   //若未完成下载则下载
                DownloadTask.sExecutorService.execute(downloadThread);
            mThreadList.add(downloadThread);                       //添加线程到集合中
        }
    }


    /**
     * 进行文件下载的线程
     **/
    class DownloadThread extends Thread {
        /**
         * 文件下载线程的信息
         */
        private ThreadInfo mThreadInfo = null;
        /**
         * 标识线程是否执行完成
         */
        public Boolean isFinished = false;
        /**
         * 广播下载进度的间隔时间
         */
        private final static int BROADCAST_TIME = 100;
        /**
         * httpUrl连接
         */
        HttpURLConnection conn = null;

        /**
         * 任意写入文件：RandomAccessFile
         */
        RandomAccessFile raf = null;
        /**
         * 输入流
         */
        InputStream input = null;

        int start = 0;

        /**
         * 进行文件下载的线程
         *
         * @param mThreadInfo 文件下载线程的信息
         **/
        public DownloadThread(ThreadInfo mThreadInfo) {
            this.mThreadInfo = mThreadInfo;

        }

        @Override
        public void run() {
            if (isPause) {
                pause(0);
                return;
            }
            //设置url上资源下载位置/范围
            start = mThreadInfo.getStart() + mThreadInfo.getFinished();
            Log.e("DownloadTask", "bytes=" + start + "-" + mThreadInfo.getEnd());
            if (mThreadInfo.getOver()) {
                mFinishedLen += mThreadInfo.getFinished();
                isFinished = true;// 标识线程执行完毕
                checkAllThreadsFinished(mThreadInfo);
                return;
            }
            try {
                conn = HttpUtils.getInstance().createConnection(mThreadInfo.getUrl());
                conn.setRequestProperty("Range", "bytes=" + start + "-" + mThreadInfo.getEnd());

                if (conn.getResponseCode() == 206) {
                    /**
                     * 获取文件长度
                     */
                    byte[] buf = new byte[2048];
                    int len = 0;
                    long time = System.currentTimeMillis();
                    double progress_result = 0;
                    double download_rate = 0;

                    input = conn.getInputStream();

                    File file = new File(DownloadConfig.getFileDir(), mFileInfo.getFileName());
                    raf = new RandomAccessFile(file, "rwd");
                    raf.seek(start);
                    mFinishedLen += mThreadInfo.getFinished();
                    while ((len = input.read(buf)) != -1) {
                        //写入文件
                        raf.write(buf, 0, len);
                        mFinishedLen += len;
                        mThreadInfo.setFinished(mThreadInfo.getFinished() + len);

                        if (System.currentTimeMillis() - time > BROADCAST_TIME) {  //每500ms刷新一次
                            time = System.currentTimeMillis();
//                            progress_result = (double) mFinishedLen / (double) mFileInfo.getLength();  // 计算下载进度
                            download_rate = (double) len / (double) (1024 * BROADCAST_TIME / 1000);        // 计算下载速率
                            {
                                /**
                                 *  通知UI更新进度条
                                 */
                                Intent intent = new Intent(IntentAction.ACTION_UPDATE);
                                intent.putExtra(KeyName.FINISHED_TAG, mFinishedLen);
                                intent.putExtra(KeyName.ID_Postion, mFileInfo.getId());
                                intent.putExtra(KeyName.DOWNLOAD_RATE_TAG, download_rate);
                                intent.putExtra(KeyName.OTHER_MESSAGE, ((Serializable) postion));
                                mContext.sendBroadcast(intent);
                            }
                        }
                        //下载暂停时，保存下载进度搭配数据库
                        if (isPause) {
                            pause(download_rate);
                            return;
                        }

                        if (raf == null || input == null) {
                            {
                                //  2016/12/29   下载连接获取输入流或文件写入失败，清空数据库下载进度，更新UI进度条，并重新下载
                                new ThreadInfoDB().delete(mThreadInfo);
                                //  2017/1/1 更新文件的总进度至DB
                                mFileInfo.setFinished(0);
                                new FileInfoDB().update(mFileInfo);
                            }
                            download();
                        }
                    }
                    isFinished = true;// 标识线程执行完毕
                    checkAllThreadsFinished(mThreadInfo);

                    //更新数据库
                    mThreadInfo.setOver(true);
                    mThreadInfo.setOvertime(getCurrentTime());
                    new ThreadInfoDB().update(mThreadInfo);
                } else {
                    mFileInfo.setIsDownload(false);
                    new FileInfoDB().update(mFileInfo);

                    Intent intent = new Intent(IntentAction.ACTION_FAILE);
                    intent.putExtra(KeyName.OTHER_MESSAGE, ((Serializable) postion));
                    mContext.sendBroadcast(intent);
                }
            } catch (Exception e) {
//                                mFileInfo.setFinished((int) (progress_result * 100));
                mFileInfo.setIsDownload(false);
                new FileInfoDB().update(mFileInfo);

                Intent intent = new Intent(IntentAction.ACTION_FAILE);
                intent.putExtra(KeyName.OTHER_MESSAGE, ((Serializable) postion));
                mContext.sendBroadcast(intent);

            } finally {//关闭连接
                try {
                    isPause = true;
                    if (raf != null) {
                        raf.close();
                    } else {
                        System.out.println("DOwnloadTask-280行 RadomAccessFile发生错误");
                    }
                    if (input != null) {
                        input.close();
                    } else {
                        System.out.println("DOwnloadTask-280行 inputStream发生错误");
                    }
                    if (conn != null) {
                        conn.disconnect();
                    } else {
                        System.out.println("DOwnloadTask-280行 HttpURLConnection发生错误");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.e("DownloadTask end", "bytes=" + start + "-" + mThreadInfo.getEnd());
                return;
            }
        }

        private void pause(double download_rate) {
            try {
                //: 2016/12/29 保存线程下载进度到数据库
                new ThreadInfoDB().update(mThreadInfo);
                //  2017/1/1 更新文件的总进度至DB
                mFileInfo.setFinished(mFinishedLen);
                mFileInfo.setIsDownload(false);
//                mFileInfo.setRate(download_rate);
                new FileInfoDB().update(mFileInfo);
                /**
                 *  通知UI更新进度条
                 */
                Intent intent = new Intent(IntentAction.ACTION_PAUSE);
//                intent.putExtra(KeyName.FINISHED_TAG, mFinishedLen);
                intent.putExtra(KeyName.FILEINFO_TAG, mFileInfo);
                intent.putExtra(KeyName.OTHER_MESSAGE, ((Serializable) postion));
                mContext.sendBroadcast(intent);
            } catch (Exception e) {
            }
        }

    }

    /**
     * 判断所有线程是否执行完成
     *
     * @param threadInfo 下载文件的线程信息
     */
    private synchronized void checkAllThreadsFinished(ThreadInfo threadInfo) {  // synchronized 同步方法，保证同一时间只有一个线程访问该方法

        boolean allFinished = true;   //所有线程下载结束标识
        // 遍历线程集合，判断是否都下载完毕
        for (DownloadThread thread : mThreadList) {
            if (!thread.isFinished) {
                allFinished = false;
                break;
            }
        }
        // 所有线程下载结束：验证md5，相同则存储下载长度，否则清空；发送广播通知UI下载任务结束
        if (allFinished) {
            mFileInfo.setFinished(mFinishedLen);
            mFileInfo.setOver(true);
            mFileInfo.setOvertime(getCurrentTime());
            new FileInfoDB().update(mFileInfo);
            Intent intent = new Intent(IntentAction.ACTION_FINISH);
            intent.putExtra(KeyName.FILEINFO_TAG, mFileInfo);
            intent.putExtra(KeyName.OTHER_MESSAGE, ((Serializable) postion));
            mContext.sendBroadcast(intent);
        }
    }

    public String getCurrentTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        String currentTime = dateFormat.format(date);
        return currentTime;
    }
}
