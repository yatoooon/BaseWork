package com.yatoooon.demo.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.yatoooon.baselibrary.di.component.AppComponent;

import com.yatoooon.demo.di.module.StatusModule;
import com.yatoooon.demo.mvp.contract.StatusContract;

import com.yatoooon.baselibrary.di.scope.FragmentScope;
import com.yatoooon.demo.mvp.ui.fragment.StatusFragment;


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
@FragmentScope
@Component(modules = StatusModule.class, dependencies = AppComponent.class)
public interface StatusComponent {
    void inject(StatusFragment fragment);

    @Component.Builder
    interface Builder {
        @BindsInstance
        StatusComponent.Builder view(StatusContract.View view);

        StatusComponent.Builder appComponent(AppComponent appComponent);

        StatusComponent build();
    }
}