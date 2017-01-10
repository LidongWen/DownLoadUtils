package com.wenld.sample_filedownload;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.wenld.sample_filedownload.adapterRecy.AdvancedAdapter;
import com.wenld.sample_filedownload.adapterRecy.ItemClickInterface;
import com.wenld.sample_filedownload.tool.FastJsonUtil;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by wenld on 2017/1/9.
 */
public class MainActivity extends AppCompatActivity {
    /**
     * UI
     */
    RecyclerView rlvAty;
    SwipeRefreshLayout mSwipeLayout;
    AdvancedAdapter adapter;
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
        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout_fagment_three);
        this.rlvAty = (RecyclerView) findViewById(R.id.rlv_fagment_three);
        adapter = new PdfThreeAdapter(this);
        rlvAty.setLayoutManager(new LinearLayoutManager(this));
        rlvAty.setAdapter(adapter);

        mSwipeLayout.setColorSchemeColors(Color.BLUE,
                Color.GREEN,
                Color.YELLOW,
                Color.RED);
        mSwipeLayout.setSize(SwipeRefreshLayout.LARGE);
    }


    void PDFonItemClick(ThreeModel item, int position) {
        try {
            QueryBuilder qb = new FileInfoDB().getQueryBuilder();
            List<FileInfo> allList = qb.where(FileInfoDao.Properties.Id.eq(item.getId())).list();
            FileInfo mFileInfo;
            if (allList != null && allList.size() > 0) {
                mFileInfo = allList.get(0);
                if (mFileInfo.getOver() != null && mFileInfo.getOver()) {
                    Intent intents = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(intents);
                } else if (mFileInfo.getIsDownload()) {
                    /**
                     * 暂停下载
                     */
                    DownloadUtils.stop(mFileInfo, null);
                    return;
                }
            } else {
                mFileInfo = new FileInfo();
                mFileInfo.setUrl(item.getFile_url());
                mFileInfo.setId(item.getId());
                mFileInfo.setFileName(item.getFile_name());
                mFileInfo.setMd5(item.getFile_url());
            }
            /**
             * 自定义消息
             */
            OtherMessage otherMessage = new OtherMessage();
            otherMessage.setPostion(position);
            otherMessage.setId("1");
            /**
             * 开始下载/继续下载
             */
            DownloadUtils.startDownload(this, mFileInfo, otherMessage);

        } catch (Exception e) {
        }

    }

    private void initData() {
        mDatas.clear();
        mDatas.addAll(FastJsonUtil.getListObjects("[{\"users\":\"店长/陈列小帮手\",\"dept_name\":\"零售管理中心\",\"file_url\":\"https://app.qipai.com:443/appCenter/file/files/终端橱窗推广与安装标准说明.pdf\",\"id\":\"61\",\"file_author\":\"零售管理中心\",\"file_desc\":\"\",\"file_img\":\"\",\"file_name\":\"终端橱窗推广与安装标准说明\",\"order_no\":\"1\",\"category_id\":\"7\",\"flag\":\"0\",\"create_time\":\"2016-12-22 09:00:28\"},{\"users\":\"店长/陈列小帮手\",\"dept_name\":\"零售管理中心\",\"file_url\":\"https://app.qipai.com:443/appCenter/file/files/POP海报制作流程与安装说明2016.pdf\",\"id\":\"60\",\"file_author\":\"零售管理中心\",\"file_desc\":\"\",\"file_img\":\"\",\"file_name\":\"POP海报制作流程与安装说明2016\",\"order_no\":\"1\",\"category_id\":\"7\",\"flag\":\"0\",\"create_time\":\"2016-12-22 09:00:27\"},{\"users\":\"店长/陈列小帮手\",\"dept_name\":\"零售管理中心\",\"file_url\":\"https://app.qipai.com:443/appCenter/file/files/集成店陈列标准.pdf\",\"id\":\"59\",\"file_author\":\"零售管理中心\",\"file_desc\":\"集成店陈列标准介绍\",\"file_img\":\"\",\"file_name\":\"集成店陈列标准\",\"order_no\":\"1\",\"category_id\":\"7\",\"flag\":\"0\",\"create_time\":\"2016-12-22 09:00:26\"},{\"users\":\"店长/陈列小帮手\",\"dept_name\":\"零售管理中心\",\"file_url\":\"https://app.qipai.com:443/appCenter/file/files/黑标9s代店陈列标准.pdf\",\"id\":\"58\",\"file_author\":\"零售管理中心\",\"file_desc\":\"九代店铺陈列标准介绍\",\"file_img\":\"\",\"file_name\":\"黑标9s代店陈列标准\",\"order_no\":\"1\",\"category_id\":\"7\",\"flag\":\"0\",\"create_time\":\"2016-12-22 09:00:25\"},{\"users\":\"店长/陈列小帮手\",\"dept_name\":\"零售管理中心\",\"file_url\":\"https://app.qipai.com:443/appCenter/file/files/八代陈列标准.pdf\",\"id\":\"57\",\"file_author\":\"零售管理中心\",\"file_desc\":\"八代店铺陈列标准介绍\",\"file_img\":\"\",\"file_name\":\"八代陈列标准\",\"order_no\":\"1\",\"category_id\":\"7\",\"flag\":\"0\",\"create_time\":\"2016-12-22 09:00:24\"}]", ThreeModel.class));
        updateUI();
    }

    private void initListener() {
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeLayout.setRefreshing(false);
            }
        });
        adapter.setListener(new ItemClickInterface<ThreeModel>() {
            @Override
            public void onItemClick(ThreeModel Item, int position) {
                PDFonItemClick(Item, position);
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

            if (mDatas == null || mDatas.size() < 1)
                return;

            if (IntentAction.ACTION_WAIT_DownLoad.equals(intent.getAction())) {
                ThreeModel threeModel = mDatas.get(otherMessage.getPostion());
                threeModel.setIsDownload(true);
                updateUIbyPostion(threeModel, otherMessage.getPostion());
            }
            //进行下载：更新下载进度和速度
            else if (IntentAction.ACTION_UPDATE.equals(intent.getAction())) {
                int finished = intent.getIntExtra(KeyName.FINISHED_TAG, 0);
                double rate = intent.getDoubleExtra(KeyName.DOWNLOAD_RATE_TAG, 0);
                ThreeModel threeModel = mDatas.get(otherMessage.getPostion());
                FileInfo mFileInfo = (FileInfo) intent.getSerializableExtra(KeyName.FILEINFO_TAG);
                if (mFileInfo != null) {
                    mergeThreeModel(threeModel, mFileInfo);
                    if (mFileInfo.getOver() != null && mFileInfo.getOver()) {
//                        Intent intents = new Intent(mContext, PDFShowActivity.class);
//                        intents.putExtra(SType.Intent_FilePath, "file://" + destFileDir + "/" + mFileInfo.getFileName());
//                        mContext.startActivity(intents);
                    }
                }
                threeModel.setFinished(finished);
                threeModel.setIsDownload(true);
                updateUIbyPostion(threeModel, otherMessage.getPostion());

            } else if (IntentAction.ACTION_FINISH.equals(intent.getAction())) { //下载结束

                ThreeModel threeModel = mDatas.get(otherMessage.getPostion());
                threeModel.setOver(true);
                QueryBuilder qb = new FileInfoDB().getQueryBuilder();
                List<FileInfo> allList = qb.where(FileInfoDao.Properties.Id.eq(threeModel.getId())).list();
                FileInfo mFileInfo;
                if (allList != null && allList.size() > 0) {
                    mFileInfo = allList.get(0);
                    mergeThreeModel(threeModel, mFileInfo);
                }
                if (threeModel.getOver()) {
//                        Intent intents = new Intent(MainActivity.this, MainActivity.class);
//                        startActivity(intents);
                }
                updateUIbyPostion(threeModel, otherMessage.getPostion());
            } else if (IntentAction.ACTION_PAUSE.equals(intent.getAction())) {  //下载暂停
                ThreeModel threeModel = mDatas.get(otherMessage.getPostion());

                QueryBuilder qb = new FileInfoDB().getQueryBuilder();
                List<FileInfo> allList = qb.where(FileInfoDao.Properties.Id.eq(threeModel.getId())).list();
                FileInfo mFileInfo;
                if (allList != null && allList.size() > 0) {
                    mFileInfo = allList.get(0);
                    mergeThreeModel(threeModel, mFileInfo);
                }
                updateUIbyPostion(threeModel, otherMessage.getPostion());
            } else if (IntentAction.ACTION_FAILE.equals(intent.getAction())) {  //下载失败
                ThreeModel threeModel = mDatas.get(otherMessage.getPostion());

                QueryBuilder qb = new FileInfoDB().getQueryBuilder();
                List<FileInfo> allList = qb.where(FileInfoDao.Properties.Id.eq(threeModel.getId())).list();
                FileInfo mFileInfo;
                if (allList != null && allList.size() > 0) {
                    mFileInfo = allList.get(0);
                    mergeThreeModel(threeModel, mFileInfo);
                }
                updateUIbyPostion(threeModel, otherMessage.getPostion());
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

                threeModel.setLength(mFileInfo.getLength());
                threeModel.setUrl(mFileInfo.getUrl());
                threeModel.setFileName(mFileInfo.getFileName());
                threeModel.setMd5(mFileInfo.getMd5());
                threeModel.setFinished(mFileInfo.getFinished());
                threeModel.setRate(mFileInfo.getRate());
                threeModel.setOver(mFileInfo.getOver());
                threeModel.setOvertime(mFileInfo.getOvertime());
                threeModel.setIsDownload(mFileInfo.getIsDownload());
            }
        }
        bindList(mDatas);
    }

    private void mergeThreeModel(ThreeModel threeModel, FileInfo mFileInfo) {

        threeModel.setLength(mFileInfo.getLength());
        threeModel.setUrl(mFileInfo.getUrl());
        threeModel.setFileName(mFileInfo.getFileName());
        threeModel.setMd5(mFileInfo.getMd5());
        threeModel.setFinished(mFileInfo.getFinished());
        threeModel.setRate(mFileInfo.getRate());
        threeModel.setOver(mFileInfo.getOver());
        threeModel.setOvertime(mFileInfo.getOvertime());
        threeModel.setIsDownload(mFileInfo.getIsDownload());

    }


    public void bindList(List<ThreeModel> mDatas) {
        adapter.setData(mDatas);
        adapter.notifyDataSetChanged();
        if (mSwipeLayout.isRefreshing())
            mSwipeLayout.setRefreshing(false);
    }

    public void updateUIbyPostion(ThreeModel mData, int postion) {
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
                viewHolder.flikerbar_item_pdfthree.setVisibility(View.VISIBLE);

                if (mData.getOver() != null && mData.getOver()) {
                    viewHolder.flikerbar_item_pdfthree.finishLoad();
                } else {
                    if (mData.getIsDownload() != null && mData.getIsDownload()) {
                        try {
                            viewHolder.flikerbar_item_pdfthree.setProgress((int) (((double) mData.getFinished() / (double) mData.getLength()) * 100));
                        } catch (Exception e) {
                        }
                        viewHolder.flikerbar_item_pdfthree.processingLoad();
                    } else {
                        if (mData.getFinished() > 0) {
                            viewHolder.flikerbar_item_pdfthree.pauseLoad();
                        } else {
                            viewHolder.flikerbar_item_pdfthree.unBeginLoad();
                        }
                    }
                }
            } else {
                adapter.notifyItemChanged(postion);
            }
        }
    }
}
