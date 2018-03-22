package com.xymaplibrary.application;

import com.baidu.mapapi.SDKInitializer;
import com.xylibrary.application.XinYiApplication;

/**
 * Created by jiajun.wang on 2018/3/21.
 */

public class XinYiMapApplication extends XinYiApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(getApplicationContext());
    }
}
