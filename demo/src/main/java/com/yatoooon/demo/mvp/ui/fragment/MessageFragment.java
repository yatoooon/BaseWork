package com.yatoooon.demo.mvp.ui.fragment;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;

import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.scwang.smart.refresh.layout.util.SmartUtil;
import com.yatoooon.baselibrary.http.imageloader.glide.ImageConfigImpl;
import com.yatoooon.baselibrary.utils.ArmsUtils;
import com.yatoooon.demo.R;
import com.yatoooon.demo.app.aop.Permissions;
import com.yatoooon.demo.app.aop.SingleClick;
import com.yatoooon.demo.app.app.AppActivity;
import com.yatoooon.demo.app.app.AppFragment;
import com.yatoooon.demo.mvp.ui.activity.HomeActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MessageFragment extends AppFragment {
    @BindView(R.id.iv_message_image)
    AppCompatImageView ivMessageImage;
    @BindView(R.id.btn_message_image1)
    AppCompatButton btnMessageImage1;
    @BindView(R.id.btn_message_image2)
    AppCompatButton btnMessageImage2;
    @BindView(R.id.btn_message_image3)
    AppCompatButton btnMessageImage3;
    @BindView(R.id.btn_message_toast)
    AppCompatButton btnMessageToast;
    @BindView(R.id.btn_message_permission)
    AppCompatButton btnMessagePermission;
    @BindView(R.id.btn_message_setting)
    AppCompatButton btnMessageSetting;
    @BindView(R.id.btn_message_black)
    AppCompatButton btnMessageBlack;
    @BindView(R.id.btn_message_white)
    AppCompatButton btnMessageWhite;
    @BindView(R.id.btn_message_tab)
    AppCompatButton btnMessageTab;

    public static MessageFragment newInstance() {
        Bundle args = new Bundle();
        MessageFragment fragment = new MessageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_message;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }

    @SingleClick
    @OnClick({R.id.btn_message_image1, R.id.btn_message_image2, R.id.btn_message_image3, R.id.btn_message_toast, R.id.btn_message_permission, R.id.btn_message_setting, R.id.btn_message_black, R.id.btn_message_white, R.id.btn_message_tab})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_message_image1:
                ivMessageImage.setVisibility(View.VISIBLE);
                ArmsUtils.obtainAppComponentFromContext(getContext())
                        .imageLoader().loadImage(getContext(),
                        ImageConfigImpl.builder()
                                .res("https://www.baidu.com/img/bd_logo.png")
                                .imageView(ivMessageImage).build());

                break;
            case R.id.btn_message_image2:
                ivMessageImage.setVisibility(View.VISIBLE);
                ArmsUtils.obtainAppComponentFromContext(getContext())
                        .imageLoader().loadImage(getContext(),
                        ImageConfigImpl.builder()
                                .res("https://www.baidu.com/img/bd_logo.png")
                                .imageView(ivMessageImage).isCircle(true).build());
                break;
            case R.id.btn_message_image3:
                ivMessageImage.setVisibility(View.VISIBLE);
                ArmsUtils.obtainAppComponentFromContext(getContext())
                        .imageLoader().loadImage(getContext(),
                        ImageConfigImpl.builder()
                                .imageRadius(SmartUtil.dp2px(10))
                                .res("https://www.baidu.com/img/bd_logo.png")
                                .imageView(ivMessageImage).build());

                break;
            case R.id.btn_message_toast:
                toast("我是吐司");
                break;
            case R.id.btn_message_permission:
                requestPermission();
                break;
            case R.id.btn_message_setting:
                XXPermissions.startApplicationDetails(this);
                break;
            case R.id.btn_message_black:
                if (getAttachActivity() instanceof AppActivity) {
                    ((AppActivity) getAttachActivity()).getStatusBarConfig().statusBarDarkFont(true).init();
                }
                break;
            case R.id.btn_message_white:
                if (getAttachActivity() instanceof AppActivity) {
                    ((AppActivity) getAttachActivity()).getStatusBarConfig().statusBarDarkFont(false).init();
                }
                break;
            case R.id.btn_message_tab:
                HomeActivity.start(getActivity(), HomeFragment.class);
                break;
        }
    }

    @Override
    public boolean isStatusBarEnabled() {
        // 使用沉浸式状态栏
        return !super.isStatusBarEnabled();
    }

    @OnClick(R.id.btn_message_tab)
    public void onViewClicked() {
    }

    @Permissions(Permission.CAMERA)
    private void requestPermission() {
        toast("获取摄像头权限成功");
    }
}
