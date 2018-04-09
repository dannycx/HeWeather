package com.dcx.style.citypickerview.widget.wheel;

/**
 * 滑轮改变监听接口
 */
public interface OnWheelChangedListener {
    /**
     * 当前项目更改时要调用的回调方法
     *
     * @param wheel    the wheel view whose state has changed
     * @param oldValue the old value of current item
     * @param newValue the new value of current item
     */
    void onChanged(WheelView wheel, int oldValue, int newValue);
}