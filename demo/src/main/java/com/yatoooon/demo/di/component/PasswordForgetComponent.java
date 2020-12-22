package com.yatoooon.demo.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.yatoooon.baselibrary.di.component.AppComponent;

import com.yatoooon.demo.di.module.PasswordForgetModule;
import com.yatoooon.demo.mvp.contract.PasswordForgetContract;

import com.yatoooon.baselibrary.di.scope.ActivityScope;
import com.yatoooon.demo.mvp.ui.activity.PasswordForgetActivity;


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
@ActivityScope
@Component(modules = PasswordForgetModule.class, dependencies = AppComponent.class)
public interface PasswordForgetComponent {
    void inject(PasswordForgetActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        PasswordForgetComponent.Builder view(PasswordForgetContract.View view);

        PasswordForgetComponent.Builder appComponent(AppComponent appComponent);

        PasswordForgetComponent build();
    }
}