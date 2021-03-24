package com.yatoooon.demo.mvp.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.yatoooon.demo.app.app.AppActivity;
import com.yatoooon.baselibrary.di.component.AppComponent;

import com.yatoooon.demo.di.component.DaggerRestartComponent;
import com.yatoooon.demo.mvp.contract.RestartContract;
import com.yatoooon.demo.mvp.presenter.RestartPresenter;

import com.yatoooon.demo.R;


public class RestartActivity extends AppActivity<RestartPresenter> implements RestartContract.View {

    public static void start(Context context) {
        Intent intent = new Intent(context, RestartActivity.class);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerRestartComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_restart;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        restart(this);
        finish();
        toast(R.string.common_crash_hint);
    }

    public static void restart(Context context) {
        Intent intent;
        if (true) {
            // 如果是未登录的情况下跳转到闪屏页
            intent = new Intent(context, SplashActivity.class);
        } else {
            // 如果是已登录的情况下跳转到首页
            intent = new Intent(context, HomeActivity.class);
        }

        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

}
