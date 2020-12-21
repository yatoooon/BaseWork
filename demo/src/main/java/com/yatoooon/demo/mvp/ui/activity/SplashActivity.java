package com.yatoooon.demo.mvp.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.airbnb.lottie.LottieAnimationView;
import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;
import com.yatoooon.baselibrary.base.BaseActivity;
import com.yatoooon.baselibrary.di.component.AppComponent;
import com.yatoooon.demo.R;
import com.yatoooon.demo.app.common.MyActivity;
import com.yatoooon.demo.app.other.AppConfig;
import com.yatoooon.demo.di.component.DaggerSplashComponent;
import com.yatoooon.demo.mvp.contract.SplashContract;
import com.yatoooon.demo.mvp.presenter.SplashPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 12/18/2020 15:20

 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public class SplashActivity extends MyActivity<SplashPresenter> implements SplashContract.View {

    @BindView(R.id.iv_splash_lottie)
    LottieAnimationView ivSplashLottie;
    @BindView(R.id.iv_splash_debug)
    AppCompatImageView ivSplashDebug;
    @BindView(R.id.iv_splash_name)
    AppCompatTextView ivSplashName;

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
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        // 设置动画监听
        ivSplashLottie.addAnimatorListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                startActivity(HomeActivity.class);
                finish();
            }
        });
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
        ivSplashLottie.removeAllAnimatorListeners();
        super.onDestroy();
    }


}
