package com.yatoooon.demo.app.popup;

import android.content.Context;

import com.yatoooon.baselibrary.base.BasePopupWindow;
import com.yatoooon.demo.R;


/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject
 *    time   : 2019/10/18
 *    desc   : 可进行拷贝的副本
 */
public final class CopyPopup {

    public static final class Builder
            extends BasePopupWindow.Builder<Builder> {

        public Builder(Context context) {
            super(context);

            setContentView(R.layout.popup_copy);
        }
    }
}