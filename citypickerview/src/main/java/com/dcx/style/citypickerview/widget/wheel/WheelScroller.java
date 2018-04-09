package com.dcx.style.citypickerview.widget.wheel;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.widget.Scroller;

/**
 * Scroller类处理滚动事件并更新
 */
public class WheelScroller {
    /**
     * Minimum delta for scrolling
     */
    public static final int MIN_DELTA_FOR_SCROLLING = 1;
    /**
     * Scrolling duration
     */
    private static final int SCROLLING_DURATION = 400;
    // Messages
    private final int MESSAGE_SCROLL = 0;
    private final int MESSAGE_JUSTIFY = 1;
    // Listener
    private ScrollingListener listener;
    // Context
    private Context context;
    // Scrolling
    private GestureDetector gestureDetector;
    private Scroller scroller;
    private int lastScrollY;
    private float lastTouchedY;
    private boolean isScrollingPerformed;
    // animation handler
    private Handler animationHandler = new Handler() {
        public void handleMessage(Message msg) {
            scroller.computeScrollOffset();
            int currY = scroller.getCurrY();
            int delta = lastScrollY - currY;
            lastScrollY = currY;
            if (delta != 0) {
                listener.onScroll(delta);
            }

            // scrolling is not finished when it comes to final Y
            // so, finish it manually
            if (Math.abs(currY - scroller.getFinalY()) < MIN_DELTA_FOR_SCROLLING) {
                currY = scroller.getFinalY();
                scroller.forceFinished(true);
            }
            if (!scroller.isFinished()) {
                animationHandler.sendEmptyMessage(msg.what);
            } else if (msg.what == MESSAGE_SCROLL) {
                justify();
            } else {
                finishScrolling();
            }
        }
    };
    // 手势监听
    private SimpleOnGestureListener gestureListener = new SimpleOnGestureListener() {
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return true;
        }

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            lastScrollY = 0;
            final int maxY = 0x7FFFFFFF;
            final int minY = -maxY;
            scroller.fling(0, lastScrollY, 0, (int) -velocityY, 0, 0, minY, maxY);
            setNextMessage(MESSAGE_SCROLL);
            return true;
        }
    };

    public WheelScroller(Context context, ScrollingListener listener) {
        gestureDetector = new GestureDetector(context, gestureListener);
        gestureDetector.setIsLongpressEnabled(false);
        scroller = new Scroller(context);
        this.listener = listener;
        this.context = context;
    }

    // 滚动滑轮
    public void scroll(int distance, int time) {
        scroller.forceFinished(true);
        lastScrollY = 0;
        scroller.startScroll(0, 0, 0, distance, time != 0 ? time : SCROLLING_DURATION);
        setNextMessage(MESSAGE_SCROLL);
        startScrolling();
    }

    //停止滑动
    public void stopScrolling() {
        scroller.forceFinished(true);
    }

    //触摸事件处理
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastTouchedY = event.getY();
                scroller.forceFinished(true);
                clearMessages();
                break;
            case MotionEvent.ACTION_MOVE:
                // perform scrolling
                int distanceY = (int) (event.getY() - lastTouchedY);
                if (distanceY != 0) {
                    startScrolling();
                    listener.onScroll(distanceY);
                    lastTouchedY = event.getY();
                }
                break;
        }
        if (!gestureDetector.onTouchEvent(event) && event.getAction() == MotionEvent.ACTION_UP) {
            justify();
        }
        return true;
    }

    // 将下一条消息设置为队列之前清除队列。
    private void setNextMessage(int message) {
        clearMessages();
        animationHandler.sendEmptyMessage(message);
    }

    // 从队列中清除消息
    private void clearMessages() {
        animationHandler.removeMessages(MESSAGE_SCROLL);
        animationHandler.removeMessages(MESSAGE_JUSTIFY);
    }

    /**
     * Justifies wheel
     */
    private void justify() {
        listener.onJustify();
        setNextMessage(MESSAGE_JUSTIFY);
    }

    // 开始滑动
    private void startScrolling() {
        if (!isScrollingPerformed) {
            isScrollingPerformed = true;
            listener.onStarted();
        }
    }

    // 完成滑动
    void finishScrolling() {
        if (isScrollingPerformed) {
            listener.onFinished();
            isScrollingPerformed = false;
        }
    }

    // 滑动监听接口
    public interface ScrollingListener {
        // 执行滚动时调用的滚动回调
        void onScroll(int distance);

        // 滚动开始时调用回调
        void onStarted();

        // 调整后调用完成回调
        void onFinished();

        // 当滚动结束时调用回调来验证视图
        void onJustify();
    }
}
