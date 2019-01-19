package com.zt.simplebanner;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouteng on 2017/3/26.
 * <p>
 * 支持循环滑动的recyclerviev，实现原理是，在其adapter中，实际数据源的头尾添加2个数据，形成新的数据源，新的数据源的头部数据等于实际数据源的尾部数据，
 * 新数据的尾部数据，等于实际数据源的头部数据
 * <p>
 * 重写触摸事件，实现最后一张和第一张的切换
 */

public class LoopRecyclerViewPager extends RecyclerView {

    private static final String TAG = "LoopRecyclerViewPager";

    private static int scrollDistance = 300;

    private int currentPosition = 0;
    private int minLoopStartCount = 2; //设置至少几张开始支持循环滑动，默认2张以及以上支持循环滑动
    private MotionEvent startEvent;
    private VelocityTracker velocityTracker;
    private int mMaximumFlingVelocity;
    private int mMinimumFlingVelocity;
    private LoopRecyclerViewAdapter mViewPagerAdapter;
    private long loopTimeInterval = 0;
    private int oldPosition = 0;
    private boolean isLooping = false; //是否自动轮播

    private boolean isLoopingWhenTouchDown = false; //手动滑动滑动banner时，首先暂停自动轮播，该变量用于记住手指触摸banner时，是否正在轮播

    private List<OnPageChangedListener> onPageChangedListeners = new ArrayList<>();


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
                isLoopingWhenTouchDown = isLooping;
                Log.d(TAG, "ACTION_DOWN isLooping=" + isLooping);
                if (isLooping) {
                    stopLoop();
                }
                startEvent = MotionEvent.obtain(ev);
                if (velocityTracker == null) {
                    velocityTracker = VelocityTracker.obtain();
                } else {
                    velocityTracker.clear();
                }
                velocityTracker.addMovement(ev);
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "ACTION_MOVE");
                velocityTracker.addMovement(ev);
                break;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "ACTION_UP isLoopingWhenTouchDown=" + isLoopingWhenTouchDown);
                if (isLoopingWhenTouchDown) {
                    startLoop(loopTimeInterval);
                }
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

                if (Math.abs(dx) <= 20) {
                    //点击事件以及长按事件
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

    public void scrollToItem(int oldPos, int newPos) {
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
        if (onPageChangedListeners != null) {
            for (OnPageChangedListener listener : onPageChangedListeners) {
                listener.OnPageChanged(oldPos, currentPosition - 1);
            }
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

    /**
     * 前后加一张之后的数量
     *
     * @return
     */
    public int getItemCount() {
        return getAdapter() != null ? getAdapter().getItemCount() : 0;
    }

    /**
     * 实际看到的张数
     *
     * @return
     */
    public int getActualItemCount() {
        return mViewPagerAdapter.getActualItemCount();
    }

    /**
     * 获取当前
     *
     * @return
     */
    public int getCurrentPosition() {
        return currentPosition;
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
            isLooping = true;
            postDelayed(loopRunnable, loopTimeInterval);
        }
    }

    public void stopLoop() {
        if (loopTimeInterval > 0) {
            isLooping = false;
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

    public void addOnPageChangedListener(OnPageChangedListener onPageChangedListener) {
        if (onPageChangedListener != null) {
            onPageChangedListeners.add(onPageChangedListener);
        }
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
