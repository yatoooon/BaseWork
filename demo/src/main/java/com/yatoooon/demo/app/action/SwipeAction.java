package com.yatoooon.demo.app.action;

/**
 *  侧滑意图
 */
public interface SwipeAction {

    /**
     * 是否使用侧滑
     */
    default boolean isSwipeEnable() {
        // 默认开启
        return false;
    }
}