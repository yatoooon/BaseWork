package com.yatoooon.demo.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import com.gyf.immersionbar.ImmersionBar;
import com.yatoooon.baselibrary.di.component.AppComponent;
import com.yatoooon.baselibrary.widget.view.CountdownView;
import com.yatoooon.baselibrary.widget.view.PasswordEditText;
import com.yatoooon.baselibrary.widget.view.RegexEditText;
import com.yatoooon.demo.R;
import com.yatoooon.demo.app.aop.SingleClick;
import com.yatoooon.demo.app.common.MyActivity;
import com.yatoooon.demo.app.helper.InputTextHelper;
import com.yatoooon.demo.app.other.IntentKey;
import com.yatoooon.demo.di.component.DaggerRegisterComponent;
import com.yatoooon.demo.mvp.contract.RegisterContract;
import com.yatoooon.demo.mvp.presenter.RegisterPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class RegisterActivity extends MyActivity<RegisterPresenter> implements RegisterContract.View {

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
    AppCompatButton btnRegisterCommit;

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

        InputTextHelper.with(this)
                .addView(etRegisterPhone)
                .addView(etRegisterCode)
                .addView(etRegisterPassword1)
                .addView(etRegisterPassword2)
                .setMain(btnRegisterCommit)
                .build();
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }

    @SingleClick
    @OnClick({R.id.cv_register_countdown, R.id.btn_register_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cv_register_countdown:
                if (etRegisterPhone.getText().toString().length() != 11) {
                    toast(R.string.common_phone_input_error);
                    return;
                }

                toast(R.string.common_code_send_hint);
                cvRegisterCountdown.start();
                break;
            case R.id.btn_register_commit:
                if (etRegisterPhone.getText().toString().length() != 11) {
                    toast(R.string.common_phone_input_error);
                    return;
                }

                if (!etRegisterPassword1.getText().toString().equals(etRegisterPassword1.getText().toString())) {
                    toast(R.string.common_password_input_unlike);
                    return;
                }

                toast(R.string.register_succeed);
                setResult(RESULT_OK, new Intent()
                        .putExtra(IntentKey.PHONE, etRegisterPhone.getText().toString())
                        .putExtra(IntentKey.PASSWORD, etRegisterPassword1.getText().toString()));
                finish();
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
}
