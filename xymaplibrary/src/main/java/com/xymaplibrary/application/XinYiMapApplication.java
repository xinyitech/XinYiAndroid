package com.xymaplibrary.application;

import android.content.Context;

import com.baidu.mapapi.SDKInitializer;

/**
 * Created by jiajun.wang on 2018/3/21.
 */

public class XinYiMapApplication  {

    public static void init(Context context) {
        SDKInitializer.initialize(context);
    }
}
