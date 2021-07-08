package com.yatoooon.demo.di.component;

import dagger.BindsInstance;
import dagger.Component;

import com.yatoooon.baselibrary.di.component.AppComponent;

import com.yatoooon.demo.di.module.TestModule;
import com.yatoooon.demo.mvp.contract.TestContract;

import com.yatoooon.baselibrary.di.scope.ActivityScope;
import com.yatoooon.demo.mvp.ui.activity.TestActivity;
import com.yatoooon.demo.mvp.ui.fragment.TestFragment;


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 04/07/2021 16:25
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = TestModule.class, dependencies = AppComponent.class)
public interface TestComponent {
    void inject(TestActivity activity);

    void inject(TestFragment fragment);

    @Component.Builder
    interface Builder {
        @BindsInstance
        TestComponent.Builder view(TestContract.View view);

        TestComponent.Builder appComponent(AppComponent appComponent);

        TestComponent build();
    }
}