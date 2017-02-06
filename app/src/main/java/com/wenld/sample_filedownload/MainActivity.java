package com.wenld.sample_filedownload;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.wenld.downloadutils.DownloadUtils;
import com.wenld.downloadutils.bean.FileInfo;
import com.wenld.downloadutils.bean.FileInfoDao;
import com.wenld.downloadutils.constant.IntentAction;
import com.wenld.downloadutils.constant.KeyName;
import com.wenld.downloadutils.db.FileInfoDB;
import com.wenld.sample_filedownload.tool.FastJsonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wenld on 2017/1/9.
 */
public class MainActivity extends AppCompatActivity {
    /**
     * UI
     */
    RecyclerView rlvAty;
    PdfThreeAdapter adapter;
    /**
     * data
     */
    List<ThreeModel> mDatas = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();
        initListener();
        /**
         * 如果想知道下载的情况，需要注册该广播
         */
        // 注册广播接收器，接收下载进度信息和结束信息
        IntentFilter filter = new IntentFilter();
        filter.addAction(IntentAction.ACTION_UPDATE);   //
        filter.addAction(IntentAction.ACTION_FINISH);   //结束时
        filter.addAction(IntentAction.ACTION_PAUSE);    //暂停
        filter.addAction(IntentAction.ACTION_FAILE);    //下载失败
        filter.addAction(IntentAction.ACTION_WAIT_DownLoad);    //进入下载队列等待下载
        registerReceiver(mReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        try {
            unregisterReceiver(mReceiver);
        } catch (Exception e) {

        }
        super.onDestroy();
    }

    private void initView() {
        this.rlvAty = (RecyclerView) findViewById(R.id.rlv_fagment_three);
        adapter = new PdfThreeAdapter(this);
        rlvAty.setLayoutManager(new LinearLayoutManager(this));
        rlvAty.setAdapter(adapter);

    }

    private void initData() {
        mDatas.clear();
        mDatas.addAll(FastJsonUtil.getListObjects("[{\"users\":\"店长/陈列小帮手\",\"dept_name\":\"零售管理中心\",\"file_url\":\"https://app.qipai.com:443/appCenter/file/files/终端橱窗推广与安装标准说明.pdf\",\"id\":\"61\",\"file_author\":\"零售管理中心\",\"file_desc\":\"\",\"file_img\":\"\",\"file_name\":\"终端橱窗推广与安装标准说明\",\"order_no\":\"1\",\"category_id\":\"7\",\"flag\":\"0\",\"create_time\":\"2016-12-22 09:00:28\"},{\"users\":\"店长/陈列小帮手\",\"dept_name\":\"零售管理中心\",\"file_url\":\"https://app.qipai.com:443/appCenter/file/files/POP海报制作流程与安装说明2016.pdf\",\"id\":\"60\",\"file_author\":\"零售管理中心\",\"file_desc\":\"\",\"file_img\":\"\",\"file_name\":\"POP海报制作流程与安装说明2016\",\"order_no\":\"1\",\"category_id\":\"7\",\"flag\":\"0\",\"create_time\":\"2016-12-22 09:00:27\"},{\"users\":\"店长/陈列小帮手\",\"dept_name\":\"零售管理中心\",\"file_url\":\"https://app.qipai.com:443/appCenter/file/files/集成店陈列标准.pdf\",\"id\":\"59\",\"file_author\":\"零售管理中心\",\"file_desc\":\"集成店陈列标准介绍\",\"file_img\":\"\",\"file_name\":\"集成店陈列标准\",\"order_no\":\"1\",\"category_id\":\"7\",\"flag\":\"0\",\"create_time\":\"2016-12-22 09:00:26\"},{\"users\":\"店长/陈列小帮手\",\"dept_name\":\"零售管理中心\",\"file_url\":\"https://app.qipai.com:443/appCenter/file/files/黑标9s代店陈列标准.pdf\",\"id\":\"58\",\"file_author\":\"零售管理中心\",\"file_desc\":\"九代店铺陈列标准介绍\",\"file_img\":\"\",\"file_name\":\"黑标9s代店陈列标准\",\"order_no\":\"1\",\"category_id\":\"7\",\"flag\":\"0\",\"create_time\":\"2016-12-22 09:00:25\"},{\"users\":\"店长/陈列小帮手\",\"dept_name\":\"零售管理中心\",\"file_url\":\"https://app.qipai.com:443/appCenter/file/files/八代陈列标准.pdf\",\"id\":\"57\",\"file_author\":\"零售管理中心\",\"file_desc\":\"八代店铺陈列标准介绍\",\"file_img\":\"\",\"file_name\":\"八代陈列标准\",\"order_no\":\"1\",\"category_id\":\"7\",\"flag\":\"0\",\"create_time\":\"2016-12-22 09:00:24\"}]", ThreeModel.class));
        updateUI();
    }

