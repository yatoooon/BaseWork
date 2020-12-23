package com.yatoooon.demo.mvp.ui.activity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.yatoooon.baselibrary.base.BaseDialog;
import com.yatoooon.baselibrary.di.component.AppComponent;
import com.yatoooon.baselibrary.http.imageloader.glide.ImageConfigImpl;
import com.yatoooon.baselibrary.utils.ArmsUtils;
import com.yatoooon.baselibrary.widget.layout.SettingBar;
import com.yatoooon.baselibrary.widget.view.SwitchButton;
import com.yatoooon.demo.R;
import com.yatoooon.demo.app.common.MyActivity;
import com.yatoooon.demo.app.dialog.MenuDialog;
import com.yatoooon.demo.app.dialog.SafeDialog;
import com.yatoooon.demo.app.dialog.UpdateDialog;
import com.yatoooon.demo.app.helper.ActivityStackManager;
import com.yatoooon.demo.app.helper.CacheDataManager;
import com.yatoooon.demo.app.other.AppConfig;
import com.yatoooon.demo.di.component.DaggerSettingComponent;
import com.yatoooon.demo.mvp.contract.SettingContract;
import com.yatoooon.demo.mvp.presenter.SettingPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SettingActivity extends MyActivity<SettingPresenter> implements SettingContract.View, SwitchButton.OnCheckedChangeListener {

    @BindView(R.id.sb_setting_language)
    SettingBar sbSettingLanguage;
    @BindView(R.id.sb_setting_update)
    SettingBar sbSettingUpdate;
    @BindView(R.id.sb_setting_phone)
    SettingBar sbSettingPhone;
    @BindView(R.id.sb_setting_password)
    SettingBar sbSettingPassword;
    @BindView(R.id.sb_setting_agreement)
    SettingBar sbSettingAgreement;
    @BindView(R.id.sb_setting_about)
    SettingBar sbSettingAbout;
    @BindView(R.id.sb_setting_switch)
    SwitchButton sbSettingSwitch;
    @BindView(R.id.sb_setting_auto)
    SettingBar sbSettingAuto;
    @BindView(R.id.sb_setting_cache)
    SettingBar sbSettingCache;
    @BindView(R.id.sb_setting_exit)
    SettingBar sbSettingExit;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerSettingComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        sbSettingSwitch.setOnCheckedChangeListener(this);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        sbSettingCache.setRightText(CacheDataManager.getTotalCacheSize(this));
        sbSettingLanguage.setRightText("简体中文");
        sbSettingPhone.setRightText("181****1413");
        sbSettingPassword.setRightText("密码强度较低");
    }


    @OnClick({R.id.sb_setting_language, R.id.sb_setting_update, R.id.sb_setting_phone, R.id.sb_setting_password, R.id.sb_setting_agreement, R.id.sb_setting_about, R.id.sb_setting_auto, R.id.sb_setting_cache, R.id.sb_setting_exit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.sb_setting_language:
                // 底部选择框
                new MenuDialog.Builder(this)
                        // 设置点击按钮后不关闭对话框
                        //.setAutoDismiss(false)
                        .setList(R.string.setting_language_simple, R.string.setting_language_complex)
                        .setListener((MenuDialog.OnListener<String>) (dialog, position, string) -> {
                            sbSettingLanguage.setRightText(string);
                            BrowserActivity.start(getActivity(), "https://www.baidu.com");
                        })
                        .setGravity(Gravity.BOTTOM)
                        .setAnimStyle(BaseDialog.ANIM_BOTTOM)
                        .show();
                break;
            case R.id.sb_setting_update:
                // 本地的版本码和服务器的进行比较
                if (20 < AppConfig.getVersionCode()) {
                    new UpdateDialog.Builder(this)
                            // 版本名
                            .setVersionName("2.0")
                            // 是否强制更新
                            .setForceUpdate(false)
                            // 更新日志
                            .setUpdateLog("修复Bug\n优化用户体验")
                            // 下载 url
                            .setDownloadUrl("https://raw.githubusercontent.com/getActivity/AndroidProject/master/AndroidProject.apk")
                            .show();
                } else {
                    toast(R.string.update_no_update);
                }
                break;
            case R.id.sb_setting_phone:
                new SafeDialog.Builder(this)
                        .setListener((dialog, phone, code) -> PhoneChangeActivity.start(getActivity(), code))
                        .show();
                break;
            case R.id.sb_setting_password:
                new SafeDialog.Builder(this)
                        .setListener((dialog, phone, code) -> PasswordResetActivity.start(getActivity(), phone, code))
                        .show();
                break;
            case R.id.sb_setting_agreement:
                BrowserActivity.start(this, "https://github.com/getActivity/Donate");
                break;
            case R.id.sb_setting_about:
                startActivity(AboutActivity.class);
                break;
            case R.id.sb_setting_auto:
                // 自动登录
                sbSettingSwitch.setChecked(!sbSettingSwitch.isChecked());
                break;
            case R.id.sb_setting_cache:
                ArmsUtils.obtainAppComponentFromContext(getActivity()).imageLoader().clear(getActivity(), ImageConfigImpl.builder().build());
                new Thread(() -> {
                    CacheDataManager.clearAllCache(this);
                    post(() -> {
                        sbSettingCache.setRightText(CacheDataManager.getTotalCacheSize(getActivity()));
                    });
                }).start();
                break;
            case R.id.sb_setting_exit:
                startActivity(LoginActivity.class);
                // 进行内存优化，销毁除登录页之外的所有界面
                ActivityStackManager.getInstance().finishAllActivities(LoginActivity.class);
                break;
        }
    }

    @Override
    public void onCheckedChanged(SwitchButton button, boolean isChecked) {

    }
}
