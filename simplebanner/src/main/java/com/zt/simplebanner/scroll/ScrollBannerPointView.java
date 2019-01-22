package com.zt.simplebanner.scroll;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.zt.simplebanner.OnPageChangedListener;
import com.zt.simplebanner.R;

public class ScrollBannerPointView extends LinearLayout implements OnPageChangedListener {

    private Context mContext;
    private int count;
    private AbsScrollBannerView scrollSlideView;

    public ScrollBannerPointView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initlialize(context);
    }

    public ScrollBannerPointView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initlialize(context);
    }

    public ScrollBannerPointView(Context context) {
        super(context);
        initlialize(context);
    }

    private void initlialize(Context context) {

        this.mContext = context;
        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER);
    }

    //必须在AbsScrollBannerView的bindData之后设置
    public void setScrollBannerView(AbsScrollBannerView scrollSlideView) {
        this.scrollSlideView = scrollSlideView;
        this.count = scrollSlideView.getCount();
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
            View point = new View(mContext);
            setUnSelectorView(point);
            addView(point);
        }
        scrollSlideView.addOnPageChangedListener(this);
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

