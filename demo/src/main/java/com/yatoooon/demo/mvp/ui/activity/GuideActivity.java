package com.yatoooon.demo.mvp.ui.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.viewpager.widget.ViewPager;

import com.rd.PageIndicatorView;
import com.yatoooon.demo.R;
import com.yatoooon.demo.app.aop.SingleClick;
import com.yatoooon.demo.app.common.MyActivity;
import com.yatoooon.demo.mvp.ui.adapter.GuidePagerAdapter;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * author : Android 轮子哥
 * github : https://github.com/getActivity/AndroidProject
 * time   : 2019/09/21
 * desc   : APP 引导页
 */
public final class GuideActivity extends MyActivity
        implements ViewPager.OnPageChangeListener {

    @BindView(R.id.vp_guide_pager)
    ViewPager vpGuidePager;
    @BindView(R.id.pv_guide_indicator)
    PageIndicatorView pvGuideIndicator;
    @BindView(R.id.btn_guide_complete)
    AppCompatButton btnGuideComplete;
    

    private GuidePagerAdapter mPagerAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_guide;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        pvGuideIndicator.setViewPager(vpGuidePager);
        mPagerAdapter = new GuidePagerAdapter();
        vpGuidePager.setAdapter(mPagerAdapter);
        vpGuidePager.addOnPageChangeListener(this);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }

    /**
     * {@link ViewPager.OnPageChangeListener}
     */

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (vpGuidePager.getCurrentItem() == mPagerAdapter.getCount() - 1 && positionOffsetPixels > 0) {
            btnGuideComplete.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == ViewPager.SCROLL_STATE_IDLE) {
            btnGuideComplete.setVisibility(vpGuidePager.getCurrentItem() == mPagerAdapter.getCount() - 1 ? View.VISIBLE : View.INVISIBLE);
        }
    }
    @SingleClick
    @OnClick(R.id.btn_guide_complete)
    public void onViewClicked(View view) {
        startActivity(HomeActivity.class);
        finish();
    }
}