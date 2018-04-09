/*
 *  Android Wheel Control.
 *  https://code.google.com/p/android-wheel/
 * 
 *  Copyright 2011 Yuri Kanivets
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.dcx.style.citypickerview.widget.wheel;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import com.dcx.style.citypickerview.widget.wheel.adapters.WheelViewAdapter;
import com.lljjcoder.style.citypickerview.R;

import java.util.LinkedList;
import java.util.List;

/**
 * 滑动轮视图
 */
public class WheelView extends View {
    // Top and bottom items offset (to hide that)
    private static final int ITEM_OFFSET_PERCENT = 0;
    // 滚轮左右间隔距离
    private static final int PADDING = 5;
    // 滚轮显示的item个数
    private static final int DEF_VISIBLE_ITEMS = 3;
    // 滚轮是否循环滚动
    boolean isCyclic = false;
    // 滚轮从上到下背景逐渐变淡，到中间，逆反改变
    private int[] SHADOWS_COLORS = new int[]{0xefE9E9E9, 0xcfE9E9E9, 0x3fE9E9E9};
    // 当前item
    private int currentItem = 0;
    // 可见条目
    private int visibleItems = DEF_VISIBLE_ITEMS;
    // 条目高度
    private int itemHeight = 0;
    // 中间线
    private Drawable centerDrawable;
    // 轮绘图
    private int wheelBackground = R.drawable.wheel_bg;
    private int wheelForeground = R.drawable.wheel_val;
    // 阴影绘制
    private GradientDrawable topShadow;
    private GradientDrawable bottomShadow;
    private boolean drawShadows = true;
    // 滚动、正在滚动、滚动偏移量
    private WheelScroller scroller;
    private boolean isScrollingPerformed;
    private int scrollingOffset;
    // 条目布局
    private LinearLayout itemsLayout;
    // 第一项数量
    private int firstItem;
    // 视图适配器
    private WheelViewAdapter viewAdapter;
    // 回收
    private WheelRecycle recycle = new WheelRecycle(this);
    // 监听
    private List<OnWheelChangedListener> changingListeners = new LinkedList<>();
    private List<OnWheelScrollListener> scrollingListeners = new LinkedList<>();
    // 滑动监听
    WheelScroller.ScrollingListener scrollingListener = new WheelScroller.ScrollingListener() {
        @Override
        public void onStarted() {
            isScrollingPerformed = true;
            notifyScrollingListenersAboutStart();
        }

        @Override
        public void onScroll(int distance) {
            doScroll(distance);
            int height = getHeight();
            if (scrollingOffset > height) {
                scrollingOffset = height;
                scroller.stopScrolling();
            } else if (scrollingOffset < -height) {
                scrollingOffset = -height;
                scroller.stopScrolling();
            }
        }

        @Override
        public void onFinished() {
            if (isScrollingPerformed) {
                notifyScrollingListenersAboutEnd();
                isScrollingPerformed = false;
            }
            scrollingOffset = 0;
            invalidate();
        }

        @Override
        public void onJustify() {
            if (Math.abs(scrollingOffset) > WheelScroller.MIN_DELTA_FOR_SCROLLING) {
                scroller.scroll(scrollingOffset, 0);
            }
        }
    };
    // 适配监听
    private DataSetObserver dataObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            invalidateWheel(false);
        }

        @Override
        public void onInvalidated() {
            invalidateWheel(true);
        }
    };

    //构造器
    public WheelView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initData(context);
    }

    public WheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initData(context);
    }

    public WheelView(Context context) {
        super(context);
        initData(context);
    }

    // 初始化数据
    private void initData(Context context) {
        scroller = new WheelScroller(context, scrollingListener);
    }

    //获取可视条目
    public int getVisibleItems() {
        return visibleItems;
    }

    // 设置可见条目数量
    public void setVisibleItems(int count) {
        visibleItems = count;
    }

    // 获取视图适配器
    public WheelViewAdapter getViewAdapter() {
        return viewAdapter;
    }

    // 设置视图适配器。 通常新的适配器包含不同的视图，所以它需要通过调用measure（）来重建视图。
    public void setViewAdapter(WheelViewAdapter viewAdapter) {
        if (this.viewAdapter != null) {
            this.viewAdapter.unregisterDataSetObserver(dataObserver);
        }
        this.viewAdapter = viewAdapter;
        if (this.viewAdapter != null) {
            this.viewAdapter.registerDataSetObserver(dataObserver);
        }
        invalidateWheel(true);
    }

    // 添加滑轮滚动监听器
    public void addChangingListener(OnWheelChangedListener listener) {
        changingListeners.add(listener);
    }

    // 通知更改的监听器
    protected void notifyChangingListeners(int oldValue, int newValue) {
        for (OnWheelChangedListener listener : changingListeners) {
            listener.onChanged(this, oldValue, newValue);
        }
    }

    // 通知监听器开始滚动
    protected void notifyScrollingListenersAboutStart() {
        for (OnWheelScrollListener listener : scrollingListeners) {
            listener.onScrollingStarted(this);
        }
    }

    // 通知监听器结束滚动
    protected void notifyScrollingListenersAboutEnd() {
        for (OnWheelScrollListener listener : scrollingListeners) {
            listener.onScrollingFinished(this);
        }
    }

    //获取当前条目
    public int getCurrentItem() {
        return currentItem;
    }

    /**
     * Sets the current item w/o animation. Does nothing when index is wrong.
     *
     * @param index the item index
     */
    public void setCurrentItem(int index) {
        setCurrentItem(index, false);
    }

    // 设置当前项目。 索引错误时什么也不做。
    public void setCurrentItem(int index, boolean animated) {
        if (viewAdapter == null || viewAdapter.getItemsCount() == 0) {
            return; // throw?
        }
        int itemCount = viewAdapter.getItemsCount();
        if (index < 0 || index >= itemCount) {
            if (isCyclic) {
                while (index < 0) {
                    index += itemCount;
                }
                index %= itemCount;
            } else {
                return; // throw?
            }
        }
        if (index != currentItem) {
            if (animated) {
                int itemsToScroll = index - currentItem;
                if (isCyclic) {
                    int scroll = itemCount + Math.min(index, currentItem) - Math.max(index, currentItem);
                    if (scroll < Math.abs(itemsToScroll)) {
                        itemsToScroll = itemsToScroll < 0 ? scroll : -scroll;
                    }
                }
                scroll(itemsToScroll, 0);
            } else {
                scrollingOffset = 0;
                int old = currentItem;
                currentItem = index;
                notifyChangingListeners(old, currentItem);
                invalidate();
            }
        }
    }

    /**
     * Tests if wheel is cyclic. That means before the 1st item there is shown the last one
     *
     * @return true if wheel is cyclic
     */
    public boolean isCyclic() {
        return isCyclic;
    }

    // 确定是否绘制阴影
    public boolean drawShadows() {
        return drawShadows;
    }

    // 设置是否绘制阴影
    public void setDrawShadows(boolean drawShadows) {
        this.drawShadows = drawShadows;
    }

    // 设置阴影渐变颜色
    public void setShadowColor(int start, int middle, int end) {
        SHADOWS_COLORS = new int[]{start, middle, end};
    }

    // 设置车轮背景的绘图
    public void setWheelBackground(int resource) {
        wheelBackground = resource;
        setBackgroundResource(wheelBackground);
    }

    // 设置车轮前景的绘图
    public void setWheelForeground(int resource) {
        wheelForeground = resource;
        centerDrawable = getContext().getResources().getDrawable(wheelForeground);
    }

    // 无效轮子
    public void invalidateWheel(boolean clearCaches) {
        if (clearCaches) {
            recycle.clearAll();
            if (itemsLayout != null) {
                itemsLayout.removeAllViews();
            }
            scrollingOffset = 0;
        } else if (itemsLayout != null) {
            // cache all items
            recycle.recycleItems(itemsLayout, firstItem, new ItemsRange());
        }
        invalidate();
    }

    // 无效资源
    private void initResourcesIfNecessary() {
        if (centerDrawable == null) {
            centerDrawable = getContext().getResources().getDrawable(wheelForeground);
        }
        if (topShadow == null) {
            topShadow = new GradientDrawable(Orientation.TOP_BOTTOM, SHADOWS_COLORS);
        }
        if (bottomShadow == null) {
            bottomShadow = new GradientDrawable(Orientation.BOTTOM_TOP, SHADOWS_COLORS);
        }
        setBackgroundResource(wheelBackground);
    }

    /**
     * Calculates desired height for layout
     *
     * @param layout the source layout
     * @return the desired layout height
     */
    private int getDesiredHeight(LinearLayout layout) {
        if (layout != null && layout.getChildAt(0) != null) {
            itemHeight = layout.getChildAt(0).getMeasuredHeight();
        }
        int desired = itemHeight * visibleItems - itemHeight * ITEM_OFFSET_PERCENT / 50;
        return Math.max(desired, getSuggestedMinimumHeight());
    }

    // 返回条目高度
    private int getItemHeight() {
        if (itemHeight != 0) {
            return itemHeight;
        }
        if (itemsLayout != null && itemsLayout.getChildAt(0) != null) {
            itemHeight = itemsLayout.getChildAt(0).getHeight();
            return itemHeight;
        }
        return getHeight() / visibleItems;
    }

    /**
     * Calculates control width and creates text layouts
     *
     * @param widthSize the input layout width
     * @param mode      the layout mode
     * @return the calculated control width
     */
    private int calculateLayoutWidth(int widthSize, int mode) {
        initResourcesIfNecessary();

        // TODO: make it static
        itemsLayout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        itemsLayout.measure(MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        int width = itemsLayout.getMeasuredWidth();
        if (mode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            width += 2 * PADDING;
            // Check against our minimum width
            width = Math.max(width, getSuggestedMinimumWidth());
            if (mode == MeasureSpec.AT_MOST && widthSize < width) {
                width = widthSize;
            }
        }
        itemsLayout.measure(MeasureSpec.makeMeasureSpec(width - 2 * PADDING, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        return width;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        buildViewForMeasuring();
        int width = calculateLayoutWidth(widthSize, widthMode);
        int height;
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = getDesiredHeight(itemsLayout);

            if (heightMode == MeasureSpec.AT_MOST) {
                height = Math.min(height, heightSize);
            }
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        layout(r - l, b - t);
    }

    // 设置布局的宽度和高度
    private void layout(int width, int height) {
        int itemsWidth = width - 2 * PADDING;
        itemsLayout.layout(0, 0, itemsWidth, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (viewAdapter != null && viewAdapter.getItemsCount() > 0) {
            updateView();
            drawItems(canvas);
            drawCenterRect(canvas);
        }
        if (drawShadows)
            drawShadows(canvas);
    }

    // 在控件的顶部和底部绘制阴影
    private void drawShadows(Canvas canvas) {
        //从中间到顶部渐变处理
        int count = getVisibleItems() == 2 ? 1 : getVisibleItems() / 2;
        int height = (int) (count * getItemHeight());

        topShadow.setBounds(0, 0, getWidth(), height);
        topShadow.draw(canvas);

        bottomShadow.setBounds(0, getHeight() - height, getWidth(), getHeight());
        bottomShadow.draw(canvas);

        Log.d("liji.wheel", "getItemHeight(): " + getItemHeight());
        Log.d("liji.wheel", "height: " + height);
        Log.d("liji.wheel", "getWidth: " + getWidth());
        Log.d("liji.wheel", "getHeight():" + getHeight());
        Log.d("liji.wheel", "visibleItems:" + visibleItems);
    }

    /**
     * Draws items
     *
     * @param canvas the canvas for drawing
     */
    private void drawItems(Canvas canvas) {
        canvas.save();
        int top = (currentItem - firstItem) * getItemHeight() + (getItemHeight() - getHeight()) / 2;
        canvas.translate(PADDING, -top + scrollingOffset);
        itemsLayout.draw(canvas);
        canvas.restore();
    }

    //对话框相关参数
    private void drawCenterRect(Canvas canvas) {
        int center = getHeight() / 2;
        int offset = (int) (getItemHeight() / 2 * 1.2);
        Paint paint = new Paint();
        paint.setColor(getResources().getColor(R.color.province_line_border));
//        paint.setColor(getResources().getColor(R.color.colorPrimary));
        // 设置线宽
        paint.setStrokeWidth((float) 5);
        // 绘制上边直线
        canvas.drawLine(0, center - offset, getWidth(), center - offset, paint);
        // 绘制下边直线
        canvas.drawLine(0, center + offset, getWidth(), center + offset, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled() || getViewAdapter() == null) {
            return true;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if (getParent() != null) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;

            case MotionEvent.ACTION_UP:
                if (!isScrollingPerformed) {
                    int distance = (int) event.getY() - getHeight() / 2;
                    if (distance > 0) {
                        distance += getItemHeight() / 2;
                    } else {
                        distance -= getItemHeight() / 2;
                    }
                    int items = distance / getItemHeight();
                    if (items != 0 && isValidItemIndex(currentItem + items)) {
                    }
                }
                break;
        }
        return scroller.onTouchEvent(event);
    }

    /**
     * Scrolls the wheel
     *
     * @param delta the scrolling value
     */
    private void doScroll(int delta) {
        scrollingOffset += delta;
        int itemHeight = getItemHeight();
        int count = scrollingOffset / itemHeight;

        int pos = currentItem - count;
        int itemCount = viewAdapter.getItemsCount();

        int fixPos = scrollingOffset % itemHeight;
        if (Math.abs(fixPos) <= itemHeight / 2) {
            fixPos = 0;
        }
        if (isCyclic && itemCount > 0) {
            if (fixPos > 0) {
                pos--;
                count++;
            } else if (fixPos < 0) {
                pos++;
                count--;
            }
            // fix position by rotating
            while (pos < 0) {
                pos += itemCount;
            }
            pos %= itemCount;
        } else {
            //
            if (pos < 0) {
                count = currentItem;
                pos = 0;
            } else if (pos >= itemCount) {
                count = currentItem - itemCount + 1;
                pos = itemCount - 1;
            } else if (pos > 0 && fixPos > 0) {
                pos--;
                count++;
            } else if (pos < itemCount - 1 && fixPos < 0) {
                pos++;
                count--;
            }
        }

        int offset = scrollingOffset;
        if (pos != currentItem) {
            setCurrentItem(pos, false);
        } else {
            invalidate();
        }
        // update offset
        scrollingOffset = offset - count * itemHeight;
        if (scrollingOffset > getHeight()) {
            scrollingOffset = scrollingOffset % getHeight() + getHeight();
        }
    }

    /**
     * Scroll the wheel
     *
     * @param time scrolling duration
     */
    public void scroll(int itemsToScroll, int time) {
        int distance = itemsToScroll * getItemHeight() - scrollingOffset;
        scroller.scroll(distance, time);
    }

    /**
     * Calculates range for wheel items
     *
     * @return the items range
     */
    private ItemsRange getItemsRange() {
        if (getItemHeight() == 0) {
            return null;
        }
        int first = currentItem;
        int count = 1;
        while (count * getItemHeight() < getHeight()) {
            first--;
            count += 2; // top + bottom items
        }
        if (scrollingOffset != 0) {
            if (scrollingOffset > 0) {
                first--;
            }
            count++;
            // process empty items above the first or below the second
            int emptyItems = scrollingOffset / getItemHeight();
            first -= emptyItems;
            count += Math.asin(emptyItems);
        }
        return new ItemsRange(first, count);
    }

    /**
     * Rebuilds wheel items if necessary. Caches all unused items.
     *
     * @return true if items are rebuilt
     */
    private boolean rebuildItems() {
        boolean updated = false;
        ItemsRange range = getItemsRange();
        if (itemsLayout != null) {
            int first = recycle.recycleItems(itemsLayout, firstItem, range);
            updated = firstItem != first;
            firstItem = first;
        } else {
            createItemsLayout();
            updated = true;
        }

        if (!updated) {
            updated = firstItem != range.getFirst() || itemsLayout.getChildCount() != range.getCount();
        }

        if (firstItem > range.getFirst() && firstItem <= range.getLast()) {
            for (int i = firstItem - 1; i >= range.getFirst(); i--) {
                if (!addViewItem(i, true)) {
                    break;
                }
                firstItem = i;
            }
        } else {
            firstItem = range.getFirst();
        }

        int first = firstItem;
        for (int i = itemsLayout.getChildCount(); i < range.getCount(); i++) {
            if (!addViewItem(firstItem + i, false) && itemsLayout.getChildCount() == 0) {
                first++;
            }
        }
        firstItem = first;
        return updated;
    }

    /**
     * Updates view. Rebuilds items and label if necessary, recalculate items sizes.
     */
    private void updateView() {
        if (rebuildItems()) {
            calculateLayoutWidth(getWidth(), MeasureSpec.EXACTLY);
            layout(getWidth(), getHeight());
        }
    }

    /**
     * Creates item layouts if necessary
     */
    private void createItemsLayout() {
        if (itemsLayout == null) {
            itemsLayout = new LinearLayout(getContext());
            itemsLayout.setOrientation(LinearLayout.VERTICAL);
        }
    }

    /**
     * Builds view for measuring
     */
    private void buildViewForMeasuring() {
        // clear all items
        if (itemsLayout != null) {
            recycle.recycleItems(itemsLayout, firstItem, new ItemsRange());
        } else {
            createItemsLayout();
        }

        // add views
        int addItems = visibleItems / 2;
        for (int i = currentItem + addItems; i >= currentItem - addItems; i--) {
            if (addViewItem(i, true)) {
                firstItem = i;
            }
        }
    }

    /**
     * Adds view for item to items layout
     *
     * @param index the item index
     * @param first the flag indicates if view should be first
     * @return true if corresponding item exists and is added
     */
    private boolean addViewItem(int index, boolean first) {
        View view = getItemView(index);
        if (view != null) {
            if (first) {
                itemsLayout.addView(view, 0);
            } else {
                itemsLayout.addView(view);
            }
            return true;
        }
        return false;
    }

    /**
     * Checks whether intem index is valid
     *
     * @param index the item index
     * @return true if item index is not out of bounds or the wheel is cyclic
     */
    private boolean isValidItemIndex(int index) {
        return viewAdapter != null && viewAdapter.getItemsCount() > 0
                && (isCyclic || index >= 0 && index < viewAdapter.getItemsCount());
    }

    /**
     * Returns view for specified item
     *
     * @param index the item index
     * @return item view or empty view if index is out of bounds
     */
    private View getItemView(int index) {
        if (viewAdapter == null || viewAdapter.getItemsCount() == 0) {
            return null;
        }
        int count = viewAdapter.getItemsCount();
        if (!isValidItemIndex(index)) {
            return viewAdapter.getEmptyItem(recycle.getEmptyItem(), itemsLayout);
        } else {
            while (index < 0) {
                index = count + index;
            }
        }
        index %= count;
        return viewAdapter.getItem(index, recycle.getItem(), itemsLayout);
    }
}
