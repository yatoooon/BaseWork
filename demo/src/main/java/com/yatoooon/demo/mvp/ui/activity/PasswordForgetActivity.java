package com.yatoooon.demo.mvp.ui.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import com.hjq.toast.ToastUtils;
import com.yatoooon.baselibrary.di.component.AppComponent;
import com.yatoooon.baselibrary.widget.view.CountdownView;
import com.yatoooon.baselibrary.widget.view.RegexEditText;
import com.yatoooon.demo.R;
import com.yatoooon.demo.app.aop.SingleClick;
import com.yatoooon.demo.app.app.AppActivity;
import com.yatoooon.demo.app.manager.InputTextManager;
import com.yatoooon.demo.di.component.DaggerPasswordForgetComponent;
import com.yatoooon.demo.mvp.contract.PasswordForgetContract;
import com.yatoooon.demo.mvp.presenter.PasswordForgetPresenter;

import butterknife.BindView;
import butterknife.OnClick;


public class PasswordForgetActivity extends AppActivity<PasswordForgetPresenter> implements PasswordForgetContract.View, TextView.OnEditorActionListener {

    @BindView(R.id.et_password_forget_phone)
    RegexEditText etPasswordForgetPhone;
    @BindView(R.id.et_password_forget_code)
    AppCompatEditText etPasswordForgetCode;
    @BindView(R.id.cv_password_forget_countdown)
    CountdownView cvPasswordForgetCountdown;
    @BindView(R.id.btn_password_forget_commit)
    AppCompatButton btnPasswordForgetCommit;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerPasswordForgetComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_password_forget;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        etPasswordForgetCode.setOnEditorActionListener(this);

        InputTextManager.with(this)
                .addView(etPasswordForgetPhone)
                .addView(etPasswordForgetCode)
                .setMain(btnPasswordForgetCommit)
                .build();
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }

    @SingleClick
    @OnClick({R.id.cv_password_forget_countdown, R.id.btn_password_forget_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cv_password_forget_countdown:
                if (etPasswordForgetPhone.getText().toString().length() != 11) {
                    etPasswordForgetPhone.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.shake_anim));
                    toast(R.string.common_phone_input_error);
                    return;
                }
                hideKeyboard(getCurrentFocus());
                toast(R.string.common_code_send_hint);
                cvPasswordForgetCountdown.start();
                break;
            case R.id.btn_password_forget_commit:
                if (etPasswordForgetPhone.getText().toString().length() != 11) {
                    etPasswordForgetPhone.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.shake_anim));

                    toast(R.string.common_phone_input_error);
                    return;
                }

                if (etPasswordForgetCode.getText().toString().length() != getResources().getInteger(R.integer.sms_code_length)) {
                    etPasswordForgetCode.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.shake_anim));
                    ToastUtils.show(R.string.common_code_error_hint);
                    return;
                }

                PasswordResetActivity.start(getActivity(), etPasswordForgetPhone.getText().toString(), etPasswordForgetCode.getText().toString());
                finish();
                break;
        }
    }
    /**
     * {@link TextView.OnEditorActionListener}
     */
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE && btnPasswordForgetCommit.isEnabled()) {
            // 模拟点击下一步按钮
            onClick(btnPasswordForgetCommit);
            return true;
        }
        return false;
    }
}
