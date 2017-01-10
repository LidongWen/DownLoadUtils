package com.wenld.sample_filedownload.adapterRecy;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by wenld- on 2016/3/17.
 */
public interface IAdapter<VH, M> {
    /**
     * 设置 主内容 View类型
     */
    int getAdvanceViewType(int position);

    /**
     * 加头View
     */
    void addHeaderView(View headerView);

    /**
     * 加尾View
     */
    void addFooterView(View footerView);

    /**
     * 数据绑定View
     */
    void onBindAdvanceViewHolder(VH holder, int i);

    /**
     * 加载 ViewGroup 容器
     */
    RecyclerView.ViewHolder onCreateAdvanceViewHolder(ViewGroup parent, int viewType);

    void setData(List<M> mDataList);

    List<M> getData();

    void setListener(ItemClickInterface<M> listener);
}
