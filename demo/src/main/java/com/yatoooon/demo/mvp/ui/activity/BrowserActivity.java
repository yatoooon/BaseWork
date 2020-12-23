package com.yatoooon.demo.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yatoooon.demo.R;
import com.yatoooon.demo.app.action.StatusAction;
import com.yatoooon.demo.app.aop.CheckNet;
import com.yatoooon.demo.app.aop.DebugLog;
import com.yatoooon.demo.app.common.MyActivity;
import com.yatoooon.demo.app.other.IntentKey;
import com.yatoooon.demo.app.widget.BrowserView;
import com.yatoooon.demo.app.widget.HintLayout;

import butterknife.BindView;

/**
 * author : Android 轮子哥
 * github : https://github.com/getActivity/AndroidProject
 * time   : 2018/10/18
 * desc   : 浏览器界面
 */
public final class BrowserActivity extends MyActivity
        implements StatusAction, OnRefreshListener {

    @BindView(R.id.pb_browser_progress)
    ProgressBar pbBrowserProgress;
    @BindView(R.id.wv_browser_view)
    BrowserView wvBrowserView;
    @BindView(R.id.sl_browser_refresh)
    SmartRefreshLayout slBrowserRefresh;
    @BindView(R.id.hl_browser_hint)
    HintLayout hlBrowserHint;

    @CheckNet
    @DebugLog
    public static void start(Context context, String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        Intent intent = new Intent(context, BrowserActivity.class);
        intent.putExtra(IntentKey.URL, url);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_browser;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        // 设置网页刷新监听
        slBrowserRefresh.setOnRefreshListener(this);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        showLoading();

        wvBrowserView.setBrowserViewClient(new MyBrowserViewClient());
        wvBrowserView.setBrowserChromeClient(new MyBrowserChromeClient(wvBrowserView));

        String url = getString(IntentKey.URL);
        wvBrowserView.loadUrl(url);
    }


    @Override
    public HintLayout getHintLayout() {
        return hlBrowserHint;
    }

    @Override
    public void onLeftClick(View v) {
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && wvBrowserView.canGoBack()) {
            // 后退网页并且拦截该事件
            wvBrowserView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        wvBrowserView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        wvBrowserView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        wvBrowserView.onDestroy();
        super.onDestroy();
    }

    /**
     * 重新加载当前页
     */
    @CheckNet
    private void reload() {
        wvBrowserView.reload();
    }

    /**
     * {@link OnRefreshListener}
     */

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        wvBrowserView.reload();
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
            pbBrowserProgress.setVisibility(View.VISIBLE);
        }

        /**
         * 完成加载网页
         */
        @Override
        public void onPageFinished(WebView view, String url) {
            pbBrowserProgress.setVisibility(View.GONE);
            slBrowserRefresh.finishRefresh();
            showComplete();
        }
    }

    private class MyBrowserChromeClient extends BrowserView.BrowserChromeClient {

        private MyBrowserChromeClient(BrowserView view) {
            super(view);
        }

        /**
         * 收到网页标题
         */
        @Override
        public void onReceivedTitle(WebView view, String title) {
            if (title != null) {
                setTitle(title);
            }
        }

        /**
         * 收到加载进度变化
         */
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            pbBrowserProgress.setProgress(newProgress);
        }
    }
}