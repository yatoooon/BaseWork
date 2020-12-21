package com.yatoooon.demo.mvp.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.yatoooon.baselibrary.base.BaseFragmentAdapter;
import com.yatoooon.baselibrary.di.component.AppComponent;
import com.yatoooon.baselibrary.widget.layout.NoScrollViewPager;
import com.yatoooon.demo.R;
import com.yatoooon.demo.app.common.MyActivity;
import com.yatoooon.demo.app.common.MyFragment;
import com.yatoooon.demo.app.helper.ActivityStackManager;
import com.yatoooon.demo.app.helper.DoubleClickHelper;
import com.yatoooon.demo.app.other.KeyboardWatcher;
import com.yatoooon.demo.di.component.DaggerHomeComponent;
import com.yatoooon.demo.mvp.contract.HomeContract;
import com.yatoooon.demo.mvp.presenter.HomePresenter;
import com.yatoooon.demo.mvp.ui.fragment.HomeFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 12/20/2020 13:53

 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public class HomeActivity extends MyActivity<HomePresenter> implements HomeContract.View, KeyboardWatcher.SoftKeyboardStateListener,
        BottomNavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.vp_home_pager)
    NoScrollViewPager vpHomePager;
    @BindView(R.id.bv_home_navigation)
    BottomNavigationView bvHomeNavigation;

    @Inject
    BaseFragmentAdapter<MyFragment> mPagerAdapter;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerHomeComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        // 不使用图标默认变色
        bvHomeNavigation.setItemIconTintList(null);
        bvHomeNavigation.setOnNavigationItemSelectedListener(this);

        KeyboardWatcher.with(this)
                .setListener(this);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mPagerAdapter.addFragment(HomeFragment.newInstance());
        mPagerAdapter.addFragment(HomeFragment.newInstance());
        mPagerAdapter.addFragment(HomeFragment.newInstance());
        mPagerAdapter.addFragment(HomeFragment.newInstance());
        // 设置成懒加载模式
        mPagerAdapter.setLazyMode(true);
        vpHomePager.setAdapter(mPagerAdapter);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_home:
                vpHomePager.setCurrentItem(0);
                return true;
            case R.id.home_found:
                vpHomePager.setCurrentItem(1);
                return true;
            case R.id.home_message:
                vpHomePager.setCurrentItem(2);
                return true;
            case R.id.home_me:
                vpHomePager.setCurrentItem(3);
                return true;
            default:
                break;
        }
        return false;
    }

    @Override
    public void onSoftKeyboardOpened(int keyboardHeight) {
        bvHomeNavigation.setVisibility(View.GONE);
    }

    @Override
    public void onSoftKeyboardClosed() {
        bvHomeNavigation.setVisibility(View.VISIBLE);
    }


    @Override
    public void onBackPressed() {
        if (DoubleClickHelper.isOnDoubleClick()) {
            // 移动到上一个任务栈，避免侧滑引起的不良反应
            moveTaskToBack(false);
            postDelayed(() -> {
                // 进行内存优化，销毁掉所有的界面
                ActivityStackManager.getInstance().finishAllActivities();
                // 销毁进程（注意：调用此 API 可能导致当前 Activity onDestroy 方法无法正常回调）
                // System.exit(0);

            }, 300);
        } else {
            toast(R.string.home_exit_hint);
        }
    }

    @Override
    protected void onDestroy() {
        vpHomePager.setAdapter(null);
        bvHomeNavigation.setOnNavigationItemSelectedListener(null);
        super.onDestroy();
    }


}
