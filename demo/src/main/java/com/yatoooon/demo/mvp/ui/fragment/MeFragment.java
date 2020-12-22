package com.yatoooon.demo.mvp.ui.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

import com.yatoooon.demo.R;
import com.yatoooon.demo.app.common.MyFragment;
import com.yatoooon.demo.mvp.ui.activity.DialogActivity;
import com.yatoooon.demo.mvp.ui.activity.LoginActivity;
import com.yatoooon.demo.mvp.ui.activity.PasswordForgetActivity;
import com.yatoooon.demo.mvp.ui.activity.PasswordResetActivity;
import com.yatoooon.demo.mvp.ui.activity.PasswordResetActivity_ViewBinding;
import com.yatoooon.demo.mvp.ui.activity.PersonInfoActivity;
import com.yatoooon.demo.mvp.ui.activity.PhoneChangeActivity;
import com.yatoooon.demo.mvp.ui.activity.RegisterActivity;
import com.yatoooon.demo.mvp.ui.activity.StatusActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class MeFragment extends MyFragment {
    @BindView(R.id.btn_me_dialog)
    AppCompatButton btnMeDialog;
    @BindView(R.id.btn_me_hint)
    AppCompatButton btnMeHint;
    @BindView(R.id.btn_me_login)
    AppCompatButton btnMeLogin;
    @BindView(R.id.btn_me_register)
    AppCompatButton btnMeRegister;
    @BindView(R.id.btn_me_forget)
    AppCompatButton btnMeForget;
    @BindView(R.id.btn_me_reset)
    AppCompatButton btnMeReset;
    @BindView(R.id.btn_me_change)
    AppCompatButton btnMeChange;
    @BindView(R.id.btn_me_personal)
    AppCompatButton btnMePersonal;
    @BindView(R.id.btn_message_setting)
    AppCompatButton btnMessageSetting;
    @BindView(R.id.btn_me_about)
    AppCompatButton btnMeAbout;
    @BindView(R.id.btn_me_guide)
    AppCompatButton btnMeGuide;
    @BindView(R.id.btn_me_browser)
    AppCompatButton btnMeBrowser;
    @BindView(R.id.btn_me_image_select)
    AppCompatButton btnMeImageSelect;
    @BindView(R.id.btn_me_image_preview)
    AppCompatButton btnMeImagePreview;
    @BindView(R.id.btn_me_video_select)
    AppCompatButton btnMeVideoSelect;
    @BindView(R.id.btn_me_video_play)
    AppCompatButton btnMeVideoPlay;
    @BindView(R.id.btn_me_crash)
    AppCompatButton btnMeCrash;
    @BindView(R.id.btn_me_pay)
    AppCompatButton btnMePay;

    public static MeFragment newInstance() {
        Bundle args = new Bundle();
        MeFragment fragment = new MeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.me_fragment;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public boolean isStatusBarEnabled() {
        // 使用沉浸式状态栏w
        return !super.isStatusBarEnabled();
    }

    @OnClick({R.id.btn_me_dialog, R.id.btn_me_hint, R.id.btn_me_login, R.id.btn_me_register, R.id.btn_me_forget, R.id.btn_me_reset, R.id.btn_me_change, R.id.btn_me_personal, R.id.btn_message_setting, R.id.btn_me_about, R.id.btn_me_guide, R.id.btn_me_browser, R.id.btn_me_image_select, R.id.btn_me_image_preview, R.id.btn_me_video_select, R.id.btn_me_video_play, R.id.btn_me_crash, R.id.btn_me_pay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_me_dialog:
                startActivity(DialogActivity.class);
                break;
            case R.id.btn_me_hint:
                startActivity(StatusActivity.class);
                break;
            case R.id.btn_me_login:
                startActivity(LoginActivity.class);
                break;
            case R.id.btn_me_register:
                startActivity(RegisterActivity.class);
                break;
            case R.id.btn_me_forget:
                startActivity(PasswordForgetActivity.class);
                break;
            case R.id.btn_me_reset:
                startActivity(PasswordResetActivity.class);
                break;
            case R.id.btn_me_change:
                startActivity(PhoneChangeActivity.class);
                break;
            case R.id.btn_me_personal:
                startActivity(PersonInfoActivity.class);
                break;
            case R.id.btn_message_setting:
                break;
            case R.id.btn_me_about:
                break;
            case R.id.btn_me_guide:
                break;
            case R.id.btn_me_browser:
                break;
            case R.id.btn_me_image_select:
                break;
            case R.id.btn_me_image_preview:
                break;
            case R.id.btn_me_video_select:
                break;
            case R.id.btn_me_video_play:
                break;
            case R.id.btn_me_crash:
                break;
            case R.id.btn_me_pay:
                break;
        }
    }
}
