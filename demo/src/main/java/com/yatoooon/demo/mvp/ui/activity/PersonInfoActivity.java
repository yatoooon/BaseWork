package com.yatoooon.demo.mvp.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.FileProvider;

import com.yatoooon.baselibrary.di.component.AppComponent;
import com.yatoooon.baselibrary.http.imageloader.glide.ImageConfigImpl;
import com.yatoooon.baselibrary.utils.ArmsUtils;
import com.yatoooon.baselibrary.widget.layout.SettingBar;
import com.yatoooon.demo.R;
import com.yatoooon.demo.app.aop.SingleClick;
import com.yatoooon.demo.app.common.AppActivity;
import com.yatoooon.demo.app.other.AppConfig;
import com.yatoooon.demo.mvp.ui.dialog.AddressDialog;
import com.yatoooon.demo.mvp.ui.dialog.InputDialog;
import com.yatoooon.demo.di.component.DaggerPersonInfoComponent;
import com.yatoooon.demo.mvp.contract.PersonInfoContract;
import com.yatoooon.demo.mvp.presenter.PersonInfoPresenter;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;


public class PersonInfoActivity extends AppActivity<PersonInfoPresenter> implements PersonInfoContract.View {

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
        sbPersonDataId.setRightText("880634");
        sbPersonDataName.setRightText("Android 轮子哥");
        String address = mProvince + mCity + mArea;
        sbPersonDataAddress.setRightText(address);
    }

    @SingleClick
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
                    // 裁剪头像
                    cropImage(new File(data.get(0)));
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


    /**
     * 裁剪图片
     */
    private void cropImage(File sourceFile) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(getContext(), AppConfig.getPackageName() + ".provider", sourceFile);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(sourceFile);
        }

        String regex = "^(.+)(\\..+)$";
        String fileName = sourceFile.getName().replaceFirst(regex, "$1_crop_" + new SimpleDateFormat("HHmmss", Locale.getDefault()).format(new Date()) + "$2");

        File outputFile = new File(sourceFile.getParent(), fileName);
        if (outputFile.exists()) {
            outputFile.delete();
        }

        intent.setDataAndType(uri, "image/*");
        // 是否进行裁剪
        intent.putExtra("crop", String.valueOf(true));
        // 宽高裁剪比例
        if (Build.MANUFACTURER.toUpperCase().contains("HUAWEI")) {
            // 华为手机特殊处理，否则不会显示正方形裁剪区域，而是显示圆形裁剪区域
            // https://blog.csdn.net/wapchief/article/details/80669647
            intent.putExtra("aspectX", 9998);
            intent.putExtra("aspectY", 9999);
        } else {
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
        }
        // 是否裁剪成圆形（注：在华为手机上没有任何效果）
        // intent.putExtra("circleCrop", false);
        // 宽高裁剪大小
        // intent.putExtra("outputX", 200);
        // intent.putExtra("outputY", 200);
        // 是否保持比例不变
        intent.putExtra("scale", true);
        // 裁剪区域小于输出大小时，是否放大图像
        intent.putExtra("scaleUpIfNeeded", true);
        // 是否将数据以 Bitmap 的形式保存
        intent.putExtra("return-data", false);
        // 设置裁剪后保存的文件路径
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outputFile));
        // 设置裁剪后保存的文件格式
        intent.putExtra("outputFormat", getImageFormat(sourceFile).toString());

        // 判断手机是否有裁剪功能
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, (resultCode, data) -> {
                if (resultCode == RESULT_OK) {
                    updateImage(outputFile, true);
                }
            });
            return;
        }

        // 没有的话就不裁剪，直接上传原图片
        // 但是这种情况极其少见，可以忽略不计
        updateImage(sourceFile, false);
    }


    /**
     * 上传图片
     */
    private void updateImage(File file, boolean deleteFile) {
        mAvatarUrl = file.getPath();
        ArmsUtils.obtainAppComponentFromContext(getActivity()).imageLoader()
                .loadImage(getActivity(), ImageConfigImpl.builder()
                        .res(mAvatarUrl)
                        .imageView(ivPersonDataAvatar)
                        .isCircle(true)
                        .build());
        return;
    }

    /**
     * 获取图片文件的格式
     */
    private Bitmap.CompressFormat getImageFormat(File file) {
        String fileName = file.getName().toLowerCase();
        if (fileName.endsWith(".png")) {
            return Bitmap.CompressFormat.PNG;
        } else if (fileName.endsWith(".webp")) {
            return Bitmap.CompressFormat.WEBP;
        }
        return Bitmap.CompressFormat.JPEG;
    }
}
