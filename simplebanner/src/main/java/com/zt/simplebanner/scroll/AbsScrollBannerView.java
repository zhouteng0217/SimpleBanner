package com.zt.simplebanner.scroll;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.zt.simplebanner.OnBannerClickListener;
import com.zt.simplebanner.OnPageChangedListener;

import java.util.ArrayList;
import java.util.List;

public abstract class AbsScrollBannerView<T> extends HorizontalScrollView {

    private LinearLayout container = null;

    protected List<T> data;
    private int curPos;
    private MotionEvent startEvent;
    private VelocityTracker velocityTracker;

    private int mMaximumFlingVelocity;
    private int mMinimumFlingVelocity;


    private long loopTimeInterval = 0;
    private final static int scrollDistance = 300;
    protected Context context;

    private List<OnPageChangedListener> onPageChangedListeners = new ArrayList<>();
    private OnBannerClickListener onBannerClickListener;

    public AbsScrollBannerView(Context context) {
        super(context);
        init(context);
    }

    public AbsScrollBannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AbsScrollBannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;

        final ViewConfiguration configuration = ViewConfiguration.get(context);
        mMaximumFlingVelocity = configuration.getScaledMaximumFlingVelocity();
        mMinimumFlingVelocity = configuration.getScaledMinimumFlingVelocity();

        container = new LinearLayout(context);
        container.setOrientation(LinearLayout.HORIZONTAL);

        addView(container, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        setSmoothScrollingEnabled(true);
        setHorizontalScrollBarEnabled(false);
    }

    public void bindData(List<T> data) {
        this.data = data;
        container.removeAllViews();

        if (data == null || data.size() == 0) {
            return;
        }
        int size = data.size();
        if (size > 1) {
            View view = getExView(size - 1, container);
            container.addView(view);
        }
        for (int i = 0; i < size; i++) {
            View view = getExView(i, container);
            final int index = i;
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onBannerClickListener != null) {
                        onBannerClickListener.onBannerClick(index);
                    }
                }
            });
            container.addView(view);
        }
        if (size > 1) {
            container.addView(getExView(0, container));
        }
        post(new Runnable() {
            @Override
            public void run() {
                scrollToItem(1);
            }
        });
    }

    public int getCount() {
        return data == null ? 0 : data.size();
    }

    protected abstract View getExView(int postion, ViewGroup parent);

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                stopLoop();
                startEvent = MotionEvent.obtain(ev);
                if (velocityTracker == null) {
                    velocityTracker = VelocityTracker.obtain();
                } else {
                    velocityTracker.clear();
                }
                velocityTracker.addMovement(ev);
                break;
            case MotionEvent.ACTION_MOVE:
                velocityTracker.addMovement(ev);
                break;
            case MotionEvent.ACTION_UP:
                startLoop(loopTimeInterval);
                velocityTracker.computeCurrentVelocity(1000, mMaximumFlingVelocity);
                int pointerId = ev.getPointerId(0);
                float velocityX = velocityTracker.getXVelocity(pointerId);
                velocityTracker.recycle();
                velocityTracker = null;

                if (getCount() <= 1) {
                    return super.dispatchTouchEvent(ev);
                }

                float dx = ev.getX() - startEvent.getX();
                float dy = ev.getY() - startEvent.getY();

                if (Math.abs(dx) < Math.abs(dy)) {
                    //竖向滑动
                    return super.dispatchTouchEvent(ev);
                }

                if (Math.abs(dx) <= 20) {
                    //点击事件以及长按事件
                    return super.dispatchTouchEvent(ev);
                }

                if (dx > scrollDistance || (dx > 0 && Math.abs(velocityX) > mMinimumFlingVelocity)) {
                    scrollToItem(curPos, curPos - 1);
                } else if (dx < -scrollDistance || (dx < 0 && Math.abs(velocityX) > mMinimumFlingVelocity)) {
                    scrollToItem(curPos, curPos + 1);
                } else {
                    scrollToItem(curPos);
                }
                return true;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:
                return super.onTouchEvent(e);
        }
        return true;
    }

    public void scrollToItem(int oldPos, int newPos) {
        if (oldPos == 0) {
            oldPos = getCount() - 1;
        } else if (oldPos == getCount() + 1) {
            oldPos = 1;
        } else {
            oldPos--;
        }
        boolean isSmoothScroll = true;
        if (newPos == 0) {
            newPos = getCount();
            isSmoothScroll = false;
        } else if (newPos == getCount() + 1) {
            newPos = 1;
            isSmoothScroll = false;
        }
        curPos = newPos;
        int width = getWidth();
        if (isSmoothScroll) {
            smoothScrollTo(curPos * width, 0);
        } else {
            scrollTo(curPos * width, 0);
        }
        if (onPageChangedListeners != null) {
            for (OnPageChangedListener listener : onPageChangedListeners) {
                listener.OnPageChanged(oldPos, curPos - 1);
            }
        }
    }

    public void scrollToItem(int pos) {
        scrollToItem(curPos, pos);
    }

    public void startLoop(long time) {
        removeCallbacks(loopRunnable);
        loopTimeInterval = time;
        if (loopTimeInterval > 0) {
            postDelayed(loopRunnable, loopTimeInterval);
        }
    }

    public void stopLoop() {
        if (loopTimeInterval > 0) {
            removeCallbacks(loopRunnable);
        }
    }

    private Runnable loopRunnable = new Runnable() {
        @Override
        public void run() {
            scrollToItem(curPos + 1);
            if (loopTimeInterval > 0) {
                postDelayed(this, loopTimeInterval);
            }
        }
    };

    public void addOnPageChangedListener(OnPageChangedListener listener) {
        if (listener != null) {
            onPageChangedListeners.add(listener);
        }
    }

    public void onResume() {
        startLoop(loopTimeInterval);
    }

    public void onPause() {
        stopLoop();
    }

    public void setOnBannerClickListener(OnBannerClickListener onBannerClickListener) {
        this.onBannerClickListener = onBannerClickListener;
    }
}
