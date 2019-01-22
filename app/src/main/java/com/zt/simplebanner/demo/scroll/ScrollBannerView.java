package com.zt.simplebanner.demo.scroll;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zt.simplebanner.demo.R;
import com.zt.simplebanner.scroll.AbsScrollBannerView;

/**
 * scrollview形式的banner的具体实现
 */
public class ScrollBannerView extends AbsScrollBannerView<String> {

    public ScrollBannerView(Context context) {
        super(context);
    }

    public ScrollBannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollBannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected View getExView(int postion, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.adp_recyclerview_item, parent, false);
        TextView titleView = (TextView) view.findViewById(R.id.title);
        titleView.setText(data.get(postion));

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(getScreenWidth(context), ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layoutParams);

        return view;
    }

    private int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }


}
