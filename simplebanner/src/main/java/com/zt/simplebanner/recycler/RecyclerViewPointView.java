package com.zt.simplebanner.recycler;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.zt.simplebanner.OnPageChangedListener;
import com.zt.simplebanner.R;

public class RecyclerViewPointView extends LinearLayout implements OnPageChangedListener {

    private LoopRecyclerViewPager recyclerViewPager;
    private int count;

    public RecyclerViewPointView(Context context) {
        this(context, null);
    }

    public RecyclerViewPointView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecyclerViewPointView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER);
    }

    //在LoopRecyclerViewPager的Adapter设置完数据后调用
    public void setRecyclerViewPager(LoopRecyclerViewPager recyclerViewPager) {
        this.recyclerViewPager = recyclerViewPager;
        this.count = recyclerViewPager == null ? 0 :
                recyclerViewPager.getActualItemCount();
        initView();
    }

    private void initView() {
        if (count < 2) {
            setVisibility(View.GONE);
            return;
        }
        setVisibility(View.VISIBLE);
        removeAllViews();
        for (int i = 0; i < count; i++) {
            View point = new View(getContext());
            setUnSelectorView(point);
            addView(point);
        }
        recyclerViewPager.addOnPageChangedListener(this);
    }

    protected void setUnSelectorView(View point) {
        if (point == null) {
            return;
        }
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(24, 24);
        layoutParams.setMargins(0, 0, 20, 0);
        point.setLayoutParams(layoutParams);
        point.setBackgroundResource(R.drawable.point_unselected_bg);
    }

    protected void setSelectorView(View point) {
        if (point == null) {
            return;
        }
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(30, 30);
        layoutParams.setMargins(0, 0, 20, 0);
        point.setLayoutParams(layoutParams);
        point.setBackgroundResource(R.drawable.point_selected_bg);
    }

    @Override
    public void OnPageChanged(int oldPosition, int newPosition) {
        setSelectorView(getChildAt(newPosition));
        setUnSelectorView(getChildAt(oldPosition));
    }
}
