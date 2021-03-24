package com.yatoooon.demo.mvp.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.airbnb.lottie.LottieAnimationView;
import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;
import com.yatoooon.baselibrary.di.component.AppComponent;
import com.yatoooon.demo.R;
import com.yatoooon.demo.app.common.AppActivity;
import com.yatoooon.demo.app.other.AppConfig;
import com.yatoooon.demo.app.widget.SlantedTextView;
import com.yatoooon.demo.di.component.DaggerSplashComponent;
import com.yatoooon.demo.mvp.contract.SplashContract;
import com.yatoooon.demo.mvp.presenter.SplashPresenter;

import butterknife.BindView;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 12/18/2020 15:20
 *
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public class SplashActivity extends AppActivity<SplashPresenter> implements SplashContract.View {

    @BindView(R.id.iv_splash_lottie)
    LottieAnimationView ivSplashLottie;
    @BindView(R.id.iv_splash_name)
    AppCompatTextView ivSplashName;
    @BindView(R.id.iv_splash_debug)
    SlantedTextView ivSplashDebug;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerSplashComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }

    @Override
    public void initActivity(@Nullable Bundle savedInstanceState) {
        // 问题及方案：https://www.cnblogs.com/net168/p/5722752.html
        // 如果当前 Activity 不是任务栈中的第一个 Activity
        if (!isTaskRoot()) {
            Intent intent = getIntent();
            // 如果当前 Activity 是通过桌面图标启动进入的
            if (intent != null && intent.hasCategory(Intent.CATEGORY_LAUNCHER)
                    && Intent.ACTION_MAIN.equals(intent.getAction())) {
                // 对当前 Activity 执行销毁操作，避免重复实例化入口
                finish();
                return;
            }
        }
        super.initActivity(savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        // 设置动画监听
        ivSplashLottie.addAnimatorListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                ivSplashLottie.removeAnimatorListener(this);
                HomeActivity.start(getContext());
                finish();
            }
        });
        ivSplashDebug.setText(AppConfig.getBuildType().toUpperCase());
        if (AppConfig.isDebug()) {
            ivSplashDebug.setVisibility(View.VISIBLE);
        } else {
            ivSplashDebug.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }

    @NonNull
    @Override
    protected ImmersionBar createStatusBarConfig() {
        return super.createStatusBarConfig()
                // 隐藏状态栏和导航栏
                .hideBar(BarHide.FLAG_HIDE_BAR);
    }

    @Override
    public void onBackPressed() {
        //禁用返回键
        //super.onBackPressed();
    }

    @Override
    public boolean isSwipeEnable() {
        return false;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
