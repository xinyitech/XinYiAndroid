package com.xymaplibrary.base;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

/**
 * Created by jiajun.wang on 2018/3/23.
 * 在使用百度地图的时候要先调用此静态方法
 */

public class XinYiMapSdk {
    public static void  initMapSdk(Application application){
        SDKInitializer.initialize(application);
    }
}
