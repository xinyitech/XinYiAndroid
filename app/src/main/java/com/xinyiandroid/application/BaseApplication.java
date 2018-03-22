package com.xinyiandroid.application;


import com.blankj.utilcode.util.Utils;
import com.xylibrary.application.XinYiApplication;

/**
 * Created by jiajun.wang on 2018/3/19.
 */
public class BaseApplication extends XinYiApplication {

    public static BaseApplication mBaseApplication;
    @Override
    public void onCreate() {
        super.onCreate();
        mBaseApplication=this;
        Utils.init(this);
    }
}
