package com.yatoooon.demo.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.yatoooon.baselibrary.di.component.AppComponent;

import com.yatoooon.demo.di.module.PasswordResetModule;
import com.yatoooon.demo.mvp.contract.PasswordResetContract;

import com.yatoooon.baselibrary.di.scope.ActivityScope;
import com.yatoooon.demo.mvp.ui.activity.PasswordResetActivity;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 12/22/2020 18:47
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = PasswordResetModule.class, dependencies = AppComponent.class)
public interface PasswordResetComponent {
    void inject(PasswordResetActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        PasswordResetComponent.Builder view(PasswordResetContract.View view);

        PasswordResetComponent.Builder appComponent(AppComponent appComponent);

        PasswordResetComponent build();
    }
}