package com.yatoooon.demo.mvp.ui.activity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.yatoooon.baselibrary.base.BaseDialog;
import com.yatoooon.baselibrary.di.component.AppComponent;
import com.yatoooon.baselibrary.http.imageloader.glide.ImageConfigImpl;
import com.yatoooon.baselibrary.utils.ArmsUtils;
import com.yatoooon.baselibrary.widget.layout.SettingBar;
import com.yatoooon.baselibrary.widget.view.SwitchButton;
import com.yatoooon.demo.R;
import com.yatoooon.demo.app.aop.SingleClick;
import com.yatoooon.demo.app.app.AppActivity;
import com.yatoooon.demo.app.manager.ThreadPoolManager;
import com.yatoooon.demo.mvp.ui.dialog.MenuDialog;
import com.yatoooon.demo.mvp.ui.dialog.SafeDialog;
import com.yatoooon.demo.mvp.ui.dialog.UpdateDialog;
import com.yatoooon.demo.app.manager.ActivityManager;
import com.yatoooon.demo.app.manager.CacheDataManager;
import com.yatoooon.demo.app.other.AppConfig;
import com.yatoooon.demo.di.component.DaggerSettingComponent;
import com.yatoooon.demo.mvp.contract.SettingContract;
import com.yatoooon.demo.mvp.presenter.SettingPresenter;

import butterknife.BindView;
import butterknife.OnClick;


public class SettingActivity extends AppActivity<SettingPresenter> implements SettingContract.View, SwitchButton.OnCheckedChangeListener {

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

    @SingleClick
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
                            .setDownloadUrl("https://down.qq.com/qqweb/QQ_1/android_apk/Android_8.5.0.5025_537066738.apk")
                            .setFileMd5("560017dc94e8f9b65f4ca997c7feb326")
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
                ThreadPoolManager.getInstance().execute(() -> {
                    CacheDataManager.clearAllCache(this);
                    post(() -> {
                        sbSettingCache.setRightText(CacheDataManager.getTotalCacheSize(getActivity()));
                    });
                });
                break;
            case R.id.sb_setting_exit:
                startActivity(LoginActivity.class);
                // 进行内存优化，销毁除登录页之外的所有界面
                ActivityManager.getInstance().finishAllActivities(LoginActivity.class);
                break;
        }
    }

    @Override
    public void onCheckedChanged(SwitchButton button, boolean isChecked) {
        toast(isChecked);
    }
}
