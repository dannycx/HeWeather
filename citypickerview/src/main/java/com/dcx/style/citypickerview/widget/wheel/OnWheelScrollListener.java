/*
 *  Copyright 2010 Yuri Kanivets
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

/**
 * 滚动监听器接口
 */
public interface OnWheelScrollListener {
    /**
     * 滚动开始时要调用的回调方法。
     *
     * @param wheel 状态改变的车轮视图
     */
    void onScrollingStarted(WheelView wheel);

    /**
     * 滚动结束时要调用的回调方法
     *
     * @param wheel 状态改变的车轮视图
     */
    void onScrollingFinished(WheelView wheel);
}