    private void initListener() {
        adapter.setLoderListener(new PdfThreeAdapter.IDownLoderListener() {
            @Override
            public void start(String id, String url, String fileName, int position) {
                /**
                 * 自定义消息
                 */
                OtherMessage otherMessage = new OtherMessage();
                otherMessage.setPostion(position);
                otherMessage.setId("1");
                /**
                 * 开始下载/继续下载
                 */
                DownloadUtils.startDownload(MainActivity.this, id, url, fileName, otherMessage);
            }

            @Override
            public void stop(String id, int position) {
                /**
                 * 暂停下载
                 */
                DownloadUtils.stopById(id);
            }

            @Override
            public void reload(String id, int position) {
                /**
                 * 自定义消息
                 */
                OtherMessage otherMessage = new OtherMessage();
                otherMessage.setPostion(position);
                otherMessage.setId("1");
                /**
                 *
                 */
                DownloadUtils.ReDownLoadById(MainActivity.this, id, otherMessage);
            }

            @Override
            public void delete(String id, int position) {
                /**
                 * 自定义消息
                 */
                OtherMessage otherMessage = new OtherMessage();
                otherMessage.setPostion(position);
                otherMessage.setId("1");

                DownloadUtils.delete(id, otherMessage);
            }
        });
    }

    /**
     * 广播接收器：更新UI和数据库
     **/
    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            OtherMessage otherMessage = (OtherMessage) intent.getSerializableExtra(KeyName.OTHER_MESSAGE); //获取我们的自定义消息
//            int finished = intent.getIntExtra(KeyName.FINISHED_TAG, 0); //获取下载总长度
//            double rate = intent.getDoubleExtra(KeyName.DOWNLOAD_RATE_TAG, 0); //下载速度
//            FileInfo mFileInfo = (FileInfo) intent.getSerializableExtra(KeyName.FILEINFO_TAG); //直接返回下载文件

            if (IntentAction.ACTION_WAIT_DownLoad.equals(intent.getAction())) {
                FileInfo mFileInfo = (FileInfo) intent.getSerializableExtra(KeyName.FILEINFO_TAG);
                if (mFileInfo != null) {
                    adapter.getFileInfoMap().put(mFileInfo.getId(), mFileInfo);
                    updateUIbyPostion(mFileInfo, otherMessage.getPostion());
                }
            }
            //进行下载：更新下载进度和速度
            else if (IntentAction.ACTION_UPDATE.equals(intent.getAction())) {
                int finished = intent.getIntExtra(KeyName.FINISHED_TAG, 0);
                String id = intent.getStringExtra(KeyName.ID_Postion);
                FileInfo mFileInfo = adapter.getFileInfoMap().get(id);
                if (mFileInfo != null) {
                    mFileInfo.setFinished(finished);
//                    adapter.getFileInfoMap().put(mFileInfo.getId(), mFileInfo);
                    updateUIbyPostion(mFileInfo, otherMessage.getPostion());
                }
            } else if (IntentAction.ACTION_FINISH.equals(intent.getAction())) { //下载结束

                FileInfo mFileInfo = (FileInfo) intent.getSerializableExtra(KeyName.FILEINFO_TAG);
                if (mFileInfo != null) {
                    adapter.getFileInfoMap().put(mFileInfo.getId(), mFileInfo);
                    updateUIbyPostion(mFileInfo, otherMessage.getPostion());
                }
            } else if (IntentAction.ACTION_PAUSE.equals(intent.getAction())) {  //下载暂停
                FileInfo mFileInfo = (FileInfo) intent.getSerializableExtra(KeyName.FILEINFO_TAG);
                if (mFileInfo != null) {
                    adapter.getFileInfoMap().put(mFileInfo.getId(), mFileInfo);
                    updateUIbyPostion(mFileInfo, otherMessage.getPostion());
                }
            } else if (IntentAction.ACTION_FAILE.equals(intent.getAction())) {  //下载失败
            }
        }
    };

    private void updateUI() {
        List<FileInfo> allList;
        FileInfo mFileInfo;

        for (ThreeModel threeModel : mDatas) {
            allList = new FileInfoDB().getQueryBuilder().where(FileInfoDao.Properties.Id.eq(threeModel.getId())).list();
            if (allList != null && allList.size() > 0) {
                mFileInfo = allList.get(0);
                adapter.getFileInfoMap().put(mFileInfo.getId(), mFileInfo);
            } else {
                mFileInfo = new FileInfo();

                mFileInfo.setUrl(threeModel.getFile_url());
                mFileInfo.setId(threeModel.getId());
                mFileInfo.setFileName(threeModel.getFile_name());
                mFileInfo.setMd5(threeModel.getFile_url());
                adapter.getFileInfoMap().put(mFileInfo.getId(), mFileInfo);
            }
        }
        bindList(mDatas);
    }


    public void bindList(List<ThreeModel> mDatas) {
        adapter.setData(mDatas);
        adapter.notifyDataSetChanged();
    }

    public void updateUIbyPostion(FileInfo fileInfo, int postion) {
//        adapter.notifyItemChanged(postion);
        LinearLayoutManager linearManager = (LinearLayoutManager) rlvAty.getLayoutManager();
        //获取最后一个可见view的位置
        int end = linearManager.findLastVisibleItemPosition();
        //获取第一个可见view的位置
        int start = linearManager.findFirstVisibleItemPosition();
        if (postion - start >= 0 && end - postion >= 0) {
//            adapter.notifyItemChanged(postion);
            View v = rlvAty.getChildAt(postion);
            if (v != null) {
                PdfThreeAdapter.Holder viewHolder = (PdfThreeAdapter.Holder) rlvAty.getChildViewHolder(v);
                if (viewHolder == null)
                    return;

                if (fileInfo == null)
                    return;

                adapter.bindProgress(viewHolder, fileInfo);
            } else {
                adapter.notifyItemChanged(postion);
            }
        }
    }
}
