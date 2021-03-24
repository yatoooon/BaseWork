package com.yatoooon.demo.mvp.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.yatoooon.demo.app.aop.DebugLog;
import com.yatoooon.demo.app.aop.SingleClick;
import com.yatoooon.demo.app.common.AppActivity;
import com.yatoooon.demo.app.manager.InputTextManager;
import com.yatoooon.demo.app.other.IntentKey;
import com.yatoooon.demo.di.component.DaggerPhoneChangeComponent;
import com.yatoooon.demo.mvp.contract.PhoneChangeContract;
import com.yatoooon.demo.mvp.presenter.PhoneChangePresenter;
import com.yatoooon.demo.mvp.ui.dialog.HintDialog;

import butterknife.BindView;
import butterknife.OnClick;


public class PhoneChangeActivity extends AppActivity<PhoneChangePresenter> implements PhoneChangeContract.View, TextView.OnEditorActionListener {

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
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
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
        etPhoneResetCode.setOnEditorActionListener(this);
        InputTextManager.with(this)
                .addView(etPhoneResetPhone)
                .addView(etPhoneResetCode)
                .setMain(btnPhoneResetCommit)
                .build();
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mVerifyCode = getString(IntentKey.CODE);
    }

    @SingleClick
    @OnClick({R.id.cv_phone_reset_countdown, R.id.btn_phone_reset_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cv_phone_reset_countdown:
                if (etPhoneResetPhone.getText().toString().length() != 11) {
                    etPhoneResetPhone.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.shake_anim));
                    toast(R.string.common_phone_input_error);
                    return;
                }

                toast(R.string.common_code_send_hint);
                cvPhoneResetCountdown.start();
                break;
            case R.id.btn_phone_reset_commit:
                if (etPhoneResetPhone.getText().toString().length() != 11) {
                    etPhoneResetPhone.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.shake_anim));
                    toast(R.string.common_phone_input_error);
                    return;
                }

                if (etPhoneResetCode.getText().toString().length() != getResources().getInteger(R.integer.sms_code_length)) {
                    ToastUtils.show(R.string.common_code_error_hint);
                    return;
                }
                hideKeyboard(getCurrentFocus());
                new HintDialog.Builder(this)
                        .setIcon(HintDialog.ICON_FINISH)
                        .setMessage(R.string.phone_reset_commit_succeed)
                        .setDuration(2000)
                        .addOnDismissListener(dialog -> finish())
                        .show();
                break;
        }
    }

    /**
     * {@link TextView.OnEditorActionListener}
     */
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE && btnPhoneResetCommit.isEnabled()) {
            // 模拟点击提交按钮
            onClick(btnPhoneResetCommit);
            return true;
        }
        return false;
    }
}
