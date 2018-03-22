package com.xinyiandroid.application;


import com.blankj.utilcode.util.Utils;
import com.xylibrary.application.XinYiApplication;
import com.xymaplibrary.application.XinYiMapApplication;

/**
 * Created by jiajun.wang on 2018/3/19.
 */
public class BaseApplication extends XinYiApplication {
    //public static BaseApplication mBaseApplication;  使用  Utils.getApp() 来替代这种方式
    @Override
    public void onCreate() {
        super.onCreate();
        // mBaseApplication=this;
        Utils.init(this);

        XinYiMapApplication.init(this);
    }
}
