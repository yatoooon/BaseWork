package com.yatoooon.demo.di.module;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.yatoooon.baselibrary.base.BaseFragmentAdapter;
import com.yatoooon.baselibrary.di.scope.ActivityScope;
import com.yatoooon.baselibrary.di.scope.FragmentScope;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

import com.yatoooon.demo.app.common.MyFragment;
import com.yatoooon.demo.mvp.contract.HomeContract;
import com.yatoooon.demo.mvp.contract.StatusContract;
import com.yatoooon.demo.mvp.model.StatusModel;
import com.yatoooon.demo.mvp.ui.adapter.StatusAdapter;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 12/20/2020 15:20
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@Module
public abstract class StatusModule {

    @Binds
    abstract StatusContract.Model bindStatusModel(StatusModel model);

    @FragmentScope
    @Provides
    static StatusAdapter provideStatusAdapter(StatusContract.View view) {
        return new StatusAdapter(((Fragment)view).getActivity());
    }
}