package com.dcx.style.citypickerview.widget.wheel;

import android.view.View;
import android.widget.LinearLayout;

import java.util.LinkedList;
import java.util.List;

/**
 * 回收存储轮子项目以重用
 */
public class WheelRecycle {
    // Cached items
    private List<View> items;

    // Cached empty items
    private List<View> emptyItems;

    // Wheel view
    private WheelView wheel;

    public WheelRecycle(WheelView wheel) {
        this.wheel = wheel;
    }

    /**
     * 从指定的布局回收项目。只保存未包含在指定范围内的项目。所有缓存的项目都从原始布局中删除。
     *
     * @param layout    包含要缓存的项目的布局
     * @param firstItem 布局中第一项的数量
     * @param range     当前滑动的范围
     * @return 第一个项目编号的新值
     */
    public int recycleItems(LinearLayout layout, int firstItem, ItemsRange range) {
        int index = firstItem;
        for (int i = 0; i < layout.getChildCount(); ) {
            if (!range.contains(index)) {
                recycleView(layout.getChildAt(i), index);
                layout.removeViewAt(i);
                if (i == 0) { // first item
                    firstItem++;
                }
            } else {
                i++; // go to next item
            }
            index++;
        }
        return firstItem;
    }

    /**
     * 获取条目视图
     *
     * @return the cached view
     */
    public View getItem() {
        return getCachedView(items);
    }

    /**
     * 获取空的条目视图
     *
     * @return the cached empty view
     */
    public View getEmptyItem() {
        return getCachedView(emptyItems);
    }

    /**
     * 清空所有视图
     */
    public void clearAll() {
        if (items != null) {
            items.clear();
        }
        if (emptyItems != null) {
            emptyItems.clear();
        }
    }

    /**
     * 将视图添加到指定的缓存。 如果为空，则创建一个缓存列表
     *
     * @param view  the view to be cached
     * @param cache the cache list
     * @return the cache list
     */
    private List<View> addView(View view, List<View> cache) {
        if (cache == null) {
            cache = new LinkedList<>();
        }

        cache.add(view);
        return cache;
    }

    /**
     * 将视图添加到缓存。 通过索引确定视图类型（项目视图或空白视图）。
     *
     * @param view  the view to be cached
     * @param index the index of view
     */
    private void recycleView(View view, int index) {
        int count = wheel.getViewAdapter().getItemsCount();

        if ((index < 0 || index >= count) && !wheel.isCyclic()) {
            // empty view
            emptyItems = addView(view, emptyItems);
        } else {
            while (index < 0) {
                index = count + index;
            }
            index %= count;
            items = addView(view, items);
        }
    }

    /**
     * 从指定的缓存中获取视图
     *
     * @param cache the cache
     * @return the first view from cache.
     */
    private View getCachedView(List<View> cache) {
        if (cache != null && cache.size() > 0) {
            View view = cache.get(0);
            cache.remove(0);
            return view;
        }
        return null;
    }
}
