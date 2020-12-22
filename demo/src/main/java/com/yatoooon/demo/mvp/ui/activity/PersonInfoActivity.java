package com.yatoooon.demo.mvp.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.yatoooon.baselibrary.di.component.AppComponent;
import com.yatoooon.baselibrary.http.imageloader.glide.ImageConfigImpl;
import com.yatoooon.baselibrary.utils.ArmsUtils;
import com.yatoooon.baselibrary.widget.layout.SettingBar;
import com.yatoooon.demo.R;
import com.yatoooon.demo.app.common.MyActivity;
import com.yatoooon.demo.app.dialog.AddressDialog;
import com.yatoooon.demo.app.dialog.InputDialog;
import com.yatoooon.demo.di.component.DaggerPersonInfoComponent;
import com.yatoooon.demo.mvp.contract.PersonInfoContract;
import com.yatoooon.demo.mvp.presenter.PersonInfoPresenter;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class PersonInfoActivity extends MyActivity<PersonInfoPresenter> implements PersonInfoContract.View {

    @BindView(R.id.iv_person_data_avatar)
    AppCompatImageView ivPersonDataAvatar;
    @BindView(R.id.fl_person_data_avatar)
    SettingBar flPersonDataAvatar;
    @BindView(R.id.sb_person_data_id)
    SettingBar sbPersonDataId;
    @BindView(R.id.sb_person_data_name)
    SettingBar sbPersonDataName;
    @BindView(R.id.sb_person_data_address)
    SettingBar sbPersonDataAddress;
    /**
     * 省
     */
    private String mProvince = "广东省";
    /**
     * 市
     */
    private String mCity = "广州市";
    /**
     * 区
     */
    private String mArea = "天河区";

    /**
     * 头像地址
     */
    private String mAvatarUrl;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerPersonInfoComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .view(this)
                .build()
                .inject(this);
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_person_info;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

        ArmsUtils.obtainAppComponentFromContext(getContext()).imageLoader()
                .loadImage(getContext(), ImageConfigImpl.builder()
                        .errorPic(R.drawable.avatar_placeholder_ic)
                        .placeholder(R.drawable.avatar_placeholder_ic)
                        .res(R.drawable.avatar_placeholder_ic)
                        .imageView(ivPersonDataAvatar)
                        .isCircle(true).build());
        String address = mProvince + mCity + mArea;
        sbPersonDataAddress.setRightText(address);
    }


    @OnClick({R.id.iv_person_data_avatar, R.id.fl_person_data_avatar, R.id.sb_person_data_name, R.id.sb_person_data_address})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_person_data_avatar:
                if (!TextUtils.isEmpty(mAvatarUrl)) {
                    // 查看头像
                    ImagePreviewActivity.start(getActivity(), mAvatarUrl);
                } else {
                    // 选择头像
                    onClick(flPersonDataAvatar);
                }
                break;
            case R.id.fl_person_data_avatar:
                ImageSelectActivity.start(this, data -> {
                    mAvatarUrl = data.get(0);
                    ArmsUtils.obtainAppComponentFromContext(getActivity()).imageLoader()
                            .loadImage(getActivity(), ImageConfigImpl.builder()
                                    .res(mAvatarUrl)
                                    .imageView(ivPersonDataAvatar)
                                    .isCircle(true)
                                    .build());
                });
                break;
            case R.id.sb_person_data_name:
                new InputDialog.Builder(this)
                        // 标题可以不用填写
                        .setTitle(getString(R.string.personal_data_name_hint))
                        .setContent(sbPersonDataName.getRightText())
                        //.setHint(getString(R.string.personal_data_name_hint))
                        //.setConfirm("确定")
                        // 设置 null 表示不显示取消按钮
                        //.setCancel("取消")
                        // 设置点击按钮后不关闭对话框
                        //.setAutoDismiss(false)
                        .setListener((dialog, content) -> {
                            if (!sbPersonDataName.getRightText().equals(content)) {
                                sbPersonDataName.setRightText(content);
                            }
                        })
                        .show();
                break;
            case R.id.sb_person_data_address:
                new AddressDialog.Builder(this)
                        //.setTitle("选择地区")
                        // 设置默认省份
                        .setProvince(mProvince)
                        // 设置默认城市（必须要先设置默认省份）
                        .setCity(mCity)
                        // 不选择县级区域
                        //.setIgnoreArea()
                        .setListener((dialog, province, city, area) -> {
                            String address = province + city + area;
                            if (!sbPersonDataAddress.getRightText().equals(address)) {
                                mProvince = province;
                                mCity = city;
                                mArea = area;
                                sbPersonDataAddress.setRightText(address);
                            }
                        })
                        .show();
                break;
        }
    }
}
