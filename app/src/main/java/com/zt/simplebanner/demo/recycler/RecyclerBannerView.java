package com.zt.simplebanner.demo.recycler;


import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.zt.simplebanner.OnBannerClickListener;
import com.zt.simplebanner.recycler.LoopRecyclerViewPager;
import com.zt.simplebanner.recycler.RecyclerViewPointView;
import com.zt.simplebanner.demo.R;

/**
 * banner的具体实现和底下的指示器封装后的View
 */
public class RecyclerBannerView extends LinearLayout {

    private LoopRecyclerViewPager recyclerViewPager;
    private RecyclerViewPointView pointView;
    private RecyclerBannerDemoAdpater bannerAdpater;
    private int style;

    public RecyclerBannerView(Context context) {
        this(context, null);
    }

    public RecyclerBannerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecyclerBannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }

    private void initViews(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_recycler_banner, this);
        recyclerViewPager = findViewById(R.id.recyclerview_pager);
        pointView = findViewById(R.id.point_view);

        LinearLayoutManager layout = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewPager.setLayoutManager(layout);
        recyclerViewPager.setHasFixedSize(true);
    }

    public void bindData() {
        bannerAdpater = new RecyclerBannerDemoAdpater(getContext());
        recyclerViewPager.setAdapter(bannerAdpater);

        bannerAdpater.setOnBannerClickListener(new OnBannerClickListener() {
            @Override
            public void onBannerClick(int position) {
                int pos = recyclerViewPager.getActualItemCount() >= recyclerViewPager.getMinLoopStartCount() ? position - 1 : position;
                if (pos >= 0 && pos < recyclerViewPager.getActualItemCount()) {
                    Toast.makeText(getContext(), "click: " + pos, Toast.LENGTH_SHORT).show();
                }
            }
        });

        //一定要在adapter的数据源设置完成后，调用
        pointView.setRecyclerViewPager(recyclerViewPager);

        recyclerViewPager.scrollToItem(1);
    }

    //style2和3适用于数据源列表有3条及以上数据的情况
    public void setStyle(int style) {
        this.style = style;
        switch (style) {
            case 1:
                recyclerViewPager.setClipToPadding(true);
                recyclerViewPager.setPadding(0, 0, 0, 0);
                bannerAdpater.setItemPadding(0);
                break;
            case 2:
                recyclerViewPager.setClipToPadding(false);
                recyclerViewPager.setPadding(45, 0, 45, 0);
                bannerAdpater.setItemPadding(10);
                break;
            case 3:
                recyclerViewPager.setClipToPadding(false);
                recyclerViewPager.setPadding(45, 0, 45, 0);
                bannerAdpater.setItemPadding(10);
                break;
        }
        recyclerViewPager.removeOnScrollListener(onScrollListener);
        recyclerViewPager.addOnScrollListener(onScrollListener);
    }

    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            int childCount = recyclerViewPager.getChildCount();
            int width = recyclerViewPager.getChildAt(0).getWidth();
            int padding = (recyclerViewPager.getWidth() - width) / 2;

            for (int i = 0; i < childCount; i++) {
                View v = recyclerView.getChildAt(i);

                if (style == 1 || style == 2) {
                    v.setScaleY(1.0f);
                    continue;
                }

                //往左 从 padding 到 -(v.getWidth()-padding) 的过程中，由大到小
                float rate = 0;
                if (v.getLeft() <= padding) {
                    if (v.getLeft() >= padding - v.getWidth()) {
                        rate = (padding - v.getLeft()) * 1f / v.getWidth();
                    } else {
                        rate = 1;
                    }
                    v.setScaleY(1 - rate * 0.1f);
                } else {
                    //往右 从 padding 到 recyclerView.getWidth()-padding 的过程中，由大到小
                    if (v.getLeft() <= recyclerView.getWidth() - padding) {
                        rate = (recyclerView.getWidth() - padding - v.getLeft()) * 1f / v.getWidth();
                    }
                    v.setScaleY(0.9f + rate * 0.1f);
                }
            }
        }
    };

    public void startAutoLoop(int time) {
        if (recyclerViewPager != null) {
            recyclerViewPager.startLoop(time);
        }
    }

    public void stopAutoLoop() {
        if (recyclerViewPager != null) {
            recyclerViewPager.stopLoop();
        }
    }

    public void onResume() {
        if (recyclerViewPager != null) {
            recyclerViewPager.onResume();
        }
    }

    public void onPause() {
        if (recyclerViewPager != null) {
            recyclerViewPager.onPause();
        }
    }

}
