package com.yatoooon.demo.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import com.hjq.toast.ToastUtils;
import com.yatoooon.baselibrary.di.component.AppComponent;
import com.yatoooon.baselibrary.widget.view.CountdownView;
import com.yatoooon.baselibrary.widget.view.RegexEditText;
import com.yatoooon.demo.R;
import com.yatoooon.demo.app.aop.DebugLog;
import com.yatoooon.demo.app.common.MyActivity;
import com.yatoooon.demo.app.helper.InputTextHelper;
import com.yatoooon.demo.app.other.IntentKey;
import com.yatoooon.demo.di.component.DaggerPhoneChangeComponent;
import com.yatoooon.demo.mvp.contract.PhoneChangeContract;
import com.yatoooon.demo.mvp.presenter.PhoneChangePresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class PhoneChangeActivity extends MyActivity<PhoneChangePresenter> implements PhoneChangeContract.View {

    @BindView(R.id.et_phone_reset_phone)
    RegexEditText etPhoneResetPhone;
    @BindView(R.id.et_phone_reset_code)
    AppCompatEditText etPhoneResetCode;
    @BindView(R.id.cv_phone_reset_countdown)
    CountdownView cvPhoneResetCountdown;
    @BindView(R.id.btn_phone_reset_commit)
    AppCompatButton btnPhoneResetCommit;
    /**
     * 验证码
     */
    private String mVerifyCode;

    @DebugLog
    public static void start(Context context, String code) {
        Intent intent = new Intent(context, PhoneChangeActivity.class);
        intent.putExtra(IntentKey.CODE, code);
        context.startActivity(intent);
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerPhoneChangeComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_phone_change;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        InputTextHelper.with(this)
                .addView(etPhoneResetPhone)
                .addView(etPhoneResetCode)
                .setMain(btnPhoneResetCommit)
                .build();
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mVerifyCode = getString(IntentKey.CODE);
    }


    @OnClick({R.id.cv_phone_reset_countdown, R.id.btn_phone_reset_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cv_phone_reset_countdown:
                if (etPhoneResetPhone.getText().toString().length() != 11) {
                    toast(R.string.common_phone_input_error);
                    return;
                }

                toast(R.string.common_code_send_hint);
                cvPhoneResetCountdown.start();
                break;
            case R.id.btn_phone_reset_commit:
                if (etPhoneResetPhone.getText().toString().length() != 11) {
                    toast(R.string.common_phone_input_error);
                    return;
                }

                if (etPhoneResetCode.getText().toString().length() != getResources().getInteger(R.integer.sms_code_length)) {
                    ToastUtils.show(R.string.common_code_error_hint);
                    return;
                }

                toast(R.string.phone_reset_commit_succeed);
                finish();
                break;
        }
    }
}
