package com.yatoooon.demo.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.yatoooon.baselibrary.integration.IRepositoryManager;
import com.yatoooon.baselibrary.mvp.BaseModel;

import com.yatoooon.baselibrary.di.scope.ActivityScope;

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
public class RestartModel extends BaseModel implements RestartContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public RestartModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }
}