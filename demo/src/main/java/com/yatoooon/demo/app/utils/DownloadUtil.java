package com.yatoooon.demo.app.utils;

import android.content.Context;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;

public class DownloadUtil {

    /**
     * 获取当前进度
     */
    public static int getDownloadProgress(long offset, long total) {
        // 计算百分比，这里踩了两个坑
        // 当文件很大的时候：字节数 * 100 会超过 int 最大值，计算结果会变成负数
        // 还有需要注意的是，long 除以 long 等于 long，这里的字节数除以总字节数应该要 double 类型的
        return (int) (((double) offset / total) * 100);
    }

    public static void calcProgressToView(ProgressBar progressBar, long offset, long total) {
        final float percent = (float) offset / total;
        progressBar.setProgress((int) (percent * progressBar.getMax()));
    }


    public static File getParentFile(@NonNull Context context) {
        final File externalSaveDir = context.getExternalCacheDir();
        if (externalSaveDir == null) {
            return context.getCacheDir();
        } else {
            return externalSaveDir;
        }
    }


    /**
     * 获取文件的 MD5
     */
    public static String getFileMd5(File file) {
        if (file == null) {
            return null;
        }
        DigestInputStream inputStream = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            inputStream = new DigestInputStream(fis, messageDigest);
            byte[] buffer = new byte[1024 * 256];
            while (true) {
                if (!(inputStream.read(buffer) > 0)) {
                    break;
                }
            }
            messageDigest = inputStream.getMessageDigest();
            byte[] md5 = messageDigest.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : md5) {
                sb.append(String.format("%02X", b));
            }
            return sb.toString().toLowerCase();
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        } finally {
            closeStream(inputStream);
        }
        return null;
    }

    /**
     * 关闭流
     */
    public static void closeStream(Closeable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
