package com.yatoooon.demo.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import com.gyf.immersionbar.ImmersionBar;
import com.yatoooon.baselibrary.base.BaseActivity;
import com.yatoooon.baselibrary.di.component.AppComponent;
import com.yatoooon.baselibrary.widget.view.CountdownView;
import com.yatoooon.baselibrary.widget.view.PasswordEditText;
import com.yatoooon.baselibrary.widget.view.RegexEditText;
import com.yatoooon.demo.R;
import com.yatoooon.demo.app.aop.DebugLog;
import com.yatoooon.demo.app.aop.SingleClick;
import com.yatoooon.demo.app.common.AppActivity;
import com.yatoooon.demo.app.manager.InputTextManager;
import com.yatoooon.demo.app.other.IntentKey;
import com.yatoooon.demo.app.widget.SubmitButton;
import com.yatoooon.demo.di.component.DaggerRegisterComponent;
import com.yatoooon.demo.mvp.contract.RegisterContract;
import com.yatoooon.demo.mvp.presenter.RegisterPresenter;

import butterknife.BindView;
import butterknife.OnClick;


public class RegisterActivity extends AppActivity<RegisterPresenter> implements RegisterContract.View, TextView.OnEditorActionListener {

    @BindView(R.id.tv_register_title)
    AppCompatTextView tvRegisterTitle;
    @BindView(R.id.et_register_phone)
    RegexEditText etRegisterPhone;
    @BindView(R.id.cv_register_countdown)
    CountdownView cvRegisterCountdown;
    @BindView(R.id.et_register_code)
    AppCompatEditText etRegisterCode;
    @BindView(R.id.et_register_password1)
    PasswordEditText etRegisterPassword1;
    @BindView(R.id.et_register_password2)
    PasswordEditText etRegisterPassword2;
    @BindView(R.id.btn_register_commit)
    SubmitButton btnRegisterCommit;


    @DebugLog
    public static void start(BaseActivity activity, String phone, String password, OnRegisterListener listener) {
        Intent intent = new Intent(activity, RegisterActivity.class);
        intent.putExtra(IntentKey.PHONE, phone);
        intent.putExtra(IntentKey.PASSWORD, password);
        activity.startActivityForResult(intent, (resultCode, data) -> {
            if (listener == null || data == null) {
                return;
            }
            if (resultCode == RESULT_OK) {
                listener.onSucceed(data.getStringExtra(IntentKey.PHONE), data.getStringExtra(IntentKey.PASSWORD));
            } else {
                listener.onCancel();
            }
        });
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerRegisterComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        // 给这个 View 设置沉浸式，避免状态栏遮挡
        ImmersionBar.setTitleBar(this, findViewById(R.id.tv_register_title));
        etRegisterPassword2.setOnEditorActionListener(this);
        InputTextManager.with(this)
                .addView(etRegisterPhone)
                .addView(etRegisterCode)
                .addView(etRegisterPassword1)
                .addView(etRegisterPassword2)
                .setMain(btnRegisterCommit)
                .build();
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        // 自动填充手机号和密码
        etRegisterPhone.setText(getString(IntentKey.PHONE));
        etRegisterPassword1.setText(getString(IntentKey.PASSWORD));
        etRegisterPassword2.setText(getString(IntentKey.PASSWORD));
    }

    @SingleClick
    @OnClick({R.id.cv_register_countdown, R.id.btn_register_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cv_register_countdown:
                if (etRegisterPhone.getText().toString().length() != 11) {
                    etRegisterPhone.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.shake_anim));
                    toast(R.string.common_phone_input_error);
                    return;
                }

                toast(R.string.common_code_send_hint);
                cvRegisterCountdown.start();
                break;
            case R.id.btn_register_commit:
                if (etRegisterPhone.getText().toString().length() != 11) {
                    etRegisterPhone.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.shake_anim));
                    btnRegisterCommit.showError(3000);
                    toast(R.string.common_phone_input_error);
                    return;
                }
                if (etRegisterCode.getText().toString().length() != getResources().getInteger(R.integer.sms_code_length)) {
                    etRegisterCode.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.shake_anim));
                    btnRegisterCommit.showError(3000);
                    toast(R.string.common_code_error_hint);
                    return;
                }
                if (!etRegisterPassword1.getText().toString().equals(etRegisterPassword2.getText().toString())) {
                    etRegisterPassword1.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.shake_anim));
                    etRegisterPassword2.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.shake_anim));
                    btnRegisterCommit.showError(3000);
                    toast(R.string.common_password_input_unlike);
                    return;
                }
                hideKeyboard(getCurrentFocus());

                btnRegisterCommit.showProgress();
                postDelayed(() -> {
                    btnRegisterCommit.showSucceed();
                    postDelayed(() -> {
                        setResult(RESULT_OK, new Intent()
                                .putExtra(IntentKey.PHONE, etRegisterPhone.getText().toString())
                                .putExtra(IntentKey.PASSWORD, etRegisterPassword1.getText().toString()));
                        finish();
                    }, 1000);
                }, 2000);
                break;
        }
    }

    @NonNull
    @Override
    protected ImmersionBar createStatusBarConfig() {
        return super.createStatusBarConfig()
                // 不要把整个布局顶上去
                .keyboardEnable(true);
    }

    /**
     * {@link TextView.OnEditorActionListener}
     */
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE && btnRegisterCommit.isEnabled()) {
            // 模拟点击注册按钮
            onClick(btnRegisterCommit);
            return true;
        }
        return false;
    }

    /**
     * 注册监听
     */
    public interface OnRegisterListener {

        /**
         * 注册成功
         *
         * @param phone    手机号
         * @param password 密码
         */
        void onSucceed(String phone, String password);

        /**
         * 取消注册
         */
        default void onCancel() {
        }
    }
}
