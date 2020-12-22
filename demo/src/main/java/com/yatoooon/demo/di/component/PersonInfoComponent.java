package com.yatoooon.demo.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.yatoooon.baselibrary.di.component.AppComponent;

import com.yatoooon.demo.di.module.PersonInfoModule;
import com.yatoooon.demo.mvp.contract.PersonInfoContract;

import com.yatoooon.baselibrary.di.scope.ActivityScope;
import com.yatoooon.demo.mvp.ui.activity.PersonInfoActivity;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 12/22/2020 19:28
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = PersonInfoModule.class, dependencies = AppComponent.class)
public interface PersonInfoComponent {
    void inject(PersonInfoActivity activity);

    @Component.Builder
    interface Builder {
        @BindsInstance
        PersonInfoComponent.Builder view(PersonInfoContract.View view);

        PersonInfoComponent.Builder appComponent(AppComponent appComponent);

        PersonInfoComponent build();
    }
}