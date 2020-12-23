package com.yatoooon.demo.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;
import com.yatoooon.demo.R;
import com.yatoooon.demo.app.action.SwipeAction;
import com.yatoooon.demo.app.aop.DebugLog;
import com.yatoooon.demo.app.common.MyActivity;
import com.yatoooon.demo.app.other.IntentKey;
import com.yatoooon.demo.app.widget.PlayerView;

import java.io.File;

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject
 *    time   : 2020/02/16
 *    desc   : 视频播放界面
 */
public final class VideoPlayActivity extends MyActivity
        implements SwipeAction, PlayerView.onGoBackListener {

    public static void start(Context context, File file) {
        if (file == null || !file.isFile()) {
            return;
        }
        start(context, file.getPath(), file.getName());
    }

    @DebugLog
    public static void start(Context context, String url, String title) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        Intent intent = new Intent(context, VideoPlayActivity.class);
        intent.putExtra(IntentKey.VIDEO, url);
        intent.putExtra(IntentKey.TITLE, title);
        context.startActivity(intent);
    }

    private PlayerView mPlayerView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_video_play;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mPlayerView = findViewById(R.id.pv_video_play_view);
        mPlayerView.setOnGoBackListener(this);
        mPlayerView.setGestureEnabled(true);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mPlayerView.setVideoTitle(getString(IntentKey.TITLE));
        mPlayerView.setVideoSource(getString(IntentKey.VIDEO));
        mPlayerView.start();
    }

    /**
     * {@link PlayerView.onGoBackListener}
     */
    @Override
    public void onClickGoBack(PlayerView view) {
        onBackPressed();
    }

    @Override
    protected void onResume() {
        mPlayerView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mPlayerView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mPlayerView.onDestroy();
        super.onDestroy();
    }

    @NonNull
    @Override
    protected ImmersionBar createStatusBarConfig() {
        return super.createStatusBarConfig()
                // 隐藏状态栏和导航栏
                .hideBar(BarHide.FLAG_HIDE_BAR);
    }

    @Override
    public boolean isSwipeEnable() {
        return false;
    }
}