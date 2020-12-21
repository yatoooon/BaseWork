package com.yatoooon.demo.mvp.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.yatoooon.baselibrary.di.component.AppComponent;
import com.yatoooon.baselibrary.http.imageloader.glide.ImageConfigImpl;
import com.yatoooon.baselibrary.utils.ArmsUtils;
import com.yatoooon.baselibrary.widget.view.CountdownView;
import com.yatoooon.baselibrary.widget.view.SwitchButton;
import com.yatoooon.demo.R;
import com.yatoooon.demo.app.aop.SingleClick;
import com.yatoooon.demo.app.common.MyFragment;

import butterknife.BindView;
import butterknife.OnClick;


public class FindFragment extends MyFragment implements SwitchButton.OnCheckedChangeListener {

    @BindView(R.id.iv_find_circle)
    AppCompatImageView ivFindCircle;
    @BindView(R.id.sb_find_switch)
    SwitchButton sbFindSwitch;
    @BindView(R.id.cv_find_countdown)
    CountdownView cvFindCountdown;

    public static FindFragment newInstance() {
        FindFragment fragment = new FindFragment();
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_find;
    }

    @Override
    public void initView() {
        sbFindSwitch.setOnCheckedChangeListener(this);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        ArmsUtils.obtainAppComponentFromContext(getContext()).imageLoader()
                .loadImage(getContext(),
                        ImageConfigImpl.builder().placeholder(R.drawable.example_bg).res(R.drawable.example_bg).imageView(ivFindCircle).isCircle(true).build());
    }


    @Override
    public void setData(@Nullable Object data) {

    }

    @Override
    public boolean isStatusBarEnabled() {
        // 使用沉浸式状态栏
        return !super.isStatusBarEnabled();
    }

    @Override
    public void onCheckedChanged(SwitchButton button, boolean isChecked) {
        toast(isChecked);
    }

    @SingleClick
    @OnClick(R.id.cv_find_countdown)
    public void onViewClicked() {
        toast(R.string.common_code_send_hint);
        cvFindCountdown.start();
    }
}
