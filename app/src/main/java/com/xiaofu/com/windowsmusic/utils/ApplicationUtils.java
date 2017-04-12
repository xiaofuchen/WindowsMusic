package com.xiaofu.com.windowsmusic.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.xiaofu.com.windowsmusic.MusicApp;

/**
 * Created by MarioStudio on 2016/5/30.
 */

public class ApplicationUtils {

    public static String getAppVersionName() {
        Context context = MusicApp.getInstance();
        String versionName = "com.xiaofu";
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versionName;
    }
}
