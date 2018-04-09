package com.dcx.style.citypickerview.widget.wheel.adapters;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;

/**
 * Wheel items adapter interface
 */
public interface WheelViewAdapter {
    /**
     * 获取条目数量
     *
     * @return the count of wheel items
     */
    int getItemsCount();

    /**
     * 获取一个视图，显示数据集中指定位置的数据
     *
     * @param index       the item index
     * @param convertView the old view to reuse if possible
     * @param parent      the parent that this view will eventually be attached to
     * @return the wheel item View
     */
    View getItem(int index, View convertView, ViewGroup parent);

    /**
     * 获取显示在第一个或之后放置的空转轮项目的视图最后一个车轮项目。
     *
     * @param convertView the old view to reuse if possible
     * @param parent      the parent that this view will eventually be attached to
     * @return the empty item View
     */
    View getEmptyItem(View convertView, ViewGroup parent);

    /**
     * 注册一个在此适配器使用的数据发生更改时调用的观察者
     *
     * @param observer the observer to be registered
     */
    void registerDataSetObserver(DataSetObserver observer);

    /**
     * 取消注册先前已注册的观察员
     *
     * @param observer the observer to be unregistered
     */
    void unregisterDataSetObserver(DataSetObserver observer);
}
