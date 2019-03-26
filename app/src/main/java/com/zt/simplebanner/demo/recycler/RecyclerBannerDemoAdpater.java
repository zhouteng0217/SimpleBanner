package com.zt.simplebanner.demo.recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zt.simplebanner.recycler.RecyclerBannerAdapter;
import com.zt.simplebanner.demo.R;

/**
 * 具体的banner界面的实现适配器
 */
public class RecyclerBannerDemoAdpater extends RecyclerBannerAdapter<String> {


    private int itemPadding;

    public RecyclerBannerDemoAdpater(Context context) {
        super(context);
        initData();
    }

    private void initData() {
        for (int i = 0; i < 5; i++) {
            list.add(i + "");
        }
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.adp_recyclerview_item, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerBannerAdapter.RecyclerBannerViewHolder viewHolder, int position) {
        final SimpleViewHolder holder = (SimpleViewHolder) viewHolder;

        holder.itemView.setPadding(itemPadding, 0, itemPadding, 0);
        holder.title.setText(list.get(position));
    }


    public void setItemPadding(int itemPadding) {
        this.itemPadding = itemPadding;
        notifyDataSetChanged();
    }

    public class SimpleViewHolder extends RecyclerBannerViewHolder {
        public final TextView title;

        private SimpleViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
        }
    }
}
