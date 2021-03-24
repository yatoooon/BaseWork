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

import com.yatoooon.baselibrary.di.component.AppComponent;
import com.yatoooon.baselibrary.widget.view.PasswordEditText;
import com.yatoooon.demo.R;
import com.yatoooon.demo.app.aop.DebugLog;
import com.yatoooon.demo.app.aop.SingleClick;
import com.yatoooon.demo.app.common.AppActivity;
import com.yatoooon.demo.app.manager.InputTextManager;
import com.yatoooon.demo.app.other.IntentKey;
import com.yatoooon.demo.di.component.DaggerPasswordResetComponent;
import com.yatoooon.demo.mvp.contract.PasswordResetContract;
import com.yatoooon.demo.mvp.presenter.PasswordResetPresenter;
import com.yatoooon.demo.mvp.ui.dialog.HintDialog;

import butterknife.BindView;
import butterknife.OnClick;


public class PasswordResetActivity extends AppActivity<PasswordResetPresenter> implements PasswordResetContract.View, TextView.OnEditorActionListener {
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
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
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
        etPasswordResetPassword2.setOnEditorActionListener(this);
        InputTextManager.with(this)
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
            etPasswordResetPassword1.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.shake_anim));
            etPasswordResetPassword2.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.shake_anim));
            toast(R.string.common_password_input_unlike);
            return;
        }
        hideKeyboard(getCurrentFocus());
        new HintDialog.Builder(this)
                .setIcon(HintDialog.ICON_FINISH)
                .setMessage(R.string.password_reset_success)
                .setDuration(2000)
                .addOnDismissListener(dialog -> finish())
                .show();
        return;
    }

    /**
     * {@link TextView.OnEditorActionListener}
     */
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE && btnPasswordResetCommit.isEnabled()) {
            // 模拟点击提交按钮
            onClick(btnPasswordResetCommit);
            return true;
        }
        return false;
    }

}
