package com.yatoooon.demo.app.aop;

import android.app.Activity;

import com.hjq.permissions.XXPermissions;
import com.yatoooon.demo.app.manager.ActivityManager;
import com.yatoooon.demo.app.other.PermissionCallback;;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.util.List;

/**
 * 权限申请处理
 */
@Aspect
public class PermissionsAspect {

    /**
     * 方法切入点
     */
    @Pointcut("execution(@com.yatoooon.demo.app.aop.Permissions * *(..))")
    public void method() {
    }

    /**
     * 在连接点进行方法替换
     */
    @Around("method() && @annotation(permissions)")
    public void aroundJoinPoint(final ProceedingJoinPoint joinPoint, Permissions permissions) {
        Activity activity = ActivityManager.getInstance().getTopActivity();
        if (activity == null || activity.isFinishing() || activity.isDestroyed()) {
            return;
        }

        XXPermissions.with(activity)
                .permission(permissions.value())
                .request(new PermissionCallback() {

                    @Override
                    public void onGranted(List<String> permissions, boolean all) {
                        if (all) {
                            try {
                                // 获得权限，执行原方法
                                joinPoint.proceed();
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }
}