package com.wenld.sample_filedownload;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wenld.downloadutils.DownLoadStutasUtil;
import com.wenld.downloadutils.bean.FileInfo;
import com.wenld.sample_filedownload.adapterRecy.AdvancedAdapter;
import com.wenld.sample_filedownload.tool.StringUtils;
import com.wenld.sample_filedownload.widgets.FlikerProgressBar;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/6/30.
 */
public class PdfThreeAdapter extends AdvancedAdapter<PdfThreeAdapter.Holder, ThreeModel> {
    Context mContext;
    public Map<String, FileInfo> fileInfoMap = new HashMap<>();

    public Map<String, FileInfo> getFileInfoMap() {
        return fileInfoMap;
    }

    public void setFileInfoMap(Map<String, FileInfo> fileInfoMap) {
        this.fileInfoMap = fileInfoMap;
    }

    public PdfThreeAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getAdvanceViewType(int position) {
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
            viewHolder.tv_memo_item_pdfthree.setText("自定义字段：" + StringUtils.processNullStr(item.getFile_desc()));
            viewHolder.tv_item_pdfthree.setTag(item.getId());
        }

        FileInfo fileInfo = fileInfoMap.get(item.getId());
        if (fileInfo == null)
            return;

        bindProgress(viewHolder, fileInfo);
//        // 完成
//        if (fileInfo.getOver() != null && fileInfo.getOver()) {
//            viewHolder.flikerbar_item_pdfthree.finishLoad();
//        } else {
//            if (fileInfo.getIsDownload()) {
//                viewHolder.flikerbar_item_pdfthree.processingLoad();
//            } else {
//                if (fileInfo.getFinished() != null && fileInfo.getFinished() > 0) {
//                    viewHolder.flikerbar_item_pdfthree.pauseLoad();
//                } else {
//                    viewHolder.flikerbar_item_pdfthree.unBeginLoad();
//                }
//            }
//        }
    }

    public static void bindProgress(Holder viewHolder, FileInfo fileInfo) {
        if (fileInfo.getFinished() != null && fileInfo.getFinished() > 0 && fileInfo.getLength() != null && fileInfo.getLength() > 0) {
            viewHolder.flikerbar_item_pdfthree.setProgress((int) (((double) fileInfo.getFinished() / (double) fileInfo.getLength()) * 100));
        }
        switch (DownLoadStutasUtil.getDownLoadStatus(fileInfo)) {
            case DownLoadStutasUtil.DownLoad_Status_waitStart://等待开始
                viewHolder.flikerbar_item_pdfthree.unBeginLoad();
                break;
            case DownLoadStutasUtil.DownLoad_Status_loading://下载中
                viewHolder.flikerbar_item_pdfthree.processingLoad();
                break;
            case DownLoadStutasUtil.DownLoad_Status_pauseLoad://暂停
                viewHolder.flikerbar_item_pdfthree.pauseLoad();
                break;
            case DownLoadStutasUtil.DownLoad_Status_finished://完成下载
                viewHolder.flikerbar_item_pdfthree.finishLoad();
                break;
        }
    }


    private SpannableString setColorSpan(String textStr, int Color) {
        SpannableString subjectSpanText = new SpannableString(textStr);
        subjectSpanText.setSpan(new ForegroundColorSpan(Color), 0, textStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return subjectSpanText;
    }

    @Override
    public Holder onCreateAdvanceViewHolder(ViewGroup parent, int viewType) {
        Holder viewHolder;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pdfthree, parent, false);
        viewHolder = new Holder(view);
        return viewHolder;
    }

    public static boolean bool = false;

    public class Holder extends AdvancedAdapter.ViewHolder {
        public ImageView iv_item_pdfthree;
        public TextView tv_item_pdfthree;
        public TextView tv_memo_item_pdfthree;
        public FlikerProgressBar flikerbar_item_pdfthree;
        public Button btnStart;
        public Button btnStop;
        public Button btnReload;
        public Button btnDelete;

        public Holder(View rootView) {
            super(rootView);
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (listener != null) {
//                        listener.onItemClick(mData.get(getAdpPosition()), getAdpPosition());
//                    }
//                }
//            });
            this.iv_item_pdfthree = (ImageView) rootView.findViewById(R.id.iv_item_pdfthree);
            this.tv_item_pdfthree = (TextView) rootView.findViewById(R.id.tv_item_pdfthree);
            this.tv_memo_item_pdfthree = (TextView) rootView.findViewById(R.id.tv_memo_item_pdfthree);
            this.flikerbar_item_pdfthree = (FlikerProgressBar) rootView.findViewById(R.id.flikerbar_item_pdfthree);

            this.btnStart = (Button) rootView.findViewById(R.id.btn_start);
            this.btnStop = (Button) rootView.findViewById(R.id.btn_stop);
            this.btnReload = (Button) rootView.findViewById(R.id.btn_reload);
            this.btnDelete = (Button) rootView.findViewById(R.id.btn_delete);
            btnDelete.setVisibility(View.GONE);

            btnStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (loderListener != null) {
                        loderListener.start(fileInfoMap.get(mData.get(getAdpPosition()).getId()).getId(), fileInfoMap.get(mData.get(getAdpPosition()).getId()).getUrl()
                                , fileInfoMap.get(mData.get(getAdpPosition()).getId()).getFileName(), getAdpPosition());
                    }
                }
            });
            btnStop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (loderListener != null) {
                        loderListener.stop(fileInfoMap.get(mData.get(getAdpPosition()).getId()).getId(), getAdpPosition());
                    }
                }
            });
            btnReload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (loderListener != null) {
                        loderListener.reload(fileInfoMap.get(mData.get(getAdpPosition()).getId()).getId(), getAdpPosition());
                    }
                }
            });
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (loderListener != null) {
                        loderListener.delete(fileInfoMap.get(mData.get(getAdpPosition()).getId()).getId(), getAdpPosition());
                    }
                }
            });
        }
    }

    IDownLoderListener loderListener;

    public void setLoderListener(IDownLoderListener loderListener) {
        this.loderListener = loderListener;
    }

    public interface IDownLoderListener {
        void start(String id, String url,String fileName, int position);

        void stop(String id, int position);

        void reload(String id, int position);

        void delete(String id, int position);
    }
}