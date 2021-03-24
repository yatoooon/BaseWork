package com.yatoooon.demo.di.module;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.yatoooon.demo.mvp.ui.adapter.FragmentPagerAdapter;
import com.yatoooon.baselibrary.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

import com.yatoooon.demo.app.app.AppFragment;
import com.yatoooon.demo.mvp.contract.HomeContract;
import com.yatoooon.demo.mvp.model.HomeModel;


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
@Module
public abstract class HomeModule {

    @Binds
    abstract HomeContract.Model bindHomeModel(HomeModel model);

    @ActivityScope
    @Provides
    static FragmentPagerAdapter<AppFragment> providePagerAdapter(HomeContract.View view) {
        if (view instanceof FragmentActivity) {
            return new FragmentPagerAdapter<>((FragmentActivity) view);
        } else if (view instanceof Fragment) {
            return new FragmentPagerAdapter<>(((Fragment) view));
        }
        return null;
    }
}