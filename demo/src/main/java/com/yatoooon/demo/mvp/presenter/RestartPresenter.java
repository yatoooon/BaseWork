package com.yatoooon.demo.mvp.presenter;

import android.app.Application;

import com.yatoooon.baselibrary.integration.AppManager;
import com.yatoooon.baselibrary.di.scope.ActivityScope;
import com.yatoooon.baselibrary.mvp.BasePresenter;
import com.yatoooon.baselibrary.http.imageloader.ImageLoader;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;

import javax.inject.Inject;

import com.yatoooon.demo.mvp.contract.RestartContract;


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
public class RestartPresenter extends BasePresenter<RestartContract.Model, RestartContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    public RestartPresenter(RestartContract.Model model, RestartContract.View rootView) {
        super(model, rootView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }
}