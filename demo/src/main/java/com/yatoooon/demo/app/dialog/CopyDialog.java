package com.yatoooon.demo.app.dialog;

import android.content.Context;
import android.view.Gravity;

import com.yatoooon.baselibrary.base.BaseDialog;
import com.yatoooon.demo.R;

/**
 * 可进行拷贝的副本
 */
public final class CopyDialog {

    public static final class Builder
            extends BaseDialog.Builder<Builder> {

        public Builder(Context context) {
            super(context);

            setContentView(R.layout.copy_dialog);
            setAnimStyle(BaseDialog.ANIM_BOTTOM);
            setGravity(Gravity.BOTTOM);
        }
    }
}