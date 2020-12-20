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
import com.yatoooon.demo.mvp.ui.activity.HomeActivity;

import javax.inject.Inject;

import butterknife.BindView;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 12/20/2020 13:53
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public class HomeFragment extends MyFragment<HomePresenter, HomeActivity> implements HomeContract.View, XCollapsingToolbarLayout.OnScrimsListener {

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

    /**
     * 通过此方法可以使 Fragment 能够与外界做一些交互和通信, 比如说外部的 Activity 想让自己持有的某个 Fragment 对象执行一些方法,
     * 建议在有多个需要与外界交互的方法时, 统一传 {@link Message}, 通过 what 字段来区分不同的方法, 在 {@link #setData(Object)}
     * 方法中就可以 {@code switch} 做不同的操作, 这样就可以用统一的入口方法做多个不同的操作, 可以起到分发的作用
     * <p>
     * 调用此方法时请注意调用时 Fragment 的生命周期, 如果调用 {@link #setData(Object)} 方法时 {@link Fragment#onCreate(Bundle)} 还没执行
     * 但在 {@link #setData(Object)} 里却调用了 Presenter 的方法, 是会报空的, 因为 Dagger 注入是在 {@link Fragment#onCreate(Bundle)} 方法中执行的
     * 然后才创建的 Presenter, 如果要做一些初始化操作,可以不必让外部调用 {@link #setData(Object)}, 在 {@link #initData(Bundle)} 中初始化就可以了
     * <p>
     * Example usage:
     * <pre>
     * public void setData(@Nullable Object data) {
     *     if (data != null && data instanceof Message) {
     *         switch (((Message) data).what) {
     *             case 0:
     *                 loadData(((Message) data).arg1);
     *                 break;
     *             case 1:
     *                 refreshUI();
     *                 break;
     *             default:
     *                 //do something
     *                 break;
     *         }
     *     }
     * }
     *
     * // call setData(Object):
     * Message data = new Message();
     * data.what = 0;
     * data.arg1 = 1;
     * fragment.setData(data);
     * </pre>
     *
     * @param data 当不需要参数时 {@code data} 可以为 {@code null}
     */
    @Override
    public void setData(@Nullable Object data) {

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
