package com.yatoooon.demo.mvp.ui.fragment;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.yatoooon.demo.R;
import com.yatoooon.demo.app.action.StatusAction;
import com.yatoooon.demo.app.aop.CheckNet;
import com.yatoooon.demo.app.aop.DebugLog;
import com.yatoooon.demo.app.common.AppFragment;
import com.yatoooon.demo.app.other.IntentKey;
import com.yatoooon.demo.app.widget.BrowserView;
import com.yatoooon.demo.app.widget.StatusLayout;
import com.yatoooon.demo.mvp.ui.activity.BrowserActivity;

/**
 * author : Android 轮子哥
 * github : https://github.com/getActivity/AndroidProject
 * time   : 2020/10/24
 * desc   : 浏览器 Fragment
 */
public final class BrowserFragment extends AppFragment
        implements StatusAction, OnRefreshListener {

    @DebugLog
    public static BrowserFragment newInstance(String url) {
        BrowserFragment fragment = new BrowserFragment();
        Bundle bundle = new Bundle();
        bundle.putString(IntentKey.URL, url);
        fragment.setArguments(bundle);
        return fragment;
    }

    private StatusLayout mStatusLayout;
    private SmartRefreshLayout mRefreshLayout;
    private BrowserView mBrowserView;

    @Override
    public int getLayoutId() {
        return R.layout.browser_fragment;
    }

    @Override
    public void initView() {
        mStatusLayout = (StatusLayout) findViewById(R.id.hl_browser_hint);
        mRefreshLayout = (SmartRefreshLayout) findViewById(R.id.sl_browser_refresh);
        mBrowserView = (BrowserView) findViewById(R.id.wv_browser_view);
        // 设置网页刷新监听
        mRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mBrowserView.setBrowserViewClient(new MyBrowserViewClient());
        mBrowserView.loadUrl(getString(IntentKey.URL));
        showLoading();
    }


    @Override
    public StatusLayout getStatusLayout() {
        return mStatusLayout;
    }

    /**
     * 重新加载当前页
     */
    @CheckNet
    private void reload() {
        mBrowserView.reload();
    }

    /**
     * {@link OnRefreshListener}
     */

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        reload();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBrowserView.onDestroy();
    }

    private class MyBrowserViewClient extends BrowserView.BrowserViewClient {

        /**
         * 网页加载错误时回调，这个方法会在 onPageFinished 之前调用
         */
        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            // 这里为什么要用延迟呢？因为加载出错之后会先调用 onReceivedError 再调用 onPageFinished
            post(() -> showError(v -> reload()));
        }

        /**
         * 开始加载网页
         */
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
        }

        /**
         * 完成加载网页
         */
        @Override
        public void onPageFinished(WebView view, String url) {
            mRefreshLayout.finishRefresh();
            showComplete();
        }

        /**
         * 跳转到其他链接
         */
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, final String url) {
            String scheme = Uri.parse(url).getScheme();
            if (scheme == null) {
                return true;
            }
            switch (scheme.toLowerCase()) {
                // 如果这是跳链接操作
                case "http":
                case "https":
                    BrowserActivity.start(getAttachActivity(), url);
                    break;
                default:
                    break;
            }
            // 已经处理该链接请求
            return true;
        }
    }
}