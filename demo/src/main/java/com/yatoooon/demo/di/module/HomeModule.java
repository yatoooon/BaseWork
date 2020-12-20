package com.yatoooon.demo.di.module;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.yatoooon.baselibrary.base.BaseFragmentAdapter;
import com.yatoooon.baselibrary.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

import com.yatoooon.demo.app.common.MyFragment;
import com.yatoooon.demo.mvp.contract.HomeContract;
import com.yatoooon.demo.mvp.contract.UserContract;
import com.yatoooon.demo.mvp.model.HomeModel;
import com.yatoooon.demo.mvp.ui.activity.HomeActivity;


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
@Module
public abstract class HomeModule {

    @Binds
    abstract HomeContract.Model bindHomeModel(HomeModel model);

    @ActivityScope
    @Provides
    static BaseFragmentAdapter<MyFragment> providePagerAdapter(HomeContract.View view) {
        if (view instanceof FragmentActivity) {
            return new BaseFragmentAdapter<>((FragmentActivity) view);
        } else if (view instanceof Fragment) {
            return new BaseFragmentAdapter<>(((Fragment) view));
        }
        return null;
    }
}