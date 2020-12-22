package com.yatoooon.demo.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;
import com.rd.PageIndicatorView;
import com.yatoooon.demo.R;
import com.yatoooon.demo.app.aop.CheckNet;
import com.yatoooon.demo.app.aop.DebugLog;
import com.yatoooon.demo.app.common.MyActivity;
import com.yatoooon.demo.app.other.IntentKey;
import com.yatoooon.demo.app.pager.ImagePagerAdapter;

import java.util.ArrayList;

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject
 *    time   : 2019/03/05
 *    desc   : 查看大图
 */
public final class ImagePreviewActivity extends MyActivity {

    public static void start(Context context, String url) {
        ArrayList<String> images = new ArrayList<>(1);
        images.add(url);
        start(context, images);
    }

    public static void start(Context context, ArrayList<String> urls) {
        start(context, urls, 0);
    }

    @CheckNet
    @DebugLog
    public static void start(Context context, ArrayList<String> urls, int index) {
        Intent intent = new Intent(context, ImagePreviewActivity.class);
        intent.putExtra(IntentKey.IMAGE, urls);
        intent.putExtra(IntentKey.INDEX, index);
        context.startActivity(intent);
    }

    private ViewPager mViewPager;
    private PageIndicatorView mIndicatorView;

    @Override
    public int getLayoutId() {
        return R.layout.image_preview_activity;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mViewPager = findViewById(R.id.vp_image_preview_pager);
        mIndicatorView = findViewById(R.id.pv_image_preview_indicator);
        mIndicatorView.setViewPager(mViewPager);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        ArrayList<String> images = getStringArrayList(IntentKey.IMAGE);
        int index = getInt(IntentKey.INDEX);
        if (images != null && images.size() > 0) {
            mViewPager.setAdapter(new ImagePagerAdapter(this, images));
            if (index != 0 && index <= images.size()) {
                mViewPager.setCurrentItem(index);
            }
        } else {
            finish();
        }
    }

    @NonNull
    @Override
    protected ImmersionBar createStatusBarConfig() {
        return super.createStatusBarConfig()
                // 隐藏状态栏和导航栏
                .hideBar(BarHide.FLAG_HIDE_BAR);
    }

    @Override
    public boolean isStatusBarDarkFont() {
        return false;
    }

    @Override
    public boolean isSwipeEnable() {
        return false;
    }
}