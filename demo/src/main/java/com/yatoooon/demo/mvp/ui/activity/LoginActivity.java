package com.yatoooon.demo.mvp.ui.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.yatoooon.baselibrary.di.component.AppComponent;
import com.yatoooon.baselibrary.http.imageloader.glide.ImageConfigImpl;
import com.yatoooon.baselibrary.utils.ArmsUtils;
import com.yatoooon.baselibrary.widget.view.ClearEditText;
import com.yatoooon.baselibrary.widget.view.PasswordEditText;
import com.yatoooon.baselibrary.widget.view.ScaleImageView;
import com.yatoooon.demo.R;
import com.yatoooon.demo.app.common.MyActivity;
import com.yatoooon.demo.app.helper.InputTextHelper;
import com.yatoooon.demo.app.other.IntentKey;
import com.yatoooon.demo.app.other.KeyboardWatcher;
import com.yatoooon.demo.di.component.DaggerLoginComponent;
import com.yatoooon.demo.mvp.contract.LoginContract;
import com.yatoooon.demo.mvp.presenter.LoginPresenter;
import com.yatoooon.demo.wxapi.WXEntryActivity;
import com.yatoooon.umeng.Platform;
import com.yatoooon.umeng.UmengClient;
import com.yatoooon.umeng.UmengLogin;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class LoginActivity extends MyActivity<LoginPresenter> implements LoginContract.View, KeyboardWatcher.SoftKeyboardStateListener {

    @BindView(R.id.iv_login_logo)
    AppCompatImageView ivLoginLogo;
    @BindView(R.id.et_login_phone)
    ClearEditText etLoginPhone;
    @BindView(R.id.et_login_password)
    PasswordEditText etLoginPassword;
    @BindView(R.id.tv_login_forget)
    AppCompatTextView tvLoginForget;
    @BindView(R.id.btn_login_commit)
    AppCompatButton btnLoginCommit;
    @BindView(R.id.ll_login_body)
    LinearLayout llLoginBody;
    @BindView(R.id.v_login_blank)
    View vLoginBlank;
    @BindView(R.id.ll_login_other)
    LinearLayout llLoginOther;
    @BindView(R.id.iv_login_qq)
    ScaleImageView ivLoginQq;
    @BindView(R.id.iv_login_wechat)
    ScaleImageView ivLoginWechat;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerLoginComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        InputTextHelper.with(this)
                .addView(etLoginPhone)
                .addView(etLoginPassword)
                .setMain(btnLoginCommit)
                .build();
        postDelayed(() -> {
            // 因为在小屏幕手机上面，因为计算规则的因素会导致动画效果特别夸张，所以不在小屏幕手机上面展示这个动画效果
            if (vLoginBlank.getHeight() > llLoginBody.getHeight()) {
                // 只有空白区域的高度大于登录框区域的高度才展示动画
                KeyboardWatcher.with(LoginActivity.this).setListener(LoginActivity.this);
            }
        }, 500);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        // 判断用户当前有没有安装 QQ
        if (!UmengClient.isAppInstalled(this, Platform.QQ)) {
            ivLoginQq.setVisibility(View.GONE);
        }

        // 判断用户当前有没有安装微信
        if (!UmengClient.isAppInstalled(this, Platform.WECHAT)) {
            ivLoginWechat.setVisibility(View.GONE);
        }

        // 如果这两个都没有安装就隐藏提示
        if (ivLoginQq.getVisibility() == View.GONE && ivLoginWechat.getVisibility() == View.GONE) {
            llLoginOther.setVisibility(View.GONE);
        }

        // 填充传入的手机号和密码
        etLoginPhone.setText(getString(IntentKey.PHONE));
        etLoginPassword.setText(getString(IntentKey.PASSWORD));

    }

    @Override
    public void onRightClick(View v) {
        // 跳转到注册界面
        startActivityForResult(RegisterActivity.class, (resultCode, data) -> {
            // 如果已经注册成功，就执行登录操作
            if (resultCode == RESULT_OK && data != null) {
                etLoginPhone.setText(data.getStringExtra(IntentKey.PHONE));
                etLoginPassword.setText(data.getStringExtra(IntentKey.PASSWORD));
                etLoginPassword.setSelection(etLoginPassword.getText().length());
                onClick(btnLoginCommit);
            }
        });
    }

    @OnClick({R.id.tv_login_forget, R.id.btn_login_commit, R.id.iv_login_qq, R.id.iv_login_wechat})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_login_forget:
                startActivity(PasswordForgetActivity.class);
                break;
            case R.id.btn_login_commit:
                if (etLoginPhone.getText().toString().length() != 11) {
                    toast(R.string.common_phone_input_error);
                    return;
                }
                showDialog();
                postDelayed(() -> {
                    hideDialog();
                    startActivity(HomeActivity.class);
                    finish();
                }, 2000);
                break;
            case R.id.iv_login_qq:
                unmengLogin(Platform.QQ);
                break;
            case R.id.iv_login_wechat:
                unmengLogin(Platform.WECHAT);
                toast("也别忘了改微信 " + WXEntryActivity.class.getSimpleName() + " 类所在的包名哦");
                break;
        }
    }

    private void unmengLogin(Platform platform) {
        toast("记得改好第三方 AppID 和 AppKey，否则会调不起来哦");
        UmengClient.login(this, platform, new UmengLogin.OnLoginListener() {
            @Override
            public void onSucceed(Platform platform, UmengLogin.LoginData data) {
                // 判断第三方登录的平台
                switch (platform) {
                    case QQ:
                        break;
                    case WECHAT:
                        break;
                    default:
                        break;
                }
                ArmsUtils.obtainAppComponentFromContext(getContext()).imageLoader()
                        .loadImage(getContext(),
                                ImageConfigImpl.builder().res(data.getAvatar()).imageView(ivLoginLogo).isCircle(true).build());
                toast("昵称：" + data.getName() + "\n" + "性别：" + data.getSex());
                toast("id：" + data.getId());
                toast("token：" + data.getToken());
            }

            @Override
            public void onError(Platform platform, Throwable t) {
                toast("第三方登录出错：" + t.getMessage());
            }

            @Override
            public void onCancel(Platform platform) {
                toast("取消第三方登录");
            }
        });
    }

    /**
     * {@link KeyboardWatcher.SoftKeyboardStateListener}
     */


    /**
     * logo 缩放比例
     */
    private final float mLogoScale = 0.8f;
    /**
     * 动画时间
     */
    private final int mAnimTime = 300;

    @Override
    public void onSoftKeyboardOpened(int keyboardHeight) {
        int screenHeight = getResources().getDisplayMetrics().heightPixels;
        int[] location = new int[2];
        // 获取这个 View 在屏幕中的坐标（左上角）
        llLoginBody.getLocationOnScreen(location);
        //int x = location[0];
        int y = location[1];
        int bottom = screenHeight - (y + llLoginBody.getHeight());
        if (keyboardHeight > bottom) {
            // 执行位移动画
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(llLoginBody, "translationY", 0, -(keyboardHeight - bottom));
            objectAnimator.setDuration(mAnimTime);
            objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            objectAnimator.start();

            // 执行缩小动画
            ivLoginLogo.setPivotX(ivLoginLogo.getWidth() / 2f);
            ivLoginLogo.setPivotY(ivLoginLogo.getHeight());
            AnimatorSet animatorSet = new AnimatorSet();
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(ivLoginLogo, "scaleX", 1.0f, mLogoScale);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(ivLoginLogo, "scaleY", 1.0f, mLogoScale);
            ObjectAnimator translationY = ObjectAnimator.ofFloat(ivLoginLogo, "translationY", 0.0f, -(keyboardHeight - bottom));
            animatorSet.play(translationY).with(scaleX).with(scaleY);
            animatorSet.setDuration(mAnimTime);
            animatorSet.start();
        }
    }

    @Override
    public void onSoftKeyboardClosed() {
        // 执行位移动画
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(llLoginBody, "translationY", llLoginBody.getTranslationY(), 0);
        objectAnimator.setDuration(mAnimTime);
        objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        objectAnimator.start();

        if (ivLoginLogo.getTranslationY() == 0) {
            return;
        }
        // 执行放大动画
        ivLoginLogo.setPivotX(ivLoginLogo.getWidth() / 2f);
        ivLoginLogo.setPivotY(ivLoginLogo.getHeight());
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(ivLoginLogo, "scaleX", mLogoScale, 1.0f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(ivLoginLogo, "scaleY", mLogoScale, 1.0f);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(ivLoginLogo, "translationY", ivLoginLogo.getTranslationY(), 0);
        animatorSet.play(translationY).with(scaleX).with(scaleY);
        animatorSet.setDuration(mAnimTime);
        animatorSet.start();
    }


}
