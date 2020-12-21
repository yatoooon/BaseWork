package com.yatoooon.demo.mvp.ui.fragment;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.gyf.immersionbar.ImmersionBar;
import com.yatoooon.baselibrary.base.BaseFragmentAdapter;
import com.yatoooon.baselibrary.di.component.AppComponent;
import com.yatoooon.demo.R;
import com.yatoooon.demo.app.common.MyFragment;
import com.yatoooon.demo.app.widget.XCollapsingToolbarLayout;
import com.yatoooon.demo.di.component.DaggerHomeComponent;
import com.yatoooon.demo.mvp.contract.HomeContract;
import com.yatoooon.demo.mvp.presenter.HomePresenter;

import javax.inject.Inject;

import butterknife.BindView;



public class HomeFragment extends MyFragment<HomePresenter> implements HomeContract.View, XCollapsingToolbarLayout.OnScrimsListener {

    @BindView(R.id.tb_home_title)
    Toolbar tbHomeTitle;
    @BindView(R.id.tv_home_address)
    AppCompatTextView tvHomeAddress;
    @BindView(R.id.tv_home_hint)
    AppCompatTextView tvHomeHint;
    @BindView(R.id.iv_home_search)
    AppCompatImageView ivHomeSearch;
    @BindView(R.id.ctl_home_bar)
    XCollapsingToolbarLayout ctlHomeBar;
    @BindView(R.id.tl_home_tab)
    TabLayout tlHomeTab;
    @BindView(R.id.vp_home_pager)
    ViewPager vpHomePager;

    @Inject
    BaseFragmentAdapter<MyFragment> mPagerAdapter;

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerHomeComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void initView() {
        mPagerAdapter.addFragment(StatusFragment.newInstance(), "列表 A");
        mPagerAdapter.addFragment(StatusFragment.newInstance(), "列表 B");
        mPagerAdapter.addFragment(StatusFragment.newInstance(), "列表 C");
        vpHomePager.setAdapter(mPagerAdapter);
        tlHomeTab.setupWithViewPager(vpHomePager);

        // 给这个 ToolBar 设置顶部内边距，才能和 TitleBar 进行对齐
        ImmersionBar.setTitleBar(getAttachActivity(), tbHomeTitle);

        //设置渐变监听
        ctlHomeBar.setOnScrimsListener(this);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public boolean isStatusBarEnabled() {
        // 使用沉浸式状态栏
        return !super.isStatusBarEnabled();
    }

    @Override
    public boolean statusBarDarkFont() {
        return ctlHomeBar.isScrimsShown();
    }

    /**
     * CollapsingToolbarLayout 渐变回调
     * <p>
     * {@link XCollapsingToolbarLayout.OnScrimsListener}
     */
    @SuppressLint("RestrictedApi")
    @Override
    public void onScrimsStateChange(XCollapsingToolbarLayout layout, boolean shown) {
        if (shown) {
            tvHomeAddress.setTextColor(ContextCompat.getColor(getAttachActivity(), R.color.black));
            tvHomeHint.setBackgroundResource(R.drawable.home_search_bar_gray_bg);
            tvHomeHint.setTextColor(ContextCompat.getColor(getAttachActivity(), R.color.black60));
            ivHomeSearch.setSupportImageTintList(ColorStateList.valueOf(getColor(R.color.colorIcon)));
            getStatusBarConfig().statusBarDarkFont(true).init();
        } else {
            tvHomeAddress.setTextColor(ContextCompat.getColor(getAttachActivity(), R.color.white));
            tvHomeHint.setBackgroundResource(R.drawable.home_search_bar_transparent_bg);
            tvHomeHint.setTextColor(ContextCompat.getColor(getAttachActivity(), R.color.white60));
            ivHomeSearch.setSupportImageTintList(ColorStateList.valueOf(getColor(R.color.white)));
            getStatusBarConfig().statusBarDarkFont(false).init();
        }
    }
}
