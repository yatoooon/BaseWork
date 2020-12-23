package com.yatoooon.demo.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

import com.yatoooon.baselibrary.di.component.AppComponent;
import com.yatoooon.baselibrary.widget.view.PasswordEditText;
import com.yatoooon.demo.R;
import com.yatoooon.demo.app.aop.DebugLog;
import com.yatoooon.demo.app.aop.SingleClick;
import com.yatoooon.demo.app.common.MyActivity;
import com.yatoooon.demo.app.helper.InputTextHelper;
import com.yatoooon.demo.app.other.IntentKey;
import com.yatoooon.demo.di.component.DaggerPasswordResetComponent;
import com.yatoooon.demo.mvp.contract.PasswordResetContract;
import com.yatoooon.demo.mvp.presenter.PasswordResetPresenter;

import butterknife.BindView;
import butterknife.OnClick;


public class PasswordResetActivity extends MyActivity<PasswordResetPresenter> implements PasswordResetContract.View {
    @BindView(R.id.et_password_reset_password1)
    PasswordEditText etPasswordResetPassword1;
    @BindView(R.id.et_password_reset_password2)
    PasswordEditText etPasswordResetPassword2;
    @BindView(R.id.btn_password_reset_commit)
    AppCompatButton btnPasswordResetCommit;
    /**
     * 手机号
     */
    private String mPhoneNumber;
    /**
     * 验证码
     */
    private String mVerifyCode;

    @DebugLog
    public static void start(Context context, String phone, String code) {
        Intent intent = new Intent(context, PasswordResetActivity.class);
        intent.putExtra(IntentKey.PHONE, phone);
        intent.putExtra(IntentKey.CODE, code);
        context.startActivity(intent);
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerPasswordResetComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_password_reset;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        InputTextHelper.with(this)
                .addView(etPasswordResetPassword1)
                .addView(etPasswordResetPassword2)
                .setMain(btnPasswordResetCommit)
                .build();
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mPhoneNumber = getString(IntentKey.PHONE);
        mVerifyCode = getString(IntentKey.CODE);
    }
    @SingleClick
    @OnClick(R.id.btn_password_reset_commit)
    public void onViewClicked(View view) {
        if (!etPasswordResetPassword1.getText().toString().equals(etPasswordResetPassword2.getText().toString())) {
            toast(R.string.common_password_input_unlike);
            return;
        }

        toast(R.string.password_reset_success);
        finish();
        return;
    }


}
