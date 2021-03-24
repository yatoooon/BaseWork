package com.yatoooon.demo.mvp.ui.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.viewpager2.widget.ViewPager2;

import com.yatoooon.demo.R;
import com.yatoooon.demo.app.aop.SingleClick;
import com.yatoooon.demo.app.app.AppActivity;
import com.yatoooon.demo.mvp.ui.adapter.GuideAdapter;

import butterknife.BindView;
import butterknife.OnClick;
import me.relex.circleindicator.CircleIndicator3;

/**
 * author : Android 轮子哥
 * github : https://github.com/getActivity/AndroidProject
 * time   : 2019/09/21
 * desc   : APP 引导页
 */
public final class GuideActivity extends AppActivity {

    @BindView(R.id.vp_guide_pager)
    ViewPager2 vpGuidePager;
    @BindView(R.id.cv_guide_indicator)
    CircleIndicator3 cvGuideIndicator;
    @BindView(R.id.btn_guide_complete)
    AppCompatButton btnGuideComplete;
    private GuideAdapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_guide;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        setOnClickListener(btnGuideComplete);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mAdapter = new GuideAdapter(this);
        mAdapter.addItem(R.drawable.guide_1_bg);
        mAdapter.addItem(R.drawable.guide_2_bg);
        mAdapter.addItem(R.drawable.guide_3_bg);
        vpGuidePager.setAdapter(mAdapter);
        vpGuidePager.registerOnPageChangeCallback(mCallback);
        cvGuideIndicator.setViewPager(vpGuidePager);
    }


    private final ViewPager2.OnPageChangeCallback mCallback = new ViewPager2.OnPageChangeCallback() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (vpGuidePager.getCurrentItem() == mAdapter.getItemCount() - 1 && positionOffsetPixels > 0) {
                cvGuideIndicator.setVisibility(View.VISIBLE);
                btnGuideComplete.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager2.SCROLL_STATE_IDLE) {
                boolean last = vpGuidePager.getCurrentItem() == mAdapter.getItemCount() - 1;
                cvGuideIndicator.setVisibility(last ? View.INVISIBLE : View.VISIBLE);
                btnGuideComplete.setVisibility(last ? View.VISIBLE : View.INVISIBLE);
            }
        }
    };

    @SingleClick
    @OnClick(R.id.btn_guide_complete)
    public void onViewClicked(View view) {
        startActivity(HomeActivity.class);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        vpGuidePager.unregisterOnPageChangeCallback(mCallback);
    }

}