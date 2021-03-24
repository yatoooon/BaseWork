package com.yatoooon.demo.mvp.ui.dialog;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.FileProvider;

import com.liulishuo.okdownload.DownloadTask;
import com.liulishuo.okdownload.StatusUtil;
import com.liulishuo.okdownload.core.breakpoint.BreakpointInfo;
import com.liulishuo.okdownload.core.cause.ResumeFailedCause;
import com.liulishuo.okdownload.core.listener.DownloadListener3;
import com.yatoooon.baselibrary.base.BaseDialog;
import com.yatoooon.demo.BuildConfig;
import com.yatoooon.demo.R;
import com.yatoooon.demo.app.aop.CheckNet;
import com.yatoooon.demo.app.aop.Permissions;
import com.yatoooon.demo.app.aop.SingleClick;
import com.hjq.permissions.Permission;
import com.yatoooon.demo.app.other.AppConfig;
import com.yatoooon.demo.app.utils.DownloadUtil;

import java.io.File;


/**
 * author : Android 轮子哥
 * github : https://github.com/getActivity/AndroidProject
 * time   : 2019/03/20
 * desc   : 升级对话框
 */
public final class UpdateDialog {

    public static final class Builder
            extends BaseDialog.Builder<Builder> {

        private final TextView mNameView;
        private final TextView mContentView;
        private final ProgressBar mProgressView;

        private final TextView mUpdateView;
        private final TextView mCloseView;

        /**
         * Apk 文件
         */
        private File mApkFile;
        /**
         * 下载地址
         */
        private String mDownloadUrl;
        /**
         * 文件 MD5
         */
        private String mFileMd5;
        /**
         * 是否强制更新
         */
        private boolean mForceUpdate;

        /**
         * 当前是否下载中
         */
        private boolean mDownloading;
        /**
         * 当前是否下载完毕
         */
        private boolean mDownloadComplete;

        public Builder(Context context) {
            super(context);

            setContentView(R.layout.dialog_update);
            setAnimStyle(BaseDialog.ANIM_BOTTOM);
            setCancelable(false);

            mNameView = findViewById(R.id.tv_update_name);
            mContentView = findViewById(R.id.tv_update_content);
            mProgressView = findViewById(R.id.pb_update_progress);
            mUpdateView = findViewById(R.id.tv_update_update);
            mCloseView = findViewById(R.id.tv_update_close);
            setOnClickListener(mUpdateView, mCloseView);
        }

        /**
         * 设置版本名
         */
        public Builder setVersionName(CharSequence name) {
            mNameView.setText(name);
            return this;
        }

        /**
         * 设置更新日志
         */
        public Builder setUpdateLog(CharSequence text) {
            mContentView.setText(text);
            mContentView.setVisibility(text == null ? View.GONE : View.VISIBLE);
            return this;
        }

        /**
         * 设置强制更新
         */
        public Builder setForceUpdate(boolean force) {
            mForceUpdate = force;
            mCloseView.setVisibility(force ? View.GONE : View.VISIBLE);
            setCancelable(!force);
            return this;
        }

        /**
         * 设置下载 url
         */
        public Builder setDownloadUrl(String url) {
            mDownloadUrl = url;
            return this;
        }

        /**
         * 设置文件 md5
         */
        public Builder setFileMd5(String md5) {
            mFileMd5 = md5;
            return this;
        }

        @SingleClick
        @Override
        public void onClick(View v) {
            if (v == mCloseView) {
                dismiss();
            } else if (v == mUpdateView) {
                // 判断下载状态
                if (mDownloadComplete) {
                    if (mApkFile.isFile()) {
                        // 下载完毕，安装 Apk
                        installApk();
                    } else {
                        // 下载失败，重新下载
                        downloadApk();
                    }
                } else if (!mDownloading) {
                    // 没有下载，开启下载
                    downloadApk();
                }
            }
        }

        /**
         * 下载 Apk
         */
        @CheckNet
        @Permissions({Permission.MANAGE_EXTERNAL_STORAGE})
        private void downloadApk() {
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            int notificationId = getContext().getApplicationInfo().uid;
            String channelId = "";
            // 适配 Android 8.0 通知渠道新特性
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(getString(R.string.update_notification_channel_id), getString(R.string.update_notification_channel_name), NotificationManager.IMPORTANCE_HIGH);
                channel.enableLights(false);
                channel.enableVibration(false);
                channel.setVibrationPattern(new long[]{0});
                channel.setSound(null, null);
                notificationManager.createNotificationChannel(channel);
                channelId = channel.getId();
            }
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getContext(), channelId)
                    .setWhen(System.currentTimeMillis())
                    // 设置通知标题
                    .setContentTitle(getString(R.string.app_name))
                    // 设置通知小图标
                    .setSmallIcon(R.mipmap.ic_launcher)
                    // 设置通知大图标
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                    // 设置通知静音
                    .setDefaults(NotificationCompat.FLAG_ONLY_ALERT_ONCE)
                    .setVibrate(new long[]{0})
                    .setSound(null)
                    // 设置通知的优先级
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);


            // 创建要下载的文件对象
            mApkFile = new File(DownloadUtil.getParentFile(getContext()), getString(R.string.app_name) + "_v" + BuildConfig.VERSION_CODE + ".apk");

            // 设置对话框不能被取消
            setCancelable(false);

            if (!TextUtils.isEmpty(mFileMd5) && mApkFile.exists() && mApkFile.isFile() && mFileMd5.equalsIgnoreCase(DownloadUtil.getFileMd5(mApkFile))) {
                installApk();
                return;
            }


            //创建下载任务
            DownloadTask downloadTask = new DownloadTask.Builder(mDownloadUrl, DownloadUtil.getParentFile(getContext()))
                    .setFilename(mApkFile.getName())
                    .setMinIntervalMillisCallbackProcess(16)
                    .setPassIfAlreadyCompleted(false)    // okdownload里用文件名校验的   没有用md5校验
                    .build();
            //开始下载
            downloadTask.enqueue(new DownloadListener3() {
                @Override
                protected void started(@NonNull DownloadTask task) {
                    // 标记为下载中
                    mDownloading = true;
                    // 标记成未下载完成
                    mDownloadComplete = false;
                    // 后台更新
                    mCloseView.setVisibility(View.GONE);
                    // 显示进度条
                    mProgressView.setVisibility(View.VISIBLE);
                    mUpdateView.setText(R.string.update_status_start);
                }

                @Override
                protected void completed(@NonNull DownloadTask task) {
                    // 显示下载成功通知
                    notificationManager.notify(notificationId, notificationBuilder
                            // 设置通知的文本
                            .setContentText(String.format(getString(R.string.update_status_successful), 100))
                            // 设置下载的进度
                            .setProgress(100, 100, false)
                            // 设置通知点击之后的意图
                            .setContentIntent(PendingIntent.getActivity(getContext(), 1, getInstallIntent(), Intent.FILL_IN_ACTION))
                            // 设置点击通知后是否自动消失
                            .setAutoCancel(true)
                            .build());
                    mUpdateView.setText(R.string.update_status_successful);
                    mDownloadComplete = true;
                    installApk();
                    endtask();
                }

                private void endtask() {
                    // 更新进度条
                    mProgressView.setProgress(0);
                    mProgressView.setVisibility(View.GONE);
                    // 标记当前不是下载中
                    mDownloading = false;
                    // 如果当前不是强制更新，对话框就恢复成可取消状态
                    if (!mForceUpdate) {
                        setCancelable(true);
                    }

                }

                @Override
                protected void canceled(@NonNull DownloadTask task) {
                    endtask();
                }

                @Override
                protected void error(@NonNull DownloadTask task, @NonNull Exception e) {
                    // 清除通知
                    notificationManager.cancel(notificationId);
                    mUpdateView.setText(R.string.update_status_failed);
                    // 删除下载的文件
                    if (task.getFile() != null) {
                        task.getFile().delete();
                    }
                    endtask();
                }

                @Override
                protected void warn(@NonNull DownloadTask task) {

                }

                @Override
                public void retry(@NonNull DownloadTask task, @NonNull ResumeFailedCause cause) {

                }

                @Override
                public void connected(@NonNull DownloadTask task, int blockCount, long currentOffset, long totalLength) {
                }

                @Override
                public void progress(@NonNull DownloadTask task, long currentOffset, long totalLength) {
                    BreakpointInfo info = StatusUtil.getCurrentInfo(task);
                    mUpdateView.setText(String.format(getString(R.string.update_status_running), DownloadUtil.getDownloadProgress(info.getTotalOffset(), info.getTotalLength())));
                    DownloadUtil.calcProgressToView(mProgressView, info.getTotalOffset(), info.getTotalLength());
                    // 更新下载通知
                    notificationManager.notify(notificationId, notificationBuilder
                            // 设置通知的文本
                            .setContentText(String.format(getString(R.string.update_status_running), DownloadUtil.getDownloadProgress(info.getTotalOffset(), info.getTotalLength())))
                            // 设置下载的进度
                            .setProgress(100, DownloadUtil.getDownloadProgress(info.getTotalOffset(), info.getTotalLength()), false)
                            // 设置点击通知后是否自动消失
                            .setAutoCancel(false)
                            // 重新创建新的通知对象
                            .build());
                }
            });
        }

        /**
         * 安装 Apk
         */
        @Permissions({Permission.REQUEST_INSTALL_PACKAGES})
        private void installApk() {
            getContext().startActivity(getInstallIntent());
        }

        /**
         * 获取安装意图
         */
        private Intent getInstallIntent() {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            Uri uri;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                uri = FileProvider.getUriForFile(getContext(), AppConfig.getPackageName() + ".provider", mApkFile);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            } else {
                uri = Uri.fromFile(mApkFile);
            }
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            return intent;
        }


    }

}