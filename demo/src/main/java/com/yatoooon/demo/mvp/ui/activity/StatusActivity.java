package com.yatoooon.demo.mvp.ui.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.yatoooon.demo.R;
import com.yatoooon.demo.app.action.StatusAction;
import com.yatoooon.demo.app.common.AppActivity;
import com.yatoooon.demo.mvp.ui.dialog.MenuDialog;
import com.yatoooon.demo.app.widget.StatusLayout;

import butterknife.BindView;

public class StatusActivity extends AppActivity implements StatusAction {
    @BindView(R.id.hl_status_hint)
    StatusLayout hlStatusHint;

    @Override
    public int getLayoutId() {
        return R.layout.activity_status;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        new MenuDialog.Builder(this)
                //.setAutoDismiss(false) // 设置点击按钮后不关闭对话框
                .setList("加载中", "请求错误", "空数据提示", "自定义提示")
                .setListener((dialog, position, object) -> {
                    switch (position) {
                        case 0:
                            showLoading();
                            postDelayed(this::showComplete, 2500);
                            break;
                        case 1:
                            showError(v -> {
                                showLoading();
                                postDelayed(this::showEmpty, 2500);
                            });
                            break;
                        case 2:
                            showEmpty();
                            break;
                        case 3:
                            showLayout(ContextCompat.getDrawable(getActivity(), R.drawable.status_order_ic), "暂无订单", null);
                            break;
                        default:
                            break;
                    }
                })
                .show();
    }


    @Override
    public StatusLayout getStatusLayout() {
        return hlStatusHint;
    }
}
