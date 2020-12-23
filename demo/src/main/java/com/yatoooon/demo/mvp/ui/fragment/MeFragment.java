package com.yatoooon.demo.mvp.ui.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

import com.tencent.bugly.crashreport.CrashReport;
import com.yatoooon.baselibrary.base.BaseActivity;
import com.yatoooon.demo.R;
import com.yatoooon.demo.app.common.MyFragment;
import com.yatoooon.demo.mvp.ui.activity.AboutActivity;
import com.yatoooon.demo.mvp.ui.activity.BrowserActivity;
import com.yatoooon.demo.mvp.ui.activity.DialogActivity;
import com.yatoooon.demo.mvp.ui.activity.GuideActivity;
import com.yatoooon.demo.mvp.ui.activity.ImagePreviewActivity;
import com.yatoooon.demo.mvp.ui.activity.ImageSelectActivity;
import com.yatoooon.demo.mvp.ui.activity.LoginActivity;
import com.yatoooon.demo.mvp.ui.activity.PasswordForgetActivity;
import com.yatoooon.demo.mvp.ui.activity.PasswordResetActivity;
import com.yatoooon.demo.mvp.ui.activity.PersonInfoActivity;
import com.yatoooon.demo.mvp.ui.activity.PhoneChangeActivity;
import com.yatoooon.demo.mvp.ui.activity.RegisterActivity;
import com.yatoooon.demo.mvp.ui.activity.SettingActivity;
import com.yatoooon.demo.mvp.ui.activity.StatusActivity;
import com.yatoooon.demo.mvp.ui.activity.VideoPlayActivity;
import com.yatoooon.demo.mvp.ui.activity.VideoSelectActivity;

import java.util.ArrayList;
import java.util.List;

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


    public static MeFragment newInstance() {
        Bundle args = new Bundle();
        MeFragment fragment = new MeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_me;
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

    @OnClick({R.id.btn_me_dialog, R.id.btn_me_hint, R.id.btn_me_login, R.id.btn_me_register, R.id.btn_me_forget, R.id.btn_me_reset, R.id.btn_me_change, R.id.btn_me_personal, R.id.btn_message_setting, R.id.btn_me_about, R.id.btn_me_guide, R.id.btn_me_browser, R.id.btn_me_image_select, R.id.btn_me_image_preview, R.id.btn_me_video_select, R.id.btn_me_video_play, R.id.btn_me_crash})
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
                startActivity(SettingActivity.class);
                break;
            case R.id.btn_me_about:
                startActivity(AboutActivity.class);
                break;
            case R.id.btn_me_guide:
                startActivity(GuideActivity.class);
                break;
            case R.id.btn_me_browser:
                BrowserActivity.start(getAttachActivity(), "https://www.baidu.com");
                break;
            case R.id.btn_me_image_select:
                ImageSelectActivity.start((BaseActivity) getAttachActivity(), new ImageSelectActivity.OnPhotoSelectListener() {
                    @Override
                    public void onSelected(List<String> data) {
                        toast("选择了" + data.toString());
                    }

                    @Override
                    public void onCancel() {
                        toast("取消了");
                    }
                });
                break;
            case R.id.btn_me_image_preview:
                ArrayList<String> images = new ArrayList<>();
                images.add("https://www.baidu.com/img/bd_logo.png");
                images.add("https://avatars1.githubusercontent.com/u/28616817");
                ImagePreviewActivity.start(getAttachActivity(), images, images.size() - 1);
                break;
            case R.id.btn_me_video_select:
                VideoSelectActivity.start((BaseActivity) getAttachActivity(), new VideoSelectActivity.OnVideoSelectListener() {
                    @Override
                    public void onSelected(List<VideoSelectActivity.VideoBean> data) {
                        toast("选择了" + data.toString());
                    }
                    @Override
                    public void onCancel() {
                        toast("取消了");
                    }
                });
                break;
            case R.id.btn_me_video_play:
                VideoPlayActivity.start(getAttachActivity(), "http://vfx.mtime.cn/Video/2019/06/29/mp4/190629004821240734.mp4", "速度与激情特别行动");
                break;
            case R.id.btn_me_crash:
                // 关闭 Bugly 异常捕捉
                CrashReport.closeBugly();
                throw new IllegalStateException("are you ok?");
        }
    }
}
