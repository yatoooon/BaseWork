package com.yatoooon.demo.mvp.ui.fragment;

import android.os.Bundle;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.yatoooon.demo.app.common.AppFragment;
import com.yatoooon.baselibrary.di.component.AppComponent;

import com.yatoooon.demo.di.component.DaggerTestComponent;
import com.yatoooon.demo.mvp.contract.TestContract;
import com.yatoooon.demo.mvp.presenter.TestPresenter;

import com.yatoooon.demo.R;


public class TestFragment extends AppFragment<TestPresenter> implements TestContract.View {

    public static TestFragment newInstance() {
        TestFragment fragment = new TestFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerTestComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_test;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void setData(@Nullable Object data) {

    }
}
