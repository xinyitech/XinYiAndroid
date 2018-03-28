package com.xinyiandroid.application;


import com.blankj.utilcode.util.Utils;
import com.xylibrary.application.XinYiApplication;
import com.xymaplibrary.base.XinYiMapSdk;


/**
 * Created by jiajun.wang on 2018/3/19.
 */
public class BaseApplication extends XinYiApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        // mBaseApplication=this;
        Utils.init(this);

        XinYiMapSdk.initMapSdk(this);
    }
}
