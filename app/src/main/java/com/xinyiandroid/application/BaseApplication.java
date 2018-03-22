package com.xinyiandroid.application;


import com.blankj.utilcode.util.Utils;
import com.xymaplibrary.application.XinYiMapApplication;

/**
 * Created by jiajun.wang on 2018/3/19.
 */
public class BaseApplication extends XinYiMapApplication {

    public static BaseApplication mBaseApplication;
    @Override
    public void onCreate() {
        super.onCreate();
        mBaseApplication=this;
        Utils.init(this);
    }
}
