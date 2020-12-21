package com.yatoooon.demo.mvp.model;

import android.app.Application;

import com.google.gson.Gson;
import com.yatoooon.baselibrary.integration.IRepositoryManager;
import com.yatoooon.baselibrary.mvp.BaseModel;

import com.yatoooon.baselibrary.di.scope.FragmentScope;

import javax.inject.Inject;

import com.yatoooon.demo.mvp.contract.StatusContract;
import com.yatoooon.demo.mvp.model.api.cache.CommonCache;
import com.yatoooon.demo.mvp.model.api.service.UserService;
import com.yatoooon.demo.mvp.model.entity.User;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.EvictDynamicKey;



/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 12/20/2020 15:20

 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@FragmentScope
public class StatusModel extends BaseModel implements StatusContract.Model {
    public static final int USERS_PER_PAGE = 10;

    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    @Inject
    public StatusModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<List<User>> getUsers(int lastIdQueried, boolean update) {
        //使用rxcache缓存,上拉刷新则不读取缓存,加载更多读取缓存
        return Observable.just(mRepositoryManager
                .obtainRetrofitService(UserService.class)
                .getUsers(lastIdQueried, USERS_PER_PAGE))
                .flatMap((Function<Observable<List<User>>, ObservableSource<List<User>>>) listObservable -> mRepositoryManager.obtainCacheService(CommonCache.class)
                        .getUsers(listObservable
                                , new DynamicKey(lastIdQueried)
                                , new EvictDynamicKey(update))
                        .map(listReply -> listReply.getData()));
    }

    @Override
    public List<String> getData(int itemcount) {
        List<String> data = new ArrayList<>();
        for (int i = itemcount; i < itemcount + 20; i++) {
            data.add("我是第" + i + "条目");
        }
        return data;
    }
}