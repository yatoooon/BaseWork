package com.yatoooon.demo.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.yatoooon.baselibrary.di.component.AppComponent;

import com.yatoooon.demo.di.module.RestartModule;
import com.yatoooon.demo.mvp.contract.RestartContract;

import com.yatoooon.baselibrary.di.scope.ActivityScope;
import com.yatoooon.demo.mvp.ui.activity.RestartActivity;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 03/22/2021 20:54
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = RestartModule.class, dependencies = AppComponent.class)
public interface RestartComponent {
    void inject(RestartActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        RestartComponent.Builder view(RestartContract.View view);

        RestartComponent.Builder appComponent(AppComponent appComponent);

        RestartComponent build();
    }
}