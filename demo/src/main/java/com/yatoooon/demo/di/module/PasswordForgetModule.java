package com.yatoooon.demo.di.module;

import com.yatoooon.baselibrary.di.scope.ActivityScope;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

import com.yatoooon.demo.mvp.contract.PasswordForgetContract;
import com.yatoooon.demo.mvp.model.PasswordForgetModel;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 12/22/2020 18:19
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@Module
public abstract class PasswordForgetModule {

    @Binds
    abstract PasswordForgetContract.Model bindPasswordForgetModel(PasswordForgetModel model);
}