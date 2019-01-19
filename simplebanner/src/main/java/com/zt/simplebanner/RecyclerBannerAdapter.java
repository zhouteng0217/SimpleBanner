package com.zt.simplebanner;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public abstract class RecyclerBannerAdapter<T> extends RecyclerView.Adapter<RecyclerBannerAdapter.RecyclerBannerViewHolder> {

    protected List<T> list = new ArrayList<>();
    protected Context context;
    private OnBannerItemClickListener onBannerItemClickListener;

    public RecyclerBannerAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<T> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public class RecyclerBannerViewHolder extends RecyclerView.ViewHolder {

        public RecyclerBannerViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onBannerItemClickListener != null) {
                        onBannerItemClickListener.onBannerItemClick(getAdapterPosition());
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public void setOnBannerItemClickListener(OnBannerItemClickListener onBannerItemClickListener) {
        this.onBannerItemClickListener = onBannerItemClickListener;
    }

    public interface OnBannerItemClickListener {
        void onBannerItemClick(int pos);
    }
}
