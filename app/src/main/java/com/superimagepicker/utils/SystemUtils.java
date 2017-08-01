package com.superimagepicker.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import java.util.List;

/**
 * Created by MMM on 2016/12/31.
 */

public class SystemUtils {

    /**
     * 获取应用程序名称
     */
    public static String getPackageName(Context context) {
        //当前应用pid
        int pid = android.os.Process.myPid();
        //任务管理类
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //遍历所有应用
        List<ActivityManager.RunningAppProcessInfo> infos = manager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : infos) {
            if (info.pid == pid)//得到当前应用
                return info.processName;//返回包名
        }
        return "";
    }

    /**
     * 获取应用程序名称
     */
    public static String getApplicationName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 获取程序版本号
    public static int getVersionCode(Context context) {
        int versionCode = 0;
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    // 获取程序版本名
    public static String getVersionName(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getDeviceId(Context context) {
        if (context != null) {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(
                    Context.TELEPHONY_SERVICE);
            return tm.getDeviceId();
        }
        return null;
    }

    public static String getPhoneNumber(Context context) {
        if (context != null) {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(
                    Context.TELEPHONY_SERVICE);
            return tm.getLine1Number();
        }
        return null;
    }

    //判断需要token的界面，如果为空，则跳转登录
    //建议在initview时调用
//    public static String getToken(Context context) {
//        return SPUtils.getString(context, "token", "");
//    }

    /**
     * 启动权限设置页面
     */
    public static void startAppSettings(Context context) {
        Intent intent = new Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + context.getPackageName()));
        context.startActivity(intent);
    }

    /**
     * 打开网络设置界面
     */
    public static void startNetSetting(Context context)
    {
        Intent intentSettings;
        if(android.os.Build.VERSION.SDK_INT > 10){//判断版本(3.0以上)
            intentSettings = new Intent(Settings.ACTION_SETTINGS);
        }else{
            intentSettings = new Intent();
            intentSettings.setClassName("com.android.phone","com.android.phone.MobileNetWorkSettings");
        }
        context.startActivity(intentSettings);
    }

}
