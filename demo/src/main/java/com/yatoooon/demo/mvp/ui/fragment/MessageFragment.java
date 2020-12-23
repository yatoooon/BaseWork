package com.yatoooon.demo.mvp.ui.fragment;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;

import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.yatoooon.baselibrary.http.imageloader.glide.ImageConfigImpl;
import com.yatoooon.baselibrary.utils.ArmsUtils;
import com.yatoooon.demo.R;
import com.yatoooon.demo.app.aop.SingleClick;
import com.yatoooon.demo.app.common.MyActivity;
import com.yatoooon.demo.app.common.MyFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MessageFragment extends MyFragment {
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
    @OnClick({R.id.btn_message_image1, R.id.btn_message_image2, R.id.btn_message_image3, R.id.btn_message_toast, R.id.btn_message_permission, R.id.btn_message_setting, R.id.btn_message_black, R.id.btn_message_white})
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
                                .transformation(new RoundedCorners((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, this.getResources().getDisplayMetrics())))
                                .res("https://www.baidu.com/img/bd_logo.png")
                                .imageView(ivMessageImage).isCircle(true).build());

                break;
            case R.id.btn_message_toast:
                toast("我是吐司");
                break;
            case R.id.btn_message_permission:
                XXPermissions.with(getAttachActivity())
                        // 可设置被拒绝后继续申请，直到用户授权或者永久拒绝
                        //.constantRequest()
                        // 支持请求6.0悬浮窗权限8.0请求安装权限
                        //.permission(Permission.SYSTEM_ALERT_WINDOW, Permission.REQUEST_INSTALL_PACKAGES)
                        // 不指定权限则自动获取清单中的危险权限
                        .permission(Permission.CAMERA)
                        .request(new OnPermission() {

                            @Override
                            public void hasPermission(List<String> granted, boolean isAll) {
                                if (isAll) {
                                    toast("获取权限成功");
                                } else {
                                    toast("获取权限成功，部分权限未正常授予");
                                }
                            }

                            @Override
                            public void noPermission(List<String> denied, boolean quick) {
                                if(quick) {
                                    toast("被永久拒绝授权，请手动授予权限");
                                    //如果是被永久拒绝就跳转到应用权限系统设置页面
                                    XXPermissions.startPermissionActivity(getAttachActivity());
                                } else {
                                    toast("获取权限失败");
                                }
                            }
                        });
                break;
            case R.id.btn_message_setting:
                XXPermissions.startPermissionActivity(getAttachActivity());
                break;
            case R.id.btn_message_black:
                if (getAttachActivity() instanceof MyActivity){
                    ((MyActivity) getAttachActivity()).getStatusBarConfig().statusBarDarkFont(true).init();
                }
                break;
            case R.id.btn_message_white:
                if (getAttachActivity() instanceof MyActivity){
                    ((MyActivity) getAttachActivity()).getStatusBarConfig().statusBarDarkFont(false).init();
                }
                break;
        }
    }

    @Override
    public boolean isStatusBarEnabled() {
        // 使用沉浸式状态栏
        return !super.isStatusBarEnabled();
    }
}
