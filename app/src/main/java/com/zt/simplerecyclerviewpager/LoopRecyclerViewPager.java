package com.zt.simplerecyclerviewpager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;

/**
 * Created by zhouteng on 2017/3/26.
 */

public class LoopRecyclerViewPager extends RecyclerView {

    private int currentPosition = 0;
    private int scrollDistance = 300;
    private int minLoopStartCount = 2; //设置至少几张开始支持循环滑动，默认2张以上支持循环滑动
    private MotionEvent startEvent;
    private VelocityTracker velocityTracker;
    private int mMaximumFlingVelocity;
    private int mMinimumFlingVelocity;
    private LoopRecyclerViewAdapter mViewPagerAdapter;
    private long loopTimeInterval = 0;
    private OnPageChangedListener onPageChangedListener;
    private int oldPosition = 0;

    public LoopRecyclerViewPager(Context context) {
        this(context, null);
    }

    public LoopRecyclerViewPager(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        final ViewConfiguration configuration = ViewConfiguration.get(context);
        mMaximumFlingVelocity = configuration.getScaledMaximumFlingVelocity();
        mMinimumFlingVelocity = configuration.getScaledMinimumFlingVelocity();
    }

    @Override
    public void setAdapter(Adapter adapter) {
        mViewPagerAdapter = ensureRecyclerViewPagerAdapter(adapter);
        super.setAdapter(mViewPagerAdapter);
        if (mViewPagerAdapter.isSupportLoop()) {
            scrollToItem(1);
        }
    }

    @SuppressWarnings("unchecked")
    @NonNull
    protected LoopRecyclerViewAdapter ensureRecyclerViewPagerAdapter(Adapter adapter) {
        return (adapter instanceof LoopRecyclerViewAdapter)
                ? (LoopRecyclerViewAdapter) adapter
                : new LoopRecyclerViewAdapter(this, adapter);

    }

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

                if (getItemCount() < minLoopStartCount) {
                    return super.dispatchTouchEvent(ev);
                }

                float dx = ev.getX() - startEvent.getX();
                float dy = ev.getY() - startEvent.getY();

                if (Math.abs(dx) < Math.abs(dy)) {
                    //竖向滑动
                    return super.dispatchTouchEvent(ev);
                }

                if (dx > scrollDistance || (dx > 0 && Math.abs(velocityX) > mMinimumFlingVelocity)) {
                    scrollToItem(currentPosition, currentPosition - 1);
                } else if (dx < -scrollDistance || (dx < 0 && Math.abs(velocityX) > mMinimumFlingVelocity)) {
                    scrollToItem(currentPosition, currentPosition + 1);
                } else {
                    smoothScrollToPosition(currentPosition);
                }
                return true;
        }
        return super.dispatchTouchEvent(ev);
    }

    private void scrollToItem(int oldPos, int newPos) {
        oldPosition = oldPos;
        if (oldPos == 0) {
            oldPos = getActualItemCount() - 1;
        } else if (oldPos == getItemCount() - 1) {
            oldPos = 1;
        } else {
            oldPos--;
        }

        currentPosition = newPos;
        boolean isSmoothScroll = true;
        if (currentPosition == 0) {
            currentPosition = getItemCount() - 2;
            isSmoothScroll = false;
        } else if (currentPosition == getItemCount() - 1) {
            currentPosition = 1;
            isSmoothScroll = false;
        }
        if (isSmoothScroll) {
            smoothScrollToPosition(currentPosition);
        } else {
            scrollToPosition(currentPosition);
        }
        if (onPageChangedListener != null) {
            onPageChangedListener.OnPageChanged(oldPos, currentPosition - 1);
        }
    }

    public void scrollToItem(int pos) {
        scrollToItem(currentPosition, pos);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:
                return super.onTouchEvent(e);
        }
        return true;
    }

    private int getActualItemCount() {
        return mViewPagerAdapter.getActualItemCount();
    }

    private int getItemCount() {
        return getAdapter() != null ? getAdapter().getItemCount() : 0;
    }

    public int getMinLoopStartCount() {
        return minLoopStartCount;
    }

    //在setAdapter之前调用
    public void setMinLoopStartCount(int minLoopStartCount) {
        this.minLoopStartCount = minLoopStartCount;
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
            scrollToItem(currentPosition + 1);
            if (loopTimeInterval > 0) {
                postDelayed(this, loopTimeInterval);
            }
        }
    };

    public void setOnPageChangedListener(OnPageChangedListener onPageChangedListener) {
        this.onPageChangedListener = onPageChangedListener;
    }

    public interface OnPageChangedListener {
        void OnPageChanged(int oldPosition, int newPosition);
    }

    public void onResume() {
        startLoop(loopTimeInterval);
    }

    public void onPause() {
        stopLoop();
    }
}
