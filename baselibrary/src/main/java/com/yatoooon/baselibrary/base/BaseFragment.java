/*
 * Copyright 2017 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yatoooon.baselibrary.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;

import com.yatoooon.baselibrary.action.ActivityAction;
import com.yatoooon.baselibrary.action.BundleAction;
import com.yatoooon.baselibrary.action.ClickAction;
import com.yatoooon.baselibrary.action.HandlerAction;
import com.yatoooon.baselibrary.action.KeyboardAction;
import com.yatoooon.baselibrary.action.ResourcesAction;
import com.yatoooon.baselibrary.base.delegate.IActivity;
import com.yatoooon.baselibrary.base.delegate.IFragment;
import com.yatoooon.baselibrary.integration.cache.Cache;
import com.yatoooon.baselibrary.integration.cache.CacheType;
import com.yatoooon.baselibrary.integration.lifecycle.FragmentLifecycleable;
import com.yatoooon.baselibrary.mvp.IPresenter;
import com.yatoooon.baselibrary.utils.ArmsUtils;
import com.trello.rxlifecycle2.android.FragmentEvent;

import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

/**
 * ================================================
 * 因为 Java 只能单继承, 所以如果要用到需要继承特定 @{@link Fragment} 的三方库, 那你就需要自己自定义 @{@link Fragment}
 * 继承于这个特定的 @{@link Fragment}, 然后再按照 {@link BaseFragment} 的格式, 将代码复制过去, 记住一定要实现{@link IFragment}
 *
 * @see <a href="https://github.com/JessYanCoding/MVPArms/wiki">请配合官方 Wiki 文档学习本框架</a>
 * @see <a href="https://github.com/JessYanCoding/MVPArms/wiki/UpdateLog">更新日志, 升级必看!</a>
 * @see <a href="https://github.com/JessYanCoding/MVPArms/wiki/Issues">常见 Issues, 踩坑必看!</a>
 * @see <a href="https://github.com/JessYanCoding/ArmsComponent/wiki">MVPArms 官方组件化方案 ArmsComponent, 进阶指南!</a>
 * Created by JessYan on 22/03/2016
 * <p>
 * ================================================
 */
public abstract class BaseFragment<P extends IPresenter> extends Fragment implements IFragment, FragmentLifecycleable, ActivityAction, ResourcesAction, HandlerAction, ClickAction, BundleAction, KeyboardAction {
    protected final String TAG = this.getClass().getSimpleName();
    private final BehaviorSubject<FragmentEvent> mLifecycleSubject = BehaviorSubject.create();
    @Inject
    @Nullable
    protected P mPresenter;//如果当前页面逻辑简单, Presenter 可以为 null
    private Cache<String, Object> mCache;

    /**
     * Activity 对象
     */
    private BaseActivity mActivity;
    /**
     * 根布局
     */
    private View mRootView;
    /**
     * 当前是否加载过
     */
    private boolean mLoading;
    private Unbinder unbinder;

    @NonNull
    @Override
    public synchronized Cache<String, Object> provideCache() {
        if (mCache == null) {
            //noinspection unchecked
            mCache = ArmsUtils.obtainAppComponentFromContext(getActivity()).cacheFactory().build(CacheType.FRAGMENT_CACHE);
        }
        return mCache;
    }

    @NonNull
    @Override
    public final Subject<FragmentEvent> provideLifecycleSubject() {
        return mLifecycleSubject;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // 获得全局的 Activity
        mActivity = (BaseActivity) requireActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mLoading = false;
        if (getLayoutId() > 0) {
            mRootView = inflater.inflate(getLayoutId(), container, false);
            mRootView.setTag(getLayoutId(), savedInstanceState);  //在这里用布局的id做了key
            unbinder = ButterKnife.bind(this, mRootView);
            return mRootView;
        } else {
            return null;
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mRootView = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mLoading) {
            mLoading = true;
            Bundle savedInstanceState = null;
            if (mRootView != null) {
                savedInstanceState = (Bundle) mRootView.getTag(getLayoutId());
            }
            initData(savedInstanceState);
            onFragmentResume(true);
            return;
        }

        if (mActivity != null && mActivity.getLifecycle().getCurrentState() == Lifecycle.State.STARTED) {
            onActivityResume();
        } else {
            onFragmentResume(false);
        }
    }

