package com.wenld.sample_filedownload;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wenld.sample_filedownload.adapterRecy.AdvancedAdapter;
import com.wenld.sample_filedownload.tool.StringUtils;
import com.wenld.sample_filedownload.widgets.FlikerProgressBar;

/**
 * Created by Administrator on 2016/6/30.
 */
public class PdfThreeAdapter extends AdvancedAdapter<PdfThreeAdapter.Holder, ThreeModel> {
    Context mContext;

    public PdfThreeAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getAdvanceViewType(int position) {
/**
 * 这边设置不同的itemType 在网格布局中会出现独占一行的问题
 */
//        if (mData != null && mData.size() > 0) {
//            switch (StringUtils.processNullStr(mData.get(position).getType())) {
//                case SType.type_APK:
//                    return 0x1001;
//                case SType.type_PDF:
//                    return 0x1002;
//                case SType.type_Video:
//                    return 0x1003;
//                case SType.type_WEB:
//                    return 0x1004;
//            }
//        }
        return 0;
    }

    @Override
    public void onBindAdvanceViewHolder(Holder viewHolder, int position) {
//
        ThreeModel item = this.mData.get(position);
        if (!item.getId().equals(viewHolder.tv_item_pdfthree.getTag())) {
            Glide.with(mContext).load(item.getFile_img())
                    .error(R.mipmap.empty_pdf)
                    .dontAnimate()
                    .into(viewHolder.iv_item_pdfthree);
            viewHolder.tv_item_pdfthree.setText(StringUtils.processNullStr(item.getFile_name()));
            viewHolder.tv_memo_item_pdfthree.setText(StringUtils.processNullStr(item.getFile_desc()));
            viewHolder.tv_item_pdfthree.setTag(item.getId());
        }

        // 完成 就隐藏
        if (item.getOver() != null && item.getOver()) {
            viewHolder.flikerbar_item_pdfthree.finishLoad();
        } else {
            if (item.getIsDownload() != null && item.getIsDownload()) {
                viewHolder.flikerbar_item_pdfthree.setProgress((int) (((double) item.getFinished() / (double) item.getLength()) * 100));
                viewHolder.flikerbar_item_pdfthree.processingLoad();
            } else {
                if (item.getFinished() > 0) {
                    viewHolder.flikerbar_item_pdfthree.pauseLoad();
                } else {
                    viewHolder.flikerbar_item_pdfthree.unBeginLoad();
                }
            }
        }
    }
//        viewHolder.tvUserName.setText(item.getNickname());
//        viewHolder.tvTime.setText(StringUtils.processNullStr(item.getCreate_time()));
//        if ("".equals(StringUtils.processNullStr(item.getReply_user_id()))) {
//            viewHolder.tvContent.setText(StringUtils.processNullStr(item.getContent()));
//        } else {
//            SpannableStringBuilder builder = new SpannableStringBuilder();
//            builder.append(StringUtils.processNullStr(item.getContent()) + " ");
//            builder.append(setClickableSpan("||@" + StringUtils.processNullStr(item.getReply_nickname()) + " :", StringUtils.processNullStr(item.getReply_user_id())));
//            builder.append(setColorSpan(StringUtils.processNullStr(item.getReply_content()), mContext.getResources().getColor(R.color.darkgray)));
//            viewHolder.tvContent.setText(builder);
//        }
//        laudChanged(viewHolder.ivZan, item.getDiscuss_like());
//        viewHolder.tvZanCount.setText(StringUtils.processNullStr(item.getLike_num()));
//        showItemAnim(viewHolder.tvContent, position);


    private SpannableString setColorSpan(String textStr, int Color) {
        SpannableString subjectSpanText = new SpannableString(textStr);
        subjectSpanText.setSpan(new ForegroundColorSpan(Color), 0, textStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return subjectSpanText;
    }

    @Override
    public PdfThreeAdapter.Holder onCreateAdvanceViewHolder(ViewGroup parent, int viewType) {
        PdfThreeAdapter.Holder viewHolder;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pdfthree, parent, false);
        viewHolder = new PdfThreeAdapter.Holder(view);
        return viewHolder;
    }

    public static boolean bool = false;

    public class Holder extends AdvancedAdapter.ViewHolder {
        public ImageView iv_item_pdfthree;
        public TextView tv_item_pdfthree;
        public TextView tv_memo_item_pdfthree;
        public FlikerProgressBar flikerbar_item_pdfthree;

        public Holder(View rootView) {
            super(rootView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onItemClick(mData.get(getAdpPosition()), getAdpPosition());
                    }
                }
            });
            this.iv_item_pdfthree = (ImageView) rootView.findViewById(R.id.iv_item_pdfthree);
            this.tv_item_pdfthree = (TextView) rootView.findViewById(R.id.tv_item_pdfthree);
            this.tv_memo_item_pdfthree = (TextView) rootView.findViewById(R.id.tv_memo_item_pdfthree);
            this.flikerbar_item_pdfthree = (FlikerProgressBar) rootView.findViewById(R.id.flikerbar_item_pdfthree);
        }

    }

//    /**
//     * 更新listView可视区域中item的进度条和下载进度、速度
//     * @param id         需要更新的文件在list中的id
//     * @param progres    需要更新的下载文件的下载进度
//     * @param rate		  需要更新的下载文件的下载速度
//     */
//    public void updateListView(int id, double progres, double rate ){
//
//        // 判断下载中的文件是否在可视区，是则更新进度和下载速度
//        ThreeModel fileInfo = mData.get(id);
//        int start = getFirstVisiblePosition();  // 可见视图的首个item的位置
//        int end   = mListView.getLastVisiblePosition();   // 可见视图的最后item的位置
//        int  position = mFileList.indexOf(fileInfo);      // 获取需要更新进度的下载文件fileInfo的位置
//        //----------
//        if( position -start >= 0 && end -position >= 0 ){
//            View view = mListView.getChildAt(position -start);
//            if(view == null) {
//                return;
//            }
//            //------------
//            fileInfo.setRate( rate);
//            fileInfo.setFinished( progres);
//            ViewHolder holder = (ViewHolder) view.getTag();
//            setData( holder, position);
//        }
//    }

}