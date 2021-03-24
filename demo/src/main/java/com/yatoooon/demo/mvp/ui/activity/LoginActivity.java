package com.yatoooon.demo.mvp.ui.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.yatoooon.baselibrary.di.component.AppComponent;
import com.yatoooon.baselibrary.http.imageloader.glide.ImageConfigImpl;
import com.yatoooon.baselibrary.utils.ArmsUtils;
import com.yatoooon.baselibrary.widget.view.ClearEditText;
import com.yatoooon.baselibrary.widget.view.PasswordEditText;
import com.yatoooon.baselibrary.widget.view.ScaleImageView;
import com.yatoooon.demo.R;
import com.yatoooon.demo.app.aop.DebugLog;
import com.yatoooon.demo.app.aop.SingleClick;
import com.yatoooon.demo.app.app.AppActivity;
import com.yatoooon.demo.app.manager.InputTextManager;
import com.yatoooon.demo.app.other.IntentKey;
import com.yatoooon.demo.app.other.KeyboardWatcher;
import com.yatoooon.demo.app.widget.SubmitButton;
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


public class LoginActivity extends AppActivity<LoginPresenter> implements UmengLogin.OnLoginListener, LoginContract.View, KeyboardWatcher.SoftKeyboardStateListener, TextView.OnEditorActionListener {

    @BindView(R.id.iv_login_logo)
    AppCompatImageView ivLoginLogo;
    @BindView(R.id.et_login_phone)
    ClearEditText etLoginPhone;
    @BindView(R.id.et_login_password)
    PasswordEditText etLoginPassword;
    @BindView(R.id.tv_login_forget)
    AppCompatTextView tvLoginForget;
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
    @BindView(R.id.btn_login_commit)
    SubmitButton btnLoginCommit;

    @DebugLog
    public static void start(Context context, String phone, String password) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra(IntentKey.PHONE, phone);
        intent.putExtra(IntentKey.PASSWORD, password);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

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
        etLoginPassword.setOnEditorActionListener(this);
        InputTextManager.with(this)
                .addView(etLoginPhone)
                .addView(etLoginPassword)
                .setMain(btnLoginCommit)
                .build();
        postDelayed(() -> {
            KeyboardWatcher.with(LoginActivity.this)
                    .setListener(LoginActivity.this);
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
        // 跳转到注册界面
        RegisterActivity.start(this, etLoginPhone.getText().toString(), etLoginPassword.getText().toString(), (phone, password) -> {
            // 如果已经注册成功，就执行登录操作
            etLoginPhone.setText(phone);
            etLoginPassword.setText(password);
            etLoginPassword.requestFocus();
            etLoginPassword.setSelection(etLoginPassword.getText().length());
            onClick(btnLoginCommit);
        });
    }

    @SingleClick
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
                // 隐藏软键盘
                hideKeyboard(getCurrentFocus());
                btnLoginCommit.showProgress();
                postDelayed(() -> {
                    hideDialog();
                    btnLoginCommit.showSucceed();
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
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(llLoginBody, "translationY", 0, -btnLoginCommit.getHeight());
            objectAnimator.setDuration(mAnimTime);
            objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            objectAnimator.start();

            // 执行缩小动画
            ivLoginLogo.setPivotX(ivLoginLogo.getWidth() / 2f);
            ivLoginLogo.setPivotY(ivLoginLogo.getHeight());
            AnimatorSet animatorSet = new AnimatorSet();
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(ivLoginLogo, "scaleX", 1.0f, mLogoScale);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(ivLoginLogo, "scaleY", 1.0f, mLogoScale);
            ObjectAnimator translationY = ObjectAnimator.ofFloat(ivLoginLogo, "translationY", 0.0f, -btnLoginCommit.getHeight());
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


    /**
     * {@link TextView.OnEditorActionListener}
     */
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE && btnLoginCommit.isEnabled()) {
            // 模拟点击登录按钮
            onClick(btnLoginCommit);
            return true;
        }
        return false;
    }

    /**
     * 授权成功的回调
     *
     * @param platform 平台名称
     * @param data     用户资料返回
     */
    @Override
    public void onSucceed(Platform platform, UmengLogin.LoginData data) {
        if (isFinishing() || isDestroyed()) {
            // Glide：You cannot start a load for a destroyed activity
            return;
        }
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
                .loadImage(getContext(), ImageConfigImpl.builder()
                        .res(data.getAvatar())
                        .imageView(ivLoginLogo)
                        .build());
        toast("昵称：" + data.getName() + "\n" + "性别：" + data.getSex());
        toast("id：" + data.getId());
        toast("token：" + data.getToken());
    }

    /**
     * 授权失败的回调
     *
     * @param platform 平台名称
     * @param t        错误原因
     */
    @Override
    public void onError(Platform platform, Throwable t) {
        toast("第三方登录出错：" + t.getMessage());
    }

    /**
     * 授权取消的回调
     *
     * @param platform 平台名称
     */
    @Override
    public void onCancel(Platform platform) {
        toast("取消第三方登录");
    }

}
