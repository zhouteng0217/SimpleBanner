package com.zt.simplebanner;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by zhouteng on 2017/3/27.
 *
 */

public class LoopRecyclerViewAdapter <VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    private LoopRecyclerViewPager mViewPager;
    private RecyclerView.Adapter<VH> mAdapter;

    public LoopRecyclerViewAdapter(LoopRecyclerViewPager viewPager, RecyclerView.Adapter<VH> adapter) {
        mAdapter = adapter;
        mViewPager = viewPager;
        setHasStableIds(mAdapter.hasStableIds());
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return mAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        super.registerAdapterDataObserver(observer);
        mAdapter.registerAdapterDataObserver(observer);
    }

    @Override
    public void unregisterAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        super.unregisterAdapterDataObserver(observer);
        mAdapter.unregisterAdapterDataObserver(observer);
    }

    @Override
    public void onViewRecycled(VH holder) {
        super.onViewRecycled(holder);
        mAdapter.onViewRecycled(holder);
    }

    @Override
    public boolean onFailedToRecycleView(VH holder) {
        return mAdapter.onFailedToRecycleView(holder);
    }

    @Override
    public void onViewAttachedToWindow(VH holder) {
        super.onViewAttachedToWindow(holder);
        mAdapter.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewDetachedFromWindow(VH holder) {
        super.onViewDetachedFromWindow(holder);
        mAdapter.onViewDetachedFromWindow(holder);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mAdapter.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        mAdapter.onDetachedFromRecyclerView(recyclerView);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        mAdapter.onBindViewHolder(holder, getActualPosition(position));
        final View itemView = holder.itemView;
        ViewGroup.LayoutParams lp;
        if (itemView.getLayoutParams() == null) {
            lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        } else {
            lp = itemView.getLayoutParams();
            if (mViewPager.getLayoutManager().canScrollHorizontally()) {
                lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
            } else {
                lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
            }
        }
        itemView.setLayoutParams(lp);
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(hasStableIds);
        mAdapter.setHasStableIds(hasStableIds);
    }

    public int getActualItemCount() {
        return mAdapter.getItemCount();
    }


    public int getActualPosition(int position) {
        if (isSupportLoop()) {
            if (position == 0) {
                return getActualItemCount() -1;
            }
            if (position == getItemCount()-1) {
                return 0;
            }
            return position - 1;
        } else {
            return position;
        }
    }

    @Override
    public int getItemCount() {
        if(isSupportLoop()) {
            return getActualItemCount() + 2;
        } else {
            return getActualItemCount();
        }
    }

    public boolean isSupportLoop() {
        return getActualItemCount() >= mViewPager.getMinLoopStartCount();
    }

    @Override
    public int getItemViewType(int position) {
        return mAdapter.getItemViewType(getActualPosition(position));
    }

    @Override
    public long getItemId(int position) {
        return mAdapter.getItemId(getActualPosition(position));
    }
}
