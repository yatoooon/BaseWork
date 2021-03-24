package com.yatoooon.demo.app.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gyf.immersionbar.ImmersionBar;
import com.hjq.bar.TitleBar;
import com.yatoooon.baselibrary.base.BaseFragment;
import com.yatoooon.baselibrary.mvp.IPresenter;
import com.yatoooon.demo.app.action.TitleBarAction;
import com.yatoooon.demo.app.action.ToastAction;

/**
 * 项目中 Fragment 懒加载基类
 */
public abstract class AppFragment<P extends IPresenter> extends BaseFragment<P>
        implements ToastAction, TitleBarAction {

    /**
     * 标题栏对象
     */
    private TitleBar mTitleBar;
    /**
     * 状态栏沉浸
     */
    private ImmersionBar mImmersionBar;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 设置标题栏沉浸
        if (isStatusBarEnabled() && getTitleBar() != null) {
            ImmersionBar.setTitleBar(this, getTitleBar());
        }

        if (getTitleBar() != null) {
            getTitleBar().setOnTitleBarListener(this);
        }

        // 初始化沉浸式状态栏
        if (isStatusBarEnabled()) {
            getStatusBarConfig().init();
        }
    }


    /**
     * 是否在 Fragment 使用沉浸式
     */
    public boolean isStatusBarEnabled() {
        return false;
    }

    /**
     * 获取状态栏沉浸的配置对象
     */
    @NonNull
    protected ImmersionBar getStatusBarConfig() {
        if (mImmersionBar == null) {
            mImmersionBar = createStatusBarConfig();
        }
        return mImmersionBar;
    }

    /**
     * 初始化沉浸式
     */
    @NonNull
    protected ImmersionBar createStatusBarConfig() {
        return ImmersionBar.with(this)
                // 默认状态栏字体颜色为黑色
                .statusBarDarkFont(isStatusBarDarkFont())
                // 指定导航栏背景颜色
                .navigationBarColor(android.R.color.white)
                // 状态栏字体和导航栏内容自动变色，必须指定状态栏颜色和导航栏颜色才可以自动变色
                .autoDarkModeEnable(true, 0.2f);
    }

    /**
     * 获取状态栏字体颜色
     */
    protected boolean isStatusBarDarkFont() {
        Activity activity = getAttachActivity();
        if (activity instanceof AppActivity) {
            return ((AppActivity) activity).isStatusBarDarkFont();
        } else {
            return false;
        }
    }

    @Override
    @Nullable
    public TitleBar getTitleBar() {
        if (mTitleBar == null) {
            mTitleBar = obtainTitleBar((ViewGroup) getView());
        }
        return mTitleBar;
    }

    /**
     * 当前加载对话框是否在显示中
     */
    public boolean isShowDialog() {
        Activity activity = getAttachActivity();
        if (activity instanceof AppActivity) {
            return ((AppActivity) activity).isShowDialog();
        } else {
            return false;
        }
    }

    /**
     * 显示加载对话框
     */
    public void showDialog() {
        Activity activity = getAttachActivity();
        if (activity instanceof AppActivity) {
            ((AppActivity) activity).showDialog();
        }
    }

    /**
     * 隐藏加载对话框
     */
    public void hideDialog() {
        Activity activity = getAttachActivity();
        if (activity instanceof AppActivity) {
            ((AppActivity) activity).hideDialog();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isStatusBarEnabled()) {
            // 重新初始化状态栏
            getStatusBarConfig().init();
        }
    }
}