    /**
     * Fragment 可见回调
     *
     * @param first 是否首次调用
     */
    protected void onFragmentResume(boolean first) {
    }

    /**
     * Activity 可见回调
     */
    protected void onActivityResume() {
    }

    /**
     * 这个 Fragment 是否已经加载过了
     */
    public boolean isLoading() {
        return mLoading;
    }

    @NonNull
    @Override
    public View getView() {
        return mRootView;
    }

    /**
     * 获取绑定的 Activity，防止出现 getActivity 为空
     */
    public BaseActivity getAttachActivity() {
        return mActivity;
    }


    /**
     * 根据资源 id 获取一个 View 对象
     */
    @Override
    public <V extends View> V findViewById(@IdRes int id) {
        return mRootView.findViewById(id);
    }

    @Override
    public Bundle getBundle() {
        return getArguments();
    }

    /**
     * startActivityForResult 方法优化
     */

    private BaseActivity.OnActivityCallback mActivityCallback;
    private int mActivityRequestCode;

    /**
     * startActivityForResult 方法优化
     */

    public void startActivityForResult(Class<? extends Activity> clazz, BaseActivity.OnActivityCallback callback) {
        getAttachActivity().startActivityForResult(clazz, callback);
    }

    public void startActivityForResult(Intent intent, BaseActivity.OnActivityCallback callback) {
        getAttachActivity().startActivityForResult(intent, null, callback);
    }

    public void startActivityForResult(Intent intent, Bundle options, BaseActivity.OnActivityCallback callback) {
        getAttachActivity().startActivityForResult(intent, options, callback);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (mActivityCallback != null && mActivityRequestCode == requestCode) {
            mActivityCallback.onActivityResult(resultCode, data);
            mActivityCallback = null;
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * 销毁当前 Fragment 所在的 Activity
     */
    public void finish() {
        if (mActivity == null || mActivity.isFinishing() || mActivity.isDestroyed()) {
            return;
        }
        mActivity.finish();
    }

    /**
     * Fragment 按键事件派发
     */
    public boolean dispatchKeyEvent(KeyEvent event) {
        List<Fragment> fragments = getChildFragmentManager().getFragments();
        for (Fragment fragment : fragments) {
            // 这个子 Fragment 必须是 BaseFragment 的子类，并且处于可见状态
            if (!(fragment instanceof BaseFragment) ||
                    fragment.getLifecycle().getCurrentState() != Lifecycle.State.RESUMED) {
                continue;
            }
            // 将按键事件派发给子 Fragment 进行处理
            if (((BaseFragment<?>) fragment).dispatchKeyEvent(event)) {
                // 如果子 Fragment 拦截了这个事件，那么就不交给父 Fragment 处理
                return true;
            }
        }
        switch (event.getAction()) {
            case KeyEvent.ACTION_DOWN:
                return onKeyDown(event.getKeyCode(), event);
            case KeyEvent.ACTION_UP:
                return onKeyUp(event.getKeyCode(), event);
            default:
                return false;
        }
    }

    /**
     * Fragment 返回键被按下时回调
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 默认不拦截按键事件，回传给 Activity
        return false;
    }

    /**
     * 按键抬起事件回调
     */
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        // 默认不拦截按键事件
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.onDestroy();//释放资源
        }
        this.mPresenter = null;
        mLoading = false;
        removeCallbacks();
        unbinder = null;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivity = null;
    }

    /**
     * 是否使用 EventBus
     * Arms 核心库现在并不会依赖某个 EventBus, 要想使用 EventBus, 还请在项目中自行依赖对应的 EventBus
     * 现在支持两种 EventBus, greenrobot 的 EventBus 和畅销书 《Android源码设计模式解析与实战》的作者 何红辉 所作的 AndroidEventBus
     * 确保依赖后, 将此方法返回 true, Arms 会自动检测您依赖的 EventBus, 并自动注册
     * 这种做法可以让使用者有自行选择三方库的权利, 并且还可以减轻 Arms 的体积
     *
     * @return 返回 {@code true} (默认为 {@code true}), Arms 会自动注册 EventBus
     */
    @Override
    public boolean useEventBus() {
        return true;
    }
}
