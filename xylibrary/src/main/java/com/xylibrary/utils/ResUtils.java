package com.xylibrary.utils;

import android.content.res.Resources;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;

import com.blankj.utilcode.util.Utils;

/**
 * Created by Fracesuit on 2017/7/21.
 */

public class ResUtils {
    //获取资源文件夹的方法
    public static Resources getResource() {
        return Utils.getApp().getResources();
    }

    //获取string操作
    public static String getString(int id) {
        return getResource().getString(id);
    }

    //获取string数组操作
    public static String[] getStringArray(int id) {
        return getResource().getStringArray(id);
    }


    //android.R.attr.colorAccent
    public static int getColor(@ColorRes int color) {
        return ContextCompat.getColor(Utils.getApp(), color);
    }



}
