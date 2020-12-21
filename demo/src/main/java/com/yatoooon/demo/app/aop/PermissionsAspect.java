package com.yatoooon.demo.app.aop;

import android.app.Activity;

import com.yatoooon.demo.R;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.XXPermissions;
import com.hjq.toast.ToastUtils;
import com.yatoooon.demo.app.helper.ActivityStackManager;;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.util.List;

/**
 *  权限申请处理
 */
@Aspect
public class PermissionsAspect {

    /**
     * 方法切入点
     */
    @Pointcut("execution(@com.hjq.demo.aop.Permissions * *(..))")
    public void method() {}

    /**
     * 在连接点进行方法替换
     */
    @Around("method() && @annotation(permissions)")
    public void aroundJoinPoint(final ProceedingJoinPoint joinPoint, Permissions permissions) {
        Activity activity = ActivityStackManager.getInstance().getTopActivity();
        if (activity == null || activity.isFinishing() || activity.isDestroyed()) {
            return;
        }
        XXPermissions.with(activity)
                .permission(permissions.value())
                .request(new OnPermission() {

                    @Override
                    public void hasPermission(List<String> granted, boolean all) {
                        if (all) {
                            try {
                                // 获得权限，执行原方法
                                joinPoint.proceed();
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void noPermission(List<String> denied, boolean quick) {
                        if (quick) {
                            ToastUtils.show(R.string.common_permission_fail);
                            XXPermissions.startPermissionActivity(activity, false);
                        } else {
                            ToastUtils.show(R.string.common_permission_hint);
                        }
                    }
                });
    }
}