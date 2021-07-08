package com.yatoooon.demo.mvp.ui.activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.yatoooon.demo.app.common.AppActivity;
import com.yatoooon.baselibrary.di.component.AppComponent;

import com.yatoooon.demo.di.component.DaggerTestComponent;
import com.yatoooon.demo.mvp.contract.TestContract;
import com.yatoooon.demo.mvp.presenter.TestPresenter;

import com.yatoooon.demo.R;


public class TestActivity extends AppActivity<TestPresenter> implements TestContract.View {

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerTestComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_test;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }

}
