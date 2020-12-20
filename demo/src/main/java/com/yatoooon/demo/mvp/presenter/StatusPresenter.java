package com.yatoooon.demo.mvp.presenter;

import android.app.Application;

import androidx.core.app.ComponentActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.OnLifecycleEvent;

import com.yatoooon.baselibrary.integration.AppManager;
import com.yatoooon.baselibrary.di.scope.FragmentScope;
import com.yatoooon.baselibrary.mvp.BasePresenter;
import com.yatoooon.baselibrary.http.imageloader.ImageLoader;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;

import javax.inject.Inject;

import com.yatoooon.demo.mvp.contract.StatusContract;
import com.yatoooon.demo.mvp.ui.adapter.StatusAdapter;

import java.util.List;


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
public class StatusPresenter extends BasePresenter<StatusContract.Model, StatusContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;
    @Inject
    StatusAdapter mAdapter;
    @Inject
    public StatusPresenter(StatusContract.Model model, StatusContract.View rootView) {
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

    public void requestData(int itemcount) {
        List<String> data = mModel.getData(itemcount);
        mAdapter.setData(data);
    }
}
