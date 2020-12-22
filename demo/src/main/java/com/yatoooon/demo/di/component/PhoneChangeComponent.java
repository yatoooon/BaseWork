package com.yatoooon.demo.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.yatoooon.baselibrary.di.component.AppComponent;

import com.yatoooon.demo.di.module.PhoneChangeModule;
import com.yatoooon.demo.mvp.contract.PhoneChangeContract;

import com.yatoooon.baselibrary.di.scope.ActivityScope;
import com.yatoooon.demo.mvp.ui.activity.PhoneChangeActivity;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 12/22/2020 19:18
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = PhoneChangeModule.class, dependencies = AppComponent.class)
public interface PhoneChangeComponent {
    void inject(PhoneChangeActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        PhoneChangeComponent.Builder view(PhoneChangeContract.View view);

        PhoneChangeComponent.Builder appComponent(AppComponent appComponent);

        PhoneChangeComponent build();
    }
}