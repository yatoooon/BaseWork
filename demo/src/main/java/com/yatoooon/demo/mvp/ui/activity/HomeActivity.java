package com.yatoooon.demo.mvp.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.yatoooon.demo.mvp.ui.adapter.FragmentPagerAdapter;
import com.yatoooon.baselibrary.di.component.AppComponent;
import com.yatoooon.baselibrary.widget.layout.NoScrollViewPager;
import com.yatoooon.demo.R;
import com.yatoooon.demo.app.common.AppActivity;
import com.yatoooon.demo.app.common.AppFragment;
import com.yatoooon.demo.app.manager.ActivityManager;
import com.yatoooon.demo.app.manager.DoubleClickHelper;
import com.yatoooon.demo.app.other.IntentKey;
import com.yatoooon.demo.di.component.DaggerHomeComponent;
import com.yatoooon.demo.mvp.contract.HomeContract;
import com.yatoooon.demo.mvp.presenter.HomePresenter;
import com.yatoooon.demo.mvp.ui.fragment.FindFragment;
import com.yatoooon.demo.mvp.ui.fragment.HomeFragment;
import com.yatoooon.demo.mvp.ui.fragment.MeFragment;
import com.yatoooon.demo.mvp.ui.fragment.MessageFragment;

import javax.inject.Inject;

import butterknife.BindView;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 12/20/2020 13:53
 *
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public class HomeActivity extends AppActivity<HomePresenter> implements HomeContract.View,
        BottomNavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.vp_home_pager)
    NoScrollViewPager vpHomePager;
    @BindView(R.id.bv_home_navigation)
    BottomNavigationView bvHomeNavigation;

    @Inject
    FragmentPagerAdapter<AppFragment> mPagerAdapter;

    public static void start(Context context) {
        start(context, HomeFragment.class);
    }

    public static void start(Context context, Class<? extends AppFragment<?>> fragmentClass) {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.putExtra(IntentKey.INDEX, fragmentClass);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

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

        // 屏蔽底部导航栏长按文本提示
        Menu menu = bvHomeNavigation.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            bvHomeNavigation.findViewById(menu.getItem(i).getItemId()).setOnLongClickListener(v -> true);
        }
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mPagerAdapter.addFragment(HomeFragment.newInstance());
        mPagerAdapter.addFragment(FindFragment.newInstance());
        mPagerAdapter.addFragment(MessageFragment.newInstance());
        mPagerAdapter.addFragment(MeFragment.newInstance());

        vpHomePager.setAdapter(mPagerAdapter);
        onNewIntent(getIntent());

    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int fragmentIndex = mPagerAdapter.getFragmentIndex(getSerializable(IntentKey.INDEX));
        if (fragmentIndex == -1) {
            return;
        }
        vpHomePager.setCurrentItem(fragmentIndex);
        switch (fragmentIndex) {
            case 0:
                bvHomeNavigation.setSelectedItemId(R.id.menu_home);
                break;
            case 1:
                bvHomeNavigation.setSelectedItemId(R.id.home_found);
                break;
            case 2:
                bvHomeNavigation.setSelectedItemId(R.id.home_message);
                break;
            case 3:
                bvHomeNavigation.setSelectedItemId(R.id.home_me);
                break;
            default:
                break;
        }
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
    public void onBackPressed() {
        if (!DoubleClickHelper.isOnDoubleClick()) {
            toast(R.string.home_exit_hint);
            return;
        }

        // 移动到上一个任务栈，避免侧滑引起的不良反应
        moveTaskToBack(false);
        postDelayed(() -> {
            // 进行内存优化，销毁掉所有的界面
            ActivityManager.getInstance().finishAllActivities();
            // 销毁进程（注意：调用此 API 可能导致当前 Activity onDestroy 方法无法正常回调）
            // System.exit(0);
        }, 300);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        vpHomePager.setAdapter(null);
        bvHomeNavigation.setOnNavigationItemSelectedListener(null);
    }


}
