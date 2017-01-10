package com.wenld.sample_filedownload.adapterRecy;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wenld- on 2015/9/24.
 * <p/>
 * 简单封装 加头加尾 recyclerView 适配器
 * 在布局 GridLayoutManager，StaggeredGridLayoutManager  头尾占一行
 */
public abstract class AdvancedAdapter<VH extends AdvancedAdapter.ViewHolder, M>
        extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements IAdapter<VH, M> {

    private ArrayList<View> mHeaderViews = new ArrayList<>();
    private ArrayList<View> mFooterViews = new ArrayList<>();

    protected ItemClickInterface<M> listener;
    protected List<M> mData;


    public final void addHeaderView(View headerView) {
        mHeaderViews.add(headerView);
    }

    public final void addFooterView(View footerView) {
        mFooterViews.add(footerView);
    }


    private SparseIntArray mHeaderViewTypes = new SparseIntArray();
    private SparseIntArray mFooterViewTypes = new SparseIntArray();

    private final static int HeaderFooterFlag = 100000;

    @Override
    public int getItemViewType(int position) {
        if (mHeaderViews.size() > 0 && position < mHeaderViews.size()) {
            //用position作为HeaderView 的   ViewType标记
            //记录每个ViewType标记
            position = (position + 1) * HeaderFooterFlag;
            mHeaderViewTypes.put(position, position);
            return position;
        }

        if (mFooterViews.size() > 0 && position > getAdvanceCount() - 1 + mHeaderViews.size()) {
            //用position作为FooterView 的   ViewType标记
            position = (position + 1) * HeaderFooterFlag;
            mFooterViewTypes.put(position, position);
            return position;
        }

        return getAdvanceViewType(position);
    }

    /**
     * @return
     */
    public int getAdvanceCount() {
        int a = 0;
        if (mData != null) {
            a = mData.size();
        }
        return a;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderViewTypes.size() > 0 && mHeaderViewTypes.get(viewType, -1) != (-1)) {
            return new HeaderHolder(mHeaderViews.get(viewType / 100000 - 1));
        }

        if (mFooterViewTypes.size() > 0 && mFooterViewTypes.get(viewType, -1) != (-1)) {
            int index = viewType / 100000 - 1 - getAdvanceCount() - mHeaderViews.size();
            return new FooterHolder(mFooterViews.get(index));
        }

        return onCreateAdvanceViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (mFooterViews.size() > 0 && (position > getAdvanceCount() - 1 + mHeaderViews.size())) {
            return;
        }

        if (mHeaderViews.size() > 0) {
            if (position < mHeaderViews.size()) {
                return;
            }
            onBindAdvanceViewHolder((VH) holder, position - mHeaderViews.size());
            return;
        }
        onBindAdvanceViewHolder((VH) holder, position - mHeaderViews.size());
    }


    class HeaderHolder extends RecyclerView.ViewHolder {

        public HeaderHolder(View itemView) {
            super(itemView);
        }
    }

    class FooterHolder extends RecyclerView.ViewHolder {

        public FooterHolder(View itemView) {
            super(itemView);
        }
    }

    public abstract class ViewHolder extends RecyclerView.ViewHolder implements IViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }

        public int getAdpPosition() {
            return Selected(getAdapterPosition());
        }
    }

    public M getItem(int position) {
        if (getItemCount() == 0) {
            return null;
        }
        return mData.get(position);
    }

    @Override
    public int getItemCount() {
        if (mHeaderViews.size() > 0 && mFooterViews.size() > 0) {
            return getAdvanceCount() + mHeaderViews.size() + mFooterViews.size();
        }
        if (mHeaderViews.size() > 0) {
            return getAdvanceCount() + mHeaderViews.size();
        }
        if (mFooterViews.size() > 0) {
            return getAdvanceCount() + mFooterViews.size();
        }

        return getAdvanceCount();
    }

    public int Selected(int position) {
        if (mHeaderViews.size() > 0 && position < mHeaderViews.size()) {
            return -1;
        }
        if (mFooterViews.size() > 0 && position > getAdvanceCount() - 1 + mHeaderViews.size()) {
            return -1;
        } else {
            return position - mHeaderViews.size();
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return (getItemViewType(position) % HeaderFooterFlag == 0)
                            ? 1 : gridManager.getSpanCount();
                }
            });
        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp != null
                && lp instanceof StaggeredGridLayoutManager.LayoutParams
                && holder.getLayoutPosition() == 0) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            p.setFullSpan(true);
        }
    }

    @Override
    public void setData(List<M> mDataList) {
        if (mDataList == null) {
            return;
        }
        this.mData = mDataList;
    }

    @Override
    public List<M> getData() {
        return mData;
    }

    @Override
    public void setListener(ItemClickInterface<M> listener) {
        this.listener = listener;
    }

    private static final int DELAY = 138;
    private int mLastPosition = -1;

    public void showItemAnim(final View view, final int position) {
//        final Context context = view.getContext();
//        if (position > mLastPosition) {
//            view.setAlpha(0);
//            view.postDelayed(new Runnable() {
//                public void run() {
//
//                    Animation animation = AnimationUtils.loadAnimation(context,
//                            R.anim.slide_in_right);
//                    animation.setAnimationListener(new Animation.AnimationListener() {
//                        @Override
//                        public void onAnimationStart(Animation animation) {
//                            view.setAlpha(1);
//                        }
//
//
//                        @Override
//                        public void onAnimationEnd(Animation animation) {
//                        }
//
//
//                        @Override
//                        public void onAnimationRepeat(Animation animation) {
//                        }
//                    });
//                    view.startAnimation(animation);
//                }
//            }, DELAY * (position % 8));
//            mLastPosition = position;
//        } else {
//            view.setAlpha(1);
//        }
    }
